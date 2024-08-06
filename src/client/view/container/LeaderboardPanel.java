package client.view.container;

import client.controller.constant.Constants;
import client.view.customs.CustomButton;
import client.view.customs.CustomLeaderboardComparator;
import client.windowKillApplication.WindowKill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static client.controller.constant.Constants.BACKGROUND_IMAGE;
import static client.controller.constant.Constants.SHOW_COLOR;

public class LeaderboardPanel extends JPanel {
    private static LeaderboardPanel INSTANCE;
    private ArrayList<String> information;
    private final Timer updateTimer;
    private boolean sortByXp;
    private CustomLeaderboardComparator comparator;

    public LeaderboardPanel() {
        this.information = new ArrayList<>();
        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLocationToCenter(GlassFrame.getINSTANCE());
        this.setLayout(null);

        sortByXp = true;
        comparator = new CustomLeaderboardComparator(sortByXp);

        addButtons();

        this.setVisible(true);

        updateTimer = new Timer(5000, e -> updateView()){{setCoalesce(true);}};
        updateView();

        GlassFrame.getINSTANCE().repaint();
    }
    private void updateView() {
        this.removeAll();
        WindowKill.client.handleLeaderboardRequest();

        addButtons();
        addLabels();

        this.revalidate();
        this.repaint();
    }
    private void addButtons() {
        CustomButton backToMenu = new CustomButton("Back");
        backToMenu.setFont(new Font("akashi", Font.BOLD, 25));
        backToMenu.setForeground(SHOW_COLOR);
        backToMenu.setBounds(0, 40, 125, 25);
        backToMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateTimer.stop();
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


        CustomButton sortByXpButton = new CustomButton("Sort By Xp");
        CustomButton sortByTimeButton = new CustomButton("Sort By Time");

        sortByXpButton.setFont(new Font("akashi", Font.BOLD, 25));
        if (sortByXp) sortByXpButton.setForeground(new Color(255, 133, 0));
        else sortByXpButton.setForeground(SHOW_COLOR);
        sortByXpButton.setBounds(200, 40, 250, 25);
        sortByXpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sortByXp = true;
                sortByXpButton.setForeground(new Color(255, 133, 0));
                sortByTimeButton.setForeground(SHOW_COLOR);
                sortByXpButton.setFont(new Font(sortByXpButton.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                sortByXpButton.setFont(new Font(sortByXpButton.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sortByXpButton.setFont(new Font(sortByXpButton.getFont().getFontName(), Font.BOLD, 25));
            }
        });

        sortByTimeButton.setFont(new Font("akashi", Font.BOLD, 25));
        if (!sortByXp) sortByTimeButton.setForeground(new Color(255, 133, 0));
        else sortByTimeButton.setForeground(SHOW_COLOR);
        sortByTimeButton.setBounds(425, 40, 250, 25);
        sortByTimeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sortByXp = false;
                sortByTimeButton.setForeground(new Color(255, 133, 0));
                sortByXpButton.setForeground(SHOW_COLOR);
                sortByTimeButton.setFont(new Font(sortByTimeButton.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                sortByTimeButton.setFont(new Font(sortByTimeButton.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sortByTimeButton.setFont(new Font(sortByTimeButton.getFont().getFontName(), Font.BOLD, 25));
            }
        });


        this.add(backToMenu);
        this.add(sortByXpButton);
        this.add(sortByTimeButton);
    }
    private void addLabels() {
        int w = this.getWidth() / 3;
        int h = this.getHeight() / 15;

        ArrayList<String> tmp = new ArrayList<>(information);

        comparator.setSortByXp(sortByXp);
        tmp.sort(comparator);

        for (int i = tmp.size() - 1; i >= 0; i--) {
            int num = tmp.size() - 1 - i;
            String[] parts = tmp.get(i).split("█");

            JLabel name = new JLabel("Name: " + parts[0]);
            name.setFont(new Font("akashi", Font.PLAIN, 25));
            name.setForeground(SHOW_COLOR);
            name.setBounds(25, h * (num + 1) + 40, 250, 40);

            JLabel XP = new JLabel("XP: " + parts[1]);
            XP.setFont(new Font("akashi", Font.PLAIN, 25));
            XP.setForeground(SHOW_COLOR);
            XP.setBounds(w + 50, h * (num + 1) + 40, 200, 40);

            JLabel time = new JLabel("Total Time: " + parts[2]);
            time.setFont(new Font("akashi", Font.PLAIN, 25));
            time.setForeground(SHOW_COLOR);
            time.setBounds(2 * w, h * (num + 1) + 40, 300, 40);

            this.add(name);
            this.add(XP);
            this.add(time);
        }
    }

    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }
    public void startTimer() {
        updateView();
        updateTimer.start();
    }

    public static LeaderboardPanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new LeaderboardPanel();
        return INSTANCE;
    }

    public void setInformation(ArrayList<String> information) {
        this.information = information;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
