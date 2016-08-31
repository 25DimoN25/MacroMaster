package dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Hotkeys dialog for menu button.
 * 
 * @author @author Saltykov Dmitry (25DimoN25)
 *
 */
public class HotkeysDialog extends Alert {
	private static HotkeysDialog instance;

	private HotkeysDialog() {
		super(AlertType.NONE);
		setContentText("CTRL + \tF9 - play macros\n"
						  + "\t\tF10 - pause macros\n"
						  + "\t\tF11 - stop macros\n"
						  + "\t\tF12 - close programm");
		
		setTitle("Available hotkeys");
		getDialogPane().getButtonTypes().add(ButtonType.OK);
	}

	public static HotkeysDialog getInstance(Stage owner) {
		if (instance == null) {
			instance = new HotkeysDialog();
		}
		
		if (!owner.equals(instance.getOwner())) {
			instance.initOwner(owner);
		}
		
		return instance;
	}
}
