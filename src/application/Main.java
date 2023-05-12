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
	private int seconds = 0;
	
	@Override	
	public void start(Stage primaryStage) {
		//timer?
		BorderPane bp = new BorderPane();
		HBox top = new HBox();
		Label timer = new Label("00:00");
		timer.setStyle("-fx-font: 24 impact;");
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
				seconds++;
				int minutes = seconds / 60;
				int remaining = seconds % 60;
				timer.setText(String.format("%02d:%02d", minutes, remaining));
	}));
		
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		top.setAlignment(Pos.CENTER_RIGHT);
		top.setPadding(new Insets(10, 10, 10, 10));
		top.getChildren().add(timer);
		
		bp.setTop(top);
		
		StackPane centerP = new StackPane();
		centerP.setPadding(new Insets(10, 10, 10, 10));
		centerP.setAlignment(Pos.CENTER);
		
		Minesweeper minesweeper = new Minesweeper(primaryStage);
		Node gameNode = minesweeper.getNode();
		Group MSG = new Group();
		MSG.getChildren().add(gameNode);
		
		centerP.getChildren().add(MSG);
		
		bp.setCenter(centerP);
		
		primaryStage.setScene(new Scene(bp, 800, 650));
		
		timeline.play();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
