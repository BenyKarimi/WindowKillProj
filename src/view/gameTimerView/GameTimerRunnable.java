package view.gameTimerView;

public class GameTimerRunnable implements Runnable {
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private volatile int seconds = 0;
    private final int period;
    private final int addSeconds;

    public GameTimerRunnable(int period, int addSeconds) {
        this.period = period;
        this.addSeconds = addSeconds;
    }

    @Override
    public void run() {
        while (running) {
            synchronized (this) {
                while (paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            seconds += addSeconds;

            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public synchronized void stop() {
        running = false;
        paused = false;
        notifyAll();
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public int getSeconds() {
        return seconds;
    }
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
