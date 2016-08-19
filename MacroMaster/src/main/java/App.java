import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	private ExecutorService executor = Executors.newFixedThreadPool(10);
	
	private TabPane commandListTab;
	private MainMenu menu;
	private StatusBar status;

	private long newMacrosId = 1L;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane rootPane = new BorderPane();

		commandListTab = new TabPane();
		rootPane.setCenter(new StackPane(new Label("Press File -> New"), commandListTab));
		
		menu = new MainMenu();
		menu.setOnActionNew(e -> {
			CommandListTab tab = new CommandListTab("New macros " + newMacrosId);		
			commandListTab.getTabs().add(tab);
			newMacrosId++;
		});
		menu.setOnActionExit(e -> Platform.exit());
		
		
		ControlBar controls = new ControlBar();
		controls.setState(ControlBar.STOPPED);
		controls.setOnActionPlay(e -> {
			controls.setState(ControlBar.PLAYING);
			CommandListTab currentTab = (CommandListTab) commandListTab.getSelectionModel().getSelectedItem();
			TableView<Command> commands = currentTab.getCommands();
			executor.execute(() -> {
				for (Command command : commands.getItems()) {
					Platform.runLater(() -> {
						commands.getSelectionModel().clearSelection();
						commands.getSelectionModel().select(command);	
						status.setText("Using: " + command);
					}); 
					command.useCommand();
				}
				commands.getSelectionModel().clearSelection();
				controls.setState(ControlBar.STOPPED);
			});	
			
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

		
		Scene scene = new Scene(rootPane, 600, 500);
		primaryStage.setScene(scene);	
		primaryStage.setTitle("MacroMaster");
		primaryStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
}
