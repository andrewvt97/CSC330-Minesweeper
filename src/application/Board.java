/**
 * 
 */
package application;

import java.util.HashMap;

/**
 * @author andre
 *
 */
public abstract class Board {
	
	private int rowSize;
    private int colSize;
    private int mines;
    private int safeTiles;
    private int tileSize;
    private Tile[][] myTiles;
    private HashMap<String, Integer> mineLocationMap;
    private HashMap<String, Integer> noMineMap;
    
	
	public Board() {
		setConfigurations();
		safeTiles = rowSize * colSize - mines;
		myTiles = new Tile[rowSize][colSize];
		initializeTiles();
		mineLocationMap = new HashMap<String, Integer>();
		noMineMap = new HashMap<String, Integer>();
		
	}

	
	public void initializeTiles() {
		for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                myTiles[row][col] = new Tile();
            }
		}
		
	}


	/**
	 * @return the rowSize
	 */
	public int getRowSize() {
		return rowSize;
	}


	/**
	 * @param rowSize the rowSize to set
	 */
	public void setRowSize(int rowSize) {
		this.rowSize = rowSize;
	}


	/**
	 * @return the colSize
	 */
	public int getColSize() {
		return colSize;
	}


	/**
	 * @param colSize the colSize to set
	 */
	public void setColSize(int colSize) {
		this.colSize = colSize;
	}


	/**
	 * @return the mines
	 */
	public int getMines() {
		return mines;
	}


	/**
	 * @param mines the mines to set
	 */
	public void setMines(int mines) {
		this.mines = mines;
	}


	/**
	 * @return the safeTiles
	 */
	public int getSafeTiles() {
		return safeTiles;
	}


	/**
	 * @param safeTiles the safeTiles to set
	 */
	public void setSafeTiles(int safeTiles) {
		this.safeTiles = safeTiles;
	}


	/**
	 * @return the tileSize
	 */
	public int getTileSize() {
		return tileSize;
	}


	/**
	 * @param tileSize the tileSize to set
	 */
	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}


	/**
	 * @return the myTiles
	 */
	public Tile[][] getMyTiles() {
		return myTiles;
	}


	/**
	 * @param myTiles the myTiles to set
	 */
	public void setMyTiles(Tile[][] myTiles) {
		this.myTiles = myTiles;
	}
	
	public void createBoardDetails(int rowPos, int colPos) {
		
		String mineLocation = "";
		int numOfMines = 0;
		
		for (int i = rowPos -1; i < rowPos + 2; i++) {
			if (i < 0 || i >= rowSize) {
				continue;
			}
			for (int j = colPos - 1; j < colPos + 2; j++) {
				if (j < 0 || j >= colSize  ) {
					continue;
				}
				mineLocation +=  Integer.toString(i);
				mineLocation +=  "," + Integer.toString(j);
				noMineMap.put(mineLocation, 1);
				mineLocation = "";
					
			}
		}
		
		System.out.println(noMineMap);
		
		while (numOfMines < mines) {
			mineLocation +=  Integer.toString((int)(Math.random() * rowSize));
			mineLocation +=  "," + Integer.toString((int)(Math.random() * colSize));
			while (mineLocationMap.containsKey(mineLocation) || noMineMap.containsKey(mineLocation)) {
				mineLocation = "";
				mineLocation +=  Integer.toString((int)(Math.random() * rowSize));
				mineLocation +=  "," + Integer.toString((int)(Math.random() * colSize));
			}
			mineLocationMap.put(mineLocation, 1);
			numOfMines += 1;
			mineLocation = "";
		}
		
		System.out.println(mineLocationMap);
	
		
	
		for(int row = 0; row < rowSize; row++) {
			
			for(int col = 0; col < colSize; col++) {
				mineLocation = Integer.toString(row) + "," + Integer.toString(col);
				myTiles[row][col] = new Tile();
				if (mineLocationMap.containsKey(mineLocation)) {
					myTiles[row][col].setInfo('r');
				}
				else {
					myTiles[row][col].setInfo((char)(findMinesNearby(mineLocation)+ '0'));

				}
			}
		}
		
		
		
		
		System.out.println("Original configuration of the board:\n");
		printInfo();		
	}
	
	public int findMinesNearby(String position ) {
		
		String location[] = position.split(",");
		int row = Integer.parseInt(location[0]);
		int column = Integer.parseInt(location[1]);
		int minesNearby = 0;
		
		// Don't need to worry about going out of bounds because the hash mineLocationMap takes care of that
		String mineLocation = "";
		
		for (int i = row -1; i < row + 2; i++) {
			for (int j = column - 1; j < column + 2; j++) {
				// Do not need to care about avoiding checking current spot because it won't have a flag anyway
				// This avoids the if statement check everytime although it checks 9 instead of 8
				mineLocation +=  Integer.toString(i);
				mineLocation +=  "," + Integer.toString(j);
				if (mineLocationMap.containsKey(mineLocation)) {
					minesNearby += 1;
				}
				
				mineLocation = "";
			}
		}
		
//		System.out.println(row);
//		System.out.println(column);
		
		return minesNearby;
	}
	
	public void printInfo() {
		for(int row = 0; row < rowSize; row++) {
			for(int col = 0; col < colSize; col++) {
				System.out.print(myTiles[row][col].getInfo() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public abstract void setConfigurations();


}
