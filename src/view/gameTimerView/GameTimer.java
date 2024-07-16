package view.gameTimerView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameTimer implements ActionListener {
    private int seconds;
    private Timer timer;

    public GameTimer() {
        seconds = 0;
        timer = new Timer(1000, this);
        timer.start();
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
        timer.stop();
    }
    public void Start() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        seconds++;
    }

    public int getSeconds() {
        return seconds;
    }
}

