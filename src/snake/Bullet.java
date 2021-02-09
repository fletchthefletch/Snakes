package snake;

import java.util.ArrayList;

public class Bullet {
	private int startHeadX, startHeadY;
	private static int numOfPellets = 5, moveMentHop = 16, count = 3, counter = 0;
	private int minWidth, maxWidth, minHeight, maxHeight;
	private boolean UP, DOWN, LEFT, RIGHT;
	private ArrayList<Pellet> bulletList = new ArrayList<>();
	
	public Bullet(int startx, int starty, boolean up, boolean right, boolean down, boolean left, int minWidth, int maxWidth, int minHeight, int maxHeight) {
		this.startHeadX = startx;
		this.startHeadY = starty;		
		this.UP = up;
		this.RIGHT = right;
		this.DOWN = down;
		this.LEFT = left;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;	
		
		// Create pellets
		for (int i = 0; i < numOfPellets; i++) {
			Pellet p = new Pellet(i, startx, starty);
			bulletList.add(i, p);
			p.move(UP, RIGHT, DOWN, LEFT);
		} 
	}
	public ArrayList<Pellet> getPellets() {
		return bulletList;
	}
	public boolean allDead() {
		if (bulletList.size() == 0) {
			return true;
		}
		return false;
	}
	public void deletePellets() {
		ArrayList<Pellet> toRemove = new ArrayList<Pellet>();
		for (Pellet p : bulletList) {
			if (((p.getX() > maxWidth) || (p.getX() < minWidth)) || ((p.getY() > maxHeight) || (p.getY() < minHeight)))     {
		        toRemove.add(p);
		    }
		}
		bulletList.removeAll(toRemove);
	}
	public boolean checkCollision(int snakeX, int snakeY) {
		for (Pellet p : bulletList) {
			if ((snakeX == p.getX()) && (snakeY == p.getY())) {
				return true;
			}
		}
		return false;
	}
	public void moveShot() {		
		if (UP) {
			startHeadY += moveMentHop;
		} else if (RIGHT) {
			startHeadX += moveMentHop;
		} else if (DOWN) {
			startHeadY -= moveMentHop;
		} else if (LEFT) {
			startHeadX -= moveMentHop;
		}
		
		deletePellets();
		
		// Move each pellet
		for (Pellet p : bulletList) {
			p.move(UP, RIGHT, DOWN, LEFT);
			
		}
	}
	
	//** Inner class for segments of the snake's body (both head and body) **//
	public class Pellet {
		//public Image pelletImage;
		int x, y, id;
		private boolean alive = true;
		public Pellet(int id, int startX, int startY) {
			this.id = id;
			this.x = startX;
			this.y = startY;
		}
		public boolean move(boolean up, boolean right, boolean down, boolean left) {
			if (count == 6) {
				counter = 1;
				count = 0;
			}

			if (up) {
				switch(id) {
					case 1:
						x += moveMentHop*counter;
						break;
					case 2:
						x += moveMentHop*2*counter;
						break;
					case 3:
						x -= moveMentHop*counter;
						break;
					case 4:
						x -= moveMentHop*2*counter;
						break;
					default:
						break;
				}
				y -= moveMentHop;
			} else if (right) {
				switch(id) {
				case 1:
					y += moveMentHop*counter;
					break;
				case 2:
					y += moveMentHop*2*counter;
					break;
				case 3:
					y -= moveMentHop*counter;
					break;
				case 4:
					y -= moveMentHop*2*counter;
					break;
				default:
					break;
			}
				x += moveMentHop;
			} else if (down) {
				switch(id) {
				case 1:
					x += moveMentHop*counter;
					break;
				case 2:
					x += moveMentHop*2*counter;
					break;
				case 3:
					x -= moveMentHop*counter;
					break;
				case 4:
					x -= moveMentHop*2*counter;
					break;
				default:
					break;
			}
				y += moveMentHop;
			} else if (left) {
				switch(id) {
				case 1:
					y += moveMentHop*counter;
					break;
				case 2:
					y += moveMentHop*2*counter;
					break;
				case 3:
					y -= moveMentHop*counter;
					break;
				case 4:
					y -= moveMentHop*2*counter;
					break;
				default:
					break;
			}
				x -= moveMentHop;
			}
			count++;
			counter = 0;
			return true;
			
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public boolean getAlive() {
			return alive;
		}
		public void setAlive(boolean set) {
			alive = set;
		}
	}
}
