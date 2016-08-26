package gui.commandlist;

import command.Command;
import command.CommandType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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
		 * Configuring 3rd column for displaying and editing key of mouse;
		 */
		TableColumn<Command, MouseButton> mouseButtonColumn = new TableColumn<>("Mouse button"); 
		mouseButtonColumn.setCellValueFactory(new PropertyValueFactory<>("mbutton"));
		mouseButtonColumn.setSortable(false);
		
		ObservableList<MouseButton> mouseButtonColumnData = FXCollections.observableArrayList(MouseButton.class.getEnumConstants());
		mouseButtonColumnData.add(0, null);
		mouseButtonColumnData.remove(MouseButton.NONE);
		
		mouseButtonColumn.setCellFactory(ComboBoxTableCell.forTableColumn(mouseButtonColumnData));
		mouseButtonColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setMbutton(t.getNewValue());
		});
		getColumns().add(mouseButtonColumn);
		
		
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
		 * Configuring 2nd column for displaying and editing pressing key of keyboard;
		 */
		TableColumn<Command, KeyCode> keyboardKeyColumn = new TableColumn<>("Keyboard key");
		keyboardKeyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
		keyboardKeyColumn.setSortable(false);
		keyboardKeyColumn.setCellFactory(e -> new KeyTableCell());
		keyboardKeyColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setKey(t.getNewValue());
		});
		getColumns().add(keyboardKeyColumn);
		
		
		/*
		 * Configuring 5th column for displaying and editing count of using commands;
		 */
		TableColumn<Command, Number> countColumn = new TableColumn<>("Count"); 
		countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
		countColumn.setSortable(false);
		countColumn.setCellFactory(TextFieldTableCell.<Command, Number>forTableColumn(new NumberStringConverter()));
		countColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setCount(t.getNewValue().intValue());
		});
		getColumns().add(countColumn);
		
		/*
		 * Configuring 6th column for displaying and editing type of delay of command;
		 */
		TableColumn<Command, Number> delayColumn = new TableColumn<>("Delay (ms)"); 
		delayColumn.setCellValueFactory(new PropertyValueFactory<>("delay"));
		delayColumn.setSortable(false);
		delayColumn.setCellFactory(TextFieldTableCell.<Command, Number>forTableColumn(new NumberStringConverter()));
		delayColumn.setOnEditCommit(t -> {
			t.getTableView().getItems().get(t.getTablePosition().getRow()).setDelay(t.getNewValue().intValue());
		});
		getColumns().add(delayColumn);

	}
		
}
