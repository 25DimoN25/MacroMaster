package gui;

import java.io.File;

import command.Command;
import command.CommandType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableRow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Tab implementation with list of commands, row with new command and button for add new command.
 * 
 * @author @author Saltykov Dmitry (25DimoN25)
 *
 */
public class CommandListTab extends Tab {
	
	private CommandList commands = new CommandList();
	private CommandList newCommand = new CommandList();
	private Button addCommand = new Button("add command");
	private Command blankCommand = new Command(CommandType.CLICK, null, MouseButton.PRIMARY, null, 1, 100);
	
	private File currentFile;
		
	public CommandListTab(String title) {
		VBox content = new VBox(3);
		content.setAlignment(Pos.CENTER);
		
		newCommand.setPrefHeight(32);
		newCommand.setMinHeight(32);
		newCommand.setMaxHeight(32);
		newCommand.widthProperty().addListener((obs, o, n) -> {
			Pane header = (Pane) newCommand.lookup("TableHeaderRow");
			if (header != null && header.isVisible()) {
				header.setMaxHeight(0);
				header.setMinHeight(0);
				header.setPrefHeight(0);
				header.setVisible(false);
			}
		});
		newCommand.getItems().add((Command) blankCommand);
				
		addCommand.setOnAction(e -> {
			Command command = (Command) blankCommand.clone();
			commands.getItems().add(command);
		});		
		
		commands.setRowFactory(table -> new MoveableRow((CommandList) table));
		
		content.getChildren().addAll(commands, newCommand, addCommand);
		setContent(content);
		setText(title);
	}
	
	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}

	public CommandList getCommands() {
		return commands;
	}

	public void setAddButtonDisable(boolean disable) {
		addCommand.setDisable(disable);
	}
	
	public void setCommandListDisable(boolean disable) {
		commands.setEditable(!disable);
	}
	
	
	private static class MoveableRow extends TableRow<Command> {
		private static boolean dropCompleted;
		private static ObservableList<Command> selectedCommands = FXCollections.observableArrayList();
		private static ObservableList<Command> originalCommands = FXCollections.observableArrayList();
		
		private CommandList table;

		public MoveableRow(CommandList table) {
			this.table = table;
		}
		
		@Override
		protected void updateItem(Command command, boolean empty){
			super.updateItem(command, empty);
			if (command != null) {
				
				setOnDragDetected(e -> {
					dropCompleted = false;
					
					selectedCommands.addAll(table.getSelectionModel().getSelectedItems());
					originalCommands.addAll(table.getItems());
								
					WritableImage dragImage = snapshot(new SnapshotParameters(), null);
					
					table.getSelectionModel().clearSelection();
					table.getItems().removeAll(selectedCommands);
					
					Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
					dragboard.setDragView(dragImage, 
								dragImage.getWidth()/2,
								dragImage.getHeight()/2);

					ClipboardContent content = new ClipboardContent();
					content.putString(selectedCommands.toString());
					dragboard.setContent(content);
				});
				
			}
			
			setOnDragEntered(e -> {
				setStyle("-fx-border-style: solid;"
						+ "-fx-border-width: 2 0 0 0;"
						+ "-fx-border-color: lightblue;");
			});
			
			setOnDragExited(e -> {
				setStyle(null);
			});

			setOnDragOver(e -> {
				if (selectedCommands.size() > 0) {
					e.acceptTransferModes(TransferMode.MOVE);	
				} else {
					e.acceptTransferModes(TransferMode.NONE);
				}
				
			});
			
			setOnDragDropped(e -> {
				
				if (getIndex() >= table.getItems().size()) {	
					table.getItems().addAll(table.getItems().size(), selectedCommands);
					table.getSelectionModel().selectRange(table.getItems().size() - selectedCommands.size(), table.getItems().size());
				} else {
					table.getItems().addAll(getIndex(), selectedCommands);
					table.getSelectionModel().selectRange(getIndex(), getIndex() + selectedCommands.size());
				}			
				
				dropCompleted = true;
			});
			
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
}
