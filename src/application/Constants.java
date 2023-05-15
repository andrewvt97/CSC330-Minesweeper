package application;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
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
	public static final Image BOMB = findNload("bomb.png");
	public static final Image DBOMB = findNload("dbomb.png");
	public static final Image FLAG = findNload("flag.png");
	public static final Image MUTE = findNload("mute.png");
	public static final Image UNMUTE = findNload("unmute.png");
	
	public static final Media C418 = new Media("https://vgmsite.com/soundtracks/minecraft/limpitozea/Volume%20Alpha%2021.%20Danny.mp3");
}
