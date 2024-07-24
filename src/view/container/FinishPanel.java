package view.container;

import controller.Controller;
import controller.constant.Constants;
import model.charactersModel.EpsilonModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static controller.constant.Constants.BACKGROUND_IMAGE;
import static controller.constant.Constants.SHOW_COLOR;

public class FinishPanel extends JPanel {
    private FinishPanel now;
    private final int finishXp;
    private final int bulletFired;
    private final int successfulBullet;
    private final int enemyKilled;
    private final String totalTime;

    public FinishPanel(int finishXp, int bulletFired, int successfulBullet, int enemyKilled, String totalTime) {
        this.finishXp = finishXp;
        this.bulletFired = bulletFired;
        this.successfulBullet = successfulBullet;
        this.enemyKilled = enemyKilled;
        this.totalTime = totalTime;

        now = this;
        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLocationToCenter(GlassFrame.getINSTANCE());
        this.setLayout(null);

        addLabels();
        addButtons();

        this.setVisible(true);

        GlassFrame.getINSTANCE().add(this);
        GlassFrame.getINSTANCE().revalidate();
        GlassFrame.getINSTANCE().repaint();
    }
    private void addLabels() {
        JLabel label = new JLabel("Game Finished");
        label.setFont(new Font("akashi", Font.BOLD, 45));
        label.setForeground(SHOW_COLOR);
        label.setBounds(225, 100, 500, 50);

        JLabel showXp = new JLabel("Your XP: " + finishXp);
        showXp.setFont(new Font("akashi", Font.BOLD, 45));
        showXp.setForeground(SHOW_COLOR);
        showXp.setBounds(250, 200, 500, 50);

        JLabel showBulletFired = new JLabel("Total Bullet Fired: " + bulletFired);
        showBulletFired.setFont(new Font("akashi", Font.BOLD, 45));
        showBulletFired.setForeground(SHOW_COLOR);
        showBulletFired.setBounds(175, 300, 700, 50);

        JLabel showSuccessfulFired = new JLabel("Total Successful Fired: " + successfulBullet);
        showSuccessfulFired.setFont(new Font("akashi", Font.BOLD, 45));
        showSuccessfulFired.setForeground(SHOW_COLOR);
        showSuccessfulFired.setBounds(125, 400, 700, 50);

        JLabel showEnemyKilled = new JLabel("Total Enemy Killed: " + enemyKilled);
        showEnemyKilled.setFont(new Font("akashi", Font.BOLD, 45));
        showEnemyKilled.setForeground(SHOW_COLOR);
        showEnemyKilled.setBounds(150, 500, 700, 50);

        JLabel showTime = new JLabel("Total Time: " + totalTime);
        showTime.setFont(new Font("akashi", Font.BOLD, 45));
        showTime.setForeground(SHOW_COLOR);
        showTime.setBounds(225, 600, 600, 50);

        this.add(showXp);
        this.add(label);
        this.add(showBulletFired);
        this.add(showSuccessfulFired);
        this.add(showEnemyKilled);
        this.add(showTime);
    }
    private void addButtons() {
        CustomButton backToMenu = new CustomButton("Go to Main");
        backToMenu.setFont(new Font("akashi", Font.BOLD, 50));
        backToMenu.setForeground(SHOW_COLOR);
        backToMenu.setBounds(175, 700, 500, 100);
        backToMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GlassFrame.getINSTANCE().remove(now);
                GlassFrame.getINSTANCE().add(MainMenuPanel.getINSTANCE());
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                backToMenu.setFont(new Font(backToMenu.getFont().getFontName(), Font.BOLD, 50));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backToMenu.setFont(new Font(backToMenu.getFont().getFontName(), Font.BOLD, 55));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backToMenu.setFont(new Font(backToMenu.getFont().getFontName(), Font.BOLD, 50));
            }
        });

        this.add(backToMenu);
    }
    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
