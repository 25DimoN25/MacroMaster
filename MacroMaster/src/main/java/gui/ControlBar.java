package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;

/**
 * 
 * Simple toolbar with play, pause, stop buttons for MacroMaster.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 * 
 */
public class ControlBar extends ToolBar {
	public static final int PLAYING = 1;
	public static final int STOPPED = 2;
	public static final int PAUSED = 3;
	
	private Button play = new Button("\u25B6", new ImageView("img/play.png"));
	private Button pause = new Button("ll", new ImageView("img/pause.png"));
	private Button stop = new Button("\u25FC", new ImageView("img/stop.png"));
	
	private int currentState = STOPPED;
	
	public ControlBar() {
		getItems().addAll(play, pause, stop);
		getItems().forEach(e -> {
			((Button) e).setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		});
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
	 * Set state to disable buttons.
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
	
	/**
	 * Press one of buttons.
	 */
	public void fireButton(int state) {
		switch (state) {
			case PLAYING:
				play.fire();
				break;
			case PAUSED:
				pause.fire();
				break;
			case STOPPED:
				stop.fire();
				break;
			default:
				break;
		}
	}
}
