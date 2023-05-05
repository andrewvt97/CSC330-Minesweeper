/**
 * 
 */
package application;

/**
 * @author andre
 *
 */
public class EasyBoard extends Board {
	
	public EasyBoard() {	 
	}
	 


	@Override
	public void setConfigurations() {
		 super.setRowSize(8);
		 super.setColSize(10);
		 super.setMines(10);
		 super.setTileSize(50); 
	}

}
