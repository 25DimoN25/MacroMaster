package gui;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

//TODO
public class MainMenu extends MenuBar {
	
	private MenuItem neww = new MenuItem("New");
	private MenuItem open = new MenuItem("Open");
	private MenuItem save = new MenuItem("Save");
	private MenuItem saveAs = new MenuItem("Save as ...");
	private MenuItem exit = new MenuItem("Exit");
	private CheckMenuItem repeat = new CheckMenuItem("Repeat");
	private MenuItem hotkeys = new MenuItem("Hotheys");
	private MenuItem about = new MenuItem("About");
	
	public MainMenu() {
		

		getMenus().addAll(
				new Menu("File", null,
					neww,
					new SeparatorMenuItem(),
					open, 
					new SeparatorMenuItem(),
					save,
					saveAs, 
					new SeparatorMenuItem(),
					exit), 
				new Menu("Options", null,
					repeat), 
				new Menu("Help", null,
						hotkeys,
						new SeparatorMenuItem(),
						about
					));
	}
	
	
	public void setOnActionNew(EventHandler<ActionEvent> value) {
		neww.setOnAction(value);
	}
	
	public void setOnActionOpen(EventHandler<ActionEvent> value) {
		open.setOnAction(value);
	}
	
	public void setOnActionSave(EventHandler<ActionEvent> value) {
		save.setOnAction(value);
	}
	
	public void setOnActionSaveAs(EventHandler<ActionEvent> value) {
		saveAs.setOnAction(value);
	}

	public void setOnActionExit(EventHandler<ActionEvent> value) {
		exit.setOnAction(value);
	}
	
	public void setOnActionHotkeys(EventHandler<ActionEvent> value) {
		hotkeys.setOnAction(value);
	}
	
	public void setOnActionAbout(EventHandler<ActionEvent> value) {
		about.setOnAction(value);
	}
	
	public boolean isRepeat() {
		return repeat.isSelected();
	}




	
}
