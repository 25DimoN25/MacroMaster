package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

//TODO
public class ControlBar extends ToolBar {
	public static final int PLAYING = 1;
	public static final int STOPPED = 2;
	public static final int PAUSED = 3;
	
	private Button play = new Button("\u25B6");
	private Button pause = new Button("ll");
	private Button stop = new Button("\u25FC");
	
	private int currentState = STOPPED;
	
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
	
	/**
	 * Set state to block any buttons.
	 * 
	 * @param state use ControlBar static constants
	 */
	public void setState(int state) {
		currentState = state;
		switch (state) {
			case PLAYING:
				play.setDisable(true);
				pause.setDisable(false);
				stop.setDisable(false);
				break;
			case PAUSED:
				play.setDisable(false);
				pause.setDisable(true);
				stop.setDisable(false);
				break;
			case STOPPED:
				play.setDisable(false);
				pause.setDisable(true);
				stop.setDisable(true);
				break;
			default:
				break;
		}

	}

	public int getState() {	
		return currentState;
	}
}
