package SpeedReader;

public class ProgramState {
  public static final int PAUSED = 1;
  public static final int RESUMED = 2;
  public static final int FINISHED = 3;
  public static final int TERMINATED = 4;
  private int currentState;

  public ProgramState() {
    this.currentState = this.PAUSED;
  }

  public void resume() {
    this.currentState = this.RESUMED;
  }

  public void pause() {
    this.currentState = this.PAUSED;
  }

  public void finish() {
    this.currentState = this.FINISHED;
  }

  public void terminate() {
    this.currentState = this.TERMINATED;
  }

  public int getState() {
    return this.currentState;
  }
}
