package gui;

import java.io.File;

import command.Command;
import command.CommandType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
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
			Command command = newCommand.getItems().get(0);
			commands.getItems().add((Command) command.clone());
		});
		
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
}
