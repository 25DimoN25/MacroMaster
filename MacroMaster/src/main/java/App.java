

import gui.CommandListTab;
import gui.ControlBar;
import gui.MainMenu;
import gui.StatusBar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	private TabPane inprogramCommandLists;
	private MainMenu menu;
	private StatusBar status;
	

	private long newMacrosId = 1L;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane rootPane = new BorderPane();

		inprogramCommandLists = new TabPane();
		
		
		menu = new MainMenu();
		menu.setOnActionNew(e -> {
			inprogramCommandLists.getTabs().add(new CommandListTab("New macros " + newMacrosId));
			newMacrosId++;
		});
		menu.setOnActionExit(e -> Platform.exit());
		
		ControlBar controls = new ControlBar();	
	
		status = new StatusBar();

		
		rootPane.setCenter(new StackPane(new Label("Press File -> New"), inprogramCommandLists));
		rootPane.setTop(new VBox(menu, controls));
		rootPane.setBottom(status);	
		
		primaryStage.setScene(new Scene(rootPane, 500, 500));
		primaryStage.setTitle("MacroMaster");
		primaryStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
}
