package view.container;

import controller.Pair;
import controller.constant.Constants;
import controller.handeler.MouseClickedActionHandled;
import controller.constant.GameValues;
import model.charactersModel.NecropickEnemy;
import model.charactersModel.StationedType;
import model.collision.Line;
import view.bulletView.BulletView;
import view.bulletView.EnemyNonRigidBulletView;
import view.charecterViews.*;
import view.collectibleView.CollectibleView;
import view.gameTimerView.GameTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static controller.Utils.aimAndBulletDrawerCalculator;

public class GamePanel extends JPanel {
    private static Point2D mousePoint = new Point2D.Double(0, 0);
    private final String id;
    private double upLeftX, upLeftY, width, height;
    public static ArrayList<GamePanel> gamePanelList = new ArrayList<>();
    public GamePanel(String id, double x, double y, double width, double height) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/akashi.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        gamePanelList.add(this);
        this.id = id;
        this.upLeftX = x;
        this.upLeftY = y;
        this.width = width;
        this.height = height;

        this.setLayout(null);
        this.setBackground(Color.BLACK);

        this.updateBound();

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePoint = new Point2D.Double(e.getX() + upLeftX, e.getY() + upLeftY);
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MouseClickedActionHandled.leftClicked(new Point2D.Double(e.getX() + upLeftX, e.getY() + upLeftY));
            }
        });

        GlassFrame.getINSTANCE().add(this);
    }
    public void updateBound(){
        setBounds((int) upLeftX, (int) upLeftY, (int) width, (int) height);
    }

    public void setUpLeftX(double upLeftX) {
        this.upLeftX = upLeftX;
    }

    public void setUpLeftY(double upLeftY) {
        this.upLeftY = upLeftY;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawEpsilon((Graphics2D) g);
        drawAim((Graphics2D) g);
        drawCollectibleView((Graphics2D) g);
        drawTriangleEnemy((Graphics2D) g);
        drawSquareEnemy((Graphics2D) g);
        drawOmenoctEnemy((Graphics2D) g);
        drawNecropickEnemy((Graphics2D) g);
        drawArchmireEnemy((Graphics2D) g);
        drawWyrmEnemy((Graphics2D) g);
        drawBlackOrbMiniBoss((Graphics2D) g);
        drawBarricadosEnemy((Graphics2D) g);
        drawBulletView((Graphics2D) g);
        drawNonRigidBullet((Graphics2D) g);
    }
    private void drawEpsilon(Graphics2D g) {
        EpsilonView epsilonView = EpsilonView.epsilonViewsList.get(0);
        Image epsilonImage = new ImageIcon("resources/Epsilon.png").getImage();
        g.drawImage(epsilonImage
                , (int)(epsilonView.getCurrentCenter().getX() - epsilonView.getCurrentRadius() - upLeftX)
                , (int)(epsilonView.getCurrentCenter().getY() - epsilonView.getCurrentRadius() - upLeftY)
                , 2 * (int)epsilonView.getCurrentRadius(), 2 * (int)epsilonView.getCurrentRadius(), null);

        g.setColor(new Color(255, 133, 0));
        for (Point2D ptr : epsilonView.getCurrentVertices()) {
            g.fillOval((int) (ptr.getX() - upLeftX), (int) (ptr.getY() - upLeftY), 10, 10);
        }
    }
    private void drawAim(Graphics2D g) {
        EpsilonView epsilonView = EpsilonView.epsilonViewsList.get(0);
        Point2D center = aimAndBulletDrawerCalculator(mousePoint, epsilonView.getCurrentCenter(), epsilonView.getCurrentRadius());
        g.setColor(new Color(15, 58, 192));
        g.fillOval((int)(center.getX() - 5 - upLeftX), (int)(center.getY() - 4 - upLeftY), 10, 10);
    }
    private void drawTriangleEnemy(Graphics2D g) {
        Image TriangleEnemyImage = new ImageIcon("resources/TriangeEnemy.png").getImage();
        for (TriangleEnemyView ptr : TriangleEnemyView.triangleEnemyViewList) {
            g.drawImage(TriangleEnemyImage, (int)(ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2) - upLeftX)
            , (int)(ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2) - upLeftY)
            , (int)(ptr.getCurrentSize()), (int)(ptr.getCurrentSize()), null);
        }
    }
    private void drawSquareEnemy(Graphics2D g) {
        Image SquareEnemyImage = new ImageIcon("resources/SquareEnemy.png").getImage();
        for (SquareEnemyView ptr : SquareEnemyView.squareEnemyViewList) {
            g.drawImage(SquareEnemyImage, (int)(ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2) - upLeftX)
                    , (int)(ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2) - upLeftY)
                    , (int)(ptr.getCurrentSize()), (int)(ptr.getCurrentSize()), null);
        }
    }
    private void drawOmenoctEnemy(Graphics2D g) {
        Image omenoctEnemyImage = new ImageIcon("resources/OmenoctEnemy.png").getImage();
        for (OmenoctEnemyView ptr : OmenoctEnemyView.omenoctEnemyViewList) {
            g.drawImage(omenoctEnemyImage, (int)(ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2) - upLeftX)
                    , (int)(ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2) - upLeftY)
                    , (int)(ptr.getCurrentSize()), (int)(ptr.getCurrentSize()), null);
        }
    }
    private void drawNecropickEnemy(Graphics2D g) {
        Image NecropickEnemyImage = new ImageIcon("resources/NecropickEnemy.png").getImage();
        Image NecropickEnemyPreShowImage = new ImageIcon("resources/NecropickEnemyPreShow.png").getImage();
        for (NecropickEnemyView ptr : NecropickEnemyView.necropickEnemyViewsList) {
            if (ptr.getStationedType().equals(StationedType.SHOW)) {
                g.drawImage(NecropickEnemyImage, (int) (ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2) - upLeftX)
                        , (int) (ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2) - upLeftY)
                        , (int) (ptr.getCurrentSize()), (int) (ptr.getCurrentSize()), null);
            }
            else if (ptr.getStationedType().equals(StationedType.PRE_SHOW)) {
                g.drawImage(NecropickEnemyPreShowImage, (int) (ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2) - upLeftX)
                        , (int) (ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2) - upLeftY)
                        , (int) (ptr.getCurrentSize()), (int) (ptr.getCurrentSize()), null);
            }
        }
    }
    private void drawArchmireEnemy(Graphics2D g) {
        Image ArchmireEnemyImage = new ImageIcon("resources/ArchmireEnemy.png").getImage();
        for (ArchmireEnemyView ptr : ArchmireEnemyView.archmireEnemyViewsList) {
            g.drawImage(ArchmireEnemyImage, (int) (ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2) - upLeftX)
                    , (int) (ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2) - upLeftY)
                    , (int) (ptr.getCurrentSize()), (int) (ptr.getCurrentSize()), null);
            for (Pair<Point2D, Integer> tmp : ptr.getCurrentCentersPointMemory()) {
                g.setColor(new Color(251, 55, 60, (int) ((1 - (GlassFrame.getINSTANCE().getTimer().getMiliSecond() - tmp.getSecond()) / 6000.0) * 125)));
                g.fillOval((int)(tmp.getFirst().getX() - ptr.getCurrentSize() / 2 - upLeftX)
                        , (int)(tmp.getFirst().getY() - ptr.getCurrentSize() / 2 - upLeftY)
                        , (int)ptr.getCurrentSize(), (int)ptr.getCurrentSize());
            }
        }
    }
    private void drawWyrmEnemy(Graphics2D g) {
        Image wyrmEnemyImage = new ImageIcon("resources/WyrmEnemy.png").getImage();
        for (WyrmEnemyView ptr : WyrmEnemyView.wyrmEnemyViewsList) {
            g.drawImage(wyrmEnemyImage, (int) (ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2) - upLeftX)
                    , (int) (ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2) - upLeftY)
                    , (int) (ptr.getCurrentSize()), (int) (ptr.getCurrentSize()), null);
        }
    }
    private void drawBlackOrbMiniBoss(Graphics2D g) {
        Image orbEnemyImage = new ImageIcon("resources/OrbEnemy.png").getImage();
        for (BlackOrbMiniBossView ptr : BlackOrbMiniBossView.blackOrbMiniBossViewsList) {
            /// draw Orbs
            for (Point2D point : ptr.getCurrentOrbLocations()) {
                g.drawImage(orbEnemyImage, (int) ((point.getX() - ptr.getOrbRadius()) - upLeftX)
                        , (int) ((point.getY() - ptr.getOrbRadius()) - upLeftY)
                        , (int) (ptr.getOrbRadius() * 2), (int) (ptr.getOrbRadius() * 2), null);
            }
            /// draw Lasers
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Line line : ptr.getCurrentOrbLasers()) {
                Line2D line2D = new Line2D.Double(line.getStart().getX() - upLeftX, line.getStart().getY() - upLeftY, line.getEnd().getX() - upLeftX, line.getEnd().getY() - upLeftY);
                drawGlowingLine(g, line2D);
            }
        }
    }
    private void drawGlowingLine(Graphics2D g2d, Line2D line) {
        Color color = new Color(0x4F, 0x1C, 0x9B, 0xFF);
        GradientPaint gradient = new GradientPaint(
                (float) line.getX1(), (float) line.getY1(), new Color(0x4F, 0x1C, 0x9B, 128),
                (float) line.getX2(), (float) line.getY2(), new Color(0x4F, 0x1C, 0x9B, 0)
        );

        g2d.setPaint(gradient);

        g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Draw the glow
        g2d.draw(line);

        g2d.setPaint(color);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(line);
    }
    private void drawBarricadosEnemy(Graphics2D g) {
        Image barricadosEnemyImage = new ImageIcon("resources/BarricadosEnemy.png").getImage();
        for (BarricadosEnemyView ptr : BarricadosEnemyView.barricadosEnemyViewsList) {
            g.drawImage(barricadosEnemyImage, (int) (ptr.getCurrentCenter().getX() - (ptr.getCurrentSize() / 2) - upLeftX)
                    , (int) (ptr.getCurrentCenter().getY() - (ptr.getCurrentSize() / 2) - upLeftY)
                    , (int) (ptr.getCurrentSize()), (int) (ptr.getCurrentSize()), null);
        }
    }
    private void drawBulletView(Graphics2D g) {
        for (BulletView ptr : BulletView.bulletViewList) {
            g.setColor(new Color(15, 58, 192));
            g.fillOval((int)(ptr.getCurrentCenter().getX() - ptr.getCurrentRadius() - upLeftX)
                    , (int)(ptr.getCurrentCenter().getY() - ptr.getCurrentRadius() - upLeftY)
                    , 2 * (int)ptr.getCurrentRadius(), 2 * (int)ptr.getCurrentRadius());
        }
    }
    private void drawCollectibleView(Graphics2D g) {
        for (CollectibleView ptr : CollectibleView.collectibleViewList) {
            g.setColor(Color.CYAN);
            g.fillRect((int) (ptr.getCurrentCenter().getX() - ptr.getCurrentSize() / 2 - upLeftX)
                    , (int)(ptr.getCurrentCenter().getY() - ptr.getCurrentSize() / 2 - upLeftY)
                    , (int) ptr.getCurrentSize(), (int) ptr.getCurrentSize());
        }
    }
    private void drawNonRigidBullet(Graphics2D g) {
        for (EnemyNonRigidBulletView ptr : EnemyNonRigidBulletView.nonRigidBulletViewsList) {
            g.setColor(new Color(255, 182, 0, 255));
            g.fillOval((int)(ptr.getCurrentCenter().getX() - ptr.getCurrentRadius() - upLeftX)
                    , (int)(ptr.getCurrentCenter().getY() - ptr.getCurrentRadius() - upLeftY)
                    , 2 * (int)ptr.getCurrentRadius(), 2 * (int)ptr.getCurrentRadius());
        }
    }
}
