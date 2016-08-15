package command;

public abstract class Command {
	
	private String description;
	private CommandType type;
	
	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}
	
	public CommandType getType() {
		return type;
	}

	private void setType(CommandType type) {
		this.type = type;
	}
	
	
	public static Command moveTo(int x, int y) {
		return new Command() {{
			super.setDescription("move to " + x + " " + y);
			super.setType(CommandType.MOUSE_MOVE_TO);
		}};
	}


}
