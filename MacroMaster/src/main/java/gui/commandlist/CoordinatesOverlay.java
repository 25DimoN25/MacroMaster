package gui.commandlist;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * Overlay to setting coordinates.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 * 
 */
public class CoordinatesOverlay extends Stage {
	private Point2D point;
	
	public CoordinatesOverlay() {
		FlowPane pane = new FlowPane(Orientation.VERTICAL);
		pane.setPadding(new Insets(20));
		pane.setStyle("-fx-background-color: rgba(230, 230, 230, 0.2);");
		
		Text coords = new Text("[0 0]");
		coords.setStyle("-fx-font: 32 arial;"
					  + "-fx-fill: white;"
					  + "-fx-effect: dropshadow(gaussian, black, 20, 0, 0, 0);");
		pane.getChildren().add(coords);
		
		
		Scene scene = new Scene(pane);	
		scene.setFill(Color.TRANSPARENT);
		
		scene.setOnMouseMoved(e -> {
			coords.setText("[" + (int) e.getScreenX() + " " + (int) e.getScreenY() + "]");
		});
		
		scene.setOnMouseClicked(e -> {
			point = new Point2D(e.getScreenX(), e.getScreenY());
			close();
		});
		
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				point = null;
				close();
			}
		});
		
		setScene(scene);
		initStyle(StageStyle.TRANSPARENT);
		setMaximized(true);
	}
	
	
	public Point2D showOverlay() {
		showAndWait();
		return point;
	}

}
