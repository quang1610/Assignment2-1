package SpeedReader;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.String;

public class WordGenerator {
  //private File file;
  private int wordCounter;
  private int sentenceCounter;
  private Scanner scanner;

  public WordGenerator(String fileName) throws IOException {
    //this.file = new File(fileName);
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

    char finalChar = returnedString.charAt(returnedString.length() - 1);
    if ((finalChar == '.') || (finalChar == '?') || (finalChar == '!')) {
      this.sentenceCounter++;
    }

    return returnedString;
  }

  public int getWordCount() {
    return this.wordCounter;
  }

  public int getSentenceCount() {
    return this.sentenceCounter;
  }
}
