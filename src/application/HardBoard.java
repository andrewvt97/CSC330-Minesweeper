/**
 * 
 */
package application;

/**
 * @author andre
 *
 */
public class HardBoard extends Board {

	public HardBoard() {	 
	}
	
	@Override
	public void setConfigurations() {
		 super.setRowSize(18);
		 super.setColSize(24);
		 super.setMines(99);
		 super.setTileSize(30); 
	}

}
