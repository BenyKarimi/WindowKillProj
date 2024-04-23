package view.container;

import controller.TypedActionHandel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static controller.constant.Constants.GLASS_FRAME_DIMENSION;

public final class GlassFrame extends JFrame {
    private static GlassFrame INSTANCE;
    private GlassFrame() throws HeadlessException {
        this.setUndecorated(true);
        this.setBackground(new Color(0,0,0,0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(GLASS_FRAME_DIMENSION);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                TypedActionHandel.handlePressedKey(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                TypedActionHandel.handleReleasedKey(e.getKeyCode());
            }
        });

        this.setVisible(true);
    }

    public static GlassFrame getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new GlassFrame();
        return INSTANCE;
    }
}
