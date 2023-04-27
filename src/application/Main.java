package application;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
					allColors[row][col] = 'r';
				}
				else {
//					System.out.println(minesInProximity(map, flagLocation, ROW_SIZE, COL_SIZE));
					allColors[row][col] = (char)(minesInProximity(map, flagLocation, ROW_SIZE, COL_SIZE)+ '0');
					System.out.println(allColors[row][col]);
				}
			}
		}
		System.out.println("Original configuration of the board:\n");
		printColors();		

		

		GridPane grid = new GridPane();

		for(int row = 0; row < ROW_SIZE; row++) {
			for(int col = 0; col < COL_SIZE; col++) {
			
				Rectangle rect = new Rectangle(50,50);
				
				rect.setFill(Color.LIGHTGREEN);
				
			
				rect.setStrokeWidth(2);
				rect.setStroke(Color.BLACK);
				
				rect.setOnMouseClicked(e -> {
					Node n = (Node)e.getSource();     
					Integer r1 = grid.getRowIndex(n);     
					Integer c1 = grid.getColumnIndex(n);  
				
					Rectangle rect1 = (Rectangle)n;  
					if (allColors[r1][c1] == 'r') {
						rect1.setFill(Color.RED);
					}
					else {
						Text text = new Text(Character.toString(allColors[r1][c1]));
						GridPane.setHalignment(text, HPos.CENTER); // align text to center horizontally
					    GridPane.setValignment(text, VPos.CENTER); // align text to center vertically
					    grid.add(text, c1, r1);
					}
					

				});
				
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
	
	private int minesInProximity(HashMap <String, Integer>map, String position, int rowSize, int colSize ) {
		
		String location[] = position.split(",");
		int row = Integer.parseInt(location[0]);
		int column = Integer.parseInt(location[1]);
		int mines = 0;
		
		// Don't need to worry about going out of bounds because the hash map takes care of that
		String flagLocation = "";
		
		for (int i = row -1; i < row + 2; i++) {
			for (int j = column - 1; j < column + 2; j++) {
				// Do not need to care about avoiding checking current spot because it won't have a flag anyway
				// This avoids the if statement check everytime although it checks 9 each time
				flagLocation +=  Integer.toString(i);
				flagLocation +=  "," + Integer.toString(j);
				if (map.containsKey(flagLocation)) {
					mines += 1;
				}
				
				flagLocation = "";
			}
		}
		
//		System.out.println(row);
//		System.out.println(column);
		
		return mines;
	}

	
}
