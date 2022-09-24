package minesweeper;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

/**
 * A Game object is a Frame used to display the GUI components needed to run the game. It contains a menu, panels, buttons and a playable field.
 * @author Nikola Stankovic
 */
public class Game extends Frame {
	private static final long serialVersionUID = 1L;	
	private int width, height;
	private int startX, startY;
	private Field field;
	private int mines;
	
	private Label timerLabel, flagsLabel;
	private Button explodeB;
	
	private MenuBar mb = new MenuBar();
	private Menu gameM;

	private Difficulty currentDiff;	
	private Scoreboard scoreboard;
	private Timer timer;
	private boolean started, disabled;
	
	/**
	 * Creates a Game object.
	 * @param rows Number of rows in the field.
	 * @param columns Number of columns in the field.
	 * @param mines Number of mines in the field.
	 * @throws IOException
	 */
	public Game(Difficulty diff) throws IOException {
		super("Minesweeper");
		
		int rows = 10, columns = 10;
		switch (diff) {
			case EASY: {	
				currentDiff = Difficulty.EASY; rows = 10; columns = 10; mines = 10;
				break;
			}
			case MEDIUM: {
				currentDiff = Difficulty.EASY; rows = 15; columns = 15; mines = 35;
				break;
			}
			case HARD: {			
				currentDiff = Difficulty.EASY; rows = 20; columns = 20; mines = 60;
				break;
			}
		}
		
		this.field = new Field(rows, columns, mines);
		this.scoreboard = new Scoreboard();
		this.started = false;
		this.disabled = false;
		this.width = 300;
		this.height = 380;
		this.setBackground(new Color(41, 41, 61));
		
		/* startX and startY are coordinates of the origin of the frame's coordinate plane. */
		startX = ((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - width) / 2;
		startY = ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - height) / 2;
		setBounds(startX, startY, width, height);
		setResizable(false);
		
		populateWindow();
		setIcon();
		
		timer = new Timer(timerLabel);	
		
		setVisible(true);
		this.requestFocus();
	}
	
	/**
	 * Sets the icon of the application to a preexisting image.
	 */
	private void setIcon() {
		Image icon = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "/icon.png");    
		this.setIconImage(icon);	
	}
	
	/**
	 * Creates panels, labels, and buttons and arranges them in the frame.
	 */
	private void populateWindow() {
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {     	
		    	timer.stopTimer();
		    	scoreboard.saveScoreboard();
		    	dispose();
		    }
		});
		
		this.setLayout(new BorderLayout());
		Panel top = new Panel();
		Panel topL = new Panel(), topC = new Panel(), topR = new Panel();
	
		explodeB = new Button("EXPLODE!");
		explodeB.setFont(new Font("Monospaced", Font.BOLD, 13));
		explodeB.setBackground(new Color(179, 179, 204));
		explodeB.setForeground(new Color(161, 0, 53));
		explodeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endGame();
			}
		});
		
		topC.setLayout(new BorderLayout());
		topC.add(explodeB, BorderLayout.CENTER);
		topC.add(new Panel(), BorderLayout.NORTH);
		topC.add(new Panel(), BorderLayout.SOUTH);
		topC.add(new Panel(), BorderLayout.EAST);
		topC.add(new Panel(), BorderLayout.WEST);
		
		topL.setLayout(new GridLayout(2, 1));
		Label l1 = new Label("Time:", Label.CENTER);
		timerLabel = new Label("00:00", Label.CENTER);
		
		l1.setForeground(new Color(179, 179, 204)); l1.setFont(new Font("Monospaced", Font.BOLD, 15));
		timerLabel.setForeground(new Color(230, 195, 0)); timerLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
		topL.add(l1);		
		topL.add(timerLabel);
		
		topR.setLayout(new GridLayout(2, 1));
		Label l2 = new Label("Flags:", Label.CENTER);
		flagsLabel = new Label("" + mines, Label.CENTER);
		
		l2.setForeground(new Color(179, 179, 204)); l2.setFont(new Font("Monospaced", Font.BOLD, 15));
		flagsLabel.setForeground(new Color(230, 195, 0)); flagsLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
		topR.add(l2);		
		topR.add(flagsLabel);
		
		top.setLayout(new GridLayout(1, 3));
		top.add(topL);
		top.add(topC);
		top.add(topR);
		
		this.add(top, BorderLayout.NORTH);
		this.add(field, BorderLayout.CENTER);
		this.add(new Panel(), BorderLayout.WEST);
		this.add(new Panel(), BorderLayout.EAST);
		this.add(new Panel(), BorderLayout.SOUTH);
		
		addMenus();
	}
	
	/**
	 * Creates the menu and adds it to the frame.
	 */
	private void addMenus() {
		gameM = new Menu("Game");	
		
		MenuItem restartM = new MenuItem("New game");
		restartM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartGame(currentDiff);
			}
		});
		gameM.add(restartM);
		
		
		Menu diffM = new Menu("Difficulty");
		MenuItem easyDiff = new MenuItem("Easy");
		MenuItem mediumDiff = new MenuItem("Medium");
		MenuItem hardDiff = new MenuItem("Hard");
		easyDiff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentDiff = Difficulty.EASY;
				restartGame(Difficulty.EASY);
			}
		});
		mediumDiff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentDiff = Difficulty.MEDIUM;
				restartGame(Difficulty.MEDIUM);
			}
		});
		hardDiff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentDiff = Difficulty.HARD;
				restartGame(Difficulty.HARD);
			}
		});
		diffM.add(easyDiff); diffM.add(mediumDiff); diffM.add(hardDiff);
		gameM.add(diffM);
		gameM.addSeparator();
		
		MenuItem scoreM = new MenuItem("Scoreboard");
		scoreM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				displayScoreboard();
			}
		});
		gameM.add(scoreM);
		gameM.addSeparator();
		
		MenuItem exitM = new MenuItem("Exit");
		exitM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				timer.stopTimer();
				scoreboard.saveScoreboard();
				dispose();
			}
		});	
		gameM.add(exitM);
		
		Menu helpM = new Menu("Help");
		
		MenuItem howToM = new MenuItem("How to play?");
		howToM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayHowToPlay();
			}
		});
		helpM.add(howToM);
		
		MenuItem aboutM = new MenuItem("About");
		aboutM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayAbout();
			}
		});
		helpM.add(aboutM);
		
		mb.add(gameM);
		mb.add(helpM);
		this.setMenuBar(mb);
	}
	
	/**
	 * Restarts the game by restarting the field and the timer.
	 * @param diff The difficulty of the game after restarting.
	 */
	private void restartGame(Difficulty diff) {
		switch (diff) {
			case EASY: {	
				width = 300; height = 380;
				startX = ((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - width) / 2;
				startY = ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - height) / 2;
				setBounds(startX, startY, width, height);
				field.restartField(10, 10, 10); 
				field.setFlags(10);
				displayFlagCount();
				break;
			}
			case MEDIUM: {
				width = 400; height = 480;
				startX = ((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - width) / 2;
				startY = ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - height) / 2;
				setBounds(startX, startY, width, height);
				field.restartField(15, 15, 35);
				field.setFlags(35);
				displayFlagCount();
				break;
			}
			case HARD: {			
				width = 550; height = 630;
				startX = ((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - width) / 2;
				startY = ((int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - height) / 2;
				setBounds(startX, startY, width, height);
				field.restartField(20, 20, 60);
				field.setFlags(60);
				displayFlagCount();
				break;
			}
		}
		timer.stopTimer(); timer = new Timer(timerLabel); timer.start();
		this.disabled = false;
		explodeB.setEnabled(true);
		this.revalidate();
	}
	
	/**
	 * Displays the current number of flags.
	 */
	public void displayFlagCount() {
		flagsLabel.setText("" + field.getFlags());
	}
	
	/**
	 * If the game hadn't already started, it starts the timer and therefore, starts the game.
	 */
	public void startGame() {
		if (!started) timer.startCount();
	}
	
	/**
	 * Checks if the game is disabled in order to prevent further clicking on the tiles.
	 * @return true if the game is disabled, false otherwise.
	 */
	public boolean isDisabled() {
		return disabled;
	}
	
	/**
	 * Opens all tiles and checks if the game was successfully completed. Displays a win or lose dialog frame accordingly.
	 */
	public void endGame() {
		boolean won = field.checkTiles();
		field.openMineTiles();
		timer.stopTimer();
		disabled = true;
		explodeB.setEnabled(false);
		if (won) displayWonScreen();
		else displayLostScreen();
	}	
	
	/**
	 * Displays a Dialog which contains the scoreboard of the current game difficulty.
	 */
	private void displayScoreboard() {
		class Score extends Dialog {
			private static final long serialVersionUID = 1L;
			private int width = 180, height = 160;
			
			public Score(Frame owner) {
				super(owner);
				height = 160 + 15 * scoreboard.getNumOfEntries(currentDiff);
				setTitle("Scoreboard");
				setBounds(Game.this.getX() + (Game.this.width - width) / 2, Game.this.getY() + (Game.this.height - height) / 2, width, height);
				setResizable(false);
				setBackground(new Color(41, 41, 61));
				
				this.addWindowListener(new WindowAdapter() {
				    public void windowClosing(WindowEvent e) { 
				    	dispose();
				    }
				});
				
				String labelText;
				String diffString = "";
				if (currentDiff == Difficulty.EASY) diffString = "(EASY)";
				else if (currentDiff == Difficulty.MEDIUM) diffString = "(MEDIUM)";
				else if (currentDiff == Difficulty.HARD) diffString = "(HARD)";
				
				if (scoreboard.getNumOfEntries(currentDiff) == 0) labelText = "<h3><font size=\"4\"><font color=\"#e6c300\">"
						+ "SCOREBOARD</h3><font size=\"4\"><font color=\"#e6c300\"><b>" + diffString
						+ "</b><br><br><font size=\"3\"><font color=\"#f0f0f5\"><b>No entries yet!</b>";
				else labelText = "<h3><font size=\"4\"><font color=\"#e6c300\">SCOREBOARD</h3><font size=\"4\">"
						+ "<font color=\"#e6c300\"><b>" + diffString + "</b><br><br><font size=\"3\">" 
						+ scoreboard.getPrintable(currentDiff).replace("\n", "<br>");
				JLabel label = new JLabel("<html><center>" + labelText + "</center></html>", JLabel.CENTER);
				label.setFont(new Font("SansSerif", Font.PLAIN, 12));
				this.add(label);
				this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				setVisible(true);
			}
		}
		Score dialog = new Score(this);
		dialog.dispose();
	}
	
	/**
	 * Displays a Dialog which contains a brief explanation of the game's rules.
	 */
	private void displayHowToPlay() {
		class HTP extends Dialog {
			private static final long serialVersionUID = 1L;
			private int width = 400, height = 280;
			
			public HTP(Frame owner) {
				super(owner);
				setTitle("How to play?");
				setBounds(Game.this.getX() + (Game.this.width - width) / 2, Game.this.getY() + (Game.this.height - height) / 2, width, height);
				setResizable(false);
				setBackground(new Color(41, 41, 61));
				
				this.addWindowListener(new WindowAdapter() {
				    public void windowClosing(WindowEvent e) { 
				    	dispose();
				    }
				});
				
				String labelText = "<h3><font size=\"5\"><font color=\"#e6c300\">HOW TO PLAY</h3>"
						+ "<font size=\"4\"><font color=\"#e6c300\"> <b>Minesweeper</b> <font size=\"3\"><font color=\"#f0f0f5\">"
						+ " is a game where mines are hidden"
						+ " in a grid of squares. Safe squares have numbers telling"
						+ " you how many mines touch the square. Use the number"
						+ " clues to solve the game by opening all of the safe squares."
						+ " If you click on a square with a mine you lose the game! You open squares"
						+ " with the left mouse button and put or remove flags on squares"
						+ " with the right mouse button. When you open a square that does not touch any mines, it will be empty."
						+ " After placing all the flags, pressing the 'EXPLODE!' button will end the<br> game." 
						+ " If all flags were correctly placed, you win the game, otherwise you lose! Opening all safe squares"
						+ " wins you the game, regardless of the placed flags.";
				JLabel label = new JLabel("<html><center>" + labelText + "</center></html>", JLabel.CENTER);
				label.setFont(new Font("SansSerif", Font.PLAIN, 12));
				this.add(label);
				this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				setVisible(true);
			}
		}
		HTP dialog = new HTP(this);
		dialog.dispose();
	}
	
	/**
	 * Displays a Dialog which contains the 'about' section of the application.
	 */
	private void displayAbout() {
		class About extends Dialog {
			private static final long serialVersionUID = 1L;
			private int width = 250, height = 275;
			
			public About(Frame owner) {
				super(owner);
				setTitle("About");
				setBounds(Game.this.getX() + (Game.this.width - width) / 2, Game.this.getY() + (Game.this.height - height) / 2, width, height);
				setResizable(false);
				setBackground(new Color(41, 41, 61));
				
				this.addWindowListener(new WindowAdapter() {
				    public void windowClosing(WindowEvent e) { 
				    	dispose();
				    }
				});
				
				String labelText = "<h3><font size=\"5\"><font color=\"#e6c300\">ABOUT</h3><h4><font size=\"4\">"
							+ "<font color=\"#e6c300\">Minesweeper</h4>"
							+ "<font color=\"#f0f0f5\">Version 1.1 (August 2022)<br>"
							+ "© Nikola Stankovic Games<br> All rights reserved.<br><br>"
							+ "Usage of the code used to make this<br>game without permission is prohibited."
							+ "<br><br><font color=\"#A10035\"><b>Created by Nikola Stankovic</b>";
				JLabel label = new JLabel("<html><center>" + labelText + "</center></html>", JLabel.CENTER);
				label.setFont(new Font("SansSerif", Font.PLAIN, 12));
				this.add(label);
				this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				setVisible(true);
			}
		}
		About dialog = new About(this);
		dialog.dispose();
	}
		
	/**
	 * Displays a Dialog which congratulates the player for winning the game and asks if a new game shall begin.
	 */
	private void displayWonScreen() {
		class Won extends Dialog {
			private static final long serialVersionUID = 1L;
			private int width = 200, height = 200;
			
			public Won(Frame owner) {
				super(owner);
				setTitle("Victorious");
				setBounds(Game.this.getX() + (Game.this.width - width) / 2, Game.this.getY() + (Game.this.height - height) / 2, width, height);
				setResizable(false);
				setBackground(new Color(41, 41, 61));
				
				this.addWindowListener(new WindowAdapter() {
				    public void windowClosing(WindowEvent e) { 
				    	dispose();
				    }
				});
				
				setLayout(new BorderLayout(2,1));
				String labelText = "<h3><font color=\"#e6c300\">Congratulations!</h3>"
						+ "Total time:<br>" + timerLabel.getText() + "<br><br>Another round?<br>";
				JLabel label = new JLabel("<html><center>" + labelText + "</center></html>", JLabel.CENTER);
				label.setFont(new Font("SansSerif", Font.PLAIN, 12));
				label.setForeground(new Color(240, 240, 245));
				this.add(label, BorderLayout.CENTER);
				
				Button button = new Button("NEW GAME");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						Game.this.restartGame(currentDiff);
					}
				});
				button.setBackground(new Color(148, 148, 184));
				button.setForeground(new Color(41, 41, 61));
				button.setFont(new Font("SansSerif", Font.BOLD, 11));
				this.add(button, BorderLayout.SOUTH);
				
				this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				
				displayScoreboardEntry();
				setVisible(true);
			}
		}
		Won dialog = new Won(this);
		dialog.dispose();
	}
	
	/**
	 * Displays a Dialog which tells the player the game was lost and asks if a new game shall begin.
	 */
	private void displayLostScreen() {
		class Lost extends Dialog {
			private static final long serialVersionUID = 1L;
			private int width = 200, height = 200;
			
			public Lost(Frame owner) {
				super(owner);
				setTitle("Defeat");
				setBounds(Game.this.getX() + (Game.this.width - width) / 2, Game.this.getY() + (Game.this.height - height) / 2, width, height);
				setResizable(false);
				setBackground(new Color(41, 41, 61));
				
				this.addWindowListener(new WindowAdapter() {
				    public void windowClosing(WindowEvent e) { 
				    	dispose();
				    }
				});
				
				setLayout(new BorderLayout(2,1));
				String labelText = "<h3><font color=\"#e6c300\">Too bad! You lost...</h3>"
						+ "Total time:<br>" + timerLabel.getText() + "<br><br>Try again?<br>";
				JLabel label = new JLabel("<html><center>" + labelText + "</center></html>", JLabel.CENTER);
				label.setFont(new Font("SansSerif", Font.PLAIN, 12));
				label.setForeground(new Color(240, 240, 245));
				this.add(label, BorderLayout.CENTER);
				
				Button button = new Button("NEW GAME");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						Game.this.restartGame(currentDiff);
					}
				});
				button.setBackground(new Color(148, 148, 184));
				button.setForeground(new Color(41, 41, 61));
				button.setFont(new Font("SansSerif", Font.BOLD, 11));
				this.add(button, BorderLayout.SOUTH);
				
				this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				setVisible(true);
			}
		}
		Lost dialog = new Lost(this);
		dialog.dispose();
	}
	
	/**
	 * Displays a Dialog which contains a TextField used to enter a name, which will be used for a scoreboard entry in case of a win.
	 */
	private void displayScoreboardEntry() {
		class Score extends Dialog {
			private static final long serialVersionUID = 1L;
			private int width = 170, height = 150;
			
			public Score(Frame owner) {
				super(owner);
				setTitle("Name");
				setBounds(Game.this.getX() + (Game.this.width - width) / 2, Game.this.getY() + (Game.this.height - height) / 2, width, height);
				setResizable(false);
				setBackground(new Color(41, 41, 61));
				
				this.addWindowListener(new WindowAdapter() {
				    public void windowClosing(WindowEvent e) { 
				    	dispose();
				    }
				});
				
				setLayout(new BorderLayout(2,1));
				String labelText = "<h3><font color=\"#e6c300\">Enter name:</h3>";
				JLabel label = new JLabel("<html><center>" + labelText + "</center></html>", JLabel.CENTER);
				label.setFont(new Font("SansSerif", Font.PLAIN, 12));
				label.setForeground(new Color(240, 240, 245));
				this.add(label, BorderLayout.NORTH);
				
				Panel p = new Panel();
				TextField tf = new TextField(15);
				
				p.add(tf);
				this.add(p, BorderLayout.CENTER);
				
				Button button = new Button("SUBMIT");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {			
						dispose();
						String name = tf.getText();
						scoreboard.addPlayer(new Player(name, timerLabel.getText()), currentDiff);
						displayScoreboard();
					}
				});
				button.setBackground(new Color(148, 148, 184));
				button.setForeground(new Color(41, 41, 61));
				button.setFont(new Font("SansSerif", Font.BOLD, 11));
				this.add(button, BorderLayout.SOUTH);
				
				this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				setVisible(true);
			}
		}
		Score dialog = new Score(this);
		dialog.dispose();
	}
	
	/**
	 * Creates a Game object and starts the game at easy difficulty.
	 * @param args Arguments for the main function.
	 */
	public static void main(String args[]) {
		try { 
			Game g = new Game(Difficulty.EASY);
			g.timer.start();
		} catch (IOException e) {}
	}
}