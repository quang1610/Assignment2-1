package SpeedReader;

public class ProgramState {
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
