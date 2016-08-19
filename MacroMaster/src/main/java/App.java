import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tulskiy.keymaster.common.Provider;

import command.Command;
import gui.CommandListTab;
import gui.ControlBar;
import gui.MainMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private CountDownLatch pauseBarrier = new CountDownLatch(1);
	
	private TabPane commandListTabs;
	private MainMenu menu;
	private Label status;
	private ControlBar controls;
	
	private long newMacrosId = 1L;

	private Provider provider;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/*
		 * Register hotkeys
		 */
		provider = Provider.getCurrentProvider(false);
		provider.register(javax.swing.KeyStroke.getKeyStroke("control F9"), e -> {
			Platform.runLater(() -> {
				controls.fireButton(ControlBar.PLAYING);
			});
		});
		provider.register(javax.swing.KeyStroke.getKeyStroke("control F10"), e -> {
			Platform.runLater(() -> {
				controls.fireButton(ControlBar.PAUSED);
			});
		});
		provider.register(javax.swing.KeyStroke.getKeyStroke("control F11"), e -> {
			Platform.runLater(() -> {
				controls.fireButton(ControlBar.STOPPED);
			});
		});
		provider.register(javax.swing.KeyStroke.getKeyStroke("control F12"), e -> {
			Platform.runLater(() -> {
				provider.reset();
				provider.stop();
				executor.shutdown();
				Platform.exit();
			});
		});
		
		
		menu = new MainMenu();
		menu.setOnActionNew(e -> {
			CommandListTab tab = new CommandListTab("New macros " + newMacrosId);		
			commandListTabs.getTabs().add(tab);
			newMacrosId++;
		});
		menu.setOnActionExit(e -> {
			provider.reset();
			provider.stop();
			executor.shutdown();
			Platform.exit();
		});
		

		controls = new ControlBar();
		controls.setState(ControlBar.STOPPED);
		controls.setOnActionPlay(e -> {
			if (commandListTabs.getSelectionModel().getSelectedItem() == null) {
				status.setText("Nothing to play!");
				return;
			}
				
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
		
			
		status = new Label();
		commandListTabs = new TabPane();
	
		BorderPane rootPane = new BorderPane();
		rootPane.setBottom(status);
		rootPane.setTop(new VBox(menu, controls));
		rootPane.setCenter(new StackPane(new Label("Press File -> New"), commandListTabs));
		
		Scene scene = new Scene(rootPane, 600, 500);
		primaryStage.setScene(scene);	
		primaryStage.setTitle("MacroMaster");
		primaryStage.setOnCloseRequest(e -> {
			provider.reset();
			provider.stop();
			executor.shutdown();
		});
		primaryStage.getIcons().add(new Image("icon.png"));
		primaryStage.show();
	}

	
	private void play() {	
		CommandListTab currentTab = (CommandListTab) commandListTabs.getSelectionModel().getSelectedItem();
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
			
			Platform.runLater(() -> {
				commands.getSelectionModel().clearSelection();
				controls.setState(ControlBar.STOPPED);
				status.setText("Finished!");
				java.awt.Toolkit.getDefaultToolkit().beep();
			});
			
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
