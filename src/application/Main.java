package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import java.util.HashMap;



public class Main extends Application {
	
	private final int ROW_SIZE = 8;
	private final int COL_SIZE = 10;
	private final int flags = 10;
	private char[][] allColors;
	String flagLocation = "";
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	@Override	
	public void start(Stage primaryStage) {
		
		int numberFlags = 0;
		
		while (numberFlags < flags) {
			flagLocation +=  Integer.toString((int)(Math.random() * ROW_SIZE));
			flagLocation +=  "," + Integer.toString((int)(Math.random() * COL_SIZE));
//			System.out.println(flagLocation);
			while (map.containsKey(flagLocation)) {
				flagLocation = "";
				flagLocation +=  Integer.toString((int)(Math.random() * ROW_SIZE));
				flagLocation +=  "," + Integer.toString((int)(Math.random() * COL_SIZE));
			}
			map.put(flagLocation, 1);
			numberFlags += 1;
			flagLocation = "";
		}
		
		System.out.println(map);
		
		allColors = new char[ROW_SIZE][COL_SIZE];

		for(int row = 0; row < ROW_SIZE; row++) {
			for(int col = 0; col < COL_SIZE; col++) {
				flagLocation = Integer.toString(row) + "," + Integer.toString(col);
				if (map.containsKey(flagLocation)) {
					allColors[row][col] = 'g';
				}
				else {
					allColors[row][col] = 'r';
				}
			}
		}
		System.out.println("Original configuration of the board:\n");
		printColors();		


		GridPane grid = new GridPane();

		for(int row = 0; row < ROW_SIZE; row++) {
			for(int col = 0; col < COL_SIZE; col++) {
			
				Rectangle rect = new Rectangle(50,50);
				
				if (allColors[row][col] == 'g') {
					rect.setFill(Color.GREEN);
				} 
				else {
					rect.setFill(Color.RED);
				}
			
				rect.setStrokeWidth(2);
				rect.setStroke(Color.BLACK);
				
				grid.add(rect, col, row);
			}
		}
		Scene scene = new Scene(grid);  
		primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void printColors() {
		for(int row = 0; row < ROW_SIZE; row++) {
			for(int col = 0; col < COL_SIZE; col++) {
				System.out.print(allColors[row][col] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	

	
}
