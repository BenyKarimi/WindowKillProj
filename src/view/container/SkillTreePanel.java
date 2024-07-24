package view.container;

import controller.constant.Constants;
import controller.handeler.SkillTreeAttackType;
import controller.handeler.SkillTreeDefenceType;
import controller.handeler.SkillTreeHandled;
import controller.handeler.SkillTreeTransformationType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import static controller.constant.Constants.BACKGROUND_IMAGE;
import static controller.constant.Constants.SHOW_COLOR;

public class SkillTreePanel extends JPanel {
    private static SkillTreePanel INSTANCE;
    private boolean[] attackTree;
    private boolean[] defenceTree;
    private boolean[] transformationTree;

    public SkillTreePanel() {
        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLocationToCenter(GlassFrame.getINSTANCE());
        this.setLayout(null);

        attackTree = new boolean[4];
        defenceTree = new boolean[4];
        transformationTree = new boolean[4];

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

        JLabel label3 = new JLabel("1000 XP");
        label3.setFont(new Font("akashi", Font.BOLD, 20));
        label3.setForeground(SHOW_COLOR);
        label3.setBounds(100, 430, 100, 50);

        JLabel label4 = new JLabel("1500 XP");
        label4.setFont(new Font("akashi", Font.BOLD, 20));
        label4.setForeground(SHOW_COLOR);
        label4.setBounds(100, 630, 100, 50);

        JLabel label1 = new JLabel("500 XP");
        label1.setFont(new Font("akashi", Font.BOLD, 20));
        label1.setForeground(SHOW_COLOR);
        label1.setBounds(385, 230, 100, 50);

        JLabel label5 = new JLabel("750 XP");
        label5.setFont(new Font("akashi", Font.BOLD, 20));
        label5.setForeground(SHOW_COLOR);
        label5.setBounds(385, 430, 100, 50);

        JLabel label6 = new JLabel("900 XP");
        label6.setFont(new Font("akashi", Font.BOLD, 20));
        label6.setForeground(SHOW_COLOR);
        label6.setBounds(385, 630, 100, 50);

        JLabel label2 = new JLabel("1000 XP");
        label2.setFont(new Font("akashi", Font.BOLD, 20));
        label2.setForeground(SHOW_COLOR);
        label2.setBounds(675, 230, 100, 50);

        JLabel label7 = new JLabel("750 XP");
        label7.setFont(new Font("akashi", Font.BOLD, 20));
        label7.setForeground(SHOW_COLOR);
        label7.setBounds(675, 430, 100, 50);

        JLabel label8 = new JLabel("1500 XP");
        label8.setFont(new Font("akashi", Font.BOLD, 20));
        label8.setForeground(SHOW_COLOR);
        label8.setBounds(675, 630, 100, 50);

        this.add(label);
        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);
        this.add(label6);
        this.add(label7);
        this.add(label8);
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

        addAttackButtons();
        addDefenceButtons();
        addTransButtons();

        this.add(backToMenu);
    }
    private void addAttackButtons() {
        CustomButton ares = new CustomButton("Ares");
        CustomButton astrape = new CustomButton("Astrape");
        CustomButton cerberus = new CustomButton("Cerberus");


        ares.setFont(new Font("akashi", Font.BOLD, 30));
        ares.setForeground(SHOW_COLOR);
        ares.setBounds(35, 200, 200, 50);
        ares.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ares.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canAttack = true;
                    SkillTreeHandled.skillTreeAttackType = SkillTreeAttackType.ARES;
                    ares.setForeground(new Color(255, 70, 70));
                    if (astrape.getForeground().equals(new Color(255, 70, 70))) {
                        astrape.setForeground(new Color(255, 133, 0));
                    }
                    if (cerberus.getForeground().equals(new Color(255, 70, 70))) {
                        cerberus.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 750) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 750;
                ares.setForeground(new Color(255, 133, 0));
                SkillTreeHandled.skillsOpened.add(0);
                attackTree[0] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ares.setFont(new Font(ares.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ares.setFont(new Font(ares.getFont().getFontName(), Font.BOLD, 30));
            }
        });


        astrape.setFont(new Font("akashi", Font.BOLD, 30));
        astrape.setForeground(SHOW_COLOR);
        astrape.setBounds(35, 400, 200, 50);
        astrape.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (astrape.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canAttack = true;
                    SkillTreeHandled.skillTreeAttackType = SkillTreeAttackType.ASTRAPE;
                    astrape.setForeground(new Color(255, 70, 70));
                    if (ares.getForeground().equals(new Color(255, 70, 70))) {
                        ares.setForeground(new Color(255, 133, 0));
                    }
                    if (cerberus.getForeground().equals(new Color(255, 70, 70))) {
                        cerberus.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 1000) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!attackTree[0]) {
                    JOptionPane.showMessageDialog(INSTANCE, "You have to buy previous first!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 1000;
                astrape.setForeground(new Color(255, 133, 0));
                SkillTreeHandled.skillsOpened.add(1);
                attackTree[1] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                astrape.setFont(new Font(astrape.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                astrape.setFont(new Font(astrape.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        cerberus.setFont(new Font("akashi", Font.BOLD, 30));
        cerberus.setForeground(SHOW_COLOR);
        cerberus.setBounds(-10, 600, 300, 50);
        cerberus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cerberus.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canAttack = true;
                    SkillTreeHandled.skillTreeAttackType = SkillTreeAttackType.CERBERUS;
                    cerberus.setForeground(new Color(255, 70, 70));
                    if (ares.getForeground().equals(new Color(255, 70, 70))) {
                        astrape.setForeground(new Color(255, 133, 0));
                    }
                    if (astrape.getForeground().equals(new Color(255, 70, 70))) {
                        astrape.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 1500) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!attackTree[1]) {
                    JOptionPane.showMessageDialog(INSTANCE, "You have to buy previous first!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 1500;
                cerberus.setForeground(new Color(255, 133, 0));
                SkillTreeHandled.skillsOpened.add(2);
                attackTree[2] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cerberus.setFont(new Font(cerberus.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cerberus.setFont(new Font(cerberus.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        this.add(ares);
        this.add(astrape);
        this.add(cerberus);
    }
    private void addDefenceButtons() {
        CustomButton aceso = new CustomButton("Aceso");
        CustomButton melampus = new CustomButton("Melampus");
        CustomButton chiron = new CustomButton("Chiron");


        aceso.setFont(new Font("akashi", Font.BOLD, 30));
        aceso.setForeground(SHOW_COLOR);
        aceso.setBounds(300, 200, 250, 50);
        aceso.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (aceso.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canDefence = true;
                    SkillTreeHandled.skillTreeDefenceType = SkillTreeDefenceType.ACESO;
                    aceso.setForeground(new Color(255, 70, 70));
                    if (melampus.getForeground().equals(new Color(255, 70, 70))) {
                        melampus.setForeground(new Color(255, 133, 0));
                    }
                    if (chiron.getForeground().equals(new Color(255, 70, 70))) {
                        chiron.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 500) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 500;
                aceso.setForeground(new Color(255, 133, 0));
                SkillTreeHandled.skillsOpened.add(3);
                defenceTree[0] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                aceso.setFont(new Font(aceso.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                aceso.setFont(new Font(aceso.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        melampus.setFont(new Font("akashi", Font.BOLD, 30));
        melampus.setForeground(SHOW_COLOR);
        melampus.setBounds(300, 400, 250, 50);
        melampus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (melampus.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canDefence = true;
                    SkillTreeHandled.skillTreeDefenceType = SkillTreeDefenceType.MELAMPUS;
                    melampus.setForeground(new Color(255, 70, 70));
                    if (aceso.getForeground().equals(new Color(255, 70, 70))) {
                        aceso.setForeground(new Color(255, 133, 0));
                    }
                    if (chiron.getForeground().equals(new Color(255, 70, 70))) {
                        chiron.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 750) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!defenceTree[0]) {
                    JOptionPane.showMessageDialog(INSTANCE, "You have to buy previous first!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 750;
                melampus.setForeground(new Color(255, 133, 0));
                SkillTreeHandled.skillsOpened.add(4);
                defenceTree[1] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                melampus.setFont(new Font(melampus.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                melampus.setFont(new Font(melampus.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        chiron.setFont(new Font("akashi", Font.BOLD, 30));
        chiron.setForeground(SHOW_COLOR);
        chiron.setBounds(300, 600, 250, 50);
        chiron.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (chiron.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canDefence = true;
                    SkillTreeHandled.skillTreeDefenceType = SkillTreeDefenceType.CHIRON;
                    chiron.setForeground(new Color(255, 70, 70));
                    if (aceso.getForeground().equals(new Color(255, 70, 70))) {
                        aceso.setForeground(new Color(255, 133, 0));
                    }
                    if (melampus.getForeground().equals(new Color(255, 70, 70))) {
                        melampus.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 900) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!defenceTree[1]) {
                    JOptionPane.showMessageDialog(INSTANCE, "You have to buy previous first!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 900;
                chiron.setForeground(new Color(255, 133, 0));
                SkillTreeHandled.skillsOpened.add(5);
                defenceTree[2] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                chiron.setFont(new Font(chiron.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                chiron.setFont(new Font(chiron.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        this.add(aceso);
        this.add(melampus);
        this.add(chiron);
    }
    private void addTransButtons() {
        CustomButton proteus = new CustomButton("Proteus");
        CustomButton empusa = new CustomButton("Empusa");
        CustomButton dolus = new CustomButton("Dolus");


        proteus.setFont(new Font("akashi", Font.BOLD, 30));
        proteus.setForeground(SHOW_COLOR);
        proteus.setBounds(550, 200, 325, 50);
        proteus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (proteus.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canTransformation = true;
                    SkillTreeHandled.skillTreeTransformationType = SkillTreeTransformationType.PROTEUS;
                    proteus.setForeground(new Color(255, 70, 70));
                    if (empusa.getForeground().equals(new Color(255, 70, 70))) {
                        empusa.setForeground(new Color(255, 133, 0));
                    }
                    if (dolus.getForeground().equals(new Color(255, 70, 70))) {
                        dolus.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 1000) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Constants.INITIAL_XP -= 1000;
                proteus.setForeground(new Color(255, 133, 0));
                SkillTreeHandled.skillsOpened.add(6);
                transformationTree[0] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                proteus.setFont(new Font(proteus.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                proteus.setFont(new Font(proteus.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        empusa.setFont(new Font("akashi", Font.BOLD, 30));
        empusa.setForeground(SHOW_COLOR);
        empusa.setBounds(550, 400, 325, 50);
        empusa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (empusa.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canTransformation = true;
                    SkillTreeHandled.skillTreeTransformationType = SkillTreeTransformationType.EMPUSA;
                    empusa.setForeground(new Color(255, 70, 70));
                    if (proteus.getForeground().equals(new Color(255, 70, 70))) {
                        proteus.setForeground(new Color(255, 133, 0));
                    }
                    if (dolus.getForeground().equals(new Color(255, 70, 70))) {
                        dolus.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 750) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!transformationTree[0]) {
                    JOptionPane.showMessageDialog(INSTANCE, "You have to buy previous first!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Constants.INITIAL_XP -= 750;
                empusa.setForeground(new Color(255, 133, 0));
                SkillTreeHandled.skillsOpened.add(7);
                transformationTree[1] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                empusa.setFont(new Font(empusa.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                empusa.setFont(new Font(empusa.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        dolus.setFont(new Font("akashi", Font.BOLD, 30));
        dolus.setForeground(SHOW_COLOR);
        dolus.setBounds(550, 600, 325, 50);
        dolus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (dolus.getForeground().equals(new Color(255, 133, 0))) {
                    SkillTreeHandled.canTransformation = true;
                    SkillTreeHandled.skillTreeTransformationType = SkillTreeTransformationType.DOLUS;
                    dolus.setForeground(new Color(255, 70, 70));
                    if (proteus.getForeground().equals(new Color(255, 70, 70))) {
                        proteus.setForeground(new Color(255, 133, 0));
                    }
                    if (empusa.getForeground().equals(new Color(255, 70, 70))) {
                        empusa.setForeground(new Color(255, 133, 0));
                    }
                    return;
                }
                if (Constants.INITIAL_XP < 1500) {
                    JOptionPane.showMessageDialog(INSTANCE, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!transformationTree[1]) {
                    JOptionPane.showMessageDialog(INSTANCE, "You have to buy previous first!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Constants.INITIAL_XP -= 1500;
                dolus.setForeground(new Color(255, 133, 0));
                transformationTree[2] = true;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                dolus.setFont(new Font(dolus.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dolus.setFont(new Font(dolus.getFont().getFontName(), Font.BOLD, 30));
            }
        });


        this.add(proteus);
        this.add(empusa);
        this.add(dolus);
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
        g2d.drawLine(135, 275, 135, 400);
        g2d.drawLine(135, 475, 135, 600);

        g2d.drawLine(425, 130, 425, 200);
        g2d.drawLine(425, 275, 425, 400);
        g2d.drawLine(425, 475, 425, 600);

        g2d.drawLine(725, 130, 725, 200);
        g2d.drawLine(725, 275, 725, 400);
        g2d.drawLine(725, 475, 725, 600);
    }
}
