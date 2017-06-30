package dialogs;

import command.Command;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditingDialog {
	static EditingDialog instance;
	
	private ObservableList<Command> editingValues;
	private Stage primaryStage;

	private TextField coordinatesX;
	private TextField coordinatesY;
	private RadioButton coordinatesRbAbsolute;
	private RadioButton coordinatesRbRelative;
	private ToggleGroup coordinatesToggleGroup;
	
	private EditingDialog() {		
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(5));
		
		/*
		 * Column constraints;
		 */
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setHalignment(HPos.RIGHT);
		
		ColumnConstraints column2 = new ColumnConstraints(100);
		
		ColumnConstraints column3 = new ColumnConstraints(150);
		column1.setHalignment(HPos.LEFT);
	
		pane.getColumnConstraints().addAll(column1, column2, column3);
		
		
		/*
		 * Coordinates offsets;
		 */
		Label coordinatesLabel = new Label("Coordinates offsets:");
		
		coordinatesX = new TextField();
		coordinatesY = new TextField();
		
		coordinatesRbAbsolute = new RadioButton("Absolute");
		coordinatesRbAbsolute.setSelected(true);
		
		coordinatesRbRelative = new RadioButton("Relative");
		
		coordinatesToggleGroup = new ToggleGroup();
		coordinatesToggleGroup.getToggles().addAll(coordinatesRbAbsolute, coordinatesRbRelative);
		
		pane.add(coordinatesLabel, 0, 0);
		pane.add(new HBox(coordinatesX, coordinatesY), 1, 0);
		pane.add(new HBox(5, coordinatesRbAbsolute, coordinatesRbRelative), 2, 0);

		
		/*
		 * Buttons
		 */
		Button cancel = new Button("Cancel");
		cancel.setOnAction(e -> {
			cancelChanges();
			primaryStage.close();
		});
		
		Button ok = new Button("Ok");
		ok.setOnAction(e -> {
			acceptChanges();
			primaryStage.close();
		});
		
		pane.add(new HBox(5, cancel, ok), 2, 3);
		
		Scene scene = new Scene(pane);
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				acceptChanges();
				primaryStage.close();
			}
		});

		primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> cancelChanges());
		primaryStage.sizeToScene();
		primaryStage.setResizable(false);
		primaryStage.setTitle("Edit");
		primaryStage.initModality(Modality.WINDOW_MODAL);
	}
	
	private void acceptChanges() {
		for (Command command : editingValues) {
			int x = coordinatesX.getText().isEmpty() ? 0 : Integer.valueOf(coordinatesX.getText());
			int y = coordinatesY.getText().isEmpty() ? 0 : Integer.valueOf(coordinatesY.getText());
			
			if (coordinatesRbAbsolute.isSelected()) {
				command.setCoordinates(new Point2D(x, y));
			} else if (coordinatesRbRelative.isSelected()) {
				int oldX = (int) command.getCoordinates().getX();
				int oldY = (int) command.getCoordinates().getY();
				command.setCoordinates(new Point2D(oldX + x, oldY + y));
			}
				
		}
		
		primaryStage.close();
	}

	private void cancelChanges() {
		primaryStage.close();
	}

	public void showEdit(ObservableList<Command> editingValues) {
		this.editingValues = editingValues;
		
		Point2D coordinates = editingValues.get(0).getCoordinates();
		if (coordinates != null) {
			coordinatesX.setText(String.valueOf((int) coordinates.getX()));
			coordinatesY.setText(String.valueOf((int) coordinates.getY()));
		} else {
			coordinatesX.setText("");
			coordinatesY.setText("");
		}

		primaryStage.showAndWait();
	}
	
	public static EditingDialog getInstance(Stage owner) {
		if (instance == null) {
			instance = new EditingDialog();
		}
		
		if (owner != null && !owner.equals(instance.primaryStage.getOwner())) {
			instance.primaryStage.initOwner(owner);
		}
		
		return instance;
	}
}
