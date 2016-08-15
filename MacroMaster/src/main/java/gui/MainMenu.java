package gui;


import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

//TODO
public class MainMenu extends MenuBar {
	public MainMenu() {
		
		getMenus().add(new Menu("File", null,
					new MenuItem("New"),
					new SeparatorMenuItem(),
					new MenuItem("Open"),
					new SeparatorMenuItem(),
					new MenuItem("Save"),
					new MenuItem("Save as ..."),
					new SeparatorMenuItem(),
					new MenuItem("Exit")
				));
		
		
	}
}
