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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tulskiy.keymaster.common.Provider;

import command.Command;
import gui.CommandList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * JavaFX Application.
 */
public class App extends Application {
	private static final String VER = "1.2.5-RELEASE";
	
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private CountDownLatch pauseBarrier = new CountDownLatch(1);
	
	private TabPane commandListTabs;
	private MainMenu menu;
	private Label status;
	private ControlBar controls;
	
	private long newMacrosId = 1L; //new macros tab id;

	private Provider provider; //Hotkey provider;
	private Future<?> task; //Only for (stop) interrupt current macros; 

	private Alert tutorialDialog;
	private Alert hotkeysDialog;
	private Alert aboutDialog;
	
	private FileChooser fileChooser;
	
	private final Logger LOG = LoggerFactory.getLogger(App.class);
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/*
		 * Register hotkeys;
		 */
		provider = Provider.getCurrentProvider(false);
		provider.register(javax.swing.KeyStroke.getKeyStroke("control F9"), e -> {
			java.awt.Toolkit.getDefaultToolkit().beep();
			controls.fireButton(ControlBar.PLAYING);
		});
		provider.register(javax.swing.KeyStroke.getKeyStroke("control F10"), e -> {
			java.awt.Toolkit.getDefaultToolkit().beep();
			controls.fireButton(ControlBar.PAUSED);
		});
		provider.register(javax.swing.KeyStroke.getKeyStroke("control F11"), e -> {
			java.awt.Toolkit.getDefaultToolkit().beep();
			controls.fireButton(ControlBar.STOPPED);
		});
		provider.register(javax.swing.KeyStroke.getKeyStroke("control F12"), e -> {
			java.awt.Toolkit.getDefaultToolkit().beep();
			exit();
		});
		
		
		/*
		 * Configurating menu actions;
		 */
		menu = new MainMenu();
		menu.setSaveDisable(true);
		menu.setOnActionNew(e -> {
			CommandListTab tab = new CommandListTab("New macros " + newMacrosId);
			CommandList commands = tab.getCommands();
			commands.setOnKeyPressed(e2 -> {
				if (e2.getCode() == KeyCode.DELETE && controls.getState() == ControlBar.STOPPED) {
					commands.getItems().removeAll(commands.getSelectionModel().getSelectedItems());
				}
			});
			commandListTabs.getTabs().add(tab);
			newMacrosId++;
		});
		menu.setOnActionExit(e -> {
			provider.reset();
			provider.stop();
			executor.shutdown();
			Platform.exit();
		});
		menu.setOnActionTutorial(e -> {
			tutorialDialog.show();
		});
		menu.setOnActionHotkeys(e -> {
			hotkeysDialog.show();
		});
		menu.setOnActionAbout(e -> {
			java.awt.Toolkit.getDefaultToolkit().beep();
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
		controls.setDisable(true);
		controls.setOnActionPlay(e -> {
			playMacros();
		});
		controls.setOnActionPause(e -> {
			pauseMacros();
		});
		controls.setOnActionStop(e -> {
			stopMacros();
		});
		
		
		/*
		 * Tutorial dialog for menu;
		 */
		tutorialDialog = new Alert(AlertType.NONE);
		tutorialDialog.setContentText("Tutorial:\n\n"
									+ "1. Click File -> New macros to create new macros;\n\n"
									+ "2. Configure fields with command parameters upper \"Add command\" button."
									+ "You can combine any actions with mouse and keyboard in one command;\n\n"
									+ "3. Press \"Add command\" to add your new command;\n\n"
									+ "4. After making macros - you can run it. Press play button;\n\n"
									+ "5. You can edit your current macros at pause;\n\n"
									+ "6. You can add or remove (DELETE key) commands only in stopped state;\n\n"
									+ "7. Pause button stops macros only between commands (i.e. its not work"
									+ "with #BIG_NUMBER#-counted commands);\n\n"
									+ "8. Stop button can interrupt macros at any time;");
		tutorialDialog.setTitle("Tutorial");
		tutorialDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		
		
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
		Hyperlink link1 = new Hyperlink(null, new ImageView("img/github.png"));
		link1.setOnAction(e -> {
			getHostServices().showDocument("https://github.com/25DimoN25/MacroMaster");	
		});
		aboutDialog.setGraphic(link1);
		aboutDialog.setHeaderText("MacroMaster - simple program to make simple macroses!");
		aboutDialog.setContentText("Version " + VER + "\n\n"
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


		 /*
		  * TabPane;
		  */
		commandListTabs = new TabPane();
		commandListTabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldV, newV) -> {
			if (newV.intValue() < 0) {
				controls.setDisable(true);
				menu.setSaveDisable(true);
			} else {
				controls.setDisable(false);
				menu.setSaveDisable(false);
			}
		});
		
		status = new Label();
		
		BorderPane rootPane = new BorderPane();
		rootPane.setBottom(status);
		rootPane.setTop(new VBox(menu, controls));
		rootPane.setCenter(new StackPane(new Label("Press File -> New"), commandListTabs));
		
		Scene scene = new Scene(rootPane, 600, 500);
		primaryStage.setScene(scene);	
		primaryStage.setTitle("MacroMaster");
		primaryStage.setOnCloseRequest(e -> {
			exit();
		});
		primaryStage.getIcons().add(new Image("img/icon.png"));
		primaryStage.show();
		
		tutorialDialog.initOwner(primaryStage);
		hotkeysDialog.initOwner(primaryStage);
		aboutDialog.initOwner(primaryStage);
		
	}

	
	/**
	 * Shutdown application.
	 */
	private void exit() {
		provider.reset();
		provider.stop();
		executor.shutdown();
		Platform.exit();
	}

	
	/**
	 * Playing macros from current selected tab.
	 */
	private void playMacros() {		
		CommandListTab currentTab = (CommandListTab) commandListTabs.getSelectionModel().getSelectedItem();

		if (controls.getState() == ControlBar.PAUSED) {
			resumeMacros(currentTab);
		} else {
			task = executor.submit(() -> {	
				try {
					LOG.debug("Start playing macros");
					controls.setState(ControlBar.PLAYING);
					startGuiChanges(currentTab);
					
					do {
						for (Command command : currentTab.getCommands().getItems()) {
					
							if (controls.getState() == ControlBar.PAUSED) {
								pauseGuiChanges(currentTab);
								pauseBarrier.await();
								resumeGuiChanges(currentTab);
							}
							
							stepGuiChanges(currentTab, command);
							LOG.debug("Use command {}", command);
							command.useCommand();

						}
						
					} while (menu.isRepeat());
				
					LOG.debug("Finish playing macros");
					controls.setState(ControlBar.STOPPED);
					finishGuiChanges(currentTab);
					
				} catch (InterruptedException exeption) {
					stopGuiChanges(currentTab);
				}
			});	
		}	
	}
	
	
	/**
	 * Pause current macros.
	 */
	private void pauseMacros() {
		LOG.debug("Pause request");
		controls.setState(ControlBar.PAUSED);
	}
	
	
	/**
	 * Resume paused macros.
	 */
	private void resumeMacros(CommandListTab currentTab) {
		LOG.debug("Resume macros");
		controls.setState(ControlBar.PLAYING);
		pauseBarrier.countDown();
		pauseBarrier = new CountDownLatch(1);
	}
	
	
	/**
	 * Stop current macros.
	 */
	private void stopMacros() {
		LOG.debug("Interrupt macros");
		controls.setState(ControlBar.STOPPED);
		task.cancel(true);
	}
	
	
	/**
	 * UI changes for start macros.
	 */
	private void startGuiChanges(CommandListTab currentTab) {
		Platform.runLater(() -> {
			currentTab.setAddButtonDisable(true);
			currentTab.setCommandListDisable(true);
			currentTab.setGraphic(new ImageView("img/play_small.png"));
			currentTab.setClosable(false);
		});
	}
	
	
	/**
	 * UI changes for every step macros.
	 */
	private void stepGuiChanges(CommandListTab currentTab, Command command) {
		Platform.runLater(() -> {
			currentTab.getCommands().getSelectionModel().clearSelection();
			currentTab.getCommands().getSelectionModel().select(command);
			status.setText("Running: " + command);
		});
	}
	
	
	/**
	 * UI changes for finish macros.
	 */
	private void finishGuiChanges(CommandListTab currentTab) {
		Platform.runLater(() -> {
			currentTab.setAddButtonDisable(false);
			currentTab.setCommandListDisable(false);
			currentTab.setGraphic(null);
			currentTab.setClosable(true);
			
			currentTab.getCommands().getSelectionModel().clearSelection();
			status.setText("Finished!");
			java.awt.Toolkit.getDefaultToolkit().beep();
		});
	}
	
	
	/**
	 * UI changes for pause macros.
	 */
	private void pauseGuiChanges(CommandListTab currentTab) {
		Platform.runLater(() -> {
			currentTab.setCommandListDisable(false);
			currentTab.setGraphic(new ImageView("img/pause_small.png"));
			status.setText("Paused..");
		});
	}
	
	
	/**
	 * UI changes for resuming after pause.
	 */
	private void resumeGuiChanges(CommandListTab currentTab) {
		Platform.runLater(() -> {
			currentTab.setCommandListDisable(true);
			currentTab.setGraphic(new ImageView("img/play_small.png"));
		});
	}
	
	
	/**
	 * UI changes for finish macros.
	 */
	private void stopGuiChanges(CommandListTab currentTab) {
		Platform.runLater(() -> {
			commandListTabs.getTabs().stream()
									.filter(tab -> !tab.equals(currentTab))
									.forEach(e -> e.setDisable(false));
			
			menu.setDisable(false);
			
			currentTab.setAddButtonDisable(false);
			currentTab.setCommandListDisable(false);
			currentTab.setGraphic(null);
			currentTab.setClosable(true);
			
			status.setText("Stoped");
		});
	}
	
	
	/**
	 * Showing "save as" dialog with current tab name and write file.
	 */
	private void saveAs(Stage owner) {
		CommandListTab currentTab = (CommandListTab) commandListTabs.getSelectionModel().getSelectedItem();

		fileChooser.setTitle("Save macros as ..");
		fileChooser.setInitialFileName(currentTab.getText()+".xmlm");
		
		File selectedFile = fileChooser.showSaveDialog(owner);
		
		if (selectedFile != null) {
			fileChooser.setInitialDirectory(selectedFile.getParentFile());
			try {
				writeFile(currentTab.getCommands().getItems(), selectedFile);
				currentTab.setCurrentFile(selectedFile);
				currentTab.setText(selectedFile.getName().substring(0, selectedFile.getName().lastIndexOf('.')));
				status.setText("Saved!");
			} catch (Exception e) {
				status.setText("Error: can't save file");
				e.printStackTrace();
			}
		
		}
		
	}
	
	
	/**
	 * Rewrite current macros. If current macros never saved - using saveAs. 
	 */
	private void save(Stage owner) {
		CommandListTab currentTab = (CommandListTab) commandListTabs.getSelectionModel().getSelectedItem();
		if (currentTab.getCurrentFile() == null) {
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
			fileChooser.setInitialDirectory(selectedFile.getParentFile());
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
				commandListTabs.getSelectionModel().select(newMacros);
				
				status.setText("Opened!");
			} catch (Exception exp) {
				status.setText("Error: can't load " + selectedFile.getName());
				exp.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Core of reading macros from filesystem.
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
