package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

//TODO
public class ControlBar extends ToolBar {
	private Button play = new Button("\u25B6");
	private Button pause = new Button("ll");
	private Button stop = new Button("\u25FC");
	
	public ControlBar() {
		getItems().addAll(play, pause, stop);	
	}

	public void setOnActionPlay(EventHandler<ActionEvent> value) {
		play.setOnAction(value);
	}
	
	public void setOnActionPause(EventHandler<ActionEvent> value) {
		pause.setOnAction(value);
	}
	
	public void setOnActionStop(EventHandler<ActionEvent> value) {
		stop.setOnAction(value);
	}
	

}
