/**
 * 
 */
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ToggleGroup;
import static application.Constants.MINEONE;
import static application.Constants.MINETWO;
import static application.Constants.MINETHREE;
import static application.Constants.MINEFOUR;
import static application.Constants.MINEFIVE;
import static application.Constants.MINESIX;
import static application.Constants.MINESEVEN;
import static application.Constants.MINEEIGHT;

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
	private BorderPane borderPane;

	public Minesweeper(Stage primaryStage) {
		this.board = new EasyBoard();
		this.primaryStage = primaryStage;
		this.flagCounter = this.board.getMines();

		this.initializeMenuUI();

		startGame(this.board);

	}

	/*
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
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("./ms.dat"));
				output.writeObject(this.board);
				output.writeBoolean(this.isFirstClick);
				output.writeInt(this.safeTilesClicked);
				output.writeInt(this.flagCounter);
				output.close();
			} catch (Exception e) {
				System.out.println("Error writing board to file.");
				e.printStackTrace();
			}
		});

		loadGameMenuItem.setOnAction(event -> {
			try {
				// Loading data from dat file
				ObjectInputStream input = new ObjectInputStream(new FileInputStream("./ms.dat"));
				this.board = (Board) input.readObject();
				this.isFirstClick = (Boolean) input.readBoolean();
				this.safeTilesClicked = (Integer) input.readInt();
				this.flagCounter = (Integer) input.readInt();
				input.close();
				startGame(this.board);
			} catch (Exception e) {
				System.out.println("Error!!!!");
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
			this.board = new EasyBoard();
			startGame(this.board);
		});

		mediumRadioMenuItem.setOnAction(event -> {
			this.board = new MediumBoard();
			startGame(this.board);
		});

		hardRadioMenuItem.setOnAction(event -> {
			this.board = new HardBoard();
			startGame(this.board);
		});

		ToggleGroup gameModeToggleGroup = new ToggleGroup();

		easyRadioMenuItem.setToggleGroup(gameModeToggleGroup);
		mediumRadioMenuItem.setToggleGroup(gameModeToggleGroup);
		hardRadioMenuItem.setToggleGroup(gameModeToggleGroup);

		gameMenu.getItems().addAll(easyRadioMenuItem, mediumRadioMenuItem, hardRadioMenuItem);

		menuBar.getMenus().addAll(fileMenu, gameMenu);

		this.borderPane = new BorderPane();
		this.borderPane.setTop(menuBar);

	}

	@Override
	public void startGame(Board board/* String level */) {
		/*
		 * if (level.equals("Easy")){ board = new EasyBoard(); } else if
		 * (level.equals("Medium")){ board = new MediumBoard(); } else { board = new
		 * HardBoard(); }
		 */

		this.isFirstClick = true;
		this.grid = new GridPane();
		createGrid();
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
	// public void setLevel(String level) {
	// this.level = level;
	// startGame(level);
	// }

	private void setGridCellState(VBox vbox, int r1, int c1) {
		int tileSize = this.board.getTileSize();

		Tile tile = this.board.getMyTiles()[r1][c1];
		
		if (tile.getInfo() == 'r' && tile.isClickedState()) {
			File file = new File("src/images/Minesweeper-Bomb.png");
			Image minesweeperBomb = new Image(file.toURI().toString());
			ImageView bombContainer = new ImageView(minesweeperBomb);

			bombContainer.setFitWidth(tileSize - 10); // Set the width to 40 pixels
			bombContainer.setFitHeight(tileSize - 10); // Set the height to 40 pixels
			vbox.setAlignment(Pos.CENTER);
			vbox.getChildren().add(bombContainer);

			this.youLose();

		} else {
			if (tile.isClickedState()) {
				vbox.setStyle("-fx-background-color: beige;");
		
				String numberAsText = Character.toString(tile.getInfo());
				vbox.setAlignment(Pos.CENTER);
				setMineNum(numberAsText, vbox);
			} else {
				vbox.setStyle("-fx-background-color: lightgreen;");
				if (tile.hasFlag()) {
					File file = new File("src/images/Minesweeper-Flag.png");
					Image minesweeperFlag = new Image(file.toURI().toString());
					ImageView flagContainer = new ImageView(minesweeperFlag);

					flagContainer.setFitWidth(tileSize - 10); // Set the width
					flagContainer.setFitHeight(tileSize - 10); // Set the height
					
					vbox.setAlignment(Pos.CENTER);
					vbox.getChildren().add(flagContainer);
				}
			}
		}
	}

	public void createGrid() {
		for (int row = 0; row < board.getRowSize(); row++) {
			for (int col = 0; col < board.getColSize(); col++) {

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

					// VBox vbox1 = (VBox)n; // not needed

					if (e.getButton() == MouseButton.PRIMARY) {

						if ((boolean) vbox.getProperties().containsKey("hasFlag")) {
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

							File file = new File("src/images/Minesweeper-Bomb.png");
							Image minesweeperBomb = new Image(file.toURI().toString());
							ImageView bombContainer = new ImageView(minesweeperBomb);

							bombContainer.setFitWidth(tileSize - 10); // Set the width to 40 pixels
							bombContainer.setFitHeight(tileSize - 10); // Set the height to 40 pixels
							vbox.setAlignment(Pos.CENTER);
							vbox.getChildren().add(bombContainer);

							youLose();

							// setLevel("Hard"); testing set level function
						} else {
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

						// System.out.println(safeTilesClicked);
						if (safeTilesClicked == board.getSafeTiles()) {
							youWin();
						}
					} else if (e.getButton() == MouseButton.SECONDARY) {

						if ((boolean) vbox.getProperties().containsKey("hasFlag")) {
							this.board.getMyTiles()[r1][c1].setFlag(false);
							vbox.getProperties().remove("hasFlag");
							vbox.getChildren().clear();
							flagCounter += 1;
						} else {

							if (this.board.getMyTiles()[r1][c1].isClickedState() == false) {

								File file = new File("src/images/Minesweeper-Flag.png");
								Image minesweeperFlag = new Image(file.toURI().toString());
								ImageView flagContainer = new ImageView(minesweeperFlag);

								flagContainer.setFitWidth(tileSize - 10); // Set the width
								flagContainer.setFitHeight(tileSize - 10); // Set the height

								vbox.getProperties().put("hasFlag", true);
								this.board.getMyTiles()[r1][c1].setFlag(true);

								vbox.setAlignment(Pos.CENTER);
								vbox.getChildren().add(flagContainer);
								flagCounter -= 1;

							}

						}

					}
					e.consume();
					System.out.println(flagCounter);

				});

				grid.add(vbox, col, row);
			}
		}

		VBox root = new VBox(this.borderPane, grid);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void findEmptyBlocks(int row, int col) { // uses recursion
		Node node;
		VBox vbox = new VBox();
		// Text text;
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
						// vbox.getChildren().add(text);
					}
				}

			}
		}

	}

	@Override
	public void youWin() {
		System.out.println("Congrats! You beat Minesweeper!");

	}

	@Override
	public void youLose() {
		System.out.println("You lose");

	}

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

}
