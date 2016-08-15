package gui;

import javafx.scene.control.ListView;

//TODO
public class CommandList extends ListView<String> {
	
	public CommandList() {
		getItems().addAll(
				"move 1 2",
				"lm click x2",
				"move 1 2",
				"lm click x2",
				"move 1 2",
				"lm click x2",
				"move 1 2",
				"lm click x2",
				"move 1 2",
				"lm click x2",
				"move 1 2",
				"lm click x2",
				"alt + f4"				
				);
	}
	
}
