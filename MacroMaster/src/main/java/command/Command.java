package command;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.impl.BaseRobotImpl;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class Command {

	private final BaseRobot ROBOT = new BaseRobotImpl(); 
	private CommandType type;
	private KeyCode key;
	private MouseButton mbutton;
	private Point2D coordinates;
	private Integer count;
	private Integer delay;
	
	public Command(CommandType type, KeyCode key, MouseButton mbutton, Point2D coordinates, int count,	int delay) {
		super();
		this.type = type;
		this.key = key;
		this.mbutton = mbutton;
		this.coordinates = coordinates;
		this.count = count;
		this.delay = delay;
	}


	
	public CommandType getType() {
		return type;
	}
	
	public KeyCode getKey() {
		return key;
	}
	
	public MouseButton getMButton() {
		return mbutton;
	}

	public Point2D getCoordinates() {
		return coordinates;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public Integer getDelay() {
		return delay;
	}
	
	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public void useCommand() {
		for (int i = 0; i < count; i++) {
			ROBOT.pressMouse(mbutton);
			ROBOT.releaseMouse(mbutton);					
		}
	}





}
