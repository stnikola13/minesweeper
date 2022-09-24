package minesweeper;

import java.awt.*;

/**
 * A Field object is a collection of tiles which represents the entire playable field of the game.
 * @author Nikola Stankovic
 */
public class Field extends Panel {
	private static final long serialVersionUID = 1L;
	private int rows, columns, mines, flags;
	private GridLayout layout;
	private Tile field[][];
	private Tile current;
	
	/**
	 * Creates a Field object.
	 * @param rows Number of rows of the field.
	 * @param columns Number of columns of the field.
	 * @param mines Number of mines the field has.
	 */
	public Field(int rows, int columns, int mines) {
		restartField(rows, columns, mines);
	}
	
	/**
	 * Restarts the entire field by removing all tiles and creating a new set of tiles. It creates and places Tile objects and places mines
	 * randomly across the field.
	 * @param rows Number of rows of the field.
	 * @param columns Number of columns of the field.
	 * @param mines Number of mines the field has.
	 */
	public void restartField(int rows, int columns, int mines) {
		this.removeAll();
		
		this.rows = rows;
		this.columns = columns;
		this.flags = this.mines = mines;
		this.field = new Tile[rows][columns];
		this.layout = new GridLayout(rows, columns, 1, 1);
		this.setCurrent(null);
		this.setLayout(layout);
		
		/* Generates random coordinates in the field and places mines without placing multiple mines on the same tile. */
		for (int i = 0; i < mines; i++) {													
			int randX = (int)(Math.random() * rows);
			int randY = (int)(Math.random() * columns);
			if (field[randX][randY] == null) {
				field[randX][randY] = new Tile("", new Color(224, 224, 235), randX, randY);
				field[randX][randY].setMine();
			}
			else i--;
		}
		
		/* Creates regular Tiles for the rest of the field. Adds all tiles both with and without mines to the field. */
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (field[i][j] == null) {
					field[i][j] = new Tile("", new Color(224, 224, 235), i, j);	
				}
				this.add(field[i][j]);
			}
		}
		this.paintComponents(this.getGraphics());
	}
	
	/**
	 * Returns current tile.
	 * @return current tile.
	 */
	public Tile getCurrent() {
		return current;
	}

	/**
	 * Sets the current tile to a new tile.
	 * @param current The tile that is being set to current.
	 */
	public void setCurrent(Tile current) {
		this.current = current;
	}
	
	/**
	 * Changes the currently selected tile.
	 * @param tile The tile that is being set to current.
	 */
	public void changeCurrent(Tile tile) {
		setCurrent(tile);
	}
	
	/**
	 * Explodes the field and ends the game.
	 */
	public void explode() {
		Game game = (Game)this.getParent();
		game.endGame();
	}
	
	/**
	 * Increases the number of flags by one.
	 */
	public void incFlags() {
		flags++;
	}
	
	/**
	 * Decreases the number of flags by one.
	 */
	public void decFlags() {
		flags--;
	}
	
	/**
	 * Returns the number of flags left on the field.
	 * @return remaining number of flags.
	 */
	public int getFlags() {
		return flags;
	}
	
	/**
	 * Sets the number of flags to a certain value.
	 * @param flags Number of flags that flags is being set to.
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	/**
	 * Checks if the game is completed. If all tiles with a mine have been flagged or if the remaining number of unopened tiles
	 * is equal to the number of mines, the game was successfully completed and the call returns true, otherwise returns false.
	 * @return true if game was successfully completed, false otherwise.
	 */
	public boolean checkTiles() {
		int success = 0;
		int tiles = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (field[i][j].hasMine() && field[i][j].hasFlag()) success++;
				if (!field[i][j].isOpened()) tiles++;
			}
		}
		if (success == mines || tiles == mines) return true;
		else return false;
	}
	
	/**
	 * Opens the tile at row x and column y.
	 * @param x The row of the tile.
	 * @param y The column of the tile.
	 */
	public void open(int x, int y) {
		if (field[x][y] == null) return;
		else field[x][y].open();
		
		/* First just sets the open flag of a tile to true before actually opening the tile due to the recursive calls of
		 * countSurroundingMines. */
		int mines = countSurruoundingMines(x, y);
		field[x][y].openTile(mines);
		
		/* If all tiles without mines have been opened, the game ends. */
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (!(field[i][j].isOpened() || field[i][j].hasMine())) return;
			}
		}
		Game game = (Game)this.getParent();
		game.endGame();
	}
	
	/**
	 * Opens all tiles with mines to mark a game's end.
	 */
	public void openMineTiles() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (field[i][j].hasMine()) field[i][j].openMineTile();
			}
		}
	}
	
	/**
	 * Counts the number of surrounding tiles with mines. If the number is greater than 0, the value is returned. If the number is
	 * equal to 0, the function for opening a tile is called for all surrounding tiles in order to open them, causing a recursive call.
	 * @param x The row of the tile.
	 * @param y The column of the tile.
	 * @return number of surrounding tiles with mines.
	 */
	private int countSurruoundingMines(int x, int y) {
		int count = 0;
		
		if ((x-1)>=0 && (y-1)>=0 && field[x-1][y-1].hasMine()) count++;						// Top left
		if ((x-1)>=0 && y>=0 && field[x-1][y].hasMine()) count++;							// Left
		if ((x-1)>=0 && (y+1)<rows && field[x-1][y+1].hasMine()) count++;					// Bottom left
		if (x>=0 && (y+1)<rows && field[x][y+1].hasMine()) count++;							// Bottom
		if ((x+1)<columns && (y+1)<rows && field[x+1][y+1].hasMine()) count++;				// Bottom right
		if ((x+1)<columns && y>=0 && field[x+1][y].hasMine()) count++;						// Right
		if ((x+1)<columns && (y-1)>=0 && field[x+1][y-1].hasMine()) count++;				// Top right
		if (x>=0 && (y-1)>=0 && field[x][y-1].hasMine()) count++;							// Top
		
		// Opens unopened surrounding tiles recursively if there are no surrounding mines.
		if (count == 0) {											
			if ((x-1)>=0 && (y-1)>=0 && !field[x-1][y-1].isOpened()) this.open(x-1, y-1);
			if ((x-1)>=0 && y>=0 && !field[x-1][y].isOpened()) this.open(x-1, y);
			if ((x-1)>=0 && (y+1)<rows && !field[x-1][y+1].isOpened()) this.open(x-1, y+1);
			if (x>=0 && (y+1)<rows && !field[x][y+1].isOpened()) this.open(x, y+1);
			if ((x+1)<columns && (y+1)<rows && !field[x+1][y+1].isOpened()) this.open(x+1, y+1);
			if ((x+1)<columns && y>=0 && !field[x+1][y].isOpened()) this.open(x+1, y);
			if ((x+1)<columns && (y-1)>=0 && !field[x+1][y-1].isOpened()) this.open(x+1, y-1);
			if (x>=0 && (y-1)>=0 && !field[x][y-1].isOpened()) this.open(x, y-1);
		}
		
		return count;
	}
}