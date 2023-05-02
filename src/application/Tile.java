package application;

public class Tile {
	
	private char info;
	private boolean clickedState;
	
	public Tile() {
		info = 'x';
		clickedState = false;
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

	/**
	 * @param clickedState the clickedState to set
	 */
	public void setClickedState(boolean clickedState) {
		this.clickedState = clickedState;
	}

}
