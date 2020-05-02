package MES;

public class StopWatch {

    private long executionTime;

    public StopWatch() {
        this.executionTime = 0;
    }

    public void start() {
        this.executionTime = System.nanoTime();
    }

    public void stop() {
        this.executionTime = System.nanoTime() - this.executionTime;
    }

    public void reset() {
        this.executionTime = 0;
    }

    public int getTimeElapsed() {
        //gets time in seconds
        return (int) (System.nanoTime() - executionTime) / 1000000000;
    }
}
