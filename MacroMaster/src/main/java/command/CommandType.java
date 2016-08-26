package command;

/**
 * Command type enumeration.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 *
 */
public enum CommandType {
	PRESS("Press"), RELEASE("Release"), CLICK("Type/Click");
	
	private String toStringValue;
	
	private CommandType(String toStringValue) {
		this.toStringValue = toStringValue;
	}
	
	@Override
	public String toString() {
		return toStringValue;
	}
}
