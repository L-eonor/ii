package MES;

public class StopWatch {

    private static long executionTime;

    public StopWatch() {
        executionTime = 0;
    }

    public void start() {
        executionTime = System.nanoTime();
    }

    public void stop() {
        executionTime = System.nanoTime() - executionTime;
    }

    public void reset() {
        executionTime = 0;
    }

    public int getTimeElapsed() {
        //gets time in seconds
        return (int) (System.nanoTime() - executionTime) / 1000000000;
    }
}
