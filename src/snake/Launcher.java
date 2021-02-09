package snake;

import snake.States.MenuState;

public class Launcher {
	static int width, height;
	// Game Launcher
    public static void main(String[] args) {
    	// Window Width
    	width = 1200;
    	// Window Height
    	height = 800;    	
    	new MenuState(width, height);
    }
}

