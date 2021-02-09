package snake.States;

import javax.swing.JFrame;
import snake.Game;
import snake.GameEngine;

public class GameState extends JFrame {	   
	public GameState(int numOfPlayers, int width, int height, int gameSize, int gameSpeed) {	
	    System.out.println("GameState has been opened.");
	    if (numOfPlayers == 1) {
	    	// Open single player
	    	Game snakes = new Game(1, width, height, gameSize, gameSpeed);
	    	GameEngine.createGame(snakes);
	    	snakes.setWindowSize(width, height);
	    } else {
	    	// Open multiplayer
	    	Game snakes = new Game(2, width, height, gameSize, gameSpeed);
	    	GameEngine.createGame(snakes);
	    	snakes.setWindowSize(width, height);
	    } 	
	}
}
