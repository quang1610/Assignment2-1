package SpeedReader;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.String;

public class WordGenerator {
	private int wordCounter;
	private int sentenceCounter;
	private Scanner scanner;

	public WordGenerator(String fileName) throws IOException {
		this.scanner = new Scanner(new File(fileName));
		this.wordCounter = 0;
		this.sentenceCounter = 0;
	}

	public boolean hasNext() {
		return this.scanner.hasNext();
	}

	public String next() {
		String returnedString = this.scanner.next();
		this.wordCounter++;

		if(this.endOfSend(returnedString)) {
			this.sentenceCounter++;
		}
		return returnedString;
	}

	private boolean endOfSend(String str) {
		for (int i = str.length() - 1; i >= 0; i--) {
			char currentChar = str.charAt(i);
			if ((currentChar == '.') || (currentChar == '?') || (currentChar == '!')) {
				return true;
			}
		}
		return false;
	}

	public int getWordCount() {
		return this.wordCounter;
	}

	public int getSentenceCount() {
		return this.sentenceCounter;
	}
}
