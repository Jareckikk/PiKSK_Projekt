package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	 
	
	private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("windowView.fxml"));
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Projektowanie i Konfiguracja Sieci Komputerowych");       
        primaryStage.setScene(new Scene(root, 480, 380));
        primaryStage.show();
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}
