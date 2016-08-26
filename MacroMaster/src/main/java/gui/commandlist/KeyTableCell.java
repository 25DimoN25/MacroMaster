package gui.commandlist;

import command.Command;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * 
 * TableCell implementation for setting KeyCode by pressing key in column cell.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 * 
 */
public class KeyTableCell extends TableCell<Command, KeyCode> {
	
	private ComboBox<KeyCode> comboBox;
	
	public KeyTableCell() {
		ObservableList<KeyCode> keyCodes = FXCollections.observableArrayList(KeyCode.class.getEnumConstants());
		keyCodes.add(0, null);
		
		comboBox = new ComboBox<>(keyCodes);	
		comboBox.setValue(null);
		comboBox.setEditable(true);
		comboBox.getEditor().setOnKeyTyped(KeyEvent::consume);
		comboBox.setConverter(new StringConverter<KeyCode>() {
			@Override
			public String toString(KeyCode object) {
				return object != null ? object.toString() : "";
			}
			@Override
			public KeyCode fromString(String string) {
				return string != null ? KeyCode.valueOf(string) : null;
			}
		});
		comboBox.setOnKeyPressed(e -> {
			comboBox.setValue(e.getCode());
			setText(e.getCode().toString());
			commitEdit(e.getCode());
			e.consume();
		});
		comboBox.getEditor().setOnKeyPressed(comboBox.getOnKeyPressed());
		
		comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {	
			setText(newValue == null ? null : newValue.toString());
			commitEdit(newValue);	
		});

		
		/*
		 * bind display type to editing state;
		 */
		contentDisplayProperty().bind(
				Bindings.when(editingProperty())
						.then(ContentDisplay.GRAPHIC_ONLY)
						.otherwise(ContentDisplay.TEXT_ONLY)
				);
		
	}
	
	@Override
	protected void updateItem(KeyCode item, boolean empty) {
		super.updateItem(item, empty);
		if (!empty) {
			setGraphic(comboBox);			
			if (item != null) {
				comboBox.setValue(item);
				setText(item.toString());
			} else {
				comboBox.setValue(null);
				setText(null);
			}
			
		} else {
			setGraphic(null);
			setText(null);
		}
	}
	
}
