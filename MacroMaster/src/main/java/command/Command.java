package command;

import org.testfx.robot.BaseRobot;
import org.testfx.robot.impl.BaseRobotImpl;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * Command entity with use function.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 *
 */
public class Command implements Cloneable {
	
	private static BaseRobot ROBOT = new BaseRobotImpl();;
	private CommandType type;
	private KeyCode key;
	private MouseButton mbutton;
	private Point2D coordinates;
	private int count;
	private int delay;
	
	
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
	
	public MouseButton getMbutton() {
		return mbutton;
	}
	
	public void setMbutton(MouseButton mbutton) {
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

	/**
	 * Use command;
	 *  
	 * @param offset move coordinates to offset value;
	 * @param scale change size of coordinates relatively XY;
	 * @param speed multiply current delay. Default 1.0;
	 * @throws InterruptedException
	 */
	public void useCommand(Point2D offset, Dimension2D scale, double speed) throws InterruptedException {
		for (int i = 0; i < count; i++) {
			if (coordinates != null) {
				//make new point with offset and scale;
				Point2D point = new Point2D(coordinates.getX() * scale.getWidth() + offset.getX(),
						coordinates.getY() * scale.getHeight() + offset.getY());
				
				ROBOT.moveMouse(point);
			}
			if (type != null) {
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
				}
			}
			
			if (delay > 0) {
				Thread.sleep((int) (delay * speed));
			}
		}

	}

	@Override
	public Object clone() {
		return new Command(getType(), getKey(), getMbutton(), getCoordinates(), getCount(), getDelay());
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getType() + ", " + getKey() + ", " + getMbutton() + ", "
				+ getCoordinates() + ", " + getCount() + ", " + getDelay() + "]";
	}

}
