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

public class SettingsDialog extends Stage {
	private static SettingsDialog instance;
	private static Settings settings = new Settings();
	
	private TextField scalingXField;
	private TextInputControl scalingYField;
	private TextInputControl offsetXField;
	private TextInputControl offsetYField;
	private TextInputControl delayField;
	
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
		 * Speed;
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
			close();
		});
		
		Button ok = new Button("Ok");
		ok.setOnAction(e -> {
			acceptChanges();
			close();
		});
		
		pane.add(new HBox(5, cancel, ok), 1, 3);

		Scene scene = new Scene(pane);
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				acceptChanges();
				close();
			} else if (e.getCode() == KeyCode.ESCAPE) {
				cancelChanges();
				close();
			}
		});
		setScene(scene);
		setOnCloseRequest(e -> cancelChanges());
		sizeToScene();
		setResizable(false);
		setTitle("Settings");
		initModality(Modality.WINDOW_MODAL);
	}
	
	private void cancelChanges() {
		scalingXField.setText(String.valueOf(settings.scalingX));
		scalingYField.setText(String.valueOf(settings.scalingY));
		offsetXField.setText(String.valueOf(settings.offsetX));
		offsetYField.setText(String.valueOf(settings.offsetY));
		delayField.setText(String.valueOf(settings.delay));
	}
	
	private void acceptChanges() {
		settings.scalingX = Double.parseDouble(scalingXField.getText());
		settings.scalingY = Double.parseDouble(scalingYField.getText());
		settings.offsetX = Integer.parseInt(offsetXField.getText());
		settings.offsetY = Integer.parseInt(offsetYField.getText());
		settings.delay = Double.parseDouble(delayField.getText());
	}
	
	public static Settings getSettings() {
		return settings;
	}
	
	public static SettingsDialog getInstance(Stage owner) {
		if (instance == null) {
			instance = new SettingsDialog();
		}
		
		if (owner != null && !owner.equals(instance.getOwner())) {
			instance.initOwner(owner);
		}
		
		return instance;
	}

	
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
