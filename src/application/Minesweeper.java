package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
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
import static application.Constants.DBOMB;
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
	private boolean isLoadedGame;
	private String level;
	private Stage primaryStage;
	private Board board;
	private GridPane grid;
	private BorderPane borderPane = new BorderPane();

	private boolean notBeaten = true;
	private boolean shown = false;
	private Timeline timeline;
	private Label timer;
	private Label flagCount;
	
	private int seconds = 0;
	private BorderPane bp;
	private Scene container;
	private MediaPlayer media;

	public Minesweeper(Stage primaryStage) {
		this.board = new EasyBoard();
		this.level = "Easy";
		this.primaryStage = primaryStage;
		this.flagCounter = this.board.getMines();
		this.initializeMenuUI();

		startGame(board);
	}

	/**
	 * Menu is created with File and Game Options
	 * File has options to Save game, Load game or exit game
	 */
	public void initializeMenuUI() {
		MenuBar menuBar = new MenuBar();

		// Create items for 'File' menu
		Menu fileMenu = new Menu("File");
		MenuItem exitMenuItem = new MenuItem("Exit");
		MenuItem saveGameMenuItem = new MenuItem("Save Game");
		MenuItem loadGameMenuItem = new MenuItem("Load Game");

		exitMenuItem.setOnAction(event -> {
			primaryStage.close();
		});

		saveGameMenuItem.setOnAction(event -> {
			try {
				this.isLoadedGame = false;
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("./ms.dat"));
				output.writeObject(this.board);
				output.writeBoolean(this.isFirstClick);
				output.writeInt(this.safeTilesClicked);
				output.writeInt(this.flagCounter);
				output.writeInt(seconds);
				output.close();
			} catch (Exception e) {
				System.out.println("Error writing board to file.");
				e.printStackTrace();
			}
		});

		loadGameMenuItem.setOnAction(event -> {
			try {
				this.isLoadedGame = true;
				// Loading data from file
				ObjectInputStream input = new ObjectInputStream(new FileInputStream("./ms.dat"));
				this.board = (Board) input.readObject();
				this.isFirstClick = (Boolean) input.readBoolean();
				this.safeTilesClicked = (Integer) input.readInt();
				this.flagCounter = (Integer) input.readInt();
				this.seconds = (Integer) input.readInt();
				input.close();
				startGame(this.board);
				if (notBeaten == false) {
					timeline.stop();
					int minutes = seconds / 60;
					int remaining = seconds % 60;
					timer.setText(String.format("%02d:%02d", minutes, remaining));
				}
			} catch (Exception e) {
				System.out.println("Error reading board from file.");
				e.printStackTrace();
			}
		});

		fileMenu.getItems().addAll(saveGameMenuItem, loadGameMenuItem, exitMenuItem);

		// Create items for 'Game' menu
		//Level options: Easy, Medium and Hard
		Menu gameMenu = new Menu("Game");
		RadioMenuItem easyRadioMenuItem = new RadioMenuItem("Easy");
		RadioMenuItem mediumRadioMenuItem = new RadioMenuItem("Medium");
		RadioMenuItem hardRadioMenuItem = new RadioMenuItem("Hard");

		easyRadioMenuItem.setOnAction(event -> {
			isLoadedGame = false;
			board = new EasyBoard();
			level = "Easy";
			setTimer();
			notBeaten = true;
			startGame(board);
		});

		mediumRadioMenuItem.setOnAction(event -> {
			isLoadedGame = false;
			this.board = new MediumBoard();
			this.level = "Medium";
			setTimer();
			notBeaten = true;
			startGame(this.board);
		});

		hardRadioMenuItem.setOnAction(event -> {
			isLoadedGame = false;
			this.board = new HardBoard();
			this.level = "Hard";
			setTimer();
			notBeaten = true;
			startGame(this.board);
		});

		ToggleGroup gameModeToggleGroup = new ToggleGroup();

		easyRadioMenuItem.setToggleGroup(gameModeToggleGroup);
		mediumRadioMenuItem.setToggleGroup(gameModeToggleGroup);
		hardRadioMenuItem.setToggleGroup(gameModeToggleGroup);

		gameMenu.getItems().addAll(easyRadioMenuItem, mediumRadioMenuItem, hardRadioMenuItem);

		menuBar.getMenus().addAll(fileMenu, gameMenu);

		this.borderPane.setTop(menuBar);

	}
	
	@Override
	/**
	 * Checks to see if game is previously loaded before setting isFirstClick to true
	 * @param board
	 */
	public void startGame(Board board) {	
		this.board = board;
		//Main BorderPane Window.
		bp = new BorderPane();
	
		if(this.isFirstClick == false && this.isLoadedGame == true) {
			this.grid = new GridPane();
			createGrid();

		}
		else {
			this.isFirstClick = true;
			this.grid = new GridPane();
			createGrid();
		}
		
		safeTilesClicked = 0;
		if(!isLoadedGame) {
			flagCounter = board.getMines();
		}
		
		//Handles restarts of audio.
		if(media != null) {
			media.pause();
			media.seek(Duration.ZERO);
			media.play();
		} else {
			media = new MediaPlayer(C418);
		}
		
		//Holds the flag image and flag counter.
		HBox flagContainer = new HBox();
		flagCount = new Label("" + flagCounter);
		if (flagCounter < 0) {
			flagCount.setStyle("-fx-font: 18 impact; -fx-text-fill: red;");
		} else {
			flagCount.setStyle("-fx-font: 18 impact;");
		}
		ImageView flagImage = new ImageView(FLAG);
		flagImage.setFitHeight(50);
		flagImage.setFitWidth(50);
		
		flagContainer.getChildren().addAll(flagImage, flagCount);
		
		//Creates the mute button. Function is responsible for event calls.
		ImageView muteButton = createMuteButton(media);
		media.setAutoPlay(true);
		
		//Creates the top portion of the screen. (The top of the BorderPane.)
		HBox top = new HBox();
		top.setSpacing(50);
		top.setAlignment(Pos.CENTER);
		top.setPadding(new Insets(10, 10, 10, 10));
		
		//...
		setTimer();
				
		top.getChildren().addAll(muteButton, flagContainer, timer);
		
		//Creates a container for the Minesweeper game.
		StackPane centerGame = new StackPane();
		centerGame.setPadding(new Insets(0, 10, 10, 10));
		centerGame.setAlignment(Pos.CENTER);
		
		Node gameNode = grid;
		//JavaFX did not want to center the StackPane, so I created a group.
		Group MSG = new Group();
		MSG.getChildren().add(gameNode);
		centerGame.getChildren().addAll(MSG);

		//Sets the background to azure. Matches the mute/unmute button I colored in. :) <3 josh
		top.setStyle("-fx-background-color: azure;");
		centerGame.setStyle("-fx-background-color: azure;");
		gameNode.setStyle("-fx-background-color: azure;");
		bp.setStyle("-fx-background-color: azure;");
		
		//Main Window - Sets the top to be the menu, and the center to be the Minesweeper game.
		bp.setTop(borderPane);
		bp.setCenter(centerGame);
		
		//Sets the top to be the mute/unmute button, flag counter, and timer.
		borderPane.setCenter(top);
		
		//Rescales the window based on the game difficulty (and therefore, the size of the grid).
		if (board.getMines() == 10) {
			container = new Scene(bp, 600, 525);
		}
		else if (board.getMines() == 40) {
			container = new Scene(bp, 800, 700);
		}
		else {
			container = new Scene(bp, 850, 730);
		}
		
		//Container is the scene name that holds bp.
		primaryStage.setScene(container);
		
		//Starts the timer.
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
	 * Saves state for each cell
	 * @param vbox
	 * @param r1
	 * @param c1
	 */
	private void setGridCellState(VBox vbox, int r1, int c1) {
		int tileSize = this.board.getTileSize();

		Tile tile = this.board.getMyTiles()[r1][c1];
		
		if (tile.getInfo() == 'r' && tile.isRevealedState() && !tile.isClickedState() && !tile.hasFlag()) {
			ImageView bombContainer = new ImageView(BOMB);
			bombContainer.setFitWidth(tileSize - 10); // Set the width to 40 pixels
			bombContainer.setFitHeight(tileSize - 10); // Set the height to 40 pixels
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(bombContainer);
		}
		
		
		if (tile.getInfo() == 'r' && tile.isClickedState() && !tile.hasFlag()) {
			ImageView bombContainer = new ImageView(BOMB);
			bombContainer.setFitWidth(tileSize - 10); // Set the width to 40 pixels
			bombContainer.setFitHeight(tileSize - 10); // Set the height to 40 pixels
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(bombContainer);
			vbox.setStyle("-fx-background-color: beige;");

			if (!shown) {
				this.youWhat("Lose");
			}
			
		} else {
			if (tile.isClickedState()) {
				vbox.setStyle("-fx-background-color: beige;");
		
				String numberAsText = Character.toString(tile.getInfo());
				vbox.setAlignment(Pos.CENTER);
				
				setMineNum(numberAsText, vbox);
			} else {
				vbox.setStyle("-fx-background-color: lightgreen;");
				if (tile.hasFlag()) {
					if (tile.getInfo() != 'r' || !shown) {
						ImageView flagContainer = new ImageView(FLAG);
	
						flagContainer.setFitWidth(tileSize - 10); // Set the width
						flagContainer.setFitHeight(tileSize - 10); // Set the height
						
						vbox.setAlignment(Pos.CENTER);
						vbox.getChildren().add(flagContainer);
					} else {
						ImageView DifusedBomb = new ImageView(DBOMB);
						
						DifusedBomb.setFitWidth(tileSize - 10); // Set the width to 40 pixels
						DifusedBomb.setFitHeight(tileSize - 10); // Set the height to 40 pixels
						
						vbox.setStyle("-fx-background-color: azure;");
						vbox.setAlignment(Pos.CENTER);
						vbox.getChildren().add(DifusedBomb);
					}
				}
			}
		}
	}

	/**
	 * Creates the grid for the game
	 */
	public void createGrid() {
		Scene scene;
		if (!shown) {
		scene = new Scene(grid);  
		primaryStage.setScene(scene);
        primaryStage.show();
		}
		
		for(int row = 0; row < board.getRowSize(); row++) {
			for(int col = 0; col < board.getColSize(); col++) {
			
				VBox vbox = new VBox();

				int tileSize = board.getTileSize();
				vbox.setPrefSize(tileSize, tileSize);

				vbox.setStyle("-fx-background-color: lightgreen;");

				// Set the border style
				vbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						new BorderWidths(2))));

				this.setGridCellState(vbox, row, col);

				vbox.setOnMouseClicked(e -> {

					Node n = (Node) e.getSource();
					Integer r1 = GridPane.getRowIndex(n);
					Integer c1 = GridPane.getColumnIndex(n);
					
			if (notBeaten) {
					if (e.getButton() == MouseButton.PRIMARY) {

						if ((boolean) vbox.getProperties().containsKey("hasFlag") || board.getMyTiles()[r1][c1].hasFlag() == true) {
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
							ImageView bombContainer = new ImageView(BOMB);
							
							bombContainer.setFitWidth(tileSize - 10); // Set the width to 40 pixels
							bombContainer.setFitHeight(tileSize - 10); // Set the height to 40 pixels
							vbox.setAlignment(Pos.CENTER);
						    vbox.getChildren().add(bombContainer);
						    vbox.setStyle("-fx-background-color: beige;");   
						    this.showBombs();
						    youWhat("Lose");
						    
						}
						else {
							safeTilesClicked += 1; // safe tiles clicked
							vbox.setStyle("-fx-background-color: beige;");
							if (board.getMyTiles()[r1][c1].getInfo() == '0') {
								findEmptyBlocks(r1, c1);
							} else {

								String text = new String(Character.toString(board.getMyTiles()[r1][c1].getInfo()));
								vbox.setAlignment(Pos.CENTER);
								setMineNum(text, vbox);
							}
						}

						if (safeTilesClicked == board.getSafeTiles()) {
							youWhat("Win");
						}

					}
					else if (e.getButton() == MouseButton.SECONDARY) {	
						if ((boolean)vbox.getProperties().containsKey("hasFlag") || board.getMyTiles()[r1][c1].hasFlag() == true) {
							vbox.getProperties().remove("hasFlag");
							this.board.getMyTiles()[r1][c1].setFlag(false);
							vbox.getChildren().clear();
							flagCounter += 1;
							flagCount.setText(Integer.toString(flagCounter));
							if (flagCounter >= 0) {
								flagCount.setStyle("-fx-font: 18 impact; -fx-text-fill: black;");
							}
						}
						else {
							if (board.getMyTiles()[r1][c1].isClickedState() == false && board.getMyTiles()[r1][c1].hasFlag() == false) {
								ImageView flagContainer = new ImageView(FLAG);
								
								flagContainer.setFitWidth(tileSize - 10); // Set the width
								flagContainer.setFitHeight(tileSize - 10); // Set the height

								vbox.getProperties().put("hasFlag", true);
								this.board.getMyTiles()[r1][c1].setFlag(true);

								vbox.setAlignment(Pos.CENTER);
								vbox.getChildren().add(flagContainer);
								flagCounter -= 1;
								flagCount.setText(Integer.toString(flagCounter));
								if (flagCounter < 0) {
									flagCount.setStyle("-fx-font: 18 impact; -fx-text-fill: red;");
								}
							} 
						}
					}
					e.consume();
					}
					else {
						
					}
				});

				grid.add(vbox, col, row);
			}
		}
		
		if (!shown) {
			VBox root = new VBox(this.borderPane, grid);
			scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		
	}

	public void findEmptyBlocks(int row, int col) { // uses recursion
		Node node;
		VBox vbox = new VBox();
		String text;

		for (int i = row - 1; i < row + 2; i++) {
			if (i < 0 || i >= board.getRowSize()) {
				continue;
			}
			for (int j = col - 1; j < col + 2; j++) {
				if (j < 0 || j >= board.getColSize() || (i == row && j == col)) {
					continue;
				}
				if (board.getMyTiles()[i][j].isClickedState() == true) {
					continue;
				}
				node = grid.getChildren().get(i * board.getColSize() + j);
				vbox = (VBox) node;
				if (board.getMyTiles()[i][j].getInfo() != 'r') {
					if ((boolean) vbox.getProperties().containsKey("hasFlag")) {
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
					} else { // must be another number then

						board.getMyTiles()[i][j].setClickedState(true);
						// images here!
						vbox.setAlignment(Pos.CENTER);
						text = new String(Character.toString(board.getMyTiles()[i][j].getInfo()));
						setMineNum(text, vbox);
					}
				}

			}
		}

	}

	@Override
	/**
	 * Denotes whether the player wins or loses. Handles the scene switching, and creates a new game when finished.
	 * @param decision Win or Lose
	 */
	public void youWhat(String decision) {
		StackPane Pane = new StackPane();
		Scene deciding;
		if (board.getMines() == 10) {
			deciding = new Scene(Pane, 600, 525);
		} else if (board.getMines() == 40) {
			deciding = new Scene(Pane, 800, 700);
		} else {
			deciding = new Scene(Pane, 850, 730);
		}
		
		Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(event -> {
        	media.stop();
        	new Minesweeper(primaryStage);	
        }); 
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> {
        	primaryStage.setScene(container);
        	primaryStage.show();
        });

        Pane.setStyle("-fx-background-color: azure;");

        Text winORLose;
        
        if (decision == "Lose") {
        	winORLose = new Text("You Lose!");
        	System.out.println("You Lost. :(");
        	winORLose.setFont(new Font(36));
        } else {
        	winORLose = new Text("You Win!");
            System.out.println("Congrats! You beat Minesweeper!");
        	winORLose.setFont(new Font(36));
        }
        
        winORLose.setFont(new Font(36));
        
        StackPane.setAlignment(winORLose, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(newGameButton, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.BOTTOM_CENTER);
        newGameButton.setTranslateX(-80);
        closeButton.setTranslateX(80);
        newGameButton.setTranslateY(-45);
        closeButton.setTranslateY(-45);
        Pane.getChildren().addAll(winORLose, newGameButton, closeButton);
       
        timeline.stop();
        
        primaryStage.setScene(deciding);
        primaryStage.show();

		notBeaten = false;

	}

	/**
	 * Takes in a string (mine proximity number) and a VBox, and switches the contents of the VBox to the appropriate image
	 * @param s String representing Mine Proximity
	 * @param v Passed-in VBox
	 */
	public void setMineNum(String s, VBox v) {
	    try {
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
	 catch (Exception e) {
			System.out.println("Could not parse " + s + " as an integer");
			return;
	  }
  }
	
	public Node getNode() {
		return grid;
	}
	
	/**
	 * Responsible for creating the Mute Button and handling when the user clicks on it.
	 * @param media Takes in the media playing
	 * @return Return the imageView of the Mute Button.
	 */
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
	
	/**
	 * Sets the timer.
	 */
	public void setTimer() {
		if(!isLoadedGame) {
			if (timeline != null) {
				timeline.stop();
				if (level == "Easy") {
					timer = new Label("04:00");
					seconds = 240;
				} else if (level == "Medium") {
					timer = new Label("06:00");
					seconds = 360;
				} else {
					timer = new Label("09:00");
					seconds = 540;
				}
				timer.setStyle("-fx-font: 24 impact;");
				timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
					seconds--;
					int minutes = seconds / 60;
					int remaining = seconds % 60;
					timer.setText(String.format("%02d:%02d", minutes, remaining));
					if (seconds == 0) {
						youWhat("Lose");
					}
		}));
				timeline.setCycleCount(Timeline.INDEFINITE);
			}
			else {
				timer = new Label("04:00");
				seconds = 240;
				
				timer.setStyle("-fx-font: 24 impact;");
				timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
						seconds--;
						int minutes = seconds / 60;
						int remaining = seconds % 60;
						timer.setText(String.format("%02d:%02d", minutes, remaining));
						if (seconds == 0) {
							youWhat("Lose");
						}
			}));
				timeline.setCycleCount(Timeline.INDEFINITE);
				
				media.setVolume(0.19f);
				media.setOnEndOfMedia(new Runnable() {
					@Override
					public void run() {
						media.seek(Duration.ZERO);
						media.setVolume(0.19f);
						media.play();
					}
				});
			}
		}
	}
	
	/**
	 * Shows the bombs. Uses a boolean to bypass the scene switching.
	 */
	public void showBombs() {
		for(int row = 0; row < board.getRowSize(); row++) {
			for(int col = 0; col < board.getColSize(); col++) {
				Tile tile = this.board.getMyTiles()[row][col];
				if (tile.getInfo() == 'r') {
					board.getMyTiles()[row][col].setRevealedState(true);
					shown = true;
					createGrid();
					shown = false;
				}
			}
		}
	}
	

	
}