package gui;

import command.Command;
import command.CommandType;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/**
 * 
 * List (TableView) of Command with editing feature.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 * 
 */
public class CommandList extends TableView<Command> {
	
	public CommandList() {
		setPrefHeight(Integer.MAX_VALUE);
		setEditable(true);
		setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
		setPlaceholder(new Label("No commands"));
		setFixedCellSize(30);
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		
		/*
		 * Configuring 1st column for displaying and editing type of command;
		 */
		TableColumn<Command, CommandType> typeColumn = new TableColumn<>("Type"); 
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		typeColumn.setSortable(false);
		
		ObservableList<CommandType> typeColumnData = FXCollections.observableArrayList(CommandType.class.getEnumConstants());
		typeColumnData.add(0, null);
		
		typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(typeColumnData));
		typeColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setType(t.getNewValue());
		});
		getColumns().add(typeColumn);
		
		
		/*
		 * Configuring 2nd column for displaying and editing pressing key of keyboard;
		 */
		TableColumn<Command, KeyCode> mouseButtonColumn = new TableColumn<>("Keyboard key");
		mouseButtonColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
		mouseButtonColumn.setSortable(false);
		
		ObservableList<KeyCode> mouseButtonColumnData = FXCollections.observableArrayList(KeyCode.class.getEnumConstants());
		mouseButtonColumnData.add(0, null);
		
		mouseButtonColumn.setCellFactory(ComboBoxTableCell.forTableColumn(mouseButtonColumnData));
		mouseButtonColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setKey(t.getNewValue());
		});
		getColumns().add(mouseButtonColumn);
		
		
		/*
		 * Configuring 3rd column for displaying and editing key of mouse;
		 */
		TableColumn<Command, MouseButton> keyboardKeyColumn = new TableColumn<>("Mouse button"); 
		keyboardKeyColumn.setCellValueFactory(new PropertyValueFactory<>("mbutton"));
		keyboardKeyColumn.setSortable(false);
		
		ObservableList<MouseButton> keyboardKeyColumnData = FXCollections.observableArrayList(MouseButton.class.getEnumConstants());
		keyboardKeyColumnData.add(0, null);
		keyboardKeyColumnData.remove(MouseButton.NONE);
		
		keyboardKeyColumn.setCellFactory(ComboBoxTableCell.forTableColumn(keyboardKeyColumnData));
		keyboardKeyColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setMbutton(t.getNewValue());
		});
		getColumns().add(keyboardKeyColumn);
		
		
		/*
		 * Configuring 4th column for displaying and editing coordinates of mouse commands;
		 */
		TableColumn<Command, Point2D> coordinatesColumn = new TableColumn<>("Coordinates"); 
		coordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));
		coordinatesColumn.setSortable(false);
		coordinatesColumn.setCellFactory(e -> new PointTableCell());
		coordinatesColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setCoordinates(t.getNewValue());
		});
		getColumns().add(coordinatesColumn);
		
		
		/*
		 * Configuring 5th column for displaying and editing count of using commands;
		 */
		TableColumn<Command, Number> ñountColumn = new TableColumn<>("Count"); 
		ñountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
		ñountColumn.setSortable(false);
		ñountColumn.setCellFactory(TextFieldTableCell.<Command, Number>forTableColumn(new NumberStringConverter()));
		ñountColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setCount(t.getNewValue().intValue());
		});
		getColumns().add(ñountColumn);
		
		
		/*
		 * Configuring 6th column for displaying and editing type of delay of command;
		 */
		TableColumn<Command, Number> delayColumn = new TableColumn<>("Delay (ms)"); 
		delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
		delayColumn.setCellFactory(TextFieldTableCell.<Command, Number>forTableColumn(new NumberStringConverter()));
		delayColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setDelay(t.getNewValue().intValue());
		});
		delayColumn.setSortable(false);
		getColumns().add(delayColumn);

	}
	
	/**
	 * 
	 * TableCell implementation for editing Point2D (coordinates of mouse command) column cells.
	 * 
	 * @author Saltykov Dmitry (25DimoN25)
	 * 
	 */
	private class PointTableCell extends TableCell<Command, Point2D> {
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
	
}
