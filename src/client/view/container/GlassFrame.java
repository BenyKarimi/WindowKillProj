package client.view.container;

import client.controller.handeler.TypedActionHandle;
import client.view.gameTimerView.GameTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static client.controller.constant.Constants.GLASS_FRAME_DIMENSION;

public final class GlassFrame extends JFrame {
    private static GlassFrame INSTANCE;
    private GameTimer timer;
    private GlassFrame() throws HeadlessException {
        this.setUndecorated(true);
        this.setBackground(new Color(0,0,0,0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(GLASS_FRAME_DIMENSION);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("client.windowKill.WindowKill");

        ImageIcon image = new ImageIcon("resources/AppImage.jpg");
        this.setIconImage(image.getImage());

        timer = new GameTimer();

        addKeyListener();

        this.setVisible(true);
    }
    public void addKeyListener() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (GamePanel.gamePanelList.size() == 0) return;
                TypedActionHandle.handlePressedKey(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (GamePanel.gamePanelList.size() == 0) return;
                TypedActionHandle.handleReleasedKey(e.getKeyCode());
            }
        });
    }

    public static GlassFrame getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new GlassFrame();
        return INSTANCE;
    }

    public GameTimer getTimer() {
        return timer;
    }
}
