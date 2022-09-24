package minesweeper;

import java.io.*;
import java.util.Scanner;

/**
 * A FileReader object is an abstraction used to work with files used to keep track of player scores in the scoreboard. It allows reading
 * files line by line, and writing in said files.
 * @author Nikola Stankovic
 */
public class FileReader {
	private File file;
	private Scanner scan;
	private FileWriter writer;
	private Difficulty diff;
	
	/**
	 * Creates a FileReader object.
	 * @param diff It is the difficulty of the game used to get the name of the file. For example, EASY will create a file named
	 * 'scoreboardEASY.txt'.
	 * @throws IOException
	 */
	public FileReader(Difficulty diff) throws IOException {
		file = new File(System.getProperty("user.dir") + "/scoreboard" + diff + ".txt");
		file.createNewFile();
		scan = new Scanner(file);
		this.diff = diff;
	}
	
	/**
	 * Checks if the file has any lines left to be read.
	 * @return true if file has next line, false otherwise.
	 */
	public boolean hasNextLine() {
		return scan.hasNextLine();
	}
	
	/**
	 * Returns the next line in the file in form of a string.
	 * @return next line in the file.
	 */
	public String getLine() {
		return scan.nextLine();
	}
	
	/**
	 * Prints a string in the file that was already created. Adds a line break at the end if the string is not empty.
	 * @param string The text that is being printed in the file.
	 * @throws IOException
	 */
	public void print(String string) throws IOException {
		writer = new FileWriter(System.getProperty("user.dir") + "/scoreboard" + diff + ".txt");
		if (!string.equals("")) writer.write(string + "\n");
		writer.close();
	}
	
	/**
	 * Closes all resources used to work with files.
	 * @throws IOException
	 */
	public void closeFile() throws IOException {
		scan.close();
	}
}