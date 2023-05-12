package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {
	
	@Override	
	public void start(Stage primaryStage) {
		primaryStage.setResizable(false);
		new Minesweeper(primaryStage);	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
