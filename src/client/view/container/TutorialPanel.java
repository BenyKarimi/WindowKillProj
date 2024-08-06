package client.view.container;

import client.controller.constant.Constants;
import client.view.customs.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static client.controller.constant.Constants.BACKGROUND_IMAGE;
import static client.controller.constant.Constants.SHOW_COLOR;

public class TutorialPanel extends JPanel {
    private static TutorialPanel INSTANCE;
    public TutorialPanel() {
        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLocationToCenter(GlassFrame.getINSTANCE());
        this.setLayout(null);

        addTextarea();
        addButtons();

        this.setVisible(true);

        GlassFrame.getINSTANCE().repaint();
    }
    private void addButtons() {
        CustomButton backToMenu = new CustomButton("Back");
        backToMenu.setFont(new Font("akashi", Font.BOLD, 25));
        backToMenu.setForeground(SHOW_COLOR);
        backToMenu.setBounds(0, 40, 125, 25);
        backToMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GlassFrame.getINSTANCE().remove(INSTANCE);
                GlassFrame.getINSTANCE().add(MainMenuPanel.getINSTANCE());
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                backToMenu.setFont(new Font(backToMenu.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backToMenu.setFont(new Font(backToMenu.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backToMenu.setFont(new Font(backToMenu.getFont().getFontName(), Font.BOLD, 25));
            }
        });
        this.add(backToMenu);
    }
    private void addTextarea() {
        JTextArea tutorialText = new JTextArea();
        tutorialText.setText("Welcome to our game!\n Dive into a world where geometric entities clash in a dynamic environment.\n\n"
                + "Objectives:\n"
                + "- Navigate Epsilon through multiple waves of attacks.\n"
                + "- Manage resources and upgrades effectively.\n\n"
                + "Controls:\n"
                + "- Movement: Arrow keys or WASD.\n"
                + "- Shooting: Left mouse button to shoot at cursor location.\n"
                + "- Abilities: Use hotkeys assigned in settings.\n\n"
                + "Gameplay:\n"
                + "- Monitor health (HP) and experience points (XP).\n"
                + "- Different geometric shapes represent enemies, each with unique patterns.\n"
                + "- Collision impacts may cause knockback depending on the enemy type.\n");


        tutorialText.setFont(new Font("akashi", Font.BOLD, 18));
        tutorialText.setForeground(SHOW_COLOR);
        tutorialText.setOpaque(false);
        tutorialText.setEditable(false);

        tutorialText.setBounds(25, 100, 800, 700);
        this.add(tutorialText);
    }
    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }

    public static TutorialPanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new TutorialPanel();
        return INSTANCE;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
