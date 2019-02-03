package SpeedReader;

import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.Integer;
import java.awt.Graphics;
import java.awt.Font;

public class SpeedReader {

  private static WordGenerator wordGen;
  private static DrawingPanel panel;
  private static int width;
  private static int height;
  private static int fontSize;
  private static int wpm;


  public static void main(String[] args) throws IOException, InterruptedException {
    if (args.length != 5) {
      System.err.println("Invalid number of arguments, expecting 5");
      System.exit(1);
    } else {
      wordGen = new WordGenerator("testText.txt");

      width = Integer.parseInt(args[1]);
      height = Integer.parseInt(args[2]);
      fontSize = Integer.parseInt(args[3]);
      wpm = Integer.parseInt(args[4]);

      panel = new DrawingPanel(width, height);

      drawWords();
    }
  }

  private static void drawWords() throws InterruptedException {
    Graphics g = panel.getGraphics();
    Font f = new Font("Courier", Font.BOLD, fontSize);
    Font smallF = new Font("Courier", Font.PLAIN, fontSize/4);
    
    long delayTime = 1000/(wpm/60);
    while (wordGen.hasNext()) {
      g.setFont(f);
      g.drawString(wordGen.next(), width/2, height/2);
      
      g.setFont(smallF);
      g.drawString("Read words: " + wordGen.getWordCount(), width*1/10, height*3/4);
      g.drawString("Read sentences: " + wordGen.getSentenceCount(), width*1/10, height*7/8);
      Thread.sleep(delayTime);
      panel.clear();
    }
  }

}
