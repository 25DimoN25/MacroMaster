import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.tulskiy.keymaster.common.Provider;

import command.Command;
import gui.CommandListTab;
import gui.ControlBar;
import gui.MainMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class App extends Application {
	public static final String VER = "1.1-RELEASE";
	
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private CountDownLatch pauseBarrier = new CountDownLatch(1);
	
	private TabPane commandListTabs;
	private MainMenu menu;
	private Label status;
	private ControlBar controls;
	
	private long newMacrosId = 1L;

	private Provider provider; //Hotkey provider;
	private Future<?> task; //Only for interrupting current macros; 

	private Alert hotkeysDialog;
	private Alert aboutDialog;
	
	private FileChooser fileChooser;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/*
		 * Register hotkeys;
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
		
		
		/*
		 * Configurating menu actions;
		 */
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
		menu.setOnActionHotkeys(e -> {
			hotkeysDialog.show();
		});
		menu.setOnActionAbout(e -> {
			aboutDialog.show();
		});
		menu.setOnActionSaveAs(e -> {
			saveAs(primaryStage);				
		});
		menu.setOnActionSave(e -> {
			save(primaryStage);
		});
		menu.setOnActionOpen(e -> {
			open(primaryStage);
		});
		
		
		/*
		 * Configurating control bar actions;
		 */
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
			task.cancel(true); //interrupt current macros with using "Future";
		});
		
		
		/*
		 * Hotkeys dialog for menu;
		 */
		hotkeysDialog = new Alert(AlertType.NONE);
		hotkeysDialog.setContentText("CTRL + \tF9 - play macros\n"
									+ "\t\tF10 - pause macros\n"
									+ "\t\tF11 - stop macros\n"
									+ "\t\tF12 - close programm");
		hotkeysDialog.setTitle("Available hotkeys");
		hotkeysDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		
		
		/*
		 * About dialog for menu;
		 */
		aboutDialog = new Alert(AlertType.INFORMATION);
		Hyperlink link1 = new Hyperlink(null, new ImageView("github.png"));
		link1.setOnAction(e -> getHostServices().showDocument("https://github.com/25DimoN25/MacroMaster"));
		aboutDialog.setGraphic(link1);
		aboutDialog.setHeaderText("MacroMaster - simple program to make simple macroses!");
		aboutDialog.setContentText("version " + VER + "\n\n"
								 + "By Saltykov D.\n\n"
								 + "Click to image and visit GitHub for more information.");
		aboutDialog.setTitle("About");

		
		/*
		 * Open save dialog for menu;
		 */
		 fileChooser = new FileChooser();
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("XML macros", "*.xmlm"),
		         new ExtensionFilter("All Files", "*.*"));


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

	
	/**
	 * Playing macros from current selected tab.
	 */
	private void play() {	
		CommandListTab currentTab = (CommandListTab) commandListTabs.getSelectionModel().getSelectedItem();
		if (currentTab == null) {
			status.setText("Nothing to play");
		} else {
			TableView<Command> commands = currentTab.getCommands();
			
			task = executor.submit(() -> {	
				do {
					for (Command command : commands.getItems()) {
						
						/*
						 * Pause (if requested) between commands;
						 */
						if (controls.getState() == ControlBar.PAUSED) {
							try {
								pauseBarrier.await();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						
						/*
						 * Break if stopped;
						 */
						if (controls.getState() == ControlBar.STOPPED) {
							return;
						}
				
						/*
						 * UI "animation";
						 */
						Platform.runLater(() -> {
							commands.getSelectionModel().clearSelection();
							commands.getSelectionModel().select(command);	
							status.setText("Using: " + command);
						});
						
						/*
						 * Core of this function;
						 */
						command.useCommand();
					}
				} while (menu.isRepeat());
				
				/*
				 * UI "animation" for ends;
				 */
				Platform.runLater(() -> {
					commands.getSelectionModel().clearSelection();
					controls.setState(ControlBar.STOPPED);
					status.setText("Finished!");
					java.awt.Toolkit.getDefaultToolkit().beep();
				});
				
			});	
			
		}
		
	}
	
	
	/**
	 * Removing pause in "play" function.
	 */
	public void resume() {
		pauseBarrier.countDown();
		pauseBarrier = new CountDownLatch(1);
	}
	
	
	/**
	 * Showing "save as" dialog with current tab name and write file.
	 */
	public void saveAs(Stage owner) {
		CommandListTab currentTab = (CommandListTab) commandListTabs.getSelectionModel().getSelectedItem();
		if (currentTab == null) {
			status.setText("Nothing to save!");
		} else {
			
			fileChooser.setTitle("Save macros as ..");
			fileChooser.setInitialFileName(currentTab.getText()+".xmlm");
			if (currentTab.getCurrentFile() != null) {
				fileChooser.setInitialDirectory(currentTab.getCurrentFile().getParentFile());
			}
			
			File destinationDir = fileChooser.showSaveDialog(owner);
			
			if (destinationDir != null) {
				
				try {
					writeFile(currentTab.getCommands().getItems(), destinationDir);
					currentTab.setCurrentFile(destinationDir);
					currentTab.setText(destinationDir.getName().substring(0, destinationDir.getName().lastIndexOf('.')));
					status.setText("Saved!");
				} catch (Exception e) {
					status.setText("Error: can't save file");
					e.printStackTrace();
				}
			
			}
		}
	}
	
	/**
	 * Rewrite current macros. If current macros never saved - using saveAs. 
	 */
	private void save(Stage owner) {
		CommandListTab currentTab = (CommandListTab) commandListTabs.getSelectionModel().getSelectedItem();
		if (currentTab == null) {
			status.setText("Nothing to save!");
		} else if (currentTab.getCurrentFile() == null) {
			saveAs(owner);
		} else {
			try {
				writeFile(currentTab.getCommands().getItems(), currentTab.getCurrentFile());
				status.setText("Saved!");
			} catch (Exception e) {
				status.setText("Error: can't save file");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Core of saving macros.
	 * @throws Exception 
	 */
	private void writeFile(ObservableList<Command> sourse, File destination) throws Exception {
		
		try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(destination))) {
			
			encoder.setPersistenceDelegate(Point2D.class, 
					new DefaultPersistenceDelegate(
							new String[] {"x", "y"}));
			encoder.setPersistenceDelegate(Command.class, 
					new DefaultPersistenceDelegate(
							new String[] {"type", "key", "mbutton", "coordinates", "count", "delay"})); 

			for (Command command : sourse) {
				encoder.writeObject(command);
			}
			
		} catch (Exception exp) {
			throw exp;
		}	
	}
	
	
	/**
	 * Show file open dialog, read file and creating tab with this file macros.
	 */
	private void open(Stage owner) {
		fileChooser.setTitle("Open macros");

		File selectedFile = fileChooser.showOpenDialog(owner);
		if (selectedFile != null) {
			try {
				ObservableList<Command> commands = readFile(selectedFile);
				
				String fileName = selectedFile.getName();
				if (selectedFile.getName().matches(".+\\.xmlm$")) {
					fileName = fileName.replaceAll("\\.xmlm$", "");
				}

				CommandListTab newMacros = new CommandListTab(fileName);
				newMacros.setCurrentFile(selectedFile);
				newMacros.getCommands().getItems().addAll(commands);
				commandListTabs.getTabs().add(newMacros);
				
			} catch (Exception exp) {
				status.setText("Error: can't load " + selectedFile.getName());
				exp.printStackTrace();
			}

			status.setText("Opened!");
		}
	}
	
	
	/**
	 * 
	 */
	private ObservableList<Command> readFile(File file) throws Exception {
		ObservableList<Command> commands = FXCollections.observableArrayList();
		
		try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(file))) {
			while (true) {
				Command newCommand = (Command) decoder.readObject();
				commands.add(newCommand);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// "DOCS: read(): if the stream contains no objects (or no more objects) - throws ArrayIndexOutOfBoundsException"
			// Its allright, do nothing
		} catch (Exception e) {
			throw e;
		}
		
		return commands;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
