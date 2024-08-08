package client.view.container;

import client.clientHandler.BattleStatus;
import client.clientHandler.Client;
import client.clientHandler.ClientState;
import client.clientHandler.SquadState;
import client.controller.constant.Constants;
import client.view.customs.CustomButton;
import client.view.customs.CustomMouseAdaptor;
import client.windowKillApplication.WindowKill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import static client.controller.constant.Constants.*;

public class SquadPanel extends JPanel {
    private static SquadPanel INSTANCE;
    private ArrayList<String> squads;
    private ArrayList<String> battleHistory;
    private final Timer updateTimer;
    private int battleTab;
    private JTextField textField;

    public SquadPanel() {
        this.squads = new ArrayList<>();
        battleHistory = new ArrayList<>();
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
        addTextFiled();

        this.revalidate();
        this.repaint();
    }
    private void addTextFiled() {
        if (WindowKill.client.getBattleStatus().equals(BattleStatus.YES) && battleTab == 2) {
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
    }
    private void addLabels() {
        if (WindowKill.client.getBattleStatus().equals(BattleStatus.YES)) {
            addBattleLabels();
        }
        else if (WindowKill.client.getSquadState().equals(SquadState.NO_SQUAD)) {
            addNoSquadLabels();
        }
        else if (WindowKill.client.getSquadState().equals(SquadState.LEADER)) {
            addLeaderLabels();
        }
        else addMemberLabels();
    }
    private void addBattleLabels() {
        if (battleTab == 0) {
            int w = this.getWidth() / 4;
            int h = this.getHeight() / 12;

            for (int i = 0; i < WindowKill.client.getSquadMembers().size(); i++) {
                String[] parts = WindowKill.client.getSquadMembers().get(i).split("█");

                JLabel name = new JLabel("Name: " + parts[0]);
                name.setFont(new Font("akashi", Font.PLAIN, 25));
                name.setForeground(SHOW_COLOR);
                name.setBounds(25, h * (i + 1) + 40, 200, 40);

                JLabel XP = new JLabel("XP: " + parts[1]);
                XP.setFont(new Font("akashi", Font.PLAIN, 25));
                XP.setForeground(SHOW_COLOR);
                XP.setBounds(w + 50, h * (i + 1) + 40, 200, 40);

                JLabel state = new JLabel("State: " + parts[2]);
                state.setFont(new Font("akashi", Font.PLAIN, 25));
                state.setForeground(SHOW_COLOR);
                state.setBounds(2 * w, h * (i + 1) + 40, 200, 40);

                this.add(name);
                this.add(XP);
                this.add(state);
            }
        }
        else if (battleTab == 1) {
            int w = this.getWidth() / 4;
            int h = this.getHeight() / 12;

            for (int i = 0; i < WindowKill.client.getEnemySquadMember().size(); i++) {
                String[] parts = WindowKill.client.getEnemySquadMember().get(i).split("█");

                JLabel name = new JLabel("Name: " + parts[0]);
                name.setFont(new Font("akashi", Font.PLAIN, 25));
                name.setForeground(SHOW_COLOR);
                name.setBounds(25, h * (i + 1) + 40, 200, 40);

                JLabel XP = new JLabel("XP: " + parts[1]);
                XP.setFont(new Font("akashi", Font.PLAIN, 25));
                XP.setForeground(SHOW_COLOR);
                XP.setBounds(w + 50, h * (i + 1) + 40, 200, 40);

                JLabel state = new JLabel("State: " + parts[2]);
                state.setFont(new Font("akashi", Font.PLAIN, 25));
                state.setForeground(SHOW_COLOR);
                state.setBounds(2 * w, h * (i + 1) + 40, 200, 40);

                this.add(name);
                this.add(XP);
                this.add(state);
            }
        }
        else if (battleTab == 2) {
            int minus = 0;
            if (WindowKill.client.getSquadState().equals(SquadState.LEADER)) minus = -100;
            String[] parts = WindowKill.client.getVaultInfo().split("█");

            JLabel vault = new JLabel("Total XP: " + parts[0]);
            vault.setFont(new Font("akashi", Font.BOLD, 25));
            vault.setForeground(SHOW_COLOR);
            vault.setBounds(650, 100, 200, 40);

            JLabel palioxis = new JLabel("Palioxis");
            palioxis.setFont(new Font("akashi", Font.BOLD, 35));
            if (Boolean.parseBoolean(parts[1])) palioxis.setForeground(new Color(255, 133, 0));
            else palioxis.setForeground(SHOW_COLOR);
            palioxis.setBounds(350 + minus, 450, 200, 40);

            JLabel adonis = new JLabel("Adonis");
            adonis.setFont(new Font("akashi", Font.BOLD, 35));
            if (Boolean.parseBoolean(parts[2])) adonis.setForeground(new Color(255, 133, 0));
            else adonis.setForeground(SHOW_COLOR);
            adonis.setBounds(350 + minus, 550, 200, 40);

            JLabel gefjon = new JLabel("Gefjon");
            gefjon.setFont(new Font("akashi", Font.BOLD, 35));
            if (Boolean.parseBoolean(parts[3])) gefjon.setForeground(new Color(255, 133, 0));
            else gefjon.setForeground(SHOW_COLOR);
            gefjon.setBounds(350 + minus, 650, 200, 40);

            if (WindowKill.client.getSquadState().equals(SquadState.LEADER)) {
                JLabel palioxisPrice = new JLabel(100 * WindowKill.client.getSquadMembers().size() + " XP");
                palioxisPrice.setFont(new Font("akashi", Font.BOLD, 20));
                palioxisPrice.setForeground(SHOW_COLOR);
                palioxisPrice.setBounds(550, 500, 200, 40);

                JLabel adonisPrice = new JLabel( "400 XP");
                adonisPrice.setFont(new Font("akashi", Font.BOLD, 20));
                adonisPrice.setForeground(SHOW_COLOR);
                adonisPrice.setBounds(550, 600, 200, 40);

                JLabel gefjonPrice = new JLabel( "300 XP");
                gefjonPrice.setFont(new Font("akashi", Font.BOLD, 20));
                gefjonPrice.setForeground(SHOW_COLOR);
                gefjonPrice.setBounds(550, 700, 200, 40);

                this.add(palioxisPrice);
                this.add(adonisPrice);
                this.add(gefjonPrice);
            }

            this.add(vault);
            this.add(palioxis);
            this.add(adonis);
            this.add(gefjon);
        }
        else {
            int w = this.getWidth() / 2;
            int h = this.getHeight() / 12;

            for (int i = 1; i < battleHistory.size(); i++) {
                String[] parts = battleHistory.get(i).split("█");

                JLabel name = new JLabel("Enemy Name: " + parts[0]);
                name.setFont(new Font("akashi", Font.PLAIN, 25));
                name.setForeground(SHOW_COLOR);
                name.setBounds(25, h * i + 40, 400, 40);

                JLabel result = new JLabel("Result: " + parts[1]);
                result.setFont(new Font("akashi", Font.PLAIN, 25));
                result.setForeground(SHOW_COLOR);
                result.setBounds(w + 50, h * i + 40, 200, 40);

                this.add(name);
                this.add(result);
            }
        }
    }
    private void addNoSquadLabels() {
        int w = this.getWidth() / 3;
        int h = this.getHeight() / 12;

        for (int i = 0; i < squads.size(); i++) {
            String[] parts = squads.get(i).split("█");

            JLabel name = new JLabel("Name: " + parts[0]);
            name.setFont(new Font("akashi", Font.PLAIN, 25));
            name.setForeground(SHOW_COLOR);
            name.setBounds(25, h * (i + 1) + 40, 250, 40);

            JLabel cnt = new JLabel("Member: " + parts[1]);
            cnt.setFont(new Font("akashi", Font.PLAIN, 25));
            cnt.setForeground(SHOW_COLOR);
            cnt.setBounds(w + 50, h * (i + 1) + 40, 200, 40);

            this.add(name);
            this.add(cnt);
        }
    }
    private void addLeaderLabels() {
        JLabel squadName = new JLabel("Name: " + WindowKill.client.getSquadName());
        squadName.setFont(new Font("akashi", Font.BOLD, 25));
        squadName.setForeground(SHOW_COLOR);
        squadName.setBounds(325, 40, 300, 25);

        this.add(squadName);

        int w = this.getWidth() / 4;
        int h = this.getHeight() / 12;

        for (int i = 0; i < WindowKill.client.getSquadMembers().size(); i++) {
            String[] parts = WindowKill.client.getSquadMembers().get(i).split("█");

            JLabel name = new JLabel("Name: " + parts[0]);
            name.setFont(new Font("akashi", Font.PLAIN, 25));
            name.setForeground(SHOW_COLOR);
            name.setBounds(25, h * (i + 1) + 40, 200, 40);

            JLabel XP = new JLabel("XP: " + parts[1]);
            XP.setFont(new Font("akashi", Font.PLAIN, 25));
            XP.setForeground(SHOW_COLOR);
            XP.setBounds(w + 50, h * (i + 1) + 40, 200, 40);

            JLabel state = new JLabel("State: " + parts[2]);
            state.setFont(new Font("akashi", Font.PLAIN, 25));
            state.setForeground(SHOW_COLOR);
            state.setBounds(2 * w, h * (i + 1) + 40, 200, 40);

            this.add(name);
            this.add(XP);
            this.add(state);
        }
    }
    private void addMemberLabels() {
        JLabel squadName = new JLabel("Name: " + WindowKill.client.getSquadName());
        squadName.setFont(new Font("akashi", Font.BOLD, 25));
        squadName.setForeground(SHOW_COLOR);
        squadName.setBounds(325, 40, 300, 25);

        this.add(squadName);

        int w = this.getWidth() / 3;
        int h = this.getHeight() / 12;

        for (int i = 0; i < WindowKill.client.getSquadMembers().size(); i++) {
            String[] parts = WindowKill.client.getSquadMembers().get(i).split("█");

            JLabel name = new JLabel("Name: " + parts[0]);
            name.setFont(new Font("akashi", Font.PLAIN, 25));
            name.setForeground(SHOW_COLOR);
            name.setBounds(25, h * (i + 1) + 40, 200, 40);

            JLabel XP = new JLabel("XP: " + parts[1]);
            XP.setFont(new Font("akashi", Font.PLAIN, 25));
            XP.setForeground(SHOW_COLOR);
            XP.setBounds(w + 50, h * (i + 1) + 40, 200, 40);

            JLabel state = new JLabel("State: " + parts[2]);
            state.setFont(new Font("akashi", Font.PLAIN, 25));
            state.setForeground(SHOW_COLOR);
            state.setBounds(2 * w, h * (i + 1) + 40, 200, 40);

            this.add(name);
            this.add(XP);
            this.add(state);
        }
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

        if (WindowKill.client.getBattleStatus().equals(BattleStatus.YES)) addBattleButtons();
        else if (WindowKill.client.getSquadState().equals(SquadState.NO_SQUAD)) addNoSquadButtons();
        else if (WindowKill.client.getSquadState().equals(SquadState.LEADER)) addLeaderButtons();
        else addMemberButtons();
    }
    private void addBattleButtons() {
        CustomButton mySquad = new CustomButton("My Squad");
        CustomButton enemySquad = new CustomButton("Enemy Squad");
        CustomButton vault = new CustomButton("Vault");
        CustomButton history = new CustomButton("History");

        mySquad.setFont(new Font("akashi", Font.BOLD, 25));
        if (battleTab == 0) mySquad.setForeground(new Color(255, 133, 0));
        else mySquad.setForeground(SHOW_COLOR);
        mySquad.setBounds(125, 40, 250, 25);
        mySquad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                battleTab = 0;
                updateView();
                mySquad.setForeground(new Color(255, 133, 0));
                enemySquad.setForeground(SHOW_COLOR);
                vault.setForeground(SHOW_COLOR);
                history.setForeground(SHOW_COLOR);
                mySquad.setFont(new Font(mySquad.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mySquad.setFont(new Font(mySquad.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mySquad.setFont(new Font(mySquad.getFont().getFontName(), Font.BOLD, 25));
            }
        });

        enemySquad.setFont(new Font("akashi", Font.BOLD, 25));
        if (battleTab == 1) enemySquad.setForeground(new Color(255, 133, 0));
        else enemySquad.setForeground(SHOW_COLOR);
        enemySquad.setBounds(300, 40, 300, 25);
        enemySquad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                battleTab = 1;
                updateView();
                enemySquad.setForeground(new Color(255, 133, 0));
                mySquad.setForeground(SHOW_COLOR);
                vault.setForeground(SHOW_COLOR);
                history.setForeground(SHOW_COLOR);
                enemySquad.setFont(new Font(enemySquad.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                enemySquad.setFont(new Font(enemySquad.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                enemySquad.setFont(new Font(enemySquad.getFont().getFontName(), Font.BOLD, 25));
            }
        });

        vault.setFont(new Font("akashi", Font.BOLD, 25));
        if (battleTab == 2) vault.setForeground(new Color(255, 133, 0));
        else vault.setForeground(SHOW_COLOR);
        vault.setBounds(500, 40, 250, 25);
        vault.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                battleTab = 2;
                updateView();
                vault.setForeground(new Color(255, 133, 0));
                mySquad.setForeground(SHOW_COLOR);
                enemySquad.setForeground(SHOW_COLOR);
                history.setForeground(SHOW_COLOR);
                vault.setFont(new Font(vault.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                vault.setFont(new Font(vault.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                vault.setFont(new Font(vault.getFont().getFontName(), Font.BOLD, 25));
            }
        });

        history.setFont(new Font("akashi", Font.BOLD, 25));
        if (battleTab == 3) history.setForeground(new Color(255, 133, 0));
        else history.setForeground(SHOW_COLOR);
        history.setBounds(650, 40, 250, 25);
        history.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                battleTab = 3;
                updateView();
                history.setForeground(new Color(255, 133, 0));
                mySquad.setForeground(SHOW_COLOR);
                enemySquad.setForeground(SHOW_COLOR);
                vault.setForeground(SHOW_COLOR);
                history.setFont(new Font(history.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                history.setFont(new Font(history.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                history.setFont(new Font(history.getFont().getFontName(), Font.BOLD, 25));
            }
        });

        if (battleTab == 2) {
            CustomButton submit = new CustomButton("Submit");
            submit.setFont(new Font("akashi", Font.BOLD, 30));
            submit.setForeground(Constants.SHOW_COLOR);
            submit.setBounds(285, 300, 300, 50);
            submit.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String init = textField.getText();
                    if (init.equals("")) {
                        JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "Please fill the text-field!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        if (!init.matches("\\d+")) {
                            JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "Please fill the text-field with integer!", "WARNING", JOptionPane.WARNING_MESSAGE);
                        }

                        if (INITIAL_XP < Integer.parseInt(init)) {
                            JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "You do not have enough Xp!", "WARNING", JOptionPane.WARNING_MESSAGE);
                        }
                        else if (WindowKill.client.getXpDonation() + Integer.parseInt(init) > 200) {
                            JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "You can not donate more than 200 xp!", "WARNING", JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            WindowKill.client.handleSendXPDonation(Integer.parseInt(init));
                            updateView();
                        }
                    }
                    submit.setFont(new Font(submit.getFont().getFontName(), Font.BOLD, 30));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    submit.setFont(new Font(submit.getFont().getFontName(), Font.BOLD, 35));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    submit.setFont(new Font(submit.getFont().getFontName(), Font.BOLD, 30));
                }
            });
            this.add(submit);

            if (WindowKill.client.getSquadState().equals(SquadState.LEADER)) {
                int vaultXp = Integer.parseInt(WindowKill.client.getVaultInfo().split("█")[0]);

                CustomButton buyPalioxis = new CustomButton("buy");
                buyPalioxis.setFont(new Font("akashi", Font.BOLD, 30));
                buyPalioxis.setForeground(Constants.SHOW_COLOR);
                buyPalioxis.setBounds(437, 450, 300, 50);
                buyPalioxis.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (vaultXp < 100 * WindowKill.client.getSquadMembers().size()) {
                            JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "You do not have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            WindowKill.client.handleBuyFromVault("PALIOXIS", 100 * WindowKill.client.getSquadMembers().size());
                            updateView();
                        }
                        buyPalioxis.setFont(new Font(buyPalioxis.getFont().getFontName(), Font.BOLD, 30));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        buyPalioxis.setFont(new Font(buyPalioxis.getFont().getFontName(), Font.BOLD, 35));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        buyPalioxis.setFont(new Font(buyPalioxis.getFont().getFontName(), Font.BOLD, 30));
                    }
                });

                CustomButton buyAdonis = new CustomButton("buy");
                buyAdonis.setFont(new Font("akashi", Font.BOLD, 30));
                buyAdonis.setForeground(Constants.SHOW_COLOR);
                buyAdonis.setBounds(437, 550, 300, 50);
                buyAdonis.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (vaultXp < 400) {
                            JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "You do not have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            WindowKill.client.handleBuyFromVault("ADONIS", 400);
                            updateView();
                        }
                        buyAdonis.setFont(new Font(buyAdonis.getFont().getFontName(), Font.BOLD, 30));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        buyAdonis.setFont(new Font(buyAdonis.getFont().getFontName(), Font.BOLD, 35));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        buyAdonis.setFont(new Font(buyAdonis.getFont().getFontName(), Font.BOLD, 30));
                    }
                });

                CustomButton buyGefjon = new CustomButton("buy");
                buyGefjon.setFont(new Font("akashi", Font.BOLD, 30));
                buyGefjon.setForeground(Constants.SHOW_COLOR);
                buyGefjon.setBounds(437, 650, 300, 50);
                buyGefjon.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (vaultXp < 300) {
                            JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "You do not have enough XP!", "WARNING", JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            WindowKill.client.handleBuyFromVault("GEFJON", 300);
                            updateView();
                        }
                        buyGefjon.setFont(new Font(buyGefjon.getFont().getFontName(), Font.BOLD, 30));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        buyGefjon.setFont(new Font(buyGefjon.getFont().getFontName(), Font.BOLD, 35));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        buyGefjon.setFont(new Font(buyGefjon.getFont().getFontName(), Font.BOLD, 30));
                    }
                });


                this.add(buyPalioxis);
                this.add(buyAdonis);
                this.add(buyGefjon);
            }
        }

        this.add(mySquad);
        this.add(enemySquad);
        this.add(vault);
        this.add(history);
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
                if (INITIAL_XP < 100) {
                    JOptionPane.showMessageDialog(GlassFrame.getINSTANCE(), "You do not have enough Xp!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    WindowKill.client.handleMakeSquad(name, 100);
                }
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

        int h = this.getHeight() / 12;
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
    private void addLeaderButtons() {
        CustomButton deleteSquad = new CustomButton("Delete Squad");
        deleteSquad.setFont(new Font("akashi", Font.BOLD, 25));
        deleteSquad.setForeground(new Color(255, 133, 0));
        deleteSquad.setBounds(575, 40, 300, 25);
        deleteSquad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                WindowKill.client.handleDeleteSquad();
                updateView();
                deleteSquad.setFont(new Font(deleteSquad.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                deleteSquad.setFont(new Font(deleteSquad.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                deleteSquad.setFont(new Font(deleteSquad.getFont().getFontName(), Font.BOLD, 25));
            }
        });
        this.add(deleteSquad);

        int h = this.getHeight() / 12;
        for (int i = 1; i < WindowKill.client.getSquadMembers().size(); i++) {
            CustomButton remove = new CustomButton("Remove");
            remove.setFont(new Font("akashi", Font.BOLD, 25));
            remove.setForeground(SHOW_COLOR);
            remove.setBounds(this.getWidth() / 4 * 3, h * (i + 1) + 45, 250, 25);
            remove.addMouseListener(new CustomMouseAdaptor(i) {
                @Override
                public void mouseClicked(MouseEvent e) {
                    WindowKill.client.removeFromSquad(WindowKill.client.getSquadMembers().get(getPointer()).split("█")[0]);
                    updateView();
                    remove.setFont(new Font(remove.getFont().getFontName(), Font.BOLD, 25));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    remove.setFont(new Font(remove.getFont().getFontName(), Font.BOLD, 30));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    remove.setFont(new Font(remove.getFont().getFontName(), Font.BOLD, 25));
                }
            });
            this.add(remove);
        }
    }
    private void addMemberButtons() {
        CustomButton leave = new CustomButton("Leave Squad");
        leave.setFont(new Font("akashi", Font.BOLD, 25));
        leave.setForeground(new Color(255, 133, 0));
        leave.setBounds(575, 40, 300, 25);
        leave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                WindowKill.client.handleLeaveSquad();
                updateView();
                leave.setFont(new Font(leave.getFont().getFontName(), Font.BOLD, 25));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                leave.setFont(new Font(leave.getFont().getFontName(), Font.BOLD, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                leave.setFont(new Font(leave.getFont().getFontName(), Font.BOLD, 25));
            }
        });
        this.add(leave);
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

    public void setBattleHistory(ArrayList<String> battleHistory) {
        this.battleHistory = battleHistory;
    }
}
