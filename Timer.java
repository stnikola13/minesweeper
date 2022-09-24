package minesweeper;

import java.awt.Label;

/**
 * A Timer object is a Thread used to count elapsed time during the course of the game.
 * @author Nikola Stankovic
 */
public class Timer extends Thread {
	private boolean count = false;
	private int counter = 0;
	private Label label;
	
	/**
	 * Creates a Timer object.
	 * @param label Label is a Label object which the timer will use to display the time.
	 */
	public Timer(Label label) {
		super();
		this.label = label;
	}
	
	/**
	 * While the thread is not interrupted, the timer cyclically sleeps, then increases the counter by one and then displays the time.
	 * The thread sleeps for 9, instead of 10 milliseconds due to the delay in context switches between threads in order to accurately
	 * count elapsed time. If count is set to false, time timer is paused and the counter doesn't increase its value.
	 */
	public void run() {	
		try {
			while (!this.isInterrupted()) {
				Thread.sleep(9);						
				if (count) incTime();					
				label.setText(formatTime(counter));
			}
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Returns time in tens of milliseconds.
	 * @return time in tens of milliseconds
	 */
	public int getTime() {
		return counter;
	}
	
	/**
	 * Atomically increases the value of the counter by one.
	 */
	private synchronized void incTime() {
		counter++;
	}
	
	/**
	 * Converts the total elapsed time in tens of milliseconds into text in the mm:ss:msms format.
	 * @param time Time is the total time in tens of milliseconds.
	 * @return a formatted string.
	 */
	private synchronized String formatTime(int time) {
		String sec, min, milli;
		
		if (time % 100 < 10) milli = "0" + (time % 100);
		else milli = "" + time % 100;
		
		if (time / 100 % 60 < 10) sec = "0" + (time / 100 % 60);
		else sec = "" + time / 100 % 60;
		
		if (time / 100 / 60 < 10) min = "0" + (time / 100 / 60);
		else min = "" + time / 100 / 60;
		
		return min + ":" + sec + ":" + milli;
	}
	
	/**
	 * Stops the timer by interrupting the thread.
	 */
	public synchronized void stopTimer() {
		this.interrupt();
	}
	
	/**
	 * Starts the timer by setting the boolean variable count to true.
	 */
	public void startCount() {
		count = true;
	}
	
	/**
	 * Restarts the timer by setting the counter back to 0.
	 */
	public void restartTimer() {
		counter = 0;
	}
}