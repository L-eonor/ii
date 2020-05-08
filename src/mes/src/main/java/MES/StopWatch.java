package MES;

public class StopWatch {

    private static long executionTime;

    public synchronized static void start() {
        executionTime = System.nanoTime();
    }

    public synchronized static void stop() {
        executionTime = System.nanoTime() - executionTime;
    }

    public synchronized static void reset() {
        executionTime = 0;
    }

    public synchronized static float getTimeElapsed() {
        //gets time in seconds
        return (float) (System.nanoTime() - executionTime) / 1000000000;
    }
}
