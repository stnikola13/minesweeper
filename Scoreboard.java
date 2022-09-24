package minesweeper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A Scoreboard object is used to keep track of player scores.
 * @author Nikola Stankovic
 */
public class Scoreboard {
	private ArrayList<Player> listEasy, listMed, listHard;
	private FileReader fileEasy, fileMed, fileHard;
	private Cipher cryptor;
	
	/**
	 * Creates a Scoreboard object. Creates three lists of players and three file readers, one for each difficulty.
	 * It loads previously saved scoreboards from respective files, after deciphering their contents.
	 * @throws IOException
	 */
	public Scoreboard() throws IOException {
		cryptor = new Cipher();
		listEasy = new ArrayList<Player>();
		listMed = new ArrayList<Player>();
		listHard = new ArrayList<Player>();
		fileEasy = new FileReader(Difficulty.EASY);
		fileMed = new FileReader(Difficulty.MEDIUM);
		fileHard = new FileReader(Difficulty.HARD);
		
		while(fileEasy.hasNextLine()) {
			String line = fileEasy.getLine();
			
			line = cryptor.decrypt(line);
			
			String info[] = line.split(";");
			Player player = new Player(info[0], info[1]);
			listEasy.add(player);		
		}
		while(fileMed.hasNextLine()) {
			String line = fileMed.getLine();
			
			line = cryptor.decrypt(line);
			
			String info[] = line.split(";");
			Player player = new Player(info[0], info[1]);
			listMed.add(player);		
		}
		while(fileHard.hasNextLine()) {
			String line = fileHard.getLine();
			
			line = cryptor.decrypt(line);
			
			String info[] = line.split(";");
			Player player = new Player(info[0], info[1]);
			listHard.add(player);		
		}
	}
	
	/**
	 * Adds a player to the scoreboard list. The list is sorted by player time. If the added player is not in the top 10, it is removed
	 * from the list.
	 * @param p The player who is added to the list.
	 * @param diff The difficulty of the game played in order to determine to which list the player needs to be added to.
	 * @return true if player was added, false otherwise.
	 */
	public boolean addPlayer(Player p, Difficulty diff) {
		ArrayList<Player> list = null;
		if (diff == Difficulty.EASY) list = listEasy;
		else if (diff == Difficulty.MEDIUM) list = listMed;
		else if (diff == Difficulty.HARD) list = listHard;
		
		/* Determines where in the list the player needs to be added in order to keep the list sorted. */
		boolean added = false;
		for (int i = 0; i < list.size(); i++) {
			Player p1 = list.get(i);
			if (Player.compare(p, p1) < 0) {
				list.add(i, p);
				added = true;
				break;
			}
			else continue;
		}
		if (!added && list.size() < 10) list.add(p);
		if (list.size() > 10) {
			list.remove(10);
			return false;
		}
		return true;
	}
	
	/**
	 * Saves all three scoreboards to their respective files after previously ciphering the contents.
	 */
	public void saveScoreboard() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < listEasy.size(); i++) {
			Player p = listEasy.get(i);
			sb.append(p.getName() + ";" + p.getTime());
			if (i != listEasy.size() - 1) sb.append("\n");
		}
		try {
			fileEasy.print(cryptor.encrypt(sb.toString()));
		} catch (IOException e) {}
		
		sb = new StringBuilder();
		for (int i = 0; i < listMed.size(); i++) {
			Player p = listMed.get(i);
			sb.append(p.getName() + ";" + p.getTime());
			if (i != listMed.size() - 1) sb.append("\n");
		}
		try {
			fileMed.print(cryptor.encrypt(sb.toString()));
		} catch (IOException e) {}
		
		sb = new StringBuilder();
		for (int i = 0; i < listHard.size(); i++) {
			Player p = listHard.get(i);
			sb.append(p.getName() + ";" + p.getTime());
			if (i != listHard.size() - 1) sb.append("\n");
		}
		try {
			fileHard.print(cryptor.encrypt(sb.toString()));
		} catch (IOException e) {}
	}
	
	/**
	 * Makes a formatted string of a scoreboard ready to be printed or displayed.
	 * @param diff The difficulty of the game.
	 * @return scoreboard of the game with difficulty diff.
	 */
	public String getPrintable(Difficulty diff) {
		ArrayList<Player> list = null;
		if (diff == Difficulty.EASY) list = listEasy;
		else if (diff == Difficulty.MEDIUM) list = listMed;
		else if (diff == Difficulty.HARD) list = listHard;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			Player p = list.get(i);
			if (i == 0 || i == 1 || i == 2) sb.append("<b><font color=\"#a10035\">");
			else sb.append("<b><font color=\"#9494b8\">");
			sb.append((i + 1) + ".\t" + p.getName() + " - " + p.getTime() + "</b>");
			if (i != list.size() - 1) sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Returns the number of entries in a scoreboard.
	 * @param diff The difficulty of the game.
	 * @return number of entries in the scoreboard of the game with difficulty diff.
	 */
	public int getNumOfEntries(Difficulty diff) {
		ArrayList<Player> list = null;
		if (diff == Difficulty.EASY) list = listEasy;
		else if (diff == Difficulty.MEDIUM) list = listMed;
		else if (diff == Difficulty.HARD) list = listHard;
		return list.size();
	}
}