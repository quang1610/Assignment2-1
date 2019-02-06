package SpeedReader;

public class ProgramState {
/*
 * This class tracks the program state, in that it keeps track of whether the program is resumed,
 * paused, finished, or terminated. In resumed state, the program runs normally, in paused state,
 * the program still runs but no further words are shown, in finished state, the program has
 * completed reading, but has not exited, and in terminated state, the program will exit.
 */
  public static final int PAUSED = 1;
  public static final int RESUMED = 2;
  public static final int FINISHED = 3;
  public static final int TERMINATED = 4;
  private int currentState;

  /*
   * ProgramState is the constructor for currentState, initializing it to 'PAUSED'
   */
  public ProgramState() {
    this.currentState = this.PAUSED;
  }

  /*
   * resume changes the currentState to RESUMED
   */
  public void resume() {
    this.currentState = this.RESUMED;
  }

  /*
   * pause changes the currentState to PAUSED
   */
  public void pause() {
    this.currentState = this.PAUSED;
  }

  /*
   * finish changes the currentState to FINISHED
   */
  public void finish() {
    this.currentState = this.FINISHED;
  }

  /*
   * terminate changes the currentState to TERMINATED
   */
  public void terminate() {
    this.currentState = this.TERMINATED;
  }

  /*
   * getState provides the current state
   *
   * @returns currentState
   */
  public int getState() {
    return this.currentState;
  }
}
