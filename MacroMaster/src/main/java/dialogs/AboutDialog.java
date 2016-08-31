package dialogs;

import java.io.IOException;
import java.net.URISyntaxException;

import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * About dialog for menu button.
 * 
 * @author @author Saltykov Dmitry (25DimoN25)
 *
 */
public class AboutDialog extends Alert {
	private static final String VER = "1.3.2-RELEASE";
	private static final String REF = "https://github.com/25DimoN25/MacroMaster";
	private static AboutDialog instance;

	private AboutDialog() {
		super(AlertType.INFORMATION);
		
		Hyperlink link1 = new Hyperlink(null, new ImageView("img/github.png"));
		link1.setOnAction(e -> {
			try {
				java.awt.Desktop.getDesktop().browse(new java.net.URI(REF));
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		});
		setGraphic(link1);
		setHeaderText("MacroMaster - simple program to make simple macroses!");
		setContentText("Version " + VER + "\n\n"
								 + "By Saltykov Dmitry, 2016 ©\n\n"
								 + "Click to image and visit GitHub for more information.");
		
		setTitle("About");
	}

	public static AboutDialog getInstance(Stage owner) {
		if (instance == null) {
			instance = new AboutDialog();
		}
		
		if (!owner.equals(instance.getOwner())) {
			instance.initOwner(owner);
		}
		
		return instance;
	}
}
