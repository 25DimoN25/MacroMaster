package command;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.impl.BaseRobotImpl;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class Command implements Cloneable {

	private BaseRobot ROBOT = new BaseRobotImpl(); 
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
	
	public void setType(CommandType type) {
		this.type = type;
	}
	
	public KeyCode getKey() {
		return key;
	}
	
	public void setKey(KeyCode key) {
		this.key = key;
	}
	
	public MouseButton getMButton() {
		return mbutton;
	}
	
	public void setMButton(MouseButton mbutton) {
		this.mbutton = mbutton;
	}

	public Point2D getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(Point2D coordinates) {
		this.coordinates = coordinates;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Integer getDelay() {
		return delay;
	}
	
	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public void useCommand() {
		try {
			for (int i = 0; i < count; i++) {
				if (coordinates != null) {
					ROBOT.moveMouse(coordinates);
				}
				
				switch (type) {
					case CLICK:
						if (mbutton != null) {
							ROBOT.pressMouse(mbutton);
							ROBOT.releaseMouse(mbutton);
						}
						if (key != null) {
							ROBOT.pressKeyboard(key);
							ROBOT.releaseKeyboard(key);
						}
						break;
					case PRESS:
						if (mbutton != null) {
							ROBOT.pressMouse(mbutton);
						}
						if (key != null) {
							ROBOT.pressKeyboard(key);
						}
						break;
					case RELEASE:
						if (mbutton != null) {
							ROBOT.releaseMouse(mbutton);
						}
						if (key != null) {
							ROBOT.releaseKeyboard(key);
						}
						break;
					default:
						break;
				}	
				
				if (delay > 0) {
					Thread.sleep(delay);
				}
			}
					
		} catch (InterruptedException e) {
			System.err.println("interrupted");
			return;
		}
	}

	@Override
	public Object clone() {
		return new Command(getType(), getKey(), getMButton(), getCoordinates(), getCount(), getDelay());
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getType() + ", " + getKey() + ", " + getMButton() + ", "
				+ getCoordinates() + ", " + getCount() + ", " + getDelay() + "]";
	}

}
