package minesweeper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * A Cipher object is used for ciphering and deciphering strings. It uses randomized keys to cipher strings. The generated keys
 * are stored in an external file along with other information making it difficult to find the key in the file. The saved key is used
 * for later deciphering of strings.
 * @author Nikola Stankovic
 */
public class Cipher {
	private int keyBigLetters;
	private int keySmallLetters;
	private int keyNumbers;
	private int keyOther;
	
	/**
	 * Creates a Cipher object. If a file with keys exists, the keys are loaded into the object. Otherwise, keys are randomly generated
	 * and a file is created to store them.
	 * @throws IOException
	 */
	public Cipher() throws IOException {
		File file = new File(System.getProperty("user.dir") + "/key.txt");
		file.createNewFile();
		Scanner scan = new Scanner(file);
		
		if (!scan.hasNextLine()) {
			keyBigLetters = (int)(Math.random() * 25 + 1);
			keySmallLetters = (int)(Math.random() * 25 + 1);
			keyNumbers = (int)(Math.random() * 9 + 1);		
			keyOther = (int)(Math.random() * 5 + 1);
			
			/* Creates a string with 500 randomized numbers, and the digits of the keys are placed at predefined locations in it. */
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 500; i++) sb.append((char)((int)(Math.random() * 10)));
			sb.setCharAt(17, (char)(keyBigLetters / 10));
			sb.setCharAt(41, (char)(keyBigLetters % 10));
			sb.setCharAt(62, (char)(keySmallLetters / 10));
			sb.setCharAt(75, (char)(keySmallLetters % 10));
			sb.setCharAt(91, (char)(keyNumbers / 10));
			sb.setCharAt(114, (char)(keyNumbers % 10));
			sb.setCharAt(121, (char)(keyOther));
			
			FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/key.txt");
			writer.write(sb.toString() + "\n");
			writer.close();
		}
		else {
			StringBuilder sb = new StringBuilder(scan.nextLine());
			keyBigLetters = (int)sb.charAt(17) * 10 + (int)sb.charAt(41);
			keySmallLetters = (int)sb.charAt(62) * 10 + (int)sb.charAt(75);
			keyNumbers = (int)sb.charAt(91) * 10 + (int)sb.charAt(114);
			keyOther = (int)sb.charAt(121);
		}
		
		scan.close();
	}
	
	/**
	 * Ciphers a string using keys that were previously generated.
	 * @param code is a string that is being ciphered.
	 * @return ciphered string.
	 */
	public String encrypt(String code) {
		StringBuilder sb = new StringBuilder(code);	
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			
			/**
			 * Capital letters are ciphered into other capital letters. Same goes for small letters and numbers, respectively.
			 */
			if (c == '\n') continue;
			else if (c == ';' || c == ':') sb.setCharAt(i, (char)(c + keyOther));
			else if (c >= 65 && c <= 90) {
				if (c + keyBigLetters <= 90) sb.setCharAt(i, (char)(c + keyBigLetters));
				else sb.setCharAt(i, (char)(c + keyBigLetters - 26));
			}
			else if (c >= 97 && c <= 122) {
				if (c + keySmallLetters <= 122) sb.setCharAt(i, (char)(c + keySmallLetters));
				else sb.setCharAt(i, (char)(c + keySmallLetters - 26));
			}
			else if (c >= 48 && c <= 57) {
				if (c + keyNumbers <= 57) sb.setCharAt(i, (char)(c + keyNumbers));
				else sb.setCharAt(i, (char)(c + keyNumbers - 10));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Deciphers a string using keys that were previously generated.
	 * @param code is a string that is being deciphered.
	 * @return deciphered string.
	 */
	public String decrypt(String code) {
		StringBuilder sb = new StringBuilder(code);	
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			
			if (c == '\n') continue;
			else if (c == ';'+ keyOther || c == ':' + keyOther) sb.setCharAt(i, (char)(c - keyOther));
			else if (c >= 65 && c <= 90) {
				if (c - keyBigLetters >= 65) sb.setCharAt(i, (char)(c - keyBigLetters));
				else sb.setCharAt(i, (char)(c - keyBigLetters + 26));
			}
			else if (c >= 97 && c <= 122) {
				if (c - keySmallLetters >= 97) sb.setCharAt(i, (char)(c - keySmallLetters));
				else sb.setCharAt(i, (char)(c - keySmallLetters + 26));
			}
			else if (c >= 48 && c <= 57) {
				if (c - keyNumbers >= 48) sb.setCharAt(i, (char)(c - keyNumbers));
				else sb.setCharAt(i, (char)(c - keyNumbers + 10));
			}
		}
		
		return sb.toString();
	}
}