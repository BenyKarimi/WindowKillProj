package client.view.container;

import client.Client;
import client.ClientState;
import client.controller.constant.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import client.windowKillApplication.*;


import static client.controller.constant.Constants.BACKGROUND_IMAGE;

public class StartingPanel extends JPanel {
    private static StartingPanel INSTANCE;
    private JTextField textField;
    public StartingPanel() {
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

        this.setVisible(true);

        addLabel();
        addButton();
        addTextField();


        GlassFrame.getINSTANCE().add(this);
        GlassFrame.getINSTANCE().repaint();
    }

    private void addLabel() {
        JLabel label1 = new JLabel("Enter name");
        label1.setFont(new Font("akashi", Font.BOLD, 55));
        label1.setForeground(Constants.SHOW_COLOR);
        label1.setBounds(275, 100, 550, 50);

        this.add(label1);
    }
    private void addTextField() {
        textField = new JTextField();
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createLineBorder(Constants.SHOW_COLOR, 3));
        textField.setForeground(new Color(255, 133, 0));
        textField.setCaretColor(Constants.SHOW_COLOR);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("akashi", Font.BOLD, 40));
        textField.setBounds(240, 200, 400, 75);

        this.add(textField);
    }
    private void addButton() {
        CustomButton online = new CustomButton("Online");
        CustomButton offline = new CustomButton("Offline");


        online.setFont(new Font("akashi", Font.BOLD, 55));
        online.setForeground(Constants.SHOW_COLOR);
        online.setBounds(285, 350, 300, 50);
        online.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                online.setForeground(new Color(255, 133, 0));
                offline.setForeground(Constants.SHOW_COLOR);
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                online.setFont(new Font(online.getFont().getFontName(), Font.BOLD, 60));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                online.setFont(new Font(online.getFont().getFontName(), Font.BOLD, 55));
            }
        });


        offline.setFont(new Font("akashi", Font.BOLD, 55));
        offline.setForeground(Constants.SHOW_COLOR);
        offline.setBounds(285, 475, 300, 50);
        offline.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                offline.setForeground(new Color(255, 133, 0));
                online.setForeground(Constants.SHOW_COLOR);
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                offline.setFont(new Font(offline.getFont().getFontName(), Font.BOLD, 60));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                offline.setFont(new Font(offline.getFont().getFontName(), Font.BOLD, 55));
            }
        });


        CustomButton submit = new CustomButton("Submit");
        submit.setFont(new Font("akashi", Font.BOLD, 45));
        submit.setForeground(Constants.SHOW_COLOR);
        submit.setBounds(285, 600, 300, 50);
        submit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                submit.setForeground(new Color(255, 133, 0));
                if (textField.getText().equals("")) {
                    JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "Please fill the text-field!", "WARNING" ,JOptionPane.WARNING_MESSAGE);
                    submit.setForeground(Constants.SHOW_COLOR);
                }
                else if (online.getForeground().equals(Constants.SHOW_COLOR) && offline.getForeground().equals(Constants.SHOW_COLOR)) {
                    JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "Please choose play mode!", "WARNING" ,JOptionPane.WARNING_MESSAGE);
                    submit.setForeground(Constants.SHOW_COLOR);
                }
                else {
                    if (online.getForeground().equals(Constants.SHOW_COLOR)) {
                        WindowKill.client = new Client(textField.getText(), ClientState.OFFLINE);
                    }
                    else {
                        WindowKill.client = new Client(textField.getText(), ClientState.ONLINE);
                    }

                    submit.setForeground(Constants.SHOW_COLOR);

                    if (WindowKill.client.canMakeConnection()) {
                        GlassFrame.getINSTANCE().remove(INSTANCE);
                        GlassFrame.getINSTANCE().add(MainMenuPanel.getINSTANCE());
                        GlassFrame.getINSTANCE().revalidate();
                        GlassFrame.getINSTANCE().repaint();
                    }
                    else {
                        JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "Could not connect to server!", "WARNING" ,JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                submit.setFont(new Font(submit.getFont().getFontName(), Font.BOLD, 50));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                submit.setFont(new Font(submit.getFont().getFontName(), Font.BOLD, 45));
            }
        });

        this.add(submit);
        this.add(online);
        this.add(offline);
    }

    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }

    public static StartingPanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new StartingPanel();
        return INSTANCE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
