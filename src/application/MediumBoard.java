/**
 * 
 */
package application;

/**
 * @author andre
 *
 */
public class MediumBoard extends Board {

	public MediumBoard() {	 
	}
	
	@Override
	public void setConfigurations() {
		 super.setRowSize(14);
		 super.setColSize(18);
		 super.setMines(40);
		 super.setTileSize(40); 
	}

}
