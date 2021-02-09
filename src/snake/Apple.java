package snake;

import java.awt.Image;

public class Apple {
	private int appleX, appleY, appleValue;
	private Image appleImage;
	public Apple(Image appleImage, int appleX, int appleY, int appleValue) {
		this.appleImage = appleImage;
		this.appleX = appleX;
		this.appleY = appleY;
		this.appleValue = appleValue;
	}
	
	public int getX() {
		return appleX;
	}
	public int getY() {
		return appleY;
	}
	public int getAppleValue() {
		return appleValue;
	}
	public Image getImage() {
		return appleImage;
	}
}
