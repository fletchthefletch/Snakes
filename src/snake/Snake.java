package snake;

import java.awt.Image;
import java.util.ArrayList;

public class Snake {
	private int snakeLength, maxLength = 70, movementHop, score;
	private int startHeadX = 480, startHeadY = 480;
	private int lives;
	protected Image head, body, dead;
	private boolean isAlive;
	private boolean UP, DOWN, LEFT, RIGHT, respawn;
	private ArrayList<snakeSegment> snakeList = new ArrayList<snakeSegment>();
	
	public Snake(Image dead, Image head, Image body, int startx, int starty, int number_of_snakes) {
		if (number_of_snakes == 1) {
			lives = 2;
		} else {
			lives = 0;
		}
		this.startHeadX = startx;
		this.startHeadY = starty;
		this.head = head;
		this.body = body;
		this.dead = dead;
		spawn(startHeadX, startHeadY);
		
	}
	//** Spawn **//
	public boolean isRespawn() {
		return respawn;
	}
	public void spawn(int x, int y) {
		snakeList.clear();
		snakeLength = 0;
		isAlive = true;
		// Snake starts off going down
		UP = false;
		RIGHT = false;
		DOWN = true;
		LEFT = false;
		
		// Add head and two segments for body
		this.grow(startHeadX, startHeadY, false, false, true, false);
		this.grow(snakeList.get(0).getX(), snakeList.get(0).getY(), false, false, true, false);
		this.grow(snakeList.get(0).getX(), snakeList.get(0).getY(), false, false, true, false);
	}
	
	//** General information retrieval **//
	public boolean getUP() {
		return UP;
	}
	public boolean getRIGHT() {
		return RIGHT;
	}	
	public boolean getDOWN() {
		return DOWN;
	}	
	public boolean getLEFT() {
		return LEFT;
	}	
	
	public int getLives() {
		return lives;
	}
	public boolean IsAlive() {
		return isAlive;
	}
	public int getScore() {
		return score;
	}
	public void increaseScore(int num) {
		this.score += num;
	}
	public int getSnakeLength() {
		return snakeLength;
	}
	public ArrayList<snakeSegment> getSnake() {
		return snakeList;
	}
	
	public int getHeadX() {
		// Return X coordinate of snake head
		return snakeList.get(snakeLength-1).getX();
	}
	public int getHeadY() {
		// Return Y coordinate of snake head
		return snakeList.get(snakeLength-1).getY();
	}
	public void setScore(int decrement) {
		this.score -= decrement;
	}
	
	//** Snake moving methods **//
	private void moveUP() {
		snakeList.get(snakeLength-1).setY(snakeList.get(snakeLength-1).getY()-movementHop);
		// set key release
	}
	private void moveDOWN() {
		snakeList.get(snakeLength-1).setYALT(snakeList.get(snakeLength-1).getY()+movementHop);
		// set key release
	}
	private void moveRIGHT() {
		snakeList.get(snakeLength-1).setXALT(snakeList.get(snakeLength-1).getX()+movementHop);
		// set key release
	}
	private void moveLEFT() {
		snakeList.get(snakeLength-1).setX(snakeList.get(snakeLength-1).getX()-movementHop);
		// set key release
	}	
	public void moveSnake(int movementHop) {
		if (IsAlive()) {
			this.movementHop = movementHop;
			
			// Store the head's X and Y here
			int headX = snakeList.get(snakeLength-1).getX();
			int headY = snakeList.get(snakeLength-1).getY();
			int currentX, currentY;
	
			// 'Move command'
			if ((UP) && (!snakeList.get(1).getSegDown())) {
				this.moveUP();
			} else if ((RIGHT) && (!snakeList.get(1).getSegLeft())) {
				this.moveRIGHT();
			} else if ((DOWN) && (!snakeList.get(1).getSegUp())) {
				this.moveDOWN();
			} else if ((LEFT) && (!snakeList.get(1).getSegRight())){
				this.moveLEFT();
			} 
			
			// Move all other segments here
			for (int i = snakeLength-2; i >= 0; i--) {
				currentX = snakeList.get(i).getX();
				currentY = snakeList.get(i).getY();
				snakeList.get(i).setX(headX);
				snakeList.get(i).setY(headY);
				headX = currentX;
				headY = currentY;
			}
		}
	}
	public void changeDirection(boolean up, boolean right, boolean down, boolean left) {
		if (isAlive) {
			respawn = false; // 
			if (up) {
				if (this.UP || this.DOWN) {
					// Current direction, or preventing snake from going backwards
					return;
				}
				UP = true;
				DOWN = false;
				LEFT = false;
				RIGHT = false;
			} else if (down) {
				if (this.UP || this.DOWN) {
					return;
				}
				UP = false;
				DOWN = true;
				LEFT = false;
				RIGHT = false;
			} else if (right) {
				if (this.RIGHT || this.LEFT) {
					return;
				}
				UP = false;
				DOWN = false;
				LEFT = false;
				RIGHT = true;
			} else if (left) {
				if (this.RIGHT || this.LEFT) {
					return;
				}
				UP = false;
				DOWN = false;
				LEFT = true;
				RIGHT = false;
			}
		}
	}
	
	//** Snake grow and die methods **//
	public void grow(int previousSegmentX, int previousSegmentY,  boolean up, boolean right, boolean down, boolean left) {
		// Increase length of snake
		if (snakeLength >= maxLength) {
			return;
		}
		int t = 16;//
		
		if (up) {
			previousSegmentY += t;
		} else if (right) {
			previousSegmentX -= t;
		} else if (down) {
			previousSegmentY -= t;
		} else if (left) {
			previousSegmentX += t;
		} 
		
		snakeList.add(0, new snakeSegment(previousSegmentX, previousSegmentY));
		snakeLength++;
	}
	public void die() {	
		isAlive = false;
		if (lives > 0) {
			spawn(startHeadX, startHeadY);
			lives--;
			respawn = true;
			return;
		}
		for (snakeSegment seg : snakeList) {
			seg.segImage = dead;
		}
		
		// Change image to dead here
	}

	//** Inner class for segments of the snake's body (both head and body) **//
	public class snakeSegment {
		public Image segImage;
		private boolean left, right, up, down;
		public boolean isHead = false;
		int x, y;
		
		public snakeSegment(int x, int y) {
			//Segment starts off moving down
			this.x = x;
			this.y = y;
			if (snakeLength == 0) {
				segImage = head;
				isHead = true;
			} else {
				segImage = body;
			}
		}
		public int getX() {
			return x;
		}		
		public int getY() {
			return y;
		}
		
		//** The below methods - setX(), setXALT(), setY(), & setYALT() significantly effect directional movement with the grid **//
		public void setX(int x) {
			this.x = Game.positionCleanly(x);
		}
		public void setXALT(int x) {
			this.x = Game.positionCleanlyALT(x);
		}
		public void setYALT(int y) {
			this.y = Game.positionCleanlyALT(y);
		}
		public void setY(int y) {
			this.y = Game.positionCleanly(y);
		}
		public boolean getSegUp() {
			return up;
		}
		public boolean getSegRight() {
			return right;
		}
		public boolean getSegLeft() {
			return left;
		}
		public boolean getSegDown() {
			return down;
		}
		public Image getImage() {
			return segImage;
		}
	}
}