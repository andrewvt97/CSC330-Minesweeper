package application;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import java.util.HashMap;



public class Main extends Application {
	
	private boolean isFirstClick = true;
	private final int ROW_SIZE = 8;
	private final int COL_SIZE = 10;
	private final int flags = 10;
	private Tile myTiles[][] = new Tile[ROW_SIZE][COL_SIZE];
	String flagLocation = "";
	int numberFlags = 0;
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	HashMap<String, Integer> noFlagMap = new HashMap<String, Integer>();
	
	@Override	
	public void start(Stage primaryStage) {
		
			

		GridPane grid = new GridPane();

		for(int row = 0; row < ROW_SIZE; row++) {
			for(int col = 0; col < COL_SIZE; col++) {
			
				VBox vbox = new VBox();
				vbox.setPrefSize(50, 50);
				 
				vbox.setStyle("-fx-background-color: lightgreen;");
				
				
				// Set the border style
				vbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

				
				vbox.setOnMouseClicked(e -> {
					
					Node n = (Node)e.getSource();     
					Integer r1 = grid.getRowIndex(n);     
					Integer c1 = grid.getColumnIndex(n); 

					VBox vbox1 = (VBox)n;
					
					System.out.println("No");
					
					if (e.getButton() == MouseButton.PRIMARY) {
						
						System.out.println("Yes");
						
//						if ((boolean)rect1.getProperties().containsKey("hasFlag")) {
//							System.out.println("haha");
//							return;
//						}
						
						if (!isFirstClick && myTiles[r1][c1].isClickedState() == true) {
							return;
						}
						
						if (isFirstClick == true) {
							isFirstClick = false;
							createGame(r1, c1);
						}
					
						
						if (myTiles[r1][c1].getInfo() == 'r') {

							vbox.setStyle("-fx-background-color: red;");
						}
						else {
							vbox.setStyle("-fx-background-color: beige;");
							if (myTiles[r1][c1].getInfo() == '0') {
								findEmptyBlocks(r1,c1,grid, COL_SIZE, ROW_SIZE, myTiles);
							}
							else {
								Text text = new Text(Character.toString(myTiles[r1][c1].getInfo()));
							    vbox.setAlignment(Pos.CENTER);
							    vbox.getChildren().add(text);
							}
						}
						myTiles[r1][c1].setClickedState(true);
					}
					else if (e.getButton() == MouseButton.SECONDARY) {
						System.out.println("oh no");

					}
					e.consume();
					

				});
				
				grid.add(vbox, col, row);
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
		VBox vbox = new VBox();
		Text text;
		
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
				if (Info[i][j].getInfo() != 'r') {
//					if ((boolean)node.getProperties().containsKey("hasFlag")) {
//						Text flagText = (Text) node.getUserData();
//				    	 grid.getChildren().remove(flagText);
//				    	 node.setUserData(null);
//				    	 node.getProperties().remove("hasFlag");
//					}
					vbox = (VBox) node;
					vbox.setStyle("-fx-background-color: beige;"); // change to css file later
					if (Info[i][j].getInfo() == '0') {
						
						Info[i][j].setClickedState(true);
						findEmptyBlocks(i, j, grid, numCols, numRows, Info);
					}
					else  { // must be another number then
						
						Info[i][j].setClickedState(true);
						text = new Text(Character.toString(myTiles[i][j].getInfo()));
						vbox.setAlignment(Pos.CENTER);
						vbox.getChildren().add(text);
					}
				}
				
			}
		}
		
		return;
	}
	
	private void createGame(int rowPos, int colPos) {
		
		
		for (int i = rowPos -1; i < rowPos + 2; i++) {
			if (i < 0 || i >= ROW_SIZE) {
				continue;
			}
			for (int j = colPos - 1; j < colPos + 2; j++) {
				if (j < 0 || j >= COL_SIZE  ) {
					continue;
				}
				flagLocation +=  Integer.toString(i);
				flagLocation +=  "," + Integer.toString(j);
				noFlagMap.put(flagLocation, 1);
				flagLocation = "";
					
			}
		}
		
		System.out.println(noFlagMap);
		
		while (numberFlags < flags) {
			flagLocation +=  Integer.toString((int)(Math.random() * ROW_SIZE));
			flagLocation +=  "," + Integer.toString((int)(Math.random() * COL_SIZE));
//			System.out.println(flagLocation);
			while (map.containsKey(flagLocation) || noFlagMap.containsKey(flagLocation)) {
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
	}
	
	

	
}
