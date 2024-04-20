package view.container;

import view.charecterViews.EpsilonView;

import javax.swing.*;
import java.awt.*;

import static controller.constant.Constants.GAME_PANEL_INITIAL_DIMENSION;

public final class GamePanel extends JPanel {
    private static GamePanel INSTANCE;
    public GamePanel() {

        this.setSize(GAME_PANEL_INITIAL_DIMENSION);
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        this.setLocationToCenter(GlassFrame.getINSTANCE());

        GlassFrame.getINSTANCE().add(this);
    }
    public void setLocationToCenter(GlassFrame glassFrame){
        setLocation(glassFrame.getWidth()/2-getWidth()/2,glassFrame.getHeight()/2-getHeight()/2);
    }
    public static GamePanel getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new GamePanel();
        return INSTANCE;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawEpsilon((Graphics2D) g);
    }
    public void drawEpsilon(Graphics2D g) {
        EpsilonView epsilonView = EpsilonView.getINSTANCE();
//        Image epsilonImage = new ImageIcon("resources/Epsilon.jpeg").getImage();
//        g.drawImage(epsilonImage
//                , (int)(epsilonView.getCurrentCenter().getX() - epsilonView.getCurrentRadius())
//                , (int)(epsilonView.getCurrentCenter().getY() - epsilonView.getCurrentRadius())
//                , 2 * (int)epsilonView.getCurrentRadius(), 2 * (int)epsilonView.getCurrentRadius(), null);

        g.setColor(Color.GREEN);
        g.fillOval((int)(epsilonView.getCurrentCenter().getX() - epsilonView.getCurrentRadius())
                , (int)(epsilonView.getCurrentCenter().getY() - epsilonView.getCurrentRadius())
                , 2 * (int)epsilonView.getCurrentRadius(), 2 * (int)epsilonView.getCurrentRadius());
    }
}
