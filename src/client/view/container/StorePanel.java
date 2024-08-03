package client.view.container;

import client.controller.constant.Constants;
import client.controller.handeler.StoreActionHandle;
import client.model.charactersModel.EpsilonModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static client.controller.constant.Constants.BACKGROUND_IMAGE;
import static client.controller.constant.Constants.SHOW_COLOR;

public class StorePanel extends JPanel {
    EpsilonModel epsilon;
    private static StorePanel now;
    public StorePanel() {
        now = this;
        epsilon = EpsilonModel.epsilonModelsList.get(0);

        addButtons();
        addLabels();

        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLayout(null);
        this.setLocationToCenter(GlassFrame.getINSTANCE());

        this.setVisible(true);

        GlassFrame.getINSTANCE().add(this);
        GlassFrame.getINSTANCE().repaint();
    }
    private void addButtons() {
        CustomButton backToGame = new CustomButton("Back");
        backToGame.setFont(new Font("akashi", Font.BOLD, 25));
        backToGame.setForeground(SHOW_COLOR);
        backToGame.setBounds(0, 40, 125, 25);
        backToGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StoreActionHandle.handelActions();
                GlassFrame.getINSTANCE().remove(now);
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                now = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backToGame.setFont(new Font(backToGame.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backToGame.setFont(new Font(backToGame.getFont().getFontName(), Font.BOLD, 25));
            }
        });

        CustomButton buyImpact = new CustomButton("Buy");
        buyImpact.setFont(new Font("akashi", Font.BOLD, 30));
        buyImpact.setForeground(SHOW_COLOR);
        buyImpact.setBounds(450, 100, 125, 50);
        buyImpact.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (epsilon.getXp() < 100) {
                    JOptionPane.showMessageDialog(now, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                epsilon.setXp(epsilon.getXp() - 100);
                StoreActionHandle.setDoImpact(true);
                StoreActionHandle.handelActions();
                GlassFrame.getINSTANCE().remove(now);
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                now = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buyImpact.setFont(new Font(buyImpact.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buyImpact.setFont(new Font(buyImpact.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        CustomButton buyBullet = new CustomButton("Buy");
        buyBullet.setFont(new Font("akashi", Font.BOLD, 30));
        buyBullet.setForeground(SHOW_COLOR);
        buyBullet.setBounds(550, 200, 125, 50);
        buyBullet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (epsilon.getXp() < 75) {
                    JOptionPane.showMessageDialog(now, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                epsilon.setXp(epsilon.getXp() - 75);
                StoreActionHandle.setThreeBullet(true);
                StoreActionHandle.handelActions();
                GlassFrame.getINSTANCE().remove(now);
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                now = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buyBullet.setFont(new Font(buyBullet.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buyBullet.setFont(new Font(buyBullet.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        CustomButton buyHP = new CustomButton("Buy");
        buyHP.setFont(new Font("akashi", Font.BOLD, 30));
        buyHP.setForeground(SHOW_COLOR);
        buyHP.setBounds(450, 300, 125, 50);
        buyHP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (epsilon.getXp() < 50) {
                    JOptionPane.showMessageDialog(now, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                epsilon.setXp(epsilon.getXp() - 50);
                StoreActionHandle.setAddHp(true);
                StoreActionHandle.handelActions();
                GlassFrame.getINSTANCE().remove(now);
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                now = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buyHP.setFont(new Font(buyHP.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buyHP.setFont(new Font(buyHP.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        CustomButton buyBanish = new CustomButton("Buy");
        buyBanish.setFont(new Font("akashi", Font.BOLD, 30));
        buyBanish.setForeground(SHOW_COLOR);
        buyBanish.setBounds(700, 400, 125, 50);
        buyBanish.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (epsilon.getXp() < 120) {
                    JOptionPane.showMessageDialog(now, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                epsilon.setXp(epsilon.getXp() - 120);
                StoreActionHandle.setBanishHovering(true);
                StoreActionHandle.handelActions();
                GlassFrame.getINSTANCE().remove(now);
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                now = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buyBanish.setFont(new Font(buyBanish.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buyBanish.setFont(new Font(buyBanish.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        CustomButton buyFreezing = new CustomButton("Buy");
        buyFreezing.setFont(new Font("akashi", Font.BOLD, 30));
        buyFreezing.setForeground(SHOW_COLOR);
        buyFreezing.setBounds(500, 500, 125, 50);
        buyFreezing.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (epsilon.getXp() < 150) {
                    JOptionPane.showMessageDialog(now, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                epsilon.setXp(epsilon.getXp() - 150);
                StoreActionHandle.setFreezing(true);
                StoreActionHandle.handelActions();
                GlassFrame.getINSTANCE().remove(now);
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                now = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buyFreezing.setFont(new Font(buyFreezing.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buyFreezing.setFont(new Font(buyFreezing.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        CustomButton buyHotBullet = new CustomButton("Buy");
        buyHotBullet.setFont(new Font("akashi", Font.BOLD, 30));
        buyHotBullet.setForeground(SHOW_COLOR);
        buyHotBullet.setBounds(500, 600, 125, 50);
        buyHotBullet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (epsilon.getXp() < 200) {
                    JOptionPane.showMessageDialog(now, "You dont have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (GlassFrame.getINSTANCE().getTimer().getSeconds() - StoreActionHandle.getHotBulletCoolDown() < 120 && StoreActionHandle.getHotBulletCoolDown() != 0) {
                    JOptionPane.showMessageDialog(now, "This Item Has 120 Second CoolDown!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                epsilon.setXp(epsilon.getXp() - 200);
                StoreActionHandle.setHotBullet(true);
                StoreActionHandle.handelActions();
                GlassFrame.getINSTANCE().remove(now);
                GlassFrame.getINSTANCE().revalidate();
                GlassFrame.getINSTANCE().repaint();
                now = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                buyHotBullet.setFont(new Font(buyHotBullet.getFont().getFontName(), Font.BOLD, 35));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buyHotBullet.setFont(new Font(buyHotBullet.getFont().getFontName(), Font.BOLD, 30));
            }
        });

        this.add(backToGame);
        this.add(buyImpact);
        this.add(buyBullet);
        this.add(buyHP);
        this.add(buyBanish);
        this.add(buyFreezing);
        this.add(buyHotBullet);
    }
    private void addLabels() {
        JLabel label = new JLabel("Impact: 100 XP");
        label.setFont(new Font("akashi", Font.BOLD, 35));
        label.setForeground(SHOW_COLOR);
        label.setBounds(125, 100, 300, 50);

        JLabel label1 = new JLabel("Three Bullet: 75 XP");
        label1.setFont(new Font("akashi", Font.BOLD, 35));
        label1.setForeground(SHOW_COLOR);
        label1.setBounds(125, 200, 375, 50);

        JLabel label2 = new JLabel("Add HP: 50 XP");
        label2.setFont(new Font("akashi", Font.BOLD, 35));
        label2.setForeground(SHOW_COLOR);
        label2.setBounds(125, 300, 300, 50);

        JLabel label3 = new JLabel("Banish NonHovering: 120 XP");
        label3.setFont(new Font("akashi", Font.BOLD, 35));
        label3.setForeground(SHOW_COLOR);
        label3.setBounds(125, 400, 550, 50);

        JLabel label4 = new JLabel("Freezing: 150 XP");
        label4.setFont(new Font("akashi", Font.BOLD, 35));
        label4.setForeground(SHOW_COLOR);
        label4.setBounds(125, 500, 375, 50);

        JLabel label5 = new JLabel("Hot Bullet: 200 XP");
        label5.setFont(new Font("akashi", Font.BOLD, 35));
        label5.setForeground(SHOW_COLOR);
        label5.setBounds(125, 600, 375, 50);


        this.add(label);
        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);
    }
    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }

    public static StorePanel getNow() {
        return now;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
        g.setColor(SHOW_COLOR);
        g.setFont(new Font("akashi", Font.BOLD, 25));
        g.drawString("XP: " + epsilon.getXp(), 700, 60);
    }
}
