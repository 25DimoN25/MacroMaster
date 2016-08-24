package gui.commandlist;

import command.Command;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TableRow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Row extension for command list, allows move rows on table with mouse.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 *  
 */
public class MoveableRow extends TableRow<Command> {
	private static boolean dropCompleted;
	private static ObservableList<Command> selectedCommands = FXCollections.observableArrayList();
	private static ObservableList<Command> originalCommands = FXCollections.observableArrayList();
	
	private CommandList table;

	public MoveableRow(CommandList table) {
		this.table = table;
	}
	
	@Override
	protected void updateItem(Command command, boolean empty) {
		super.updateItem(command, empty);
		
		if (command != null) {	
			setOnDragDetected(e -> {
				if (table.getSelectionModel().getSelectedItems().size() < table.getItems().size()
						&& table.isEditable()) {

					dropCompleted = false;
					
					selectedCommands.addAll(table.getSelectionModel().getSelectedItems());
					originalCommands.addAll(table.getItems());
								
					WritableImage dragImage = snapshot(new SnapshotParameters(), null);	//creating real image for dragging;
					
					table.getSelectionModel().clearSelection();		//remove selected command
					table.getItems().removeAll(selectedCommands);	//on start dragging;
					
					Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
					dragboard.setDragView(dragImage, 
								dragImage.getWidth()/2,
								dragImage.getHeight()/2);
	
					ClipboardContent content = new ClipboardContent();
					content.putString(selectedCommands.toString());
					dragboard.setContent(content);
				}
			});
			
			setOnMouseClicked(null); // do nothing on mouse click on non-empty row;
		} else {
			setOnDragDetected(null);
			setOnMouseClicked(e -> { // else remove selected;
				table.getSelectionModel().clearSelection();
			});
		}
		
		
		/*
		 * Show line between rows to insert;
		 */
		setOnDragEntered(e -> {
			if (getIndex() <= table.getItems().size()) {
				setStyle("-fx-border-style: solid;"
						+ "-fx-border-width: 3 0 0 0;"
						+ "-fx-border-color: lightblue;");
			}
		});
		
		
		/*
		 * Disable showing line on mouse exit;
		 */
		setOnDragExited(e -> {
			setStyle(null);
		});

		
		/*
		 * Indicates is allows drop;
		 */
		setOnDragOver(e -> {
			if (selectedCommands.size() > 0 && table.isEditable()) {
				e.acceptTransferModes(TransferMode.MOVE);	
			} else {
				e.acceptTransferModes(TransferMode.NONE);
			}
			
		});
		
		
		/*
		 * Adding elements to new position;
		 */
		setOnDragDropped(e -> {
			
			if (getIndex() > table.getItems().size()) {	
				table.getItems().addAll(table.getItems().size(), selectedCommands);
				table.getSelectionModel().selectRange(table.getItems().size() - selectedCommands.size(), table.getItems().size());
			} else {
				table.getItems().addAll(getIndex(), selectedCommands);
				table.getSelectionModel().selectRange(getIndex(), getIndex() + selectedCommands.size());
			}			
			
			dropCompleted = true;
		});
		
		
		/*
		 * If rows dropped not to program - return to original table state;
		 */
		setOnDragDone(e -> {
			if (!dropCompleted) {
				table.getItems().clear();
				table.getItems().addAll(originalCommands);
			}
			
			selectedCommands.clear();
			originalCommands.clear();
		});
		
	}
}