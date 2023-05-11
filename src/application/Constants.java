package application;

import javafx.scene.image.Image;
import java.io.InputStream;

public final class Constants {
	private static Image findNload(String p)  {
		InputStream r = Constants.class.getResourceAsStream(path + p);
		
		return new Image(r);
	}
	
	public static final String path = "/images/";
	
	public static final Image MINEONE = findNload("1.png");
	public static final Image MINETWO = findNload("2.png");
	public static final Image MINETHREE = findNload("3.png");
	public static final Image MINEFOUR = findNload("4.png");
	public static final Image MINEFIVE = findNload("5.png");
	public static final Image MINESIX = findNload("6.png");
	public static final Image MINESEVEN = findNload("7.png");
	public static final Image MINEEIGHT = findNload("8.png");
}
