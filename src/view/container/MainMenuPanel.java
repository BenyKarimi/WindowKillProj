package view.container;

import controller.Controller;
import controller.constant.Constants;
import controller.handeler.StoreActionHandel;
import controller.handeler.TypedActionHandel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import static controller.constant.Constants.BACKGROUND_IMAGE;
import static controller.constant.Constants.SHOW_COLOR;

public class MainMenuPanel extends JPanel {
    private static MainMenuPanel INSTANCE;
    public MainMenuPanel() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/akashi.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

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
        startGame.setBounds(200, 250, 450, 40);
        startGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GlassFrame.getINSTANCE().remove(INSTANCE);
                GamePanel gamePanel = new GamePanel();
                Controller controller = new Controller();
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

        CustomButton skillTree = new CustomButton("Skill Tree");
        skillTree.setFont(new Font("akashi", Font.BOLD, 40));
        skillTree.setForeground(SHOW_COLOR);
        skillTree.setBounds(275, 350, 300, 40);
        skillTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /// should be done with logic
            }

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
        tutorial.setBounds(275, 450, 300, 40);
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
        setting.setBounds(275, 550, 300, 40);
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

        CustomButton exit = new CustomButton("Exit");
        exit.setFont(new Font("akashi", Font.BOLD, 40));
        exit.setForeground(SHOW_COLOR);
        exit.setBounds(325, 650, 200, 40);
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
        this.add(skillTree);
        this.add(tutorial);
        this.add(setting);
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

        this.add(gameName);
        this.add(gameName1);
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
