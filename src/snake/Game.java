package snake;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import snake.Bullet.Pellet;
import snake.Snake.snakeSegment;
import snake.States.MenuState;
import java.util.Date;

import javax.swing.JLabel;

public class Game extends GameEngine {
	private int window_width, window_height;
	private int min_map_width, max_map_width, min_map_height, max_map_height;
	private int thickness, growBy = 1; // Size increment when an apple is eaten
	private static int tileSize = 16, scoreNeededToFire = 5, scoreNeededToLayMine = 1;
	private int gameSize; // determines the 'size' of the map
	private boolean flag = true, firstTime = true, gameOver, 
			hasBeenPressed = false, hasBeenPressedPlayer2 = false, 
			snake1Alive, snake2Alive, scoreFlagIsTrue = true;
	private int number_of_players;
	private ArrayList<Image> images = new ArrayList<>();
	private int speed = 2; // movement 'hop' for each snake segment
	private ArrayList<Snake> snakes = new ArrayList<Snake>();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Mine> mines = new ArrayList<Mine>();
	private ArrayList<Apple> apples = new ArrayList<Apple>();
	private int appleValue = 15;
	private boolean runOnce = true;

	private long round = 0, gameSpeed, number_of_seconds_left = 10000, timeLeft;
	long bar;

	// Get initial time
	Date date = new Date();
    long timeMilli = date.getTime();
    long oldTime = timeMilli;
	Date date2 = new Date();
    long timeMilli2 = date2.getTime();
    long oldTime2 = timeMilli2;
    
	Date date3;
	long timeMilli3;
    long bar2;
    
    boolean gameTimerIsSet = true;
    
    Color gridGrey = Color.decode("#444444"), borderGrey = Color.decode("#3d3d3d"), darkGrey = Color.decode("333333");

	public Game(int numOfPlayers, int width, int height, int gameSize, int gameSpeed) {
		super();
		this.number_of_players = numOfPlayers;
		this.window_width = width;
		this.window_height = height;
		this.gameSize = gameSize;
		this.gameSpeed = gameSpeed;
		gameOver = false;
		snake1Alive = true;
		snake2Alive = true;
	}

	public void loadImagesIntoGame() {
		// Load Game Images/resources
		try {
			images.add(loadImage("src/images/head.png"));
			images.add(loadImage("src/images/body.png"));
			images.add(loadImage("src/images/head2.png"));
			images.add(loadImage("src/images/body2.png"));
			images.add(loadImage("src/images/apple.png"));
			images.add(loadImage("src/images/disk.png"));
			images.add(loadImage("src/images/dead.png"));
			images.add(loadImage("src/images/mineOn.png"));
			images.add(loadImage("src/images/mineOff.png"));
		} catch (Exception a) {
			// Could not load an image
			System.out.println("Could not load all images.");
			System.exit(1);
		}
	}
	
	private void newApple() {
		// Create a new apple
		int appleX = -1, appleY = -1;
		// Make sure the new apple is within the map's dimensions
		while (appleX < min_map_width) {
			appleX = rand(max_map_width);
		}
		while (appleY < min_map_height) {
			appleY = rand(max_map_height);
		}

		// Make sure the apple does not have the same location as another apple 
		for (Apple a : apples) {
			while ((appleX == a.getX()) && (appleY == a.getY())) {
				appleX = rand(max_map_width);
				appleY = rand(max_map_height);
			}
		}
				
		// Position cleanly within grid
		appleX = positionCleanly(appleX);
		appleY = positionCleanly(appleY);
		
		apples.add(new Apple(images.get(4), appleX, appleY, appleValue));
	}
	
	public static int positionCleanly(int coordinate) {
		// Locate item cleanly within a grid tile
		return coordinate-(coordinate % tileSize);
	}
	
	public static int positionCleanlyALT(int coordinate) {
		// Locate item cleanly within a grid tile
		return coordinate-(coordinate % tileSize) + 16;
	}
	
	private void spawnSnakes() {
		if (firstTime) {
		// Create snakes
			int j = 0;
			for (int i = 0; i < number_of_players; i++) {
				snakes.add(i, new Snake(images.get(6), images.get(j), images.get(j+1), 480 + (i*128), 480, number_of_players));
				j += 2;
			}
			firstTime = false;
		}
	}
	
	private boolean checkIfAllSnakesAreDead() {
		for (Snake s : snakes) {
			if (s.IsAlive()) {
				// At least one snake is alive
				return false;
			}
		}
		return true;
	}
	
	private void moveSnake() {
		long bar;
		bar = timeMilli - oldTime;
	
		if (bar > gameSpeed) {
			// Move snake 1
			if (snake1Alive) {
		  		snakes.get(0).moveSnake(speed);
			}
	  		if ((number_of_players == 2) && (snake2Alive)) {
	  			// Move snake 2
	  			snakes.get(1).moveSnake(speed);
	  		}
	  		
			// Move bullets
			for (int j = 0; j < bullets.size(); j++) {
				bullets.get(j).moveShot();
				bullets.get(j).moveShot();
			}
			oldTime = timeMilli;
		}
		 date = new Date();
		 timeMilli = date.getTime();
	}

	private void toggleMines() {
		long bar;
		bar = timeMilli2 - oldTime2;
	
		if (bar > 300) {
			for (Mine m : mines) {
				m.toggleImage();
			}
			oldTime2 = timeMilli2;
		}
		 date2 = new Date();
		 timeMilli2 = date2.getTime();
	}
	
	private void endMultiGame() {
		if ((!checkIfAllSnakesAreDead()) && (!gameTimerIsSet)) {
			if (runOnce) {
				date3 = new Date();
				timeMilli3 = date3.getTime();
			    bar2 = timeMilli3;
				runOnce = false;	
			}
			long bar;
			bar = timeMilli3 - bar2;
			if (bar > number_of_seconds_left) {
				// End game
				for (Snake s : snakes) {
					s.die();
				}
				gameOver = true;
				return;
			}
			date3 = new Date();
			timeMilli3 = date3.getTime();
			timeLeft = bar;
		}
	}
	@Override
	public void init() {
		loadImagesIntoGame();
		// Start main game loop
		this.gameLoop(60);
		spawnSnakes();	
	}	
	
	private void runChecks(Snake s, int id) {	
		// Check wall collision
		if (((s.getHeadX() > max_map_width) || (s.getHeadX() < min_map_width)) || ((s.getHeadY() > max_map_height) || (s.getHeadY() < min_map_height)))     {
			s.die();
			gameTimerIsSet = false;
		}
		
		boolean kill = false;
		ArrayList<snakeSegment> segs = new ArrayList<>();
		segs = s.getSnake();
		int headX = s.getHeadX(), headY = s.getHeadY();

		// Check body collision
		for (snakeSegment seg : segs) {
			if ((seg.getX() == headX) && (seg.getY() == headY) && (!seg.isHead)) {
				kill = true;
			} 		 
		}
		if (kill) {
			s.die();
			gameTimerIsSet = false;
		}
		
		// Check if apple is eaten
		boolean another = false;
		int appleId = 0;
		for (Apple a : apples) {
			if ((s.getHeadX() == a.getX()) && (s.getHeadY() == a.getY())) {
				// Grow a certain number of times
				for (int i = 0; i < growBy; i++) {
					s.grow(segs.get(0).getX(), segs.get(0).getY(), segs.get(0).getSegUp(), 
							segs.get(0).getSegRight(), segs.get(0).getSegDown(), segs.get(0).getSegLeft());
				
				}
				// Increase score
				s.increaseScore(a.getAppleValue());
				another = true;
				// We need a new apple
				break;
			}
			appleId++;
		} 
		if (another) {
			apples.remove(appleId);
			newApple();
		}
		
		if (number_of_players == 1) {
			return;
		}
		
		// 2 player checks after here
		try {			
			int other;
			if (id == 0) {
				other = 1;
			} else {
				other = 0;
			}
			Snake otherS = snakes.get(other);
			// If snake1's head collided with snake2's body
			for (snakeSegment seg : otherS.getSnake()) {
				if ((seg.getX() == headX) && (seg.getY() == headY) && (!seg.isHead)) {			
					s.die();
					if (id == 0) {
						snake1Alive = false;
					} else {
						snake2Alive = false;
					}	
					gameTimerIsSet = false;
					break;
				}		
			}
			
			// If both snake head's collide
			if ((s.getHeadX() == otherS.getHeadX()) && (s.getHeadY() == otherS.getHeadY())) {
				s.die();
				otherS.die();
				snake1Alive = false;
				snake2Alive = false;
			}			
			
			// Check bullet collision
			for (Bullet b : bullets) {
				for (snakeSegment seg : segs) {
					if (b.checkCollision(seg.getX(), seg.getY())) {
						// Snake must die
						s.die();
						if (id == 0) {
					  		snake1Alive = false;
						} else if (id == 1) {
					  		snake2Alive = false;
						}
						gameTimerIsSet = false;
						break;
					}
				}
			}
			
			// Check mine collision
			for (Mine m : mines) {
				if ((headX == m.getX()) && (headY == m.getY())) {
					// Snake must die
					s.die();
					if (id == 0) {
				  		snake1Alive = false;
					} else if (id == 1) {
				  		snake2Alive = false;
					}
					gameTimerIsSet = false;
					break;
				}
			}
			
			if (!gameTimerIsSet) {
				endMultiGame();
			}
			
			
		}catch (Exception e) {
			System.out.println("Error");
		}
	}
	
	@Override
	public void update(double dt) {
		if (checkIfAllSnakesAreDead()) {
			// GAME OVER, all snakes are dead
			gameOver = true;
			return;
		}
		else if (!flag) {
			moveSnake();
			int id = 0;
			for (Snake s : snakes) {
				runChecks(s, id);
				id++;
			}
			
			// Remove gone pellets
			for (Bullet item : bullets) {
				item.deletePellets();
			}
			// Remove gone bullets
			ArrayList<Bullet> toRemove = new ArrayList<Bullet>();
			for (Bullet b : bullets) {
			    if (b.allDead()) {
			        toRemove.add(b);
			    }
			}
			bullets.removeAll(toRemove);
			toggleMines();
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {		
		// All keyboard operations in here
		try {
			Snake S0 = snakes.get(0);
			synchronized(S0) {
				// Snake 1 controls
				if ((event.getKeyCode() == KeyEvent.VK_UP) && (!hasBeenPressed)) {
					hasBeenPressed = true;
					S0.changeDirection(true, false, false, false);
				} else if ((event.getKeyCode() == KeyEvent.VK_DOWN) && (!hasBeenPressed)) {
					hasBeenPressed = true; 
					S0.changeDirection(false, false, true, false);
				} else if ((event.getKeyCode() == KeyEvent.VK_RIGHT) && (!hasBeenPressed)) {
					hasBeenPressed = true; 
					S0.changeDirection(false, true, false, false);
				} else if ((event.getKeyCode() == KeyEvent.VK_LEFT) && (!hasBeenPressed)) {
					hasBeenPressed = true;
					S0.changeDirection(false, false, false, true);
				}
			}
			if (number_of_players == 2) {
				Snake S1 = snakes.get(1);
				synchronized(S1) {
				// Snake 2 controls
					if ((event.getKeyCode() == KeyEvent.VK_W) && (!hasBeenPressedPlayer2)) {
						hasBeenPressedPlayer2 = true;
						snakes.get(1).changeDirection(true, false, false, false);
					} else if ((event.getKeyCode() == KeyEvent.VK_S) && (!hasBeenPressedPlayer2)) {
						hasBeenPressedPlayer2 = true;
						snakes.get(1).changeDirection(false, false, true, false);
					} else if ((event.getKeyCode() == KeyEvent.VK_D) && (!hasBeenPressedPlayer2)) {
						hasBeenPressedPlayer2 = true;
						snakes.get(1).changeDirection(false, true, false, false);
					} else if ((event.getKeyCode() == KeyEvent.VK_A) && (!hasBeenPressedPlayer2)) {
						hasBeenPressedPlayer2 = true;
						snakes.get(1).changeDirection(false, false, false, true);
					}
				}
			}
		} catch (Error e) {
			System.out.println(e);
			return;
		}		
		
		// Bullet controls
		if (number_of_players == 2) {
			if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				if ((snakes.get(0).getScore() >= scoreNeededToFire) && (snakes.get(0).IsAlive())) {
					// Snake 1 fires
					snakes.get(0).setScore(scoreNeededToFire); // number to decrement by
					Bullet b = new Bullet(snakes.get(0).getHeadX(), snakes.get(0).getHeadY(),
							snakes.get(0).getUP(), snakes.get(0).getRIGHT(), snakes.get(0).getDOWN(), snakes.get(0).getLEFT(), 
							min_map_width, max_map_width, min_map_height, max_map_height);
					bullets.add(b);
					b.moveShot();
				}
			}
			if (event.getKeyCode() == KeyEvent.VK_Q) {
				if ((snakes.get(1).getScore() >= scoreNeededToFire) && (snakes.get(1).IsAlive())) {
					// Snake 2 fires
					snakes.get(1).setScore(scoreNeededToFire); // number to decrement by
					Bullet b = new Bullet(snakes.get(1).getHeadX(), snakes.get(1).getHeadY(),
							snakes.get(1).getUP(), snakes.get(1).getRIGHT(), snakes.get(1).getDOWN(), snakes.get(1).getLEFT(), 
							min_map_width, max_map_width, min_map_height, max_map_height);
					bullets.add(b);
					b.moveShot();
				}
			}
		}
		
		// Set mine controls
		if (number_of_players == 2) {
			if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
				if ((snakes.get(0).getScore() >= scoreNeededToLayMine) && (snakes.get(0).IsAlive())) {
					// Snake 1 fires
					snakes.get(0).setScore(scoreNeededToLayMine); // number to decrement by
					// Create mine
					int loc = snakes.get(0).getSnakeLength()-2;
					mines.add(new Mine(images.get(7), images.get(8), snakes.get(0).getSnake().get(loc).getX(), snakes.get(0).getSnake().get(loc).getY()));
				}
			}
			if (event.getKeyCode() == KeyEvent.VK_E) {
				if ((snakes.get(1).getScore() >= scoreNeededToLayMine) && (snakes.get(1).IsAlive())) {
					// Snake 2 fires
					snakes.get(1).setScore(scoreNeededToLayMine); // number to decrement by
					// Create mine
					int loc = snakes.get(1).getSnakeLength()-2; // get second-to-head segment
					mines.add(new Mine(images.get(7), images.get(8), snakes.get(1).getSnake().get(loc).getX(), snakes.get(1).getSnake().get(loc).getY()));
				}
			}
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent event) {
		// If key released belong to player 1
		if ((event.getKeyCode() == KeyEvent.VK_UP) || 
				(event.getKeyCode() == KeyEvent.VK_DOWN) ||
				(event.getKeyCode() == KeyEvent.VK_RIGHT) ||
				(event.getKeyCode() == KeyEvent.VK_LEFT)) {
			hasBeenPressed = false;
		}
		// If key released belong to player 2
		else if ((event.getKeyCode() == KeyEvent.VK_W) || 
				(event.getKeyCode() == KeyEvent.VK_A) ||
				(event.getKeyCode() == KeyEvent.VK_D) ||
				(event.getKeyCode() == KeyEvent.VK_S)) {		
			hasBeenPressedPlayer2 = false;
		}
	}
	
	@Override
	public void paintComponent() {
		// Clear window
		changeBackgroundColor(darkGrey);
		clearBackground (window_width, window_height);

		// Draw map
		drawMap();
		if (flag) {
			// Two apples in the game
			newApple();
			newApple();
			flag = false;
		}
		
		// Paint apples
		for (Apple a : apples) {
			drawImage(images.get(4), a.getX(), a.getY());
		}
		
		// Paint mines
		for (Mine m : mines) {
			drawImage(m.getImage(), m.getX(), m.getY());
		}
		
		// Paint snakes
		for (Snake s : snakes) {
			ArrayList<snakeSegment> full = s.getSnake();
			for (int i = s.getSnakeLength()-1; i >= 0 ; i--) {
				drawImage(full.get(i).getImage(), full.get(i).getX(), full.get(i).getY());
			}
		}
		
		// Paint bullets 
		for (Bullet b : bullets) {
			ArrayList<Pellet> pellets = b.getPellets();
			for (Pellet p : pellets) {
				drawImage(images.get(5), p.getX(), p.getY());
			}
		}
		
		if (number_of_players == 1) {
			// Player 1 Score
			changeColor(yellow);
			drawText(min_map_width, min_map_height - 17, "Score : " + Integer.toString(snakes.get(0).getScore()), "Arial", 20);
			
			// Paint Lives
			changeColor(yellow);
			drawText(min_map_width+150, min_map_height - 17, "Lives Remaining: " + Integer.toString(snakes.get(0).getLives()), "Arial", 20);		
			
			// Respawn panel
			if (!gameOver) {
				if (snakes.get(0).isRespawn()) {
					changeColor(white);
					drawRectangle(460, 415, 65, 90);
				}
			}
		}
		else if (number_of_players == 2) {
			// Write timer
			if ((!checkIfAllSnakesAreDead()) && (!runOnce)) {
					changeColor(Color.decode("#FF8C00"));
					drawText(window_width - 250,  min_map_height - 17, "Time Left: " + Double.toString((((double)number_of_seconds_left) - (double)timeLeft)/1000), "Arial", 30);
				}

			// Write Snake 1 Score
			changeColor(yellow);
			drawText(min_map_width, min_map_height - 17, "Green Snake Score : " + Integer.toString(snakes.get(0).getScore()), "Arial", 20);
			
			changeColor(yellow);
			drawText(min_map_width+300, min_map_height - 17, "Blue Snake Score : " + Integer.toString(snakes.get(1).getScore()), "Arial", 20);	
		}
		if (gameOver) {
			if (number_of_players == 1) {
				changeColor(Color.decode("#FF8C00"));
				drawText(max_map_width/2-100, max_map_height/2, "GAME OVER", "Arial", 50);
				
				if (scoreFlagIsTrue) {
					MenuState.getCurrentScore(snakes.get(0).getScore());
					scoreFlagIsTrue = false;
				}	
			} else if (number_of_players == 2) {
				if (snakes.get(0).getScore() > snakes.get(1).getScore()) {
					// Green snake wins!
					changeColor(green);
					drawText(max_map_width/2-200, max_map_height/2, "GREEN SNAKE WINS!", "Arial", 50);	
				} else if (snakes.get(1).getScore() > snakes.get(0).getScore()) {
					// Blue snake wins!
					changeColor(Color.decode("#00BFB2"));
					drawText(max_map_width/2-200, max_map_height/2, "BLUE SNAKE WINS!", "Arial", 50);	
				} else {
					changeColor(Color.decode("#FF8C00"));
					drawText(max_map_width/2-100, max_map_height/2, "DRAW...", "Century Gothic", 50);						
				}
			}
			// System.exit(0); // Exit here if you want to close both the game and the menu
		}
	}
	
	private void drawMap() {
		// Create Border
		int minX = 0, maxX = window_width, minY = 0, maxY = window_height;
		int multiplier = 0;
		for (thickness = 0; thickness < gameSize; thickness++) { // Thickness of the border around the game
			changeColor(darkGrey);
			for (int i = minX+(tileSize*multiplier) + tileSize; i <= maxX-(tileSize*multiplier) - tileSize; i += tileSize) { // Top border
				drawRectangle(i, minY+(tileSize*multiplier), tileSize, tileSize);		
			}
			
			for (int i = minY+(tileSize*multiplier) + tileSize; i <= maxY - (tileSize*multiplier) - tileSize; i += tileSize) { // Left border
				drawRectangle(minX+(tileSize*multiplier), i, tileSize, tileSize);		
			}
			
			for (int i = minX+(multiplier * tileSize) + tileSize; i <= maxX-(tileSize*multiplier)- tileSize; i += tileSize) { // Bottom border
				drawRectangle(i, maxY-(tileSize*multiplier), tileSize, tileSize);		
			}	
			
			for (int i = minY+(tileSize*multiplier) + tileSize; i <= maxY-(tileSize*multiplier)- tileSize; i += tileSize) { // Right border
				drawRectangle(maxX-(tileSize*multiplier), i, tileSize, tileSize);		
			}
			multiplier += 1;
		}	
		
		// Set new size
		min_map_width = minX+(tileSize*multiplier);
		max_map_width = maxX-(tileSize*(multiplier));
		min_map_height = minY+(tileSize*multiplier);
		max_map_height  = maxY - (tileSize*(multiplier));
		
		// Create inner grid
		for (minX = min_map_width; minX <= max_map_width; minX+=tileSize) {
			for (minY = min_map_height; minY <= max_map_height; minY+=tileSize) { // Top to bottom

				changeColor(gridGrey);
				drawSolidRectangle(minX, minY, tileSize, tileSize);
				changeColor(borderGrey);
				drawRectangle(minX, minY, tileSize, tileSize);		
			}	
		}		
	}
}
