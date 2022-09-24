package minesweeper;

/**
 * A Player object is used to keep track of information of a played game, including the name of the player who completed the game,
 * as well as the time needed to complete it. The time is kept in form of a string and three integers - minutes, seconds and milliseconds.
 * @author Nikola Stankovic
 */
public class Player {
	private String name;
	private String time;
	private int m, s, ms;
	
	/**
	 * Creates a Player object.
	 * @param name Name is a string which contains the name of the player who completed the game.
	 * @param time Time is a string which contains the time it took to complete the game in the mm:ss:msms format.
	 */
	public Player(String name, String time) {
		this.name = name;
		this.time = time;
		
		String times[] = time.split(":");
		m = Integer.parseInt(times[0]);
		s = Integer.parseInt(times[1]);
		ms = Integer.parseInt(times[2]);
	}
	
	/**
	 * Returns the time of the player as a string.
	 * @return time.
	 */
	public String getTime() {
		return time;
	}
	
	/**
	 * Returns the name of the player as a string.
	 * @return name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the total time of the player in milliseconds.
	 * @return time in milliseconds.
	 */
	public int getTimeMs() {
		return ((m * 60) + s) * 1000 + ms * 10;
	}
	
	/**
	 * Compares two players by comparing their total times.
	 * @param p1 The first player.
	 * @param p2 The second player.
	 * @return 0 if the times are equal, 1 if P1 was slower than P2, and -1 if P1 was faster than P2.
	 */
	public static int compare(Player p1, Player p2) {
		if (p1.getTimeMs() == p2.getTimeMs()) return 0;
		else if (p1.getTimeMs() > p2.getTimeMs()) return 1;
		else return -1;
	}
}