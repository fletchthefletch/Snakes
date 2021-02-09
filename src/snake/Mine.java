package snake;
import java.awt.Image;

public class Mine {
	private int startHeadX, startHeadY;
	private Image ON, OFF, current;
	public Mine(Image on, Image off, int mineX, int mineY) {
		this.startHeadX = mineX;
		this.startHeadY = mineY;
		this.ON = on;
		this.OFF = off;
		this.current = OFF; 
	}
	public int getX() {
		return startHeadX;
	}
	public int getY() {
		return startHeadY;
	}
	public void toggleImage() {
		if (current == ON) {
			current = OFF;
			return;
		}
		current = ON;
		return;
	}
	public Image getImage() {
		return current;
	}
}
