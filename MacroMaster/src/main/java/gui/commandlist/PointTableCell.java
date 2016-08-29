package gui.commandlist;
import command.Command;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 * 
 * TableCell implementation for editing Point2D (coordinates of mouse command) column cells.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 * 
 */
public class PointTableCell extends TableCell<Command, Point2D> {
	private TextField fieldX;
	private TextField fieldY;
	private Button getCoordsButton;	
	private HBox pane;
	
	private static CoordinatesOverlay overlay = new CoordinatesOverlay();
	
	public PointTableCell() {
		fieldX = new TextField();
		fieldX.setPromptText("x");
		fieldX.setTextFormatter(new TextFormatter<>(e -> e.getControlNewText().matches("\\d{0,5}") ? e : null));
		
		fieldY = new TextField();
		fieldY.setPromptText("y");
		fieldY.setTextFormatter(new TextFormatter<>(e -> e.getControlNewText().matches("\\d{0,5}") ? e : null));

		getCoordsButton = new Button("*");
		getCoordsButton.setMaxWidth(25);
		getCoordsButton.setMinWidth(25);
		getCoordsButton.setPrefWidth(25);
		getCoordsButton.setOnAction(e -> {
			Point2D point = overlay.showOverlay();
			if (point != null) {
				fieldX.setText(String.valueOf((int) point.getX()));
				fieldY.setText(String.valueOf((int) point.getY()));
				setText("x:" + (int) point.getX() + ", y:" + (int) point.getY());	
				commitEdit(point);
			} else {
				cancelEdit();
			}
		});
		
		
		/*
		 * Auto-width (3px for borders of textfield);
		 */
		widthProperty().addListener((obs, oldValue, newValue) -> {
			double width = (newValue.intValue() / 2) - (getCoordsButton.getPrefWidth() / 2) - 3;
			fieldX.setMaxWidth(width);
			fieldX.setMinWidth(width);
			fieldX.setPrefWidth(width);

			fieldY.setMaxWidth(width);
			fieldY.setMinWidth(width);
			fieldY.setPrefWidth(width);
		});

		
		/*
		 * ESC - cancel editing, ENTER - accept changes;
		 */
		EventHandler<KeyEvent> keyEvent = e -> {
			if (isEditing()) {
				if (e.getCode() == KeyCode.ENTER) {
					if (fieldX.getText().length() > 0 && fieldY.getText().length() > 0) {
						setText("x:" + fieldX.getText() + ", y:" + fieldY.getText());	
						commitEdit(new Point2D(Integer.parseInt(fieldX.getText()), Integer.parseInt(fieldY.getText())));
					} else {
						setText(null);	
						commitEdit(null);
					}
				} else if (e.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			}
		};
		
		fieldX.setOnKeyPressed(keyEvent);
		fieldY.setOnKeyPressed(keyEvent);
		
		pane = new HBox(fieldX, fieldY, getCoordsButton);
		
		
		/*
		 * bind display type to editing state;
		 */
		contentDisplayProperty().bind(
				Bindings.when(editingProperty())
						.then(ContentDisplay.GRAPHIC_ONLY)
						.otherwise(ContentDisplay.TEXT_ONLY));
	}
	
	
	@Override
	protected void updateItem(Point2D item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty) {
			setGraphic(pane);
			if (item != null) {
				fieldX.setText(String.valueOf((int) item.getX()));
				fieldY.setText(String.valueOf((int) item.getY()));
				setText("x:" + (int) item.getX() + ", y:" + (int) item.getY());
			} else {
				fieldX.setText("");
				fieldY.setText("");
				setText(null);
			}

		} else {
			setGraphic(null);
			setText(null);
		}
	}
	
}