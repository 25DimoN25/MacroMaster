package gui;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import command.Command;
import command.CommandType;
import dialogs.EditingDialog;
import gui.commandlist.CommandList;
import gui.commandlist.MoveableRow;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Tab implementation with list of commands, row with new command and button for add new command.
 * 
 * @author @author Saltykov Dmitry (25DimoN25)
 *
 */
public class CommandListTab extends Tab {
	private static final Logger LOG = LoggerFactory.getLogger(CommandListTab.class);
	
	private CommandList commands = new CommandList();
	private CommandList newCommand = new CommandList();
	private Button addCommand = new Button("add command");
	private Command blankCommand = new Command(CommandType.CLICK, null, null, null, 1, 100);
	private CopyPasteContextMenu contextMenu = new CopyPasteContextMenu();
	private Stage owner;
	private File currentFile;
	
	private ObservableList<Command> cuttedCommands = FXCollections.observableArrayList();
	
	public CommandListTab(String title, Stage owner) {
		this.owner = owner;
		
		VBox content = new VBox(3);
		content.setAlignment(Pos.CENTER);
		
		newCommand.bindColumnsWidthTo(commands);
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
		newCommand.setRowFactory(table -> new TableRow<Command>() { //Disable focus ability;
			@Override
			protected void updateItem(Command command, boolean empty) {
				setFocusTraversable(false);
			}
		});
		
		addCommand.setOnAction(e -> {
			Command command = (Command) blankCommand.clone();
			commands.getItems().add(command);
			LOG.debug("Added command: {}", command);
		});	
		
		commands.setRowFactory(table -> new MoveableRow((CommandList) table));
		contextMenu.setOnActionCopy(e -> copy());
		contextMenu.setOnActionCut(e -> cut());
		contextMenu.setOnActionDelete(e -> removeSelected());
		contextMenu.setOnActionPaste(e -> paste());
		contextMenu.setOnActionEdit(e -> editSelected());
		commands.setContextMenu(contextMenu);
		
		content.getChildren().addAll(commands, newCommand, addCommand);
		setContent(content);
		setText(title);
		
		
		/*
		 * Register hotkeys to delete and copy/cut/paste;
		 */
		commands.setOnKeyPressed(e -> {
			if (commands.isEditable()) {
				if (e.getCode() == KeyCode.DELETE) {
					removeSelected();
				} else if (e.isControlDown()) {
					if (e.getCode() == KeyCode.C) {
						copy();
					} else if (e.getCode() == KeyCode.X) {
						cut();
					} else if (e.getCode() == KeyCode.V) {
						paste();
					}
				}
			}
		});

		commands.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Integer> c) {
				if (c.getList().size() == 0) {
					contextMenu.setCopyCutEditDelDisabled(true);
				} else {
					contextMenu.setCopyCutEditDelDisabled(false);
				}
			}
		});
	}
	
	
	/**
	 * Open edit dialog for selected commands.
	 */
	private void editSelected() {
		EditingDialog.getInstance(owner).showEdit(
				getCommands().getSelectionModel().getSelectedItems());
		commands.refresh();
	}


	/**
	 * Copy selected commands.
	 */
	private void copy() {
		contextMenu.setPasteDisable(false);
		ObservableList<Command> selectedCommands = commands.getSelectionModel().getSelectedItems();
		if (selectedCommands.size() > 0) {
			cuttedCommands.clear();
			cuttedCommands.addAll(selectedCommands);
		}
		LOG.debug("Copied {} command(s)", selectedCommands.size());
	}
	
	/**
	 * Copy and remove selected commands.
	 */
	private void cut() {
		copy();
		removeSelected();
	}
	
	
	/**
	 * Paste copied commands after selected items.
	 */
	private void paste() {
		ObservableList<Command> selectedCommands = commands.getSelectionModel().getSelectedItems();
		if (selectedCommands.size() > 0) {
			int pasteIndex = commands.getSelectionModel().getSelectedIndices().get(selectedCommands.size() - 1) + 1;
			
			for (Command command : cuttedCommands) {
				commands.getItems().add(pasteIndex++, (Command) command.clone());
			}
			commands.getSelectionModel().clearSelection();
			commands.getSelectionModel().selectRange(pasteIndex - cuttedCommands.size(), pasteIndex);
		} else {
			for (Command command : cuttedCommands) {
				commands.getItems().add((Command) command.clone());
			}
			commands.getSelectionModel().clearSelection();
			commands.getSelectionModel().selectRange(commands.getItems().size() - cuttedCommands.size(), commands.getItems().size());
		}
		LOG.debug("Pasted {} commad(s)", cuttedCommands.size());
	}
	
	
	/**
	 * Delete selected commands.
	 */
	private void removeSelected() {
		ObservableList<Command> selectedCommands = FXCollections.observableArrayList(commands.getSelectionModel().getSelectedItems());
		commands.getSelectionModel().clearSelection();
		commands.getItems().removeAll(selectedCommands);
		
		LOG.debug("Removed {} command(s)", selectedCommands.size());
		selectedCommands.clear();
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
	
}
