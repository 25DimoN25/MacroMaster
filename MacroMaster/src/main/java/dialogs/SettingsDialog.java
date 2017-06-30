package dialogs;

import java.util.function.UnaryOperator;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Settings dialog for menu.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 *
 */
public class SettingsDialog {
	private static SettingsDialog instance;
	private static Settings settings = new Settings();
	
	private TextField scalingXField;
	private TextInputControl scalingYField;
	private TextInputControl offsetXField;
	private TextInputControl offsetYField;
	private TextInputControl delayField;
	private Stage primaryStage;
	
	private SettingsDialog() {
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(5));
		
		UnaryOperator<Change> floatNumberFormatter = change ->
			change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null;
		UnaryOperator<Change> offsetFormatter = change ->
			change.getControlNewText().matches("-?\\d*") ? change : null;

		
		/*
		 * Column constraints;
		 */
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setHalignment(HPos.RIGHT);
		ColumnConstraints column2 = new ColumnConstraints(100);
		column2.setHalignment(HPos.LEFT);
		pane.getColumnConstraints().addAll(column1, column2);

		
		/*
		 * Scaling;
		 */
		Label scalingLabel = new Label("Scalling coefs:");
		
		scalingXField = new TextField(String.valueOf(settings.scalingX));
		scalingXField.setPromptText("Width scale");
		scalingXField.setTooltip(new Tooltip("Width scale"));
		scalingXField.setTextFormatter(new TextFormatter<>(floatNumberFormatter));
		
		scalingYField = new TextField(String.valueOf(settings.scalingY));
		scalingYField.setPromptText("Height scale");
		scalingYField.setTooltip(new Tooltip("Height scale"));
		scalingYField.setTextFormatter(new TextFormatter<>(floatNumberFormatter));
		
		pane.add(scalingLabel, 0, 0);
		pane.add(new HBox(scalingXField, scalingYField), 1, 0);
		
		
		/*
		 * Offset;
		 */
		Label offsetLabel = new Label("Offset:");
		
		offsetXField = new TextField(String.valueOf(settings.offsetX));
		offsetXField.setPromptText("X offset");
		offsetXField.setTooltip(new Tooltip("X offset"));
		offsetXField.setTextFormatter(new TextFormatter<>(offsetFormatter));
		
		offsetYField = new TextField(String.valueOf(settings.offsetY));
		offsetYField.setPromptText("Y offset");
		offsetYField.setTooltip(new Tooltip("Y offset"));
		offsetYField.setTextFormatter(new TextFormatter<>(offsetFormatter));
		
		pane.add(offsetLabel, 0, 1);
		pane.add(new HBox(offsetXField, offsetYField), 1, 1);
		
		
		/*
		 * Delay coefficient;
		 */
		Label delayLabel = new Label("Delay coef:");
		
		delayField = new TextField(String.valueOf(settings.delay));
		delayField.setPromptText("Delay coefficient");
		delayField.setTooltip(new Tooltip("Delay coefficient"));
		delayField.setTextFormatter(new TextFormatter<>(floatNumberFormatter));
		
		pane.add(delayLabel, 0, 2);
		pane.add(delayField, 1, 2);
		
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
		
		pane.add(new HBox(5, cancel, ok), 1, 3);

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
		primaryStage.setTitle("Settings");
		primaryStage.initModality(Modality.WINDOW_MODAL);
	}
	
	/**
	 * Cancel current changes in textfields.
	 */
	private void cancelChanges() {
		scalingXField.setText(String.valueOf(settings.scalingX));
		scalingYField.setText(String.valueOf(settings.scalingY));
		offsetXField.setText(String.valueOf(settings.offsetX));
		offsetYField.setText(String.valueOf(settings.offsetY));
		delayField.setText(String.valueOf(settings.delay));
	}
	
	/**
	 * Accept changes in textfields.
	 */
	private void acceptChanges() {
		settings.scalingX = scalingXField.getText().isEmpty() ? 1 : Double.parseDouble(scalingXField.getText());
		settings.scalingY = scalingYField.getText().isEmpty() ? 1 : Double.parseDouble(scalingYField.getText());
		settings.offsetX = offsetXField.getText().isEmpty() ? 0 : Integer.parseInt(offsetXField.getText());
		settings.offsetY = offsetYField.getText().isEmpty() ? 0 : Integer.parseInt(offsetYField.getText());
		settings.delay = delayField.getText().isEmpty() ? 1 : Double.parseDouble(delayField.getText());
	}
	
	public static Settings getSettings() {
		return settings;
	}
	
	public static SettingsDialog getInstance(Stage owner) {
		if (instance == null) {
			instance = new SettingsDialog();
		}
		
		if (owner != null && !owner.equals(instance.primaryStage.getOwner())) {
			instance.primaryStage.initOwner(owner);
		}
		
		return instance;
	}

	public void show() {
		instance.primaryStage.showAndWait();
	}
	
	/**
	 * 	Contains settings fields.
	 */
	public static class Settings {
		private double scalingX = 1.0;
		private double scalingY = 1.0;
		private int offsetX = 0;
		private int offsetY = 0;
		private double delay = 1.0;
		
		private Settings() {}
		
		public double getScalingX() {
			return scalingX;
		}

		public double getScalingY() {
			return scalingY;
		}

		public int getOffsetX() {
			return offsetX;
		}

		public int getOffsetY() {
			return offsetY;
		}

		public double getSpeed() {
			return delay;
		}
	}


}
