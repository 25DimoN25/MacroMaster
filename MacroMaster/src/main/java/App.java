import gui.CommandList;
import gui.ControlBar;
import gui.MainMenu;
import gui.StatusBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane rootPane = new BorderPane();
		
			

		TabPane inprogramCommandLists = new TabPane();
		inprogramCommandLists.getTabs().addAll(
				new Tab("1st prog", new CommandList()),
				new Tab("2st prog", new CommandList()),
				new Tab("3st prog", new CommandList())
				);	
		
		rootPane.setCenter(inprogramCommandLists);
		
		
		MainMenu menu = new MainMenu();
		ControlBar controls = new ControlBar();	
		rootPane.setTop(new VBox(menu, controls));
		
		
		StatusBar status = new StatusBar();
		rootPane.setBottom(status);
		
		
		primaryStage.setScene(new Scene(rootPane, 500, 500));
		primaryStage.setTitle("MacroMaster");
		primaryStage.show();
	}

}
