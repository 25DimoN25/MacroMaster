import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import command.Command;
import gui.CommandListTab;
import gui.ControlBar;
import gui.MainMenu;
import gui.StatusBar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private CountDownLatch pauseBarrier = new CountDownLatch(1);
	
	private TabPane commandListTab;
	private MainMenu menu;
	private StatusBar status;
	private ControlBar controls;
	
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
		
		
		controls = new ControlBar();
		controls.setState(ControlBar.STOPPED);
		controls.setOnActionPlay(e -> {
			if (controls.getState() == ControlBar.PAUSED) {
				resume();
			} else {
				play();
			}
			controls.setState(ControlBar.PLAYING);
		});
		controls.setOnActionPause(e -> {
			controls.setState(ControlBar.PAUSED);
			status.setText("Paused...");
		});
		controls.setOnActionStop(e -> {
			controls.setState(ControlBar.STOPPED);
			resume();
			status.setText("Stopped");
		});
		
		rootPane.setTop(new VBox(menu, controls));
		
		status = new StatusBar();	
		rootPane.setBottom(status);	

		
		Scene scene = new Scene(rootPane, 600, 500);
		primaryStage.setScene(scene);	
		primaryStage.setTitle("MacroMaster");
		primaryStage.show();
	}

	
	private void play() {	
		CommandListTab currentTab = (CommandListTab) commandListTab.getSelectionModel().getSelectedItem();
		TableView<Command> commands = currentTab.getCommands();
		
		executor.execute(() -> {
			
			do {
				for (Command command : commands.getItems()) {
					
					if (controls.getState() == ControlBar.PAUSED) {
						try {
							pauseBarrier.await();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					
					if (controls.getState() == ControlBar.STOPPED) {
						return;
					}
			
					Platform.runLater(() -> {
						commands.getSelectionModel().clearSelection();
						commands.getSelectionModel().select(command);	
						status.setText("Using: " + command);
					}); 
					command.useCommand();
					
				}
			} while (menu.isRepeat());
			
			commands.getSelectionModel().clearSelection();
			controls.setState(ControlBar.STOPPED);
			
		});	
	}
	
	public void resume() {
		pauseBarrier.countDown();
		pauseBarrier = new CountDownLatch(1);
	}
		
	public static void main(String[] args) {
		launch(args);
	}
	
}
