/**
 * 
 */
package application;

/**
 * @author andre
 *
 */
public interface Game {
	
	public void startGame(Board board);
	//public void startGame(String level);
	
	public void youWin();
	public void youLose();
}
