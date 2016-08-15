package gui;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

//TODO
public class ControlBar extends ToolBar {
	public ControlBar() {
		getItems().addAll(
				new Button("\u25B6"),
				new Button("ll"),
				new Button("\u25FE")
				);	
	}
}
