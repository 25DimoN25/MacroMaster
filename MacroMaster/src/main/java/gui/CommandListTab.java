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
	
	public CommandListTab(String title) {
		VBox content = new VBox(3);
		content.setAlignment(Pos.CENTER);

		newCommand.setPrefHeight(27);
		newCommand.setMinHeight(27);
		newCommand.setMaxHeight(27);
		
		newCommand.widthProperty().addListener((obs, o, n) -> {
			Pane header = (Pane) newCommand.lookup("TableHeaderRow");
			if (header != null && header.isVisible()) {
				header.setMaxHeight(0);
				header.setMinHeight(0);
				header.setPrefHeight(0);
				header.setVisible(false);
			}
		});
		newCommand.getItems().add(new Command(CommandType.CLICK, KeyCode.CONTROL, MouseButton.PRIMARY, new Point2D(10, 10), 1, 0));
		
		content.getChildren().addAll(commands, newCommand, addCommand);
		setContent(content);
		setText(title);
	}
	
	public TableView<Command> getCommands() {
		return commands;
	}
	
}
