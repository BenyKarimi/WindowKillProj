package view.gameTimerView;

public class GameTimer {
    private int seconds;
    private int miliSecond;
    private Thread secondThread;
    private Thread miliSecondThread;
    private boolean stopped;

    public GameTimer() {
        seconds = 0;
        stopped = false;
        secondThread = new Thread(() -> {
            while (!stopped) {
                seconds++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        secondThread.start();
        miliSecondThread = new Thread(() -> {
            while (!stopped) {
                miliSecond += 100;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        miliSecondThread.start();
    }

    @Override
    public String toString() {
        String out = "";
        if (seconds / 60 < 10) {
            out += ("0" + Integer.valueOf(seconds / 60).toString());
        }
        else {
            out += (Integer.valueOf(seconds / 60).toString());
        }
        out += ":";
        if (seconds % 60 < 10) {
            out += ("0" + Integer.valueOf(seconds % 60).toString());
        }
        else {
            out += (Integer.valueOf(seconds % 60).toString());
        }
        return out;
    }

    public void Stop() {
        stopped = true;
    }
    public void Start() {
        stopped = false;
    }
    public void Reset() {seconds = 0;}
    public int getSeconds() {
        return seconds;
    }

    public int getMiliSecond() {
        return miliSecond;
    }
}

