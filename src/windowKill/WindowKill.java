package windowKill;

import view.container.MainMenuPanel;
import view.soundManager.GameBackgroundSong;

public class WindowKill implements Runnable {
    public static GameBackgroundSong song = new GameBackgroundSong();
    @Override
    public void run() {
        song.play();
        MainMenuPanel menuPanel = MainMenuPanel.getINSTANCE();
    }

    public static void main(String[] args) {
        new WindowKill().run();
    }
}
