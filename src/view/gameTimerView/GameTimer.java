package view.gameTimerView;

public class GameTimer {
    private Thread secondThread;
    private Thread miliSecondThread;
    private GameTimerRunnable secondRunnable;
    private GameTimerRunnable miliSecondRunnable;

    public GameTimer() {
        secondRunnable = new GameTimerRunnable(1000, 1);
        secondThread = new Thread(secondRunnable);
        secondThread.start();
        miliSecondRunnable = new GameTimerRunnable(100, 100);
        miliSecondThread = new Thread(miliSecondRunnable);
        miliSecondThread.start();
    }

    @Override
    public String toString() {
        String out = "";
        if (secondRunnable.getSeconds() / 60 < 10) {
            out += ("0" + Integer.valueOf(secondRunnable.getSeconds() / 60).toString());
        }
        else {
            out += (Integer.valueOf(secondRunnable.getSeconds() / 60).toString());
        }
        out += ":";
        if (secondRunnable.getSeconds() % 60 < 10) {
            out += ("0" + Integer.valueOf(secondRunnable.getSeconds() % 60).toString());
        }
        else {
            out += (Integer.valueOf(secondRunnable.getSeconds() % 60).toString());
        }
        return out;
    }

    public void Stop() {
        secondRunnable.pause();
        miliSecondRunnable.pause();
    }
    public void Start() {
        secondRunnable.resume();
        miliSecondRunnable.resume();
    }
    public void Reset() {
        secondRunnable.setSeconds(0);
        miliSecondRunnable.setSeconds(0);
    }
    public int getSeconds() {
        return secondRunnable.getSeconds();
    }

    public int getMiliSecond() {
        return miliSecondRunnable.getSeconds();
    }
    public void setSeconds(int time) {
        secondRunnable.setSeconds(time);
    }
    public void setMiliSeconds(int time) {
        miliSecondRunnable.setSeconds(time);
    }
}

