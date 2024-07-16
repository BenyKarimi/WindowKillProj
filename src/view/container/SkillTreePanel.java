package view.container;

import controller.constant.Constants;
import controller.handeler.SkillTreeHandled;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import static controller.constant.Constants.BACKGROUND_IMAGE;
import static controller.constant.Constants.SHOW_COLOR;

public class SkillTreePanel extends JPanel {
    private static SkillTreePanel INSTANCE;

    public SkillTreePanel() {
        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLocationToCenter(GlassFrame.getINSTANCE());
        this.setLayout(null);

        addButtons();
        addLabels();

        this.setVisible(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX() + " " + e.getY());
            }
        });

        GlassFrame.getINSTANCE().repaint();
    }
    private void addLabels() {
        JLabel label = new JLabel("750 XP");
        label.setFont(new Font("akashi", Font.BOLD, 20));
        label.setForeground(SHOW_COLOR);
        label.setBounds(100, 230, 100, 50);

        JLabel label1 = new JLabel("500 XP");
        label1.setFont(new Font("akashi", Font.BOLD, 20));
        label1.setForeground(SHOW_COLOR);
        label1.setBounds(385, 230, 100, 50);

        JLabel label2 = new JLabel("1000 XP");
        label2.setFont(new Font("akashi", Font.BOLD, 20));
        label2.setForeground(SHOW_COLOR);
        label2.setBounds(675, 230, 100, 50);

        this.add(label);
        this.add(label1);
        this.add(label2);
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

        CustomButton writ_of_ares = new CustomButton("Writ of Ares");
        writ_of_ares.setFont(new Font("akashi", Font.BOLD, 30));
        writ_of_ares.setForeground(SHOW_COLOR);
        writ_of_ares.setBounds(0, 200, 275, 50);
        writ_of_ares.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SkillTreeHandled.canAttack) return;
                if (Constants.INITIAL_XP < 750) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 750;
                SkillTreeHandled.canAttack = true;
                writ_of_ares.setForeground(new Color(255, 133, 0));
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                writ_of_ares.setFont(new Font(writ_of_ares.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                writ_of_ares.setFont(new Font(writ_of_ares.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        CustomButton writ_of_aceso = new CustomButton("Writ of Aceso");
        writ_of_aceso.setFont(new Font("akashi", Font.BOLD, 30));
        writ_of_aceso.setForeground(SHOW_COLOR);
        writ_of_aceso.setBounds(275, 200, 300, 50);
        writ_of_aceso.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SkillTreeHandled.canDefence) return;
                if (Constants.INITIAL_XP < 500) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 500;
                SkillTreeHandled.canDefence = true;
                writ_of_aceso.setForeground(new Color(255, 133, 0));
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                writ_of_aceso.setFont(new Font(writ_of_aceso.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                writ_of_aceso.setFont(new Font(writ_of_aceso.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        CustomButton writ_of_proteus = new CustomButton("Writ of Proteus");
        writ_of_proteus.setFont(new Font("akashi", Font.BOLD, 30));
        writ_of_proteus.setForeground(SHOW_COLOR);
        writ_of_proteus.setBounds(550, 200, 325, 50);
        writ_of_proteus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SkillTreeHandled.canProteus) return;
                if (Constants.INITIAL_XP < 1000) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 1000;
                SkillTreeHandled.canProteus = true;
                writ_of_proteus.setForeground(new Color(255, 133, 0));
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                writ_of_proteus.setFont(new Font(writ_of_proteus.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                writ_of_proteus.setFont(new Font(writ_of_proteus.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        this.add(backToMenu);
        this.add(writ_of_ares);
        this.add(writ_of_aceso);
        this.add(writ_of_proteus);
    }
    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }
    public static SkillTreePanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new SkillTreePanel();
        return INSTANCE;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
        g.setColor(SHOW_COLOR);
        g.setFont(new Font("akashi", Font.BOLD, 25));
        g.drawString("XP: " + Constants.INITIAL_XP, 700, 60);

        /// draw tree
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 133, 0));
        float[] dashPattern = {10, 5};
        BasicStroke dashed = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f);
        g2d.setStroke(dashed);
        g2d.drawLine(135, 130, 725, 130);
        g2d.drawLine(135, 130, 135, 200);
        g2d.drawLine(425, 130, 425, 200);
        g2d.drawLine(725, 130, 725, 200);
    }
}
