package minesweeper;

import java.awt.*;
import java.awt.event.*;

/**
 * A Tile object is a Label used to display a square in a field of squares. Once clicked on, either the
 * tile is opened or a flag is placed on the tile or removed off the tile. 
 * @author Nikola Stankovic
 */
public class Tile extends Label {
	private static final long serialVersionUID = 1L;
	private int posX, posY;
	private boolean mine;
	private boolean flag;
	private boolean opened;
	
	/**
	 * Creates a Tile object.
	 * @param text The text that will be displayed on the tile.
	 * @param color The color of the tile.
	 * @param x	The row in a matrix where the tile is located.
	 * @param y The column in a matrix where the tile is located.
	 */
	public Tile(String text, Color color, int x, int y) {
		this.posX = x;
		this.posY = y;
		this.mine = false;
		this.flag = false;
		this.opened = false;
		
		this.setBackground(color);
		this.setText(text);
		this.setAlignment(Label.CENTER);
		this.setFont(new Font("Serif", Font.BOLD, 14));
		this.setForeground(new Color(230, 195, 0));
		
		/* Clicking on a Tile changes the current tile in the grandparent class Game.
		 * If the game is disabled, a click has no effect. Left click opens the tile if no mine is present on it,
		 * or explodes the tile if a mine is present. Right click places a flag if the tile hadn't been opened, there are flags remaining,
		 * and if a flag isn't already on the tile. Otherwise, if a flag is placed on the tile, it removes the flag.
		 */
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Field field = (Field)(Tile.this.getParent());
				field.changeCurrent(Tile.this);
				Game game = (Game)(field.getParent());
				
				if (e.getButton() == MouseEvent.BUTTON1 && !game.isDisabled()) {			// Left click
				     if (mine == true) field.explode();
				     else field.open(posX, posY);
				}
				else if (e.getButton() == MouseEvent.BUTTON3 && !game.isDisabled()) {		// Right click
					if (flag == false && field.getFlags() > 0 && !opened) placeFlag();
					else if (flag == true) removeFlag();
				}
			}
		});
	}
	
	/**
	 * Changes the text color of the tile.
	 * @param color Color of the text.
	 */
	public void changeTextColor(Color color) {
		this.setForeground(color);
		this.revalidate();
	}
	
	/**
	 * Changes the background color of the tile.
	 * @param color Background color of the tile.
	 */
	public void changeBackgroundColor(Color color) {
		this.setBackground(color);
		this.revalidate();
	}
	
	/**
	 * Changes the text on the tile.
	 * @param string The text to be displayed on the tile.
	 */
	public void changeText(String string) {
		this.setText(string);
		this.revalidate();
	}
	
	/**
	 * Places a mine on a tile.
	 */
	public void setMine() {
		mine = true;
	}
	
	/**
	 * Checks if the tile has a flag placed on it.
	 * @return true if a flag is placed, false otherwise.
	 */
	public boolean hasFlag() {
		return flag;
	}
	
	/**
	 * Checks if the tile has a mine.
	 * @return true if a mine is present, false otherwise.
	 */
	public boolean hasMine() {
		return mine;
	}
	
	/**
	 * Places and displays a flag on the tile.
	 */
	private void placeFlag() {
		this.flag = true;
		this.changeText("\u00B6");									// ASCII symbol that resembles a flag.
		this.changeTextColor(new Color(77, 0, 0));
		
		Field field = (Field)(Tile.this.getParent());
		Game game = (Game)(field.getParent());
		field.decFlags();											// Reports a decrease in the number of flags to the parent field.
		game.displayFlagCount();
	}
	
	/**
	 * Removes the flag off the tile.
	 */
	private void removeFlag() {
		this.flag = false;
		this.changeText("");
		this.changeTextColor(new Color(230, 195, 0));
		
		Field field = (Field)(Tile.this.getParent());
		Game game = (Game)(field.getParent());
		field.incFlags();											// Reports an increase in the number of flags to the parent field.
		game.displayFlagCount();
	}
	
	/**
	 * Opens a tile by changing the background color of it and displays the number of surrounding tiles with mines.
	 * @param mines The number of mines surrounding the tile.
	 */
	public void openTile(int mines) {
		this.changeBackgroundColor(new Color(65, 65, 88));
		if (mines > 0) changeText("" + mines);
	}
	
	/**
	 * Checks if the tile was already opened.
	 * @return true if the tile was already opened, false otherwise.
	 */
	public boolean isOpened() {
		return opened;
	}
	
	/**
	 * Changes the open status of the tile to true. Removes a flag from the tile if such exists.
	 */
	public void open() {
		if (flag) removeFlag();
		opened = true;
		
		Field field = (Field)(Tile.this.getParent());				// Reports to the Game that a tile was opened, which then the Game class
		Game game = (Game)(field.getParent());						// checks if it is the first tile to be opened and if so, starts the game.
		game.startGame();
	}
	
	/**
	 * Opens a tile with a mine by changing the background color of the tile and displays the mine.
	 */
	public void openMineTile() {
		this.changeBackgroundColor(new Color(128, 0, 0));
		changeText("\u00D8");										// ASCII symbol that resembles a mine.
		this.changeTextColor(Color.BLACK);
	}
}