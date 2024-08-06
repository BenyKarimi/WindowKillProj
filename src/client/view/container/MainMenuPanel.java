package client.view.container;

import client.clientHandler.ClientState;
import client.controller.updater.Controller;
import client.controller.constant.Constants;
import client.controller.handeler.SkillTreeHandled;
import client.controller.saveAndLoad.FileManager;
import client.windowKillApplication.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static client.controller.constant.Constants.BACKGROUND_IMAGE;
import static client.controller.constant.Constants.SHOW_COLOR;

public class MainMenuPanel extends JPanel {
    private static MainMenuPanel INSTANCE;
    public MainMenuPanel() {
        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLayout(null);
        this.setLocationToCenter(GlassFrame.getINSTANCE());

        addButtons();
        addLabels();

        this.setVisible(true);

        GlassFrame.getINSTANCE().add(this);
        GlassFrame.getINSTANCE().repaint();
    }
    private void addButtons() {
        CustomButton startGame = new CustomButton("Start new Game");
        startGame.setFont(new Font("akashi", Font.BOLD, 40));
        startGame.setForeground(SHOW_COLOR);
        startGame.setBounds(200, 175, 450, 40);
        startGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean load = false;
                if (FileManager.canLoad(false)) {
                    int response = JOptionPane.showConfirmDialog(GlassFrame.getINSTANCE(), "Do you want to start the game from the dropped point?", "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) load = true;
                }

                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_WINDOWS);
                    robot.keyPress(KeyEvent.VK_D);
                    robot.keyRelease(KeyEvent.VK_WINDOWS);
                    robot.keyRelease(KeyEvent.VK_D);
                } catch (AWTException ex) {
                    throw new RuntimeException(ex);
                }
                WindowKill.client.makeClientBusy();
                GlassFrame.getINSTANCE().remove(INSTANCE);
                Controller controller = new Controller(load);
                new SkillTreeHandled();
                GlassFrame.getINSTANCE().setExtendedState(JFrame.MAXIMIZED_BOTH);
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                startGame.setFont(new Font(startGame.getFont().getFontName(), Font.BOLD, 40));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                startGame.setFont(new Font(startGame.getFont().getFontName(), Font.BOLD, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startGame.setFont(new Font(startGame.getFont().getFontName(), Font.BOLD, 40));
            }
        });


        CustomButton leaderboard = new CustomButton("Leaderboard");
        leaderboard.setFont(new Font("akashi", Font.BOLD, 40));
        leaderboard.setForeground(SHOW_COLOR);
        leaderboard.setBounds(235, 265, 375, 40);
        leaderboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (WindowKill.client.getClientState().equals(ClientState.OFFLINE)) {
                    JOptionPane.showMessageDialog(INSTANCE, "You are in offline mode!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                GlassFrame.getINSTANCE().remove(INSTANCE);
                GlassFrame.getINSTANCE().add(SkillTreePanel.getINSTANCE());
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                leaderboard.setFont(new Font(leaderboard.getFont().getFontName(), Font.BOLD, 40));            }

            @Override
            public void mouseEntered(MouseEvent e) {
                leaderboard.setFont(new Font(leaderboard.getFont().getFontName(), Font.BOLD, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                leaderboard.setFont(new Font(leaderboard.getFont().getFontName(), Font.BOLD, 40));
            }
        });


        CustomButton skillTree = new CustomButton("Skill Tree");
        skillTree.setFont(new Font("akashi", Font.BOLD, 40));
        skillTree.setForeground(SHOW_COLOR);
        skillTree.setBounds(275, 355, 300, 40);
        skillTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GlassFrame.getINSTANCE().remove(INSTANCE);
                GlassFrame.getINSTANCE().add(SkillTreePanel.getINSTANCE());
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                skillTree.setFont(new Font(skillTree.getFont().getFontName(), Font.BOLD, 40));            }

            @Override
            public void mouseEntered(MouseEvent e) {
                skillTree.setFont(new Font(skillTree.getFont().getFontName(), Font.BOLD, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                skillTree.setFont(new Font(skillTree.getFont().getFontName(), Font.BOLD, 40));
            }
        });

        CustomButton tutorial = new CustomButton("Tutorial");
        tutorial.setFont(new Font("akashi", Font.BOLD, 40));
        tutorial.setForeground(SHOW_COLOR);
        tutorial.setBounds(275, 445, 300, 40);
        tutorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GlassFrame.getINSTANCE().remove(INSTANCE);
                GlassFrame.getINSTANCE().add(TutorialPanel.getINSTANCE());
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                tutorial.setFont(new Font(tutorial.getFont().getFontName(), Font.BOLD, 40));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                tutorial.setFont(new Font(tutorial.getFont().getFontName(), Font.BOLD, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tutorial.setFont(new Font(tutorial.getFont().getFontName(), Font.BOLD, 40));
            }
        });

        CustomButton setting = new CustomButton("Setting");
        setting.setFont(new Font("akashi", Font.BOLD, 40));
        setting.setForeground(SHOW_COLOR);
        setting.setBounds(275, 535, 300, 40);
        setting.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GlassFrame.getINSTANCE().remove(INSTANCE);
                GlassFrame.getINSTANCE().add(SettingPanel.getINSTANCE());
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                setting.setFont(new Font(setting.getFont().getFontName(), Font.BOLD, 40));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setting.setFont(new Font(setting.getFont().getFontName(), Font.BOLD, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setting.setFont(new Font(setting.getFont().getFontName(), Font.BOLD, 40));
            }
        });

        CustomButton squad = new CustomButton("Squad");
        squad.setFont(new Font("akashi", Font.BOLD, 40));
        squad.setForeground(SHOW_COLOR);
        squad.setBounds(275, 625, 300, 40);
        squad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (WindowKill.client.getClientState().equals(ClientState.OFFLINE)) {
                    JOptionPane.showMessageDialog(INSTANCE, "You are in offline mode!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                GlassFrame.getINSTANCE().remove(INSTANCE);
                GlassFrame.getINSTANCE().add(SquadPanel.getINSTANCE());
                SquadPanel.getINSTANCE().startTimer();
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                squad.setFont(new Font(squad.getFont().getFontName(), Font.BOLD, 40));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                squad.setFont(new Font(squad.getFont().getFontName(), Font.BOLD, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                squad.setFont(new Font(squad.getFont().getFontName(), Font.BOLD, 40));
            }
        });

        CustomButton exit = new CustomButton("Exit");
        exit.setFont(new Font("akashi", Font.BOLD, 40));
        exit.setForeground(SHOW_COLOR);
        exit.setBounds(325, 715, 200, 40);
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exit.setFont(new Font(exit.getFont().getFontName(), Font.BOLD, 45));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exit.setFont(new Font(exit.getFont().getFontName(), Font.BOLD, 40));
            }
        });

        this.add(startGame);
        this.add(leaderboard);
        this.add(skillTree);
        this.add(tutorial);
        this.add(setting);
        this.add(squad);
        this.add(exit);
    }
    private void addLabels() {
        JLabel gameName = new JLabel("Window");
        gameName.setFont(new Font("akashi", Font.BOLD, 55));
        gameName.setForeground(SHOW_COLOR);
        gameName.setBounds(300, 50, 500, 50);

        JLabel gameName1 = new JLabel("Kill");
        gameName1.setFont(new Font("akashi", Font.BOLD, 55));
        gameName1.setForeground(SHOW_COLOR);
        gameName1.setBounds(475, 110, 100, 50);

        JLabel gameName2 = new JLabel(WindowKill.client.getClientState().toString());
        gameName2.setFont(new Font("akashi", Font.BOLD, 25));
        gameName2.setForeground(SHOW_COLOR);
        gameName2.setBounds(15, 40, 125, 25);

        this.add(gameName);
        this.add(gameName1);
        this.add(gameName2);
    }

    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }

    public static MainMenuPanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new MainMenuPanel();
        return INSTANCE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
