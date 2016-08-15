package gui;

import command.Command;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

//TODO
public class CommandListTab extends Tab {
	
	private ListView<Command> commands = new ListView<>();
	private Button addCommand = new Button("add command");
	
	public CommandListTab(String title) {
		VBox content = new VBox();
		content.setAlignment(Pos.CENTER);
		
		commands.setPrefHeight(Integer.MAX_VALUE);
		commands.setCellFactory(e -> new CustomListCell());
		commands.getItems().addAll(
				Command.moveTo(10, 80),
				Command.moveTo(68, 546),
				Command.moveTo(13, 213),
				Command.moveTo(456, 321),
				Command.moveTo(322, 1020)
				);
		
		
		
		
		
		content.getChildren().addAll(commands, addCommand);
		setContent(content);
		setText(title);
	}
	
}


class CustomListCell extends ListCell<Command> {
    @Override
    public void updateItem(Command item, boolean empty) {
		super.updateItem(item, empty);

		if (item != null) {
			setText(item.getDescription());
		}
	}
}