package application;

public class Tile {
	
	private char info;
	private boolean finished;
	
	public Tile() {
		info = 'x';
		finished = false;
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
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * @param finished the finished to set
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	
}
