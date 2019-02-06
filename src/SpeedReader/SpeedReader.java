package SpeedReader;

import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.Integer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class SpeedReader {

  private static WordGenerator wordGen;
  private static DrawingPanel panel;
  private static int width;
  private static int height;
  private static int fontSize;
  private static int wpm;
  private static long delayTime;
  private static SpeedReader.PanelMouse pMouse;
  private static ProgramState state;

  private static final int SMALL_FONT_SIZE = 10;

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
      delayTime = 1000 / (wpm / 60);
      state = new ProgramState();

      pMouse = new PanelMouse();
      panel = new DrawingPanel(width, height);
      panel.addMouseListener(pMouse);
      startSpeedReading();
    }
  }

  private static void startSpeedReading() throws InterruptedException {
    Graphics g = panel.getGraphics();
    Font f = new Font("Courier", Font.BOLD, fontSize);
    Font smallF = new Font("Courier", Font.PLAIN, SMALL_FONT_SIZE);

    String currStr = "";
    while (state.getState() != ProgramState.TERMINATED) {
      if (wordGen.hasNext()) {
        if (state.getState() == ProgramState.RESUMED) {
          currStr = wordGen.next();
        }
      } else {
        state.finish();
      }
      panel.clearWithoutRepaint();
      drawWord(currStr, g, f);
      drawStats(g, smallF, state.getState());

      Thread.sleep(delayTime);
    }
    panel.clearWithoutRepaint();
  }

  private static void drawWord(String str, Graphics g, Font f) {
    g.setFont(f);

    FontRenderContext frc = g.getFontMetrics(f).getFontRenderContext();
    Rectangle2D rec = f.getStringBounds(str, frc);

    g.drawString(str, width / 2 - (int) rec.getWidth() / 2, height / 2 - (int) rec.getHeight() / 2);
  }

  private static void drawStats(Graphics g, Font f, int currentState) {
    g.setFont(f);

    FontRenderContext frc = g.getFontMetrics(f).getFontRenderContext();
    Rectangle2D rec = f.getStringBounds("Lg", frc);

    g.drawString("Word count: " + wordGen.getWordCount(), 5, 20);
    g.drawString("Sentence Count: " + wordGen.getSentenceCount(), 5, 30 + (int) rec.getHeight());
    g.drawString("Words per minute: " + wpm, 5, 40 + (int) rec.getHeight() * 2);
    g.drawString("Press on the left of the screen to reduce speed", 5,
        50 + (int) rec.getHeight() * 3);
    g.drawString("Press on the right of the screen to increase speed", 5,
        60 + (int) rec.getHeight() * 4);

    if (currentState == ProgramState.RESUMED) {
      g.setColor(Color.RED);
      g.drawString("Press in the middle of the screen to pause or resume", 5,
          70 + (int) rec.getHeight() * 5);
      g.setColor(Color.BLACK);
    } else if (currentState == ProgramState.PAUSED) {
      g.setColor(Color.RED);
      g.drawString("Click on the middle of the screen to continue!", 5,
          70 + (int) rec.getHeight() * 5);
      g.setColor(Color.BLACK);
    } else if (currentState == ProgramState.FINISHED) {
      g.setColor(Color.RED);
      g.drawString("You have finished reading!", 20, 70 + (int) rec.getHeight() * 5);
      g.setColor(Color.BLACK);
    }
  }

  static class PanelMouse implements MouseListener {
    private int clickedX;
    // private int clickedY;

    public PanelMouse() {
      this.clickedX = -1;
      // this.clickedY = -1;
    }

    public void mouseClicked(MouseEvent e) {
      this.clickedX = e.getX();
      // this.clickedY = e.getY();
      int width = e.getComponent().getWidth();
      // int height = e.getComponent().getHeight();

      if ((this.clickedX < 2 * width / 3) && (this.clickedX > width / 3)) {
        changeState(state);
      } else {
        if ((this.clickedX < width / 3) && (this.clickedX > 0)) {
          wpm -= 20;
          delayTime = 1000 / (wpm / 60);
        } else if ((this.clickedX > width * 2 / 3) && (this.clickedX < width)) {
          wpm += 20;
          delayTime = 1000 / (wpm / 60);
        }
      }
      // System.out.println(this.clickedX + " " + this.clickedY);
      // System.out.println("Current State: " + state.getState() + " " + clickedX + "
      // " + width + " " + height);
    }

    private void changeState(ProgramState state) {
      switch (state.getState()) {
        case ProgramState.PAUSED:
          state.resume();
          break;
        case ProgramState.RESUMED:
          state.pause();
          break;
        case ProgramState.FINISHED:
          state.terminate();
          break;
        default:
          break;
      }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
  }

}
