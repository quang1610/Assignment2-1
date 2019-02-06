package SpeedReader;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.String;

public class WordGenerator {
	private int wordCounter;
	private int sentenceCounter;
	private Scanner scanner;

	/*
	 * WordGenerator creates a new scanner for a text file and initializes wordCounter and sentenceCounter to 0
	 *
	 * @param <filename> The name of the text file which contains the appropriate text
	 * @returns n/a
	 * @throws IOException if file name is not found
	 *
	 */
	public WordGenerator(String fileName) throws IOException {
		this.scanner = new Scanner(new File(fileName));
		this.wordCounter = 0;
		this.sentenceCounter = 0;
	}

	/* hasNext checks if scanner has another token remaining
	 *
	 *
	 * @param n/a
	 * @returns boolean in relation to whether the scanner has another token
	 * @throws n/a
	 */
	/
	public boolean hasNext() {
		return this.scanner.hasNext();
	}

	/*
	 * next pulls the next token from scanner
	 *
	 * @param n/a
	 * @returns returnedString, the next token
	 * @throws n/a
	 */
	public String next() {
		String returnedString = this.scanner.next();
		this.wordCounter++;

		if(this.endOfSend(returnedString)) {
			this.sentenceCounter++;
		}
		return returnedString;
	}

	/*
	 * endOfSend checks if the character has an end of sentence character at its end
	 *
	 * @param <str>, a string to be checked for a '.' '?' or '!'
	 * @returns boolean, relative to the presence of a '.' '?' or '!'
	 * @throws n/a
	 */
	private boolean endOfSend(String str) {
		for (int i = str.length() - 1; i >= 0; i--) {
			char currentChar = str.charAt(i);
			if ((currentChar == '.') || (currentChar == '?') || (currentChar == '!')) {
				return true;
			}
		}
		return false;
	}

	/*
	 * getWordCount returns the current value of wordCounter
	 *
	 * @param n/a
	 * @returns int, the value of wordCounter
	 * @throws n/a
	 */
	public int getWordCount() {
		return this.wordCounter;
	}

	/*
	 * getSentenceCounter returns the current value of sentenceCounter
	 *
	 * @param n/a
	 * @returns int, the value of sentenceCounter
	 * @throws n/a
	 */
	public int getSentenceCount() {
		return this.sentenceCounter;
	}
}
