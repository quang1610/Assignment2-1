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

  // static Objects
  private static WordGenerator wordGen;
  private static DrawingPanel panel;
  private static SpeedReader.PanelMouse pMouse;
  private static ProgramState state;

  // static primitive variables
  private static int width;
  private static int height;
  private static int fontSize;
  private static int wpm;
  private static long delayTime;

  // constant decide the size of font to print stats
  private static final int SMALL_FONT_SIZE = 10;

  /*
   * main method
   * 
   * @param args a String array, should have length of 5 in format: fileName panel-Width
   * panel-Height fontSize Words-per-minute
   * 
   * @throws IOException InterruptedException
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    if (args.length != 5) {
      System.err.println("Invalid number of arguments, expecting 5");
      System.exit(1);
    } else {
      // initialize the Objects and static variables
      wordGen = new WordGenerator("testText.txt");
      state = new ProgramState();
      // read from the args array and set value for width, height, fontSize, readSpeed.
      width = Integer.parseInt(args[1]);
      height = Integer.parseInt(args[2]);
      fontSize = Integer.parseInt(args[3]);
      wpm = Integer.parseInt(args[4]);
      delayTime = 1000 / (wpm / 60);

      // create mouse listener to panel
      pMouse = new PanelMouse();
      // create panel
      panel = new DrawingPanel(width, height);
      // add mouse listener to panel
      panel.addMouseListener(pMouse);

      // start the Speed Reading Program
      startSpeedReading();
    }
  }

  /*
   * Run the Speed Reading program
   * 
   * @throws InterruptedException because we use Thread.sleep(delayTime)
   */
  private static void startSpeedReading() throws InterruptedException {
    // set up the Graphics object to draw and the fonts to print out text on the Panel
    Graphics g = panel.getGraphics();
    Font f = new Font("Courier", Font.BOLD, fontSize);
    Font smallF = new Font("Courier", Font.PLAIN, SMALL_FONT_SIZE);

    // currStr holds the current String of the word being displayed on the screen
    String currStr = "";
    /*
     * If the state (see more in ProgramState.java) is not set to TERMINATED, then run the program.
     * in each loop: - If wordGen has next word to display: - If the program is in RESUMED state,
     * set currStr = next word to display. Else wordGen has no more word, set state to FINISHED. -
     * Clean the panel to draw new String. - Draw currStr. - Draw Stats on the left upper conner. -
     * go to sleep for delayTime. Else if state is set to TERMINATED then escape the loop, clear the
     * Panel and end the method.
     * 
     */
    while (state.getState() != ProgramState.TERMINATED) {
      if (wordGen.hasNext()) {
        if (state.getState() == ProgramState.RESUMED) {
          currStr = wordGen.next();
        }
      } else {
        state.finish();
      }
      // clear the panel
      panel.clearWithoutRepaint();
      // draw currStr
      drawWord(currStr, g, f);
      // draw Stats
      drawStats(g, smallF, state.getState());
      // go to Sleep
      Thread.sleep(delayTime);
    }
    panel.clearWithoutRepaint();
  }

  /*
   * draw the current Word in the middle of the screen with Font f.
   * 
   * @param str the String we need to print on the screen.
   * 
   * @param g the Graphics object belong to panel.
   * 
   * @param f the font we used for str.
   */
  private static void drawWord(String str, Graphics g, Font f) {
    g.setFont(f);
    // get a rectangle that bound around str. Used this to get the width and height of str on the
    // screen.
    FontRenderContext frc = g.getFontMetrics(f).getFontRenderContext();
    Rectangle2D rec = f.getStringBounds(str, frc);
    // draw str
    g.drawString(str, width / 2 - (int) rec.getWidth() / 2, height / 2 - (int) rec.getHeight() / 2);
  }

  /*
   * Print out the stats (words count, sentences count, prompts to guide the user)
   * 
   * @param g the Graphics object belong to panel.
   * 
   * @param f the font we used for stats.
   * 
   * @param currentState the state of the program at the moment (see more ProgramState.java)
   */
  private static void drawStats(Graphics g, Font f, int currentState) {
    g.setFont(f);
    // get the rectangle bound around the text of font f with largest height possible (that's why I
    // chose "Lg")
    FontRenderContext frc = g.getFontMetrics(f).getFontRenderContext();
    Rectangle2D rec = f.getStringBounds("Lg", frc);

    // draw Stats
    g.drawString("Word count: " + wordGen.getWordCount(), 5, 20);
    g.drawString("Sentence Count: " + wordGen.getSentenceCount(), 5, 30 + (int) rec.getHeight());
    g.drawString("Words per minute: " + wpm, 5, 40 + (int) rec.getHeight() * 2);
    g.drawString("Press on the left of the screen to reduce speed", 5, // guide
        50 + (int) rec.getHeight() * 3);
    g.drawString("Press on the right of the screen to increase speed", 5, // guide
        60 + (int) rec.getHeight() * 4);

    // draw prompts based on the current state of the program
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
      g.drawString("You have finished reading! Please press in the middle of the screen to exit!",
          20, 70 + (int) rec.getHeight() * 5);
      g.setColor(Color.BLACK);
    }
  }

  /*
   * nested static class to handle the mouse event on the panel
   */
  static class PanelMouse implements MouseListener {

    // constructor for PanelMouse()
    public PanelMouse() {}

    /*
     * This function is called whenever the mouse is clicked.
     * 
     * If we click on the left side of the screen, the wpm is decreased by 20 wpm.
     * 
     * If we click on the right side of the screen, the wpm is increased by 20 wpm.
     * 
     * If we click on the middle of the screen, we change the program stats (ie RESUMED to PAUSED,
     * PAUSED to RESUMED...)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     * 
     * @param e MouseEvent.
     */
    public void mouseClicked(MouseEvent e) {
      // the x coordinate of the point where we click.
      int clickedX = e.getX();
      // get the width of the panel.
      int width = e.getComponent().getWidth();

      // if we click around the middle of the screen
      if ((clickedX < 2 * width / 3) && (clickedX > width / 3)) {
        changeState(state);
      } else {
        // if we clicked on the left of the screen
        if ((clickedX < width / 3) && (clickedX > 0)) {
          wpm -= 20;
          delayTime = 1000 / (wpm / 60);
          // if we clicked on the right of the screen
        } else if ((clickedX > width * 2 / 3) && (clickedX < width)) {
          wpm += 20;
          delayTime = 1000 / (wpm / 60);
        }
      }
    }

    /*
     * change state base on the current program's state.
     * 
     * If it is currently PAUSED, change to RESUMED. If it is currently RESUMED, change to PAUSED.
     * If it is currently FINISHED, change to TERMINATED. FINISHED state is reached when we finish
     * reading the text file. The program is still running then. You need to click on the middle of
     * the screen to TERMINATE
     */
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

    //unused method
    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
  }

}
