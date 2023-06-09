package application;
import java.io.Serializable;

public class Tile implements Serializable {
	
	private char info;
	private boolean clickedState;
	private boolean revealedState;
	private boolean hasFlag;
	
	public Tile() {
		info = 'x';
		clickedState = false;
		hasFlag = false;
	}

	/**
	 * @return the info
	 */
	public char getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(char info) {
		this.info = info;
	}

	/**
	 * @return the clickedState
	 */
	public boolean isClickedState() {
		return clickedState;
	}
	
	public boolean hasFlag() {
		return this.hasFlag;
	}

	/**
	 * @param clickedState the clickedState to set
	 */
	public void setClickedState(boolean clickedState) {
		this.clickedState = clickedState;
	}
	
	public void setFlag(boolean hasFlag) {
		this.hasFlag = hasFlag;
	}

	/**
	 * Returns the revealed state (Bomb that wasn't clicked, but shown).
	 * @return boolean Revealed.
	 */
	public boolean isRevealedState() {
		return revealedState;
	}

	/**
	 * Sets the revealed state of a certain tile
	 * @param revealedState Takes in a boolean to set the revealed state of a certain tile.
	 */
	public void setRevealedState(boolean revealedState) {
		this.revealedState = revealedState;
	}

}
