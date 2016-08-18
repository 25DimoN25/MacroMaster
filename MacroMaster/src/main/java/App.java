import command.Command;
import gui.CommandListTab;
import gui.ControlBar;
import gui.MainMenu;
import gui.StatusBar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	private TabPane scriptTabs;
	private MainMenu menu;
	private StatusBar status;

	private long newMacrosId = 1L;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane rootPane = new BorderPane();

		scriptTabs = new TabPane();
		rootPane.setCenter(new StackPane(new Label("Press File -> New"), scriptTabs));
		
		menu = new MainMenu();
		menu.setOnActionNew(e -> {
			CommandListTab tab = new CommandListTab("New macros " + newMacrosId);		
			scriptTabs.getTabs().add(tab);
			newMacrosId++;
		});
		menu.setOnActionExit(e -> Platform.exit());
		
		ControlBar controls = new ControlBar();	
		controls.setOnActionPlay(e -> {
			CommandListTab currentTab = (CommandListTab) scriptTabs.getSelectionModel().getSelectedItem();
			for (Command command : currentTab.getCommands().getItems()) {
				command.useCommand();
			}
		});
		controls.setOnActionPause(e -> {
			System.out.println("pause pressed");
		});
		controls.setOnActionStop(e -> {
			System.out.println("stop pressed");
		});
		
		rootPane.setTop(new VBox(menu, controls));
	
		
		status = new StatusBar();	
		rootPane.setBottom(status);	

		
		Scene scene = new Scene(rootPane, 500, 500);
		primaryStage.setScene(scene);	
		primaryStage.setTitle("MacroMaster");
		primaryStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
}
