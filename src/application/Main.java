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
	private Tile myTiles[][] = new Tile[ROW_SIZE][COL_SIZE];
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
	
		
	
		for(int row = 0; row < ROW_SIZE; row++) {
			for(int col = 0; col < COL_SIZE; col++) {
				flagLocation = Integer.toString(row) + "," + Integer.toString(col);
				myTiles[row][col] = new Tile();
				if (map.containsKey(flagLocation)) {
					myTiles[row][col].setInfo('r');
				}
				else {
//					System.out.println(minesInProximity(map, flagLocation, ROW_SIZE, COL_SIZE));
					myTiles[row][col].setInfo((char)(minesInProximity(map, flagLocation, ROW_SIZE, COL_SIZE)+ '0'));

				}
			}
		}
		System.out.println("Original configuration of the board:\n");
		printInfo();		

		

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
					
					if (myTiles[r1][c1].isClickedState() == true) {
						return;
					}
				
					Rectangle rect1 = (Rectangle)n;  
					if (myTiles[r1][c1].getInfo() == 'r') {
						rect1.setFill(Color.RED);
					}
					else {
						findEmptyBlocks(r1,c1,grid, COL_SIZE, ROW_SIZE, myTiles);
						rect1.setFill(Color.BEIGE);
						if (myTiles[r1][c1].getInfo() != '0') {
						Text text = new Text(Character.toString(myTiles[r1][c1].getInfo()));
						GridPane.setHalignment(text, HPos.CENTER); // align text to center horizontally
					    GridPane.setValignment(text, VPos.CENTER); // align text to center vertically
					    grid.add(text, c1, r1);
						}
					}
					myTiles[r1][c1].setClickedState(true);
					

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
	
	private void printInfo() {
		for(int row = 0; row < ROW_SIZE; row++) {
			for(int col = 0; col < COL_SIZE; col++) {
				System.out.print(myTiles[row][col].getInfo() + " ");
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
				// This avoids the if statement check everytime although it checks 9 instead of 8
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
	
	
	private void findEmptyBlocks(int row, int col, GridPane grid, int numCols, int numRows, Tile Info[][]) { // uses recursion
		Node node;
		Rectangle rectangle;
		for (int i = row -1; i < row + 2; i++) {
			if (i < 0 || i >= numRows) {
				continue;
			}
			for (int j = col - 1; j < col + 2; j++) {
				if (j < 0 || j >= numCols || (i  == row && j == col) ) {
					continue;
				}
				if (Info[i][j].isClickedState() == true) {
					continue;
				}
				node = grid.getChildren().get(i * numCols + j);
				if (Info[i][j].getInfo() == '0') {
					rectangle = (Rectangle) node;
					rectangle.setFill(Color.BEIGE);
					Info[i][j].setClickedState(true);
					findEmptyBlocks(i, j, grid, numCols, numRows, Info);
				}
				
			}
		}
		
		return;
	}

	
}
