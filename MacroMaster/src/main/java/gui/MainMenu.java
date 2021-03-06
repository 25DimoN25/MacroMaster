package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * Main menu of MacroMaster.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 *
 */
public class MainMenu extends MenuBar {
	
	private MenuItem neww = new MenuItem("New");
	private MenuItem open = new MenuItem("Open");
	private MenuItem save = new MenuItem("Save");
	private MenuItem saveAs = new MenuItem("Save as ...");
	private MenuItem exit = new MenuItem("Exit");
	private CheckMenuItem repeat = new CheckMenuItem("Repeat macros");
	private CheckMenuItem scrollTo = new CheckMenuItem("Scroll to running");
	private MenuItem others = new MenuItem("Others");
	private MenuItem tutorial = new MenuItem("Tutorial");
	private MenuItem hotkeys = new MenuItem("Hotkeys");
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
					repeat,
					scrollTo,
					new SeparatorMenuItem(),
					others), 
				new Menu("Help", null,
						tutorial,
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
	
	public void setOnActionOthers(EventHandler<ActionEvent> value) {
		others.setOnAction(value);
	}
	
	public void setOnActionTutorial(EventHandler<ActionEvent> value) {
		tutorial.setOnAction(value);
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
	
	public boolean isScrollToUsing() {
		return scrollTo.isSelected();
	}


	public void setSaveDisable(boolean disable) {
		save.setDisable(disable);
		saveAs.setDisable(disable);
	}

	public boolean isSaveDisable() {
		return save.isDisable() && saveAs.isDisable();
	}

}
