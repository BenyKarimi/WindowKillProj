package client.windowKillApplication;

import client.Client;
import client.view.container.MainMenuPanel;
import client.view.container.StartingPanel;
import client.view.soundManager.GameBackgroundSong;

public class WindowKill implements Runnable {
    public static GameBackgroundSong song = new GameBackgroundSong();
    public static Client client;
    @Override
    public void run() {
        song.play();
        StartingPanel startingPanel = StartingPanel.getINSTANCE();
    }
}
