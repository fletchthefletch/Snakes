package snake.States;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class MenuState extends JFrame implements ActionListener, MouseListener {
	private static int numberOfPlayers, width = 1200, height = 800, gameSize = 4, gameSpeed = 0, currentBest;
    private static JFrame f; 
    private static ArrayList<JButton> buttons = new ArrayList<>();
    private static int spacingWidth = 100, spacingHeight = 5, borderWidth = 2;
    static boolean playing;
    static JLabel title, scoreLbl, score; 
    JPanel scorePanel, mainPanel, titlePanel;
    Color grey = Color.decode("#333333");
    Color white = Color.decode("#FFFFFF");
    Color color1 = Color.decode("#E0CA3C");
    Color color2 = Color.decode("#00BFB2");
    Color color3 = Color.decode("#FF8C00");
    Color color4 = Color.decode("#DE4D86"); 
    Color color5 = Color.decode("#FF0000");
    GameState G;
    Dimension d;
    Color defaultGrey;
    Border mouseHoverBorder, greyBorder, noBorder;
    Font centurySmall, centuryLarge;
    
	public MenuState(int width, int height) {	
	    System.out.println("MenuState has been opened.");
    	width = 1200; // Window width
    	height = 800; // Window height
    	currentBest = 0;
        d = new Dimension(350, 100);
        centurySmall = new Font("Century Gothic", Font.PLAIN,  25);
        centuryLarge = new Font("Century Gothic", Font.BOLD,  60);
        noBorder = new LineBorder(white, borderWidth);
        greyBorder = new LineBorder(grey, borderWidth); 
        // Create menu frame
        f = new JFrame("Snakes");

        createMainPanel();
        createTitlePanel();
        loadTitleImages();
        makeScorePanel();
        makeButtons();
        
        // Construction
        mainPanel.add(Box.createRigidArea(new Dimension(spacingWidth, 30)));
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(100, 30)));
        mainPanel.add(scorePanel);
        
        // Add buttons to menu panel
        for (JButton j : buttons) {
        	mainPanel.add(j);
            mainPanel.add(Box.createRigidArea(new Dimension(spacingWidth, spacingHeight)));
        }        
        
        defaultGrey = title.getForeground();
        f.add(mainPanel); 
        fullScreen(f);

        f.setVisible(true);
    }
	
    private void createMainPanel() {
        mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(white);        
        mainPanel.setSize(width/2, height/2);
    }
    
    private void createTitlePanel() {
        titlePanel = new JPanel();//
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setMaximumSize(new Dimension(width, 75)); 
        title = new JLabel("SNAKES"); 
        title.setSize(200, 50);
        title.setFont(centuryLarge);
        title.setForeground(grey);
        titlePanel.setBackground(white);
    }
    
    private void loadTitleImages() {
        try {
			BufferedImage sPic = ImageIO.read(new File("src/Images/menuSnakeC.png"));
			BufferedImage sPicR = ImageIO.read(new File("src/Images/menuSnakeRC.png"));
			JLabel snake = new JLabel(new ImageIcon(sPic));
			JLabel snakeReverse = new JLabel(new ImageIcon(sPicR));
			titlePanel.add(snake);
			titlePanel.add(title);
			titlePanel.add(snakeReverse);
        }
        catch (Exception e) {
        	titlePanel.add(title);
        	System.out.println("Could not load snake images.");
        }
    }

    private void makeScorePanel() {
        scorePanel = new JPanel();    
        scorePanel.setMaximumSize(new Dimension(width, 75));
        scoreLbl = new JLabel("Best Score: ");
        score = new JLabel(Integer.toString(currentBest));
        scorePanel.add(scoreLbl);
        scorePanel.add(score);
        score.setFont(new Font("Century Gothic", Font.BOLD,  25));
        scoreLbl.setFont(new Font("Century Gothic", Font.PLAIN,  25));
        score.setForeground(Color.decode("#9400D3"));
    }
    
    private void makeButtons() {
        // Create buttons 
        buttons.add(new JButton("Single Player (Easy)"));
        buttons.add(new JButton("Single Player (Medium)"));
        buttons.add(new JButton("Single Player (Hard)"));
        buttons.add(new JButton("2-Player"));
        buttons.add(new JButton("Quit"));        
        for (JButton j : buttons) {
        	j.addActionListener(this);
        	j.addMouseListener(this);
        	j.setFocusable(false);
        	j.setMaximumSize(d);
        	j.setAlignmentX(Component.CENTER_ALIGNMENT);
        	j.setBackground(white);
        	j.setBorder(noBorder);
        	if (j.getText() == "Quit") {
        		j.setFont(new Font("Century Gothic", Font.BOLD,  25));
        	} else {
        		j.setFont(centurySmall);
        	}
        }
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Quit") {
			System.exit(0);
		} else if (e.getActionCommand() == "2-Player") {
	    	numberOfPlayers = 2;
	    	gameSize = 4;
	    	gameSpeed = 50;
	    	G = new GameState(numberOfPlayers, width, height, gameSize, gameSpeed);
		} else if (e.getActionCommand() == "Single Player (Easy)") {
			numberOfPlayers = 1;
	    	gameSize = 4;
	    	gameSpeed = 100;
	    	G = new GameState(numberOfPlayers, width, height, gameSize, gameSpeed);
		} else if (e.getActionCommand() == "Single Player (Medium)") {
			numberOfPlayers = 1;
	    	gameSize = 9;
	    	gameSpeed = 50;
			G = new GameState(numberOfPlayers, width, height, gameSize, gameSpeed);
		} else if (e.getActionCommand() == "Single Player (Hard)") {
			numberOfPlayers = 1;
	    	gameSize = 12;
	    	gameSpeed = 25;
			G = new GameState(numberOfPlayers, width, height, gameSize, gameSpeed);
		} 
	}

	public static void getCurrentScore(int score) {
		if (score > currentBest) {
			currentBest = score;
			MenuState.score.setText(Integer.toString(currentBest));
		}
	}
	
	private void fullScreen(JFrame j) {
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    j.setSize(screen.width, screen.height);
	}
	@Override
	public void mouseClicked(MouseEvent e) {	
	}
	@Override
	public void mousePressed(MouseEvent e) {			
	}
	@Override
	public void mouseReleased(MouseEvent e) {	
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == buttons.get(0)) {
			buttons.get(0).setBorder(greyBorder);
			buttons.get(0).setForeground(color1);
		} else if (e.getSource() == buttons.get(1)) {
			buttons.get(1).setBorder(greyBorder);
			buttons.get(1).setForeground(color4);
		} else if (e.getSource() == buttons.get(2)) {
			buttons.get(2).setBorder(greyBorder);
			buttons.get(2).setForeground(color2);
		}  else if (e.getSource() == buttons.get(3)) {
			buttons.get(3).setBorder(greyBorder);
			buttons.get(3).setForeground(color3);
		}  else if (e.getSource() == buttons.get(4)) {
			buttons.get(4).setBorder(new LineBorder(color5, borderWidth));
			buttons.get(4).setForeground(color5);
		} 
	}
	@Override
	public void mouseExited(MouseEvent e) {
		for (JButton j : buttons) {
			j.setForeground(defaultGrey);
			j.setBorder(noBorder);
			j.setBackground(white);
		}
	} 
}
