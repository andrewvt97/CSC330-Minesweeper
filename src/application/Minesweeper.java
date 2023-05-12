/**
 * 
 */
package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;

import static application.Constants.MINEONE;
import static application.Constants.MINETWO;
import static application.Constants.MINETHREE;
import static application.Constants.MINEFOUR;
import static application.Constants.MINEFIVE;
import static application.Constants.MINESIX;
import static application.Constants.MINESEVEN;
import static application.Constants.MINEEIGHT;
import static application.Constants.BOMB;
import static application.Constants.FLAG;
import static application.Constants.MUTE;
import static application.Constants.UNMUTE;
import static application.Constants.C418;

/**
 * @author andre
 *
 */
public class Minesweeper implements Game {
	
	private int safeTilesClicked;
	private int flagCounter;
	private boolean isFirstClick;
	private String level;
	private Stage primaryStage;
	private Board board;
	private GridPane grid;
	private boolean notBeaten = true;
	private Timeline timeline;
	
	private int seconds = 0;
	BorderPane bp = new BorderPane();
	Scene container = new Scene(bp, 800, 650);
	MediaPlayer media = new MediaPlayer(C418);
	
	public Minesweeper(Stage primaryStage) {
		this.primaryStage = primaryStage;
		startGame("Medium");
		
	}
	
	@Override
	public void startGame(String level) {
		//relocated handling of windows to startGame. 
		if (level.equals("Easy")){
				board = new EasyBoard();
		}
		else if (level.equals("Medium")){
			board = new MediumBoard();
		}
		else {
			board = new HardBoard();
		}
		
		grid = new GridPane();
		isFirstClick = true;
		safeTilesClicked = 0;
		flagCounter = board.getMines();
		
		createGrid();
		
		//window handling
		HBox top = new HBox();
		Label timer = new Label("00:00");
		StackPane centerP = new StackPane();
		
		timer.setStyle("-fx-font: 24 impact;");
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
				seconds++;
				int minutes = seconds / 60;
				int remaining = seconds % 60;
				timer.setText(String.format("%02d:%02d", minutes, remaining));
	}));
		
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		ImageView muteButton = createMuteButton(media);
		media.setAutoPlay(true);
		
		top.setSpacing(50);
		top.setAlignment(Pos.CENTER);
		top.setPadding(new Insets(10, 10, 10, 10));
		top.getChildren().addAll(muteButton, timer);
		
		bp.setTop(top);
		//bp.getChildren().addAll(time);
		
		centerP.setPadding(new Insets(10, 10, 10, 10));
		centerP.setAlignment(Pos.CENTER);
		
		Node gameNode = grid;
		Group MSG = new Group();
		MSG.getChildren().add(gameNode);

		top.setStyle("-fx-background-color: azure;");
		centerP.setStyle("-fx-background-color: azure;");
		
		centerP.getChildren().add(MSG);
		
		bp.setCenter(centerP);
		
		primaryStage.setScene(container);
		
		timeline.play();
		
	}

	/**
	 * @return the safeTilesClicked
	 */
	public int getSafeTilesClicked() {
		return safeTilesClicked;
	}

	/**
	 * @param safeTilesClicked the safeTilesClicked to set
	 */
	public void setSafeTilesClicked(int safeTilesClicked) {
		this.safeTilesClicked = safeTilesClicked;
	}

	/**
	 * @return the flagCounter
	 */
	public int getFlagCounter() {
		return flagCounter;
	}

	/**
	 * @param flagCounter the flagCounter to set
	 */
	public void setFlagCounter(int flagCounter) {
		this.flagCounter = flagCounter;
	}
	
	

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
		startGame(level);
	}

	public void createGrid() {
		Scene scene = new Scene(grid);  
		primaryStage.setScene(scene);
        primaryStage.show();
		
		for(int row = 0; row < board.getRowSize(); row++) {
			for(int col = 0; col < board.getColSize(); col++) {
			
				VBox vbox = new VBox();
				int tileSize = board.getTileSize();
				vbox.setPrefSize(tileSize, tileSize);
				 
				vbox.setStyle("-fx-background-color: lightgreen;");
				
				
				// Set the border style
				vbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

				
				vbox.setOnMouseClicked(e -> {
					
					Node n = (Node)e.getSource();     
					Integer r1 = GridPane.getRowIndex(n);     
					Integer c1 = GridPane.getColumnIndex(n); 

//						VBox vbox1 = (VBox)n; // not needed
					
					if (notBeaten) {
					
					if (e.getButton() == MouseButton.PRIMARY) {
						
						
						if ((boolean)vbox.getProperties().containsKey("hasFlag")) {
							return;
						}
						
						if (!isFirstClick && board.getMyTiles()[r1][c1].isClickedState() == true) {
							return;
						}
						
						if (isFirstClick == true) {
							isFirstClick = false;
							board.createBoardDetails(r1, c1);
						}
					
						board.getMyTiles()[r1][c1].setClickedState(true);
						if (board.getMyTiles()[r1][c1].getInfo() == 'r') {
							
							//File file = new File("src/images/Minesweeper-Bomb.png");
							//Image minesweeperBomb = new Image(file.toURI().toString());
							ImageView bombContainer = new ImageView(BOMB);
							
							bombContainer.setFitWidth(tileSize - 10); // Set the width to 40 pixels
							bombContainer.setFitHeight(tileSize - 10); // Set the height to 40 pixels
							vbox.setAlignment(Pos.CENTER);
						    vbox.getChildren().add(bombContainer);
						    vbox.setStyle("-fx-background-color: beige;");
						    
						    youLose();
						    
//						    setLevel("Hard"); testing set level function
						}
						else {
							safeTilesClicked += 1; // safe tiles clicked
							vbox.setStyle("-fx-background-color: beige;");
							if (board.getMyTiles()[r1][c1].getInfo() == '0') {
								findEmptyBlocks(r1,c1);
							}
							else {
								
								String text = new String(Character.toString(board.getMyTiles()[r1][c1].getInfo()));
							    vbox.setAlignment(Pos.CENTER);
							    setMineNum(text, vbox);
							}
						}
						
						
//							System.out.println(safeTilesClicked);
						if (safeTilesClicked == board.getSafeTiles()) {
							youWin();
						}
					}
					else if (e.getButton() == MouseButton.SECONDARY) {
						//youWin(primaryStage, container); //debugging                                            josh note :)
						
						if ((boolean)vbox.getProperties().containsKey("hasFlag")) {
							
							vbox.getProperties().remove("hasFlag");
							vbox.getChildren().clear();
							flagCounter += 1;
						}
						else {
							
							if (board.getMyTiles()[r1][c1].isClickedState() == false) {
							
								//File file = new File("src/images/Minesweeper-Flag.png");
								//Image minesweeperFlag = new Image(file.toURI().toString());
								ImageView flagContainer = new ImageView(FLAG);
								
								flagContainer.setFitWidth(tileSize - 10); // Set the width
								flagContainer.setFitHeight(tileSize - 10); // Set the height
								
								vbox.getProperties().put("hasFlag", true);
								
								vbox.setAlignment(Pos.CENTER);
							    vbox.getChildren().add(flagContainer);
							    flagCounter -= 1;
						    
							}
						    
						}
						

					}
					e.consume();
					System.out.println(flagCounter);
					}
					else {
						
					}
				});
			
				
				grid.add(vbox, col, row);
			}
		}
		
		
	}
	
	public void findEmptyBlocks(int row, int col) { // uses recursion
		Node node;
		VBox vbox = new VBox();
		//Text text;
		String text;
		
		for (int i = row -1; i < row + 2; i++) {
			if (i < 0 || i >= board.getRowSize()) {
				continue;
			}
			for (int j = col - 1; j < col + 2; j++) {
				if (j < 0 || j >= board.getColSize() || (i  == row && j == col) ) {
					continue;
				}
				if (board.getMyTiles()[i][j].isClickedState() == true) {
					continue;
				}
				node = grid.getChildren().get(i * board.getColSize() + j);
				vbox = (VBox) node;
				if (board.getMyTiles()[i][j].getInfo() != 'r') {
					if ((boolean)vbox.getProperties().containsKey("hasFlag")) {
						vbox.getProperties().remove("hasFlag");
						vbox.getChildren().clear();
						flagCounter += 1;
					}
					vbox = (VBox) node;
					safeTilesClicked += 1;
					vbox.setStyle("-fx-background-color: beige;"); // change to css file later
					if (board.getMyTiles()[i][j].getInfo() == '0') {
						
						board.getMyTiles()[i][j].setClickedState(true);
						findEmptyBlocks(i, j);
					}
					else  { // must be another number then
						
						board.getMyTiles()[i][j].setClickedState(true);
						//images here!			
						vbox.setAlignment(Pos.CENTER);
						text = new String(Character.toString(board.getMyTiles()[i][j].getInfo()));
						setMineNum(text, vbox);
						//vbox.getChildren().add(text);
					}
				}
				
			}
		}
		
	}
	
	
	@Override
	public void youWin() {
		StackPane winPane = new StackPane();
		Scene winScene = new Scene(winPane, 800, 650);
		Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
        	primaryStage.setScene(container);
        	primaryStage.show();
        }); 
        
        winPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); 
        Text winText = new Text("You Win!");
        winText.setFont(new Font(36));
        
        StackPane.setAlignment(winText, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.BOTTOM_CENTER);
        winPane.getChildren().addAll(winText, closeButton);
       
        timeline.stop();
        
        primaryStage.setScene(winScene);
        primaryStage.show();
        
		System.out.println("Congrats! You beat Minesweeper!");

	}

	@Override
	public void youLose() {
		StackPane winPane = new StackPane();
		Scene winScene = new Scene(winPane, 800, 650);
		Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
        	primaryStage.setScene(container);
        	primaryStage.show();
        }); 
        
        winPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); 
        Text winText = new Text("You Lost... :(");
        winText.setFont(new Font(36));
        
        StackPane.setAlignment(winText, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.BOTTOM_CENTER);
        winPane.getChildren().addAll(winText, closeButton);
       
        timeline.stop();
        
        primaryStage.setScene(winScene);
        primaryStage.show();
		
		System.out.println("You lose. :(");

	}
	
	public void setMineNum(String s, VBox v) {
		int leNumber = Integer.parseInt(s);
		switch (leNumber) {
		case 1:
			v.getChildren().add(new ImageView(MINEONE));
			break;
		case 2:
			v.getChildren().add(new ImageView(MINETWO));
			break;
		case 3:
			v.getChildren().add(new ImageView(MINETHREE));
			break;
		case 4:
			v.getChildren().add(new ImageView(MINEFOUR));
			break;
		case 5:
			v.getChildren().add(new ImageView(MINEFIVE));
			break;
		case 6:
			v.getChildren().add(new ImageView(MINESIX));
			break;
		case 7:
			v.getChildren().add(new ImageView(MINESEVEN));
			break;
		case 8:
			v.getChildren().add(new ImageView(MINEEIGHT));
			break;
		}
	}
	
	
	public Node getNode() {
		return grid;
	}
	
	public static ImageView createMuteButton(MediaPlayer media) {
        ImageView imageView = new ImageView(UNMUTE);
        
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        imageView.setOnMouseClicked(event -> {
            if (media.isMute()) {
                media.setMute(false);
                imageView.setImage(UNMUTE);
            } else {
                media.setMute(true);
                imageView.setImage(MUTE);
            }
        });

        return imageView;
	}
	
}

