package gui;

import javax.swing.text.AbstractDocument.Content;

import command.Command;
import command.CommandType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class CommandList extends TableView<Command> {
	public CommandList() {
		setPrefHeight(Integer.MAX_VALUE);
		setEditable(true);
		setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
		setPlaceholder(new Label("No commands"));
		
		TableColumn<Command, CommandType> typeColumn = new TableColumn<>("Type"); 
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		typeColumn.setSortable(false);
//		typeColumn.setCellFactory();
		getColumns().add(typeColumn);
		
		TableColumn<Command, KeyCode> mouseButtonColumn = new TableColumn<>("Key");
		mouseButtonColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
		mouseButtonColumn.setSortable(false);
//		mouseButtonColumn.setCellFactory();
		getColumns().add(mouseButtonColumn);
		
		TableColumn<Command, MouseButton> keyboardKeyColumn = new TableColumn<>("MButton"); 
		keyboardKeyColumn.setCellValueFactory(new PropertyValueFactory<>("mButton"));
		keyboardKeyColumn.setSortable(false);
//		keyboardKeyColumn.setCellFactory();
		getColumns().add(keyboardKeyColumn);
		
		TableColumn<Command, Point2D> coordinatesColumn = new TableColumn<>("Coordinates"); 
		coordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));
		coordinatesColumn.setSortable(false);
//		coordinatesColumn.setCellFactory();
		getColumns().add(coordinatesColumn);
		
		TableColumn<Command, Integer> ñountColumn = new TableColumn<>("Count"); 
		ñountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
		ñountColumn.setSortable(false);
//		ñountColumn.setCellFactory();
		getColumns().add(ñountColumn);
		
		TableColumn<Command, Integer> delayColumn = new TableColumn<>("Delay"); 
		delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
		delayColumn.setCellFactory(e -> new DelayTableCell());
		delayColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setDelay(t.getNewValue());
		});
		delayColumn.setSortable(false);
		getColumns().add(delayColumn);
		
		

	}

	
	private class DelayTableCell extends TableCell<Command, Integer> {
		TextField textField;
		
		public DelayTableCell() {
			textField = new TextField("");
			textField.focusedProperty().addListener((obs, old, focused) -> {
				if (!focused) {
					commitEdit(Integer.parseInt(textField.getText()));
				}
			});
			textField.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                	commitEdit(Integer.parseInt(textField.getText()));
                } else if (e.getCode() == KeyCode.ESCAPE) {
                	cancelEdit();
                }
                
            });
			
			setContentDisplay(ContentDisplay.TEXT_ONLY);
			editingProperty().addListener((obs, old, editing) -> {
				if (editing) {
					setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				} else {
					setContentDisplay(ContentDisplay.TEXT_ONLY);
				}
			});
			
			
		}
		
		@Override
		public void commitEdit(Integer newValue) {
			super.commitEdit(newValue);
			setText(newValue.toString());
		}
		
		@Override
		protected void updateItem(Integer item, boolean empty) {
			super.updateItem(item, empty);
			
			if (!empty) {
				setGraphic(textField);
			}
			
			if (item == null) {	
				textField.setText(null);
			    setText(null);
			} else {	    
			    textField.setText(item.toString());
			    setText(item.toString());
			}
		}
	}
}
