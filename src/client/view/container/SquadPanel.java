package client.view.container;

import client.controller.constant.Constants;
import client.windowKillApplication.WindowKill;
import server.models.SquadState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static client.controller.constant.Constants.BACKGROUND_IMAGE;
import static client.controller.constant.Constants.SHOW_COLOR;

public class SquadPanel extends JPanel {
    private static SquadPanel INSTANCE;
    private ArrayList<String> squads;
    private Timer updateTimer;

    public SquadPanel() {
        this.squads = new ArrayList<>();
        this.setSize(Constants.SIDE_PANELS_DIMENSION);
        this.setLocationToCenter(GlassFrame.getINSTANCE());
        this.setLayout(null);

        addButtons();

        this.setVisible(true);

        updateTimer = new Timer(5000, e -> updateView()){{setCoalesce(true);}};
        updateView();

        GlassFrame.getINSTANCE().repaint();
    }
    private void updateView() {
        this.removeAll();
        WindowKill.client.handleSquadRequest();

        addButtons();
        addLabels();

        this.revalidate();
        this.repaint();
    }
    private void addLabels() {
        if (WindowKill.client.getSquadState().equals(SquadState.NO_SQUAD)) {
            addNoSquadLabels();
        }
        else if (WindowKill.client.getSquadState().equals(SquadState.LEADER)) {
            addLeaderLabels();
        }
        else addMemberLabels();
    }
    private void addNoSquadLabels() {
        int w = this.getWidth() / 3;
        int h = this.getHeight() / 10;

        for (int i = 0; i < squads.size(); i++) {
            String[] parts = squads.get(i).split("█");

            JLabel name = new JLabel("Name: " + parts[0]);
            name.setFont(new Font("akashi", Font.PLAIN, 25));
            name.setForeground(SHOW_COLOR);
            name.setBounds(25, h * (i + 1) + 40, 200, 40);

            JLabel cnt = new JLabel("Member: " + parts[1]);
            cnt.setFont(new Font("akashi", Font.PLAIN, 25));
            cnt.setForeground(SHOW_COLOR);
            cnt.setBounds(w + 25, h * (i + 1) + 40, 200, 40);

            this.add(name);
            this.add(cnt);
        }
    }
    private void addLeaderLabels() {

    }
    private void addMemberLabels() {

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
        this.add(backToMenu);

        if (WindowKill.client.getSquadState().equals(SquadState.NO_SQUAD)) addNoSquadButtons();
        else if (WindowKill.client.getSquadState().equals(SquadState.LEADER)) addLeaderButtons();
        else addMemberButtons();
    }
    private void addNoSquadButtons() {
        CustomButton newSquad = new CustomButton("New Squad");
        newSquad.setFont(new Font("akashi", Font.BOLD, 25));
        newSquad.setForeground(new Color(255, 133, 0));
        newSquad.setBounds(600, 40, 250, 25);
        newSquad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = showNameInputDialog();
                WindowKill.client.handleMakeSquad(name);
                updateView();
                newSquad.setFont(new Font(newSquad.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                newSquad.setFont(new Font(newSquad.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                newSquad.setFont(new Font(newSquad.getFont().getFontName(), Font.BOLD, 25));
            }
        });
        this.add(newSquad);

        int h = this.getHeight() / 10;
        for (int i = 0; i < squads.size(); i++) {
            CustomButton join = new CustomButton("Join");
            join.setFont(new Font("akashi", Font.BOLD, 25));
            join.setForeground(SHOW_COLOR);
            join.setBounds(this.getWidth() / 3 * 2 + 25, h * (i + 1) + 45, 250, 25);
            join.addMouseListener(new CustomMouseAdaptor(i) {
                @Override
                public void mouseClicked(MouseEvent e) {
                    WindowKill.client.handleJoinToSquad(squads.get(getPointer()).split("█")[0]);
                    updateView();
                    join.setFont(new Font(join.getFont().getFontName(), Font.BOLD, 25));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    join.setFont(new Font(join.getFont().getFontName(), Font.BOLD, 30));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    join.setFont(new Font(join.getFont().getFontName(), Font.BOLD, 25));
                }
            });
            this.add(join);
        }
    }
    private void addMemberButtons() {

    }
    private void addLeaderButtons() {

    }


    private static String showNameInputDialog() {
        JDialog dialog = new JDialog(GlassFrame.getINSTANCE(), "Enter Squad Name", true);
        dialog.setSize(250, 100);
        dialog.setLocation(GlassFrame.getINSTANCE().getWidth()/2-dialog.getWidth()/2,GlassFrame.getINSTANCE().getHeight()/2-dialog.getHeight()/2);
        dialog.setLayout(new FlowLayout());

        JTextField nameField = new JTextField(20);
        dialog.add(nameField);

        JButton okButton = new JButton("OK");
        dialog.add(okButton);

        okButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);

        return nameField.getText();
    }
    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }
    public void startTimer() {
        updateView();
        updateTimer.start();
    }

    public static SquadPanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new SquadPanel();
        return INSTANCE;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND_IMAGE, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void setSquads(ArrayList<String> squads) {
        this.squads = squads;
    }
}
