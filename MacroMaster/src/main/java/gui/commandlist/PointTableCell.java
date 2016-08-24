package gui.commandlist;

import command.Command;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
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
	TextField fieldX;
	TextField fieldY;
	HBox pane;
	
	public PointTableCell() {
		fieldX = new TextField();
		fieldX.setTextFormatter(new TextFormatter<>(e -> e.getControlNewText().matches("\\d{0,5}") ? e : null));
		
		fieldY = new TextField();
		fieldY.setTextFormatter(new TextFormatter<>(e -> e.getControlNewText().matches("\\d{0,5}") ? e : null));

		/*
		 * Auto-width (3px for borders of textfield);
		 */
		widthProperty().addListener((obs, oldV, newV) -> {
			fieldX.setMaxWidth(newV.intValue()/2-3);
			fieldX.setMinWidth(newV.intValue()/2-3);
			fieldX.setPrefWidth(newV.intValue()/2-3);
			
			fieldY.setMaxWidth(newV.intValue()/2-3);
			fieldY.setMinWidth(newV.intValue()/2-3);
			fieldY.setPrefWidth(newV.intValue()/2-3);
		});
		
		pane = new HBox(fieldX, fieldY);
		
		/*
		 * bind display type to editing state;
		 */
		contentDisplayProperty().bind(
				Bindings.when(editingProperty())
						.then(ContentDisplay.GRAPHIC_ONLY)
						.otherwise(ContentDisplay.TEXT_ONLY)
				);
		
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