package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * 
 * Drag and drop file overlay for scene;
 * 
 * @author Saltykov Dmitry (25DimoN25)
 *
 */
public class DropFileOverlay extends BorderPane {

	public DropFileOverlay() {
		setVisible(false);
		
		Label label = new Label("Drop macros here");
		label.setWrapText(false);
		label.setFont(Font.font(32));
		label.setTextFill(Color.BLACK);

		setCenter(label);
		
		setStyle("-fx-background-color: rgba(240, 240, 240, 0.7);"
				 + "-fx-border-width: 3;"
				 + "-fx-border-radius: 20;"
				 + "-fx-border-style: dashed;"
				 + "-fx-border-color: blue;"
				 + "-fx-border-insets: 15;");
	}
}
