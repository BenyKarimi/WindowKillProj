package client.view.container;

import client.controller.constant.Constants;
import client.controller.constant.GameValues;
import client.view.charecterViews.EpsilonView;

import javax.swing.*;
import java.awt.*;

public class InformationPanel extends JPanel {
    private static InformationPanel INSTANCE;

    public InformationPanel() {
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        this.setBounds(0, 0, GlassFrame.getINSTANCE().getWidth() / 8, GlassFrame.getINSTANCE().getHeight() / 16);

        GlassFrame.getINSTANCE().add(this);
    }

    public static InformationPanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new InformationPanel();
        return INSTANCE;
    }

    public static void setINSTANCE(InformationPanel INSTANCE) {
        InformationPanel.INSTANCE = INSTANCE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawInformation((Graphics2D) g);
    }
    private void drawInformation(Graphics2D g) {
        int width = this.getWidth() / 5;
        int height = this.getHeight() / 2;
        g.setColor(Constants.SHOW_COLOR);
        g.setFont(new Font("akashi", Font.BOLD, 12));
        g.drawString("XP: " + EpsilonView.epsilonViewsList.get(0).getCurrentXp(), 0, height);
        g.drawString("HP: " + EpsilonView.epsilonViewsList.get(0).getCurrentHp(), width, height);
        g.drawString(GlassFrame.getINSTANCE().getTimer().toString(), 2 * width, height);
        g.drawString("Wave: " + GameValues.waveNumber, 3 * width, height);
        g.drawString("PR: " + GameValues.progressRate, 4 * width, height);
    }
}
