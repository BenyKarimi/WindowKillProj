package view.container;

import controller.MouseClickedActionHandled;
import view.bulletView.BulletView;
import view.charecterViews.EpsilonView;
import view.charecterViews.SquareEnemyView;
import view.charecterViews.TriangleEnemyView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;

import static controller.Utils.aimAndBulletDrawerCalculator;
import static controller.constant.Constants.GAME_PANEL_INITIAL_DIMENSION;

public final class GamePanel extends JPanel {
    private static GamePanel INSTANCE;
    private Point2D mousePoint = new Point2D.Double(0, 0);
    public GamePanel() {

        this.setSize(GAME_PANEL_INITIAL_DIMENSION);
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        this.setLocationToCenter(GlassFrame.getINSTANCE());

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePoint = new Point2D.Double(e.getX(), e.getY());
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MouseClickedActionHandled.leftClicked(new Point2D.Double(e.getX(), e.getY()));
            }
        });

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
        drawAim((Graphics2D) g);
        drawTriangleEnemy((Graphics2D) g);
        drawSquareEnemy((Graphics2D) g);
        drawBulletView((Graphics2D) g);
    }
    private void drawEpsilon(Graphics2D g) {
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
    private void drawAim(Graphics2D g) {
        EpsilonView epsilonView = EpsilonView.getINSTANCE();
        Point2D center = aimAndBulletDrawerCalculator(mousePoint, epsilonView.getCurrentCenter(), epsilonView.getCurrentRadius());
        g.setColor(new Color(79, 28, 155));
        g.fillOval((int)(center.getX() - 5), (int)(center.getY() - 4), 10, 10);
    }
    private void drawTriangleEnemy(Graphics2D g) {
        Image TriangleEnemyImage = new ImageIcon("resources/TriangeEnemy.jpeg").getImage();
        for (TriangleEnemyView ptr : TriangleEnemyView.triangleEnemyViewList) {
            g.drawImage(TriangleEnemyImage, (int)(ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2))
            , (int)(ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2))
            , (int)(ptr.getCurrentSize()), (int)(ptr.getCurrentSize()), null);
        }
    }
    private void drawSquareEnemy(Graphics2D g) {
        Image SquareEnemyImage = new ImageIcon("resources/SquareEnemy.jpeg").getImage();
        for (SquareEnemyView ptr : SquareEnemyView.squareEnemyViewList) {
            g.drawImage(SquareEnemyImage, (int)(ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2))
                    , (int)(ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2))
                    , (int)(ptr.getCurrentSize()), (int)(ptr.getCurrentSize()), null);
        }
    }
    private void drawBulletView(Graphics2D g) {
        for (BulletView ptr : BulletView.bulletViewList) {
            g.setColor(new Color(15, 58, 192));
            g.fillOval((int)(ptr.getCurrentCenter().getX() - ptr.getCurrentRadius())
                    , (int)(ptr.getCurrentCenter().getY() - ptr.getCurrentRadius())
                    , 2 * (int)ptr.getCurrentRadius(), 2 * (int)ptr.getCurrentRadius());
        }
    }
}
