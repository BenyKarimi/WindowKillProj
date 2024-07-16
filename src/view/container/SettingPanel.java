package view.container;

import controller.constant.Constants;
import controller.constant.GameValues;
import controller.constant.Level;
import windowKill.WindowKill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static controller.constant.Constants.BACKGROUND_IMAGE;
import static controller.constant.Constants.SHOW_COLOR;

public class SettingPanel extends JPanel {
    private static SettingPanel INSTANCE;

    public SettingPanel() {
        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLocationToCenter(GlassFrame.getINSTANCE());
        this.setLayout(null);

        addButtons();
        addLabels();
        addSliders();

        this.setVisible(true);

        GlassFrame.getINSTANCE().repaint();
    }
    private void addLabels() {
        JLabel label = new JLabel("Game Level: ");
        label.setFont(new Font("akashi", Font.BOLD, 35));
        label.setForeground(SHOW_COLOR);
        label.setBounds(125, 200, 300, 50);

        JLabel label1 = new JLabel("Sensitivity: ");
        label1.setFont(new Font("akashi", Font.BOLD, 35));
        label1.setForeground(SHOW_COLOR);
        label1.setBounds(125, 400, 375, 50);

        JLabel label2 = new JLabel("Volume: ");
        label2.setFont(new Font("akashi", Font.BOLD, 35));
        label2.setForeground(SHOW_COLOR);
        label2.setBounds(125, 600, 300, 50);

        this.add(label);
        this.add(label1);
        this.add(label2);
    }
    private void addSliders() {
        JSlider volumeSlider = new JSlider(0, 100, 100);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(1);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setBounds(350, 480, 400, 300);

        volumeSlider.setBackground(Color.WHITE);
        volumeSlider.setOpaque(false);
        volumeSlider.setForeground(SHOW_COLOR);
        volumeSlider.setFont(new Font("akashi", Font.BOLD, 15));

        volumeSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(volumeSlider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Rectangle t = trackRect;
                Paint p = new GradientPaint(0, 0, new Color(255, 133, 0), t.width, t.height, SHOW_COLOR, true);
                g2d.setPaint(p);
                g2d.fillRect(t.x, t.y, t.width, t.height);
            }
        });

        volumeSlider.setFocusable(false);

        volumeSlider.addChangeListener(e -> {
            int volume = ((JSlider) e.getSource()).getValue();
            WindowKill.song.setVolume(volume);
        });


        JSlider sensitiveSlider = new JSlider(10, 100, 55);
        sensitiveSlider.setMajorTickSpacing(10);
        sensitiveSlider.setMinorTickSpacing(1);
        sensitiveSlider.setPaintTicks(true);
        sensitiveSlider.setPaintLabels(true);
        sensitiveSlider.setBounds(400, 285, 400, 300);

        sensitiveSlider.setBackground(Color.WHITE);
        sensitiveSlider.setOpaque(false);
        sensitiveSlider.setForeground(SHOW_COLOR);
        sensitiveSlider.setFont(new Font("akashi", Font.BOLD, 15));

        sensitiveSlider.setFocusable(false);

        sensitiveSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(sensitiveSlider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Rectangle t = trackRect;
                Paint p = new GradientPaint(0, 0, new Color(255, 133, 0), t.width, t.height, SHOW_COLOR, true);
                g2d.setPaint(p);
                g2d.fillRect(t.x, t.y, t.width, t.height);
            }
        });

        sensitiveSlider.addChangeListener(e -> {
            int sens = ((JSlider) e.getSource()).getValue();
            Constants.EPSILON_SPEED = sens / 10.0;
        });

        this.add(volumeSlider);
        this.add(sensitiveSlider);
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

        CustomButton easy = new CustomButton("Easy");
        easy.setFont(new Font("akashi", Font.BOLD, 30));
        easy.setForeground(new Color(255, 133, 0));
        easy.setBounds(400, 150, 150, 50);

        CustomButton medium = new CustomButton("Medium");
        medium.setFont(new Font("akashi", Font.BOLD, 30));
        medium.setForeground(SHOW_COLOR);
        medium.setBounds(400, 200, 200, 50);

        CustomButton hard = new CustomButton("Hard");
        hard.setFont(new Font("akashi", Font.BOLD, 30));
        hard.setForeground(SHOW_COLOR);
        hard.setBounds(400, 250, 150, 50);

        easy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameValues.level = Level.EASY;
                easy.setForeground(new Color(255, 133, 0));
                medium.setForeground(SHOW_COLOR);
                hard.setForeground(SHOW_COLOR);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                easy.setFont(new Font(easy.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                easy.setFont(new Font(easy.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        medium.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameValues.level = Level.MEDIUM;
                medium.setForeground(new Color(255, 133, 0));
                easy.setForeground(SHOW_COLOR);
                hard.setForeground(SHOW_COLOR);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                medium.setFont(new Font(medium.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                medium.setFont(new Font(medium.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        hard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameValues.level = Level.HARD;
                hard.setForeground(new Color(255, 133, 0));
                medium.setForeground(SHOW_COLOR);
                easy.setForeground(SHOW_COLOR);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hard.setFont(new Font(hard.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hard.setFont(new Font(hard.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        this.add(backToMenu);
        this.add(easy);
        this.add(medium);
        this.add(hard);
    }
    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }
    public static SettingPanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new SettingPanel();
        return INSTANCE;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
