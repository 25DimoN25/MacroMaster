package gui;

import java.text.NumberFormat;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import command.Command;
import command.CommandType;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;

public class CommandList extends TableView<Command> {
	public CommandList() {
		setPrefHeight(Integer.MAX_VALUE);
		setEditable(true);
		setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
		setPlaceholder(new Label("No commands"));
		setFixedCellSize(30);
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		TableColumn<Command, CommandType> typeColumn = new TableColumn<>("Type"); 
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		typeColumn.setSortable(false);
		typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(CommandType.class.getEnumConstants()));
		typeColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setType(t.getNewValue());
		});
		getColumns().add(typeColumn);
		
		TableColumn<Command, KeyCode> mouseButtonColumn = new TableColumn<>("Key");
		mouseButtonColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
		mouseButtonColumn.setSortable(false);
		mouseButtonColumn.setCellFactory(ComboBoxTableCell.forTableColumn(KeyCode.class.getEnumConstants()));
		mouseButtonColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setKey(t.getNewValue());
		});
		getColumns().add(mouseButtonColumn);
		
		TableColumn<Command, MouseButton> keyboardKeyColumn = new TableColumn<>("MButton"); 
		keyboardKeyColumn.setCellValueFactory(new PropertyValueFactory<>("mButton"));
		keyboardKeyColumn.setSortable(false);
		keyboardKeyColumn.setCellFactory(ComboBoxTableCell.forTableColumn(MouseButton.class.getEnumConstants()));
		keyboardKeyColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setMButton(t.getNewValue());
		});
		getColumns().add(keyboardKeyColumn);
		
		TableColumn<Command, Point2D> coordinatesColumn = new TableColumn<>("Coordinates"); 
		coordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));
		coordinatesColumn.setSortable(false);
		coordinatesColumn.setCellFactory(e -> new PointTableCell());
		coordinatesColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setCoordinates(t.getNewValue());
		});
		getColumns().add(coordinatesColumn);
		
		TableColumn<Command, Number> ñountColumn = new TableColumn<>("Count"); 
		ñountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
		ñountColumn.setSortable(false);
		ñountColumn.setCellFactory(TextFieldTableCell.<Command, Number>forTableColumn(new NumberStringConverter()));
		ñountColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setCount(t.getNewValue().intValue());
		});
		getColumns().add(ñountColumn);
		
		TableColumn<Command, Number> delayColumn = new TableColumn<>("Delay"); 
		delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
		delayColumn.setCellFactory(TextFieldTableCell.<Command, Number>forTableColumn(new NumberStringConverter()));
		delayColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setDelay(t.getNewValue().intValue());
		});
		delayColumn.setSortable(false);
		getColumns().add(delayColumn);

		
		setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.DELETE) {
				getItems().removeAll(getSelectionModel().getSelectedItems());
			}
		});
	}
	
	private class PointTableCell extends TableCell<Command, Point2D> {
		TextField fieldX;
		TextField fieldY;
		HBox pane;
		
		public PointTableCell() {
			fieldX = new TextField();
			fieldX.setTextFormatter(new TextFormatter<>(e -> e.getControlNewText().matches("\\d{0,5}") ? e : null));
			
			fieldY = new TextField();
			fieldY.setTextFormatter(new TextFormatter<>(e -> e.getControlNewText().matches("\\d{0,5}") ? e : null));

			
			widthProperty().addListener((obs, oldV, newV) -> {
				fieldX.setMaxWidth(newV.intValue()/2-3);
				fieldX.setMinWidth(newV.intValue()/2-3);
				fieldX.setPrefWidth(newV.intValue()/2-3);
				
				fieldY.setMaxWidth(newV.intValue()/2-3);
				fieldY.setMinWidth(newV.intValue()/2-3);
				fieldY.setPrefWidth(newV.intValue()/2-3);
			});
			
			pane = new HBox(fieldX, fieldY);
			
			

			contentDisplayProperty().bind(
					Bindings.when(editingProperty())
							.then(ContentDisplay.GRAPHIC_ONLY)
							.otherwise(ContentDisplay.TEXT_ONLY)
					);
			
			EventHandler<KeyEvent> keyEvent = e -> {
				if (isEditing()) {
					if (e.getCode() == KeyCode.ENTER) {
						setText("x:" + fieldX.getText() + ", y:" + fieldY.getText());
						commitEdit(new Point2D(Integer.parseInt(fieldX.getText()), Integer.parseInt(fieldY.getText())));
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
				fieldX.setText(String.valueOf((int)item.getX()));
				fieldY.setText(String.valueOf((int)item.getY()));
				setText("x:" + (int)item.getX() + ", y:" + (int)item.getY());
			}
		}
	}
	
}
