package gui;

import command.Command;
import command.CommandType;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

//TODO
public class CommandListTab extends Tab {
	
	private TableView<Command> commands = new CommandList();
	private TableView<Command> newCommand = new CommandList();
	private Button addCommand = new Button("add command");
	private Command blankCommand = new Command(CommandType.CLICK, null, MouseButton.PRIMARY, null, 1, 100);
	
	
	public CommandListTab(String title) {
		VBox content = new VBox(3);
		content.setAlignment(Pos.CENTER);

		commands.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.DELETE) {
				commands.getItems().removeAll(commands.getSelectionModel().getSelectedItems());
			}
		});
		
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
	
	public TableView<Command> getCommands() {
		return commands;
	}
	
}
