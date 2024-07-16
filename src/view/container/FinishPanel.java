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
    private int finishXp;

    public FinishPanel(int finishXp) {
        this.finishXp = finishXp;
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
        label.setFont(new Font("akashi", Font.BOLD, 55));
        label.setForeground(SHOW_COLOR);
        label.setBounds(200, 250, 500, 50);

        JLabel showXp = new JLabel("Your XP: " + finishXp);
        showXp.setFont(new Font("akashi", Font.BOLD, 55));
        showXp.setForeground(SHOW_COLOR);
        showXp.setBounds(225, 400, 500, 50);

        this.add(showXp);
        this.add(label);
    }
    private void addButtons() {
        CustomButton backToMenu = new CustomButton("Go to Main");
        backToMenu.setFont(new Font("akashi", Font.BOLD, 50));
        backToMenu.setForeground(SHOW_COLOR);
        backToMenu.setBounds(175, 525, 500, 100);
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
