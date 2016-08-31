package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Dialog with tutorials for menu;
 * 
 * @author @author Saltykov Dmitry (25DimoN25)
 *
 */
public class TutorialDialog extends Alert {
	private static TutorialDialog instance;
	
	private TutorialDialog() {
		super(AlertType.NONE);

		setContentText(
				  "New macros:\n"
				+ "Click File -> New to create a new macros;\n\n"
						  
				+ "Add commands:\n"
				+ "Configure table row, positioned highter \"Add command\" button, set the necessary parameters "
				+ "and press Add button. You can combine any actions with mouse and keyboard in one command;\n\n"
				
				+ "Run macros:\n"
				+ "You can run your macros in the current tab. Press play button;\n\n"
				
				+ "Pause macros:\n"
				+ "You can edit your current macros at pause. Pause button stops macros only between commands "
				+ "(i.e. its not working with #BIG_NUMBER#-counted commands);\n\n"
				
				+ "Stop macros:\n"
				+ "You can add or remove commands only in stopped state. Stop button can interrupt macros "
				+ "at any time;\n\n"
				
				+ "Editing:\n"
				+ "You can move rows, change they position using mouse drag gesture. You can use default hotkeys "
				+ "to delete selected rows (DELETE button), copy (CTRL+C), cut (CTRL+X) and paste (CTRL+V);\n\n"
				
				+ "Save/Open:\n"
				+ "You can save your current macros in XML format, use File->Save. For open use File->Open or move "
				+ "file on the MacroMaster from system explorer;");
		
		setTitle("Tutorial");
		getDialogPane().getButtonTypes().add(ButtonType.OK);
	}

	public static TutorialDialog getInstance(Stage owner) {
		if (instance == null) {
			instance = new TutorialDialog();
		}
		
		if (!owner.equals(instance.getOwner())) {
			instance.initOwner(owner);
		}
		
		return instance;
	}
}
