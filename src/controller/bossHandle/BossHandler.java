package controller.bossHandle;

import controller.Utils;
import controller.constant.Constants;
import controller.constant.GameValues;
import controller.random.RandomHelper;
import model.charactersModel.boss.*;
import model.movement.Direction;
import model.panelModel.PanelModel;
import model.panelModel.Rigid;

import java.awt.geom.Point2D;

public class BossHandler {
    private BossHead bossHead;
    private BossRightHand bossRightHand;
    private BossLeftHand bossLeftHand;
    private BossPunch bossPunch;
    private BossAttackType bossAttackType;
    private int lastAttack;
    private int lastProjectileAttack;
    private int vomitAttackMade;

    public BossHandler() {
        this.lastAttack = 0;
    }
    public void updateModels(int time) {
        if (bossHead != null) {
            bossHead.calculateVertices();
            bossHead.updateEpsilonInsideAoe(time);
        }
        if (bossLeftHand != null) bossLeftHand.calculateVertices();
        if (bossRightHand != null) bossRightHand.calculateVertices();
        if (bossPunch != null) bossPunch.calculateVertices();
    }

    public void makeAttack(int time, boolean firstRoundFinish) {
        if (lastAttack != 0 && time - lastAttack < 30000) return;

        if (bossHead != null) bossHead.getAoeCenters().clear();
//        if (!firstRoundFinish) {
//            if (bossRightHand != null && bossLeftHand != null && bossHead.getHeadPanel().getCenter().getY() <= bossRightHand.getRightHandPanel().getCenter().getY()
//                    && bossHead.getHeadPanel().getCenter().getY() <= bossLeftHand.getLeftHandPanel().getCenter().getY()) {
//                bossAttackType = RandomHelper.makeFirstRoundBossAttack();
//                if (bossAttackType.equals(BossAttackType.SQUEEZE)) makeSqueezeAttack();
//                else makeProjectileAttack();
//            }
//            else if (bossLeftHand != null || bossRightHand != null) {
//                bossAttackType = BossAttackType.PROJECTILE;
//                makeProjectileAttack();
//            }
//        }
//        else {
//            /// TODO: second round attacks
//        }

        bossAttackType = BossAttackType.VOMIT;
        makeVomitAttack(time);
        lastAttack = time;
    }

    private void makeSqueezeAttack() {
        bossHead.setCanInjure(true);
        bossRightHand.setCanInjure(false);
        bossLeftHand.setCanInjure(false);
        bossHead.getHeadPanel().setRigid(Rigid.NO);
        bossLeftHand.getLeftHandPanel().setRigid(Rigid.NO);
        bossRightHand.getRightHandPanel().setRigid(Rigid.NO);
    }
    private void makeProjectileAttack() {
        bossHead.setCanInjure(false);
        bossRightHand.setCanInjure(true);
        bossLeftHand.setCanInjure(true);
        bossHead.getHeadPanel().setRigid(Rigid.NO);
        bossLeftHand.getLeftHandPanel().setRigid(Rigid.NO);
        bossRightHand.getRightHandPanel().setRigid(Rigid.NO);
    }
    private void makeVomitAttack(int time) {
        bossHead.setCanInjure(true);
        bossRightHand.setCanInjure(false);
        bossLeftHand.setCanInjure(false);
        bossHead.getHeadPanel().setRigid(Rigid.NO);
        bossLeftHand.getLeftHandPanel().setRigid(Rigid.NO);
        bossRightHand.getRightHandPanel().setRigid(Rigid.NO);
        vomitAttackMade = time;
    }
    private void makePowerPunchAttack() {

    }


    public void updateAttack(int time, Point2D epsilonCenter, PanelModel epsilonPanel) {
        if (bossAttackType.equals(BossAttackType.PROJECTILE) && time - lastProjectileAttack >= 5000) {
            if (bossRightHand != null) bossRightHand.makeAttack(epsilonCenter);
            if (bossLeftHand != null) bossLeftHand.makeAttack(epsilonCenter);
            lastProjectileAttack = time;
        }
        if (bossAttackType.equals(BossAttackType.VOMIT)) {
            if (time - vomitAttackMade >= 2000 && bossHead.getAoeCenters().isEmpty()) {
                bossHead.setAoeCenters(RandomHelper.bossAoeRandomCenters(epsilonPanel.getX(), epsilonPanel.getY(), epsilonPanel.getWidth(), epsilonPanel.getHeight()));
            }
        }
        updateDirection(epsilonCenter, epsilonPanel);
    }

    private void updateDirection(Point2D epsilonCenter, PanelModel epsilonPanel) {
        if (bossAttackType.equals(BossAttackType.SQUEEZE)) updateSqueezeDirection(epsilonPanel);
        if (bossAttackType.equals(BossAttackType.PROJECTILE)) updateProjectileDirection(epsilonCenter);
        if (bossAttackType.equals(BossAttackType.VOMIT)) updateVomitDirection();
    }
    private void updateSqueezeDirection(PanelModel panel) {
        Point2D rightDest = new Point2D.Double(panel.getX() + panel.getWidth() + bossRightHand.getRightHandPanel().getWidth() / 2, panel.getY() + panel.getHeight() / 2);
        Point2D leftDest = new Point2D.Double(panel.getX() - bossLeftHand.getLeftHandPanel().getWidth() / 2, panel.getY() + panel.getHeight() / 2);

        if (Utils.pointsApproxEqual(rightDest, bossRightHand.getRightHandPanel().getCenter())) {
            makeRightHandStop();
            bossRightHand.getRightHandPanel().setRigid(Rigid.YES);
        }
        else {
            Point2D delta = new Point2D.Double(rightDest.getX() - bossRightHand.getRightHandPanel().getCenter().getX(), rightDest.getY() - bossRightHand.getRightHandPanel().getCenter().getY());
            Direction toPoint = new Direction(delta);
            changeRightDirection(new Direction(toPoint.getDirectionVector()));
        }

        if (Utils.pointsApproxEqual(leftDest, bossLeftHand.getLeftHandPanel().getCenter())) {
            makeLeftHandStop();
            bossLeftHand.getLeftHandPanel().setRigid(Rigid.YES);
        }
        else {
            Point2D delta = new Point2D.Double(leftDest.getX() - bossLeftHand.getLeftHandPanel().getCenter().getX(), leftDest.getY() - bossLeftHand.getLeftHandPanel().getCenter().getY());
            Direction toPoint = new Direction(delta);
            changeLeftDirection(new Direction(toPoint.getDirectionVector()));
        }

        makeHeadStop();
        makePunchStop();
    }
    private void updateProjectileDirection(Point2D epsilon) {
        makeHeadStop();
        makePunchStop();

        boolean rightCheck = true;
        if (bossRightHand != null) {
            Point2D rightDest = new Point2D.Double(bossHead.getHeadPanel().getX() + bossHead.getHeadPanel().getWidth() + 2 * Constants.ERROR + bossRightHand.getRightHandPanel().getWidth() / 2, bossHead.getHeadPanel().getCenter().getY());
            rightCheck = Utils.pointsApproxEqual(rightDest, bossRightHand.getRightHandPanel().getCenter());
            if (rightCheck) {
                makeRightHandStop();
            }
            else {
                Point2D delta = new Point2D.Double(rightDest.getX() - bossRightHand.getRightHandPanel().getCenter().getX(), rightDest.getY() - bossRightHand.getRightHandPanel().getCenter().getY());
                Direction toPoint = new Direction(delta);
                changeRightDirection(new Direction(toPoint.getDirectionVector()));
            }
        }

        boolean leftCheck = true;
        if (bossLeftHand != null) {
            Point2D leftDest = new Point2D.Double(bossHead.getHeadPanel().getX() - bossLeftHand.getLeftHandPanel().getWidth() / 2 - 2 * Constants.ERROR, bossHead.getHeadPanel().getCenter().getY());
            leftCheck = Utils.pointsApproxEqual(leftDest, bossLeftHand.getLeftHandPanel().getCenter());
            if (leftCheck) {
                makeLeftHandStop();
            }
            else {
                Point2D delta = new Point2D.Double(leftDest.getX() - bossLeftHand.getLeftHandPanel().getCenter().getX(), leftDest.getY() - bossLeftHand.getLeftHandPanel().getCenter().getY());
                Direction toPoint = new Direction(delta);
                changeLeftDirection(new Direction(toPoint.getDirectionVector()));
            }
        }

        if (rightCheck && leftCheck) {
            Point2D rightDelta = new Point2D.Double(epsilon.getX() - bossRightHand.getRightHandPanel().getCenter().getX(), epsilon.getY() - bossRightHand.getRightHandPanel().getCenter().getY());
            rightDelta = new Point2D.Double(rightDelta.getX() * Math.cos(Math.PI / 2) + rightDelta.getY() * Math.sin(Math.PI / 2), rightDelta.getX() * -Math.sin(Math.PI / 2) + rightDelta.getY() * Math.cos(Math.PI / 2));
            Direction rightToPoint = new Direction(rightDelta);
            changeRightDirection(new Direction(rightToPoint.getDirectionVector()));

            Point2D leftDelta = new Point2D.Double(epsilon.getX() - bossLeftHand.getLeftHandPanel().getCenter().getX(), epsilon.getY() - bossLeftHand.getLeftHandPanel().getCenter().getY());
            leftDelta = new Point2D.Double(leftDelta.getX() * Math.cos(Math.PI / 2) + leftDelta.getY() * Math.sin(Math.PI / 2), leftDelta.getX() * -Math.sin(Math.PI / 2) + leftDelta.getY() * Math.cos(Math.PI / 2));
            Direction leftToPoint = new Direction(leftDelta);
            changeLeftDirection(new Direction(leftToPoint.getDirectionVector()));

            Point2D headDelta = new Point2D.Double(epsilon.getX() - bossHead.getHeadPanel().getCenter().getX(), epsilon.getY() - bossHead.getHeadPanel().getCenter().getY());
            headDelta = new Point2D.Double(headDelta.getX() * Math.cos(Math.PI / 2) + headDelta.getY() * Math.sin(Math.PI / 2), headDelta.getX() * -Math.sin(Math.PI / 2) + headDelta.getY() * Math.cos(Math.PI / 2));
            Direction headToPoint = new Direction(headDelta);
            changeHeadDirection(new Direction(headToPoint.getDirectionVector()));
        }
    }
    private void updateVomitDirection() {
        makeHeadStop();
        makePunchStop();
        makeLeftHandStop();
        makeRightHandStop();
    }
    private void updatePowerPunchDirection() {

    }

    private void makeHeadStop() {
        if (bossHead == null) return;
        bossHead.setDirection(new Direction(new Point2D.Double(0, 0)));
        bossHead.setSpeed(0);
        bossHead.getHeadPanel().setDirection(new Direction(new Point2D.Double(0, 0)));
        bossHead.getHeadPanel().setSpeed(0);
    }
    private void makeRightHandStop() {
        if (bossRightHand == null) return;
        bossRightHand.setDirection(new Direction(new Point2D.Double(0, 0)));
        bossRightHand.setSpeed(0);
        bossRightHand.getRightHandPanel().setDirection(new Direction(new Point2D.Double(0, 0)));
        bossRightHand.getRightHandPanel().setSpeed(0);
    }
    private void makeLeftHandStop() {
        if (bossLeftHand == null) return;
        bossLeftHand.setDirection(new Direction(new Point2D.Double(0, 0)));
        bossLeftHand.setSpeed(0);
        bossLeftHand.getLeftHandPanel().setDirection(new Direction(new Point2D.Double(0, 0)));
        bossLeftHand.getLeftHandPanel().setSpeed(0);
    }
    private void makePunchStop() {
        if (bossPunch == null) return;
        bossPunch.setDirection(new Direction(new Point2D.Double(0, 0)));
        bossPunch.setSpeed(0);
        bossPunch.getPunchPanel().setDirection(new Direction(new Point2D.Double(0, 0)));
        bossPunch.getPunchPanel().setSpeed(0);
    }
    private void changeHeadDirection(Direction direction) {
        bossHead.setDirection(direction);
        bossHead.setSpeed(2);
        bossHead.getHeadPanel().setDirection(direction);
        bossHead.getHeadPanel().setSpeed(2);
    }
    private void changeRightDirection(Direction direction) {
        bossRightHand.setDirection(direction);
        bossRightHand.setSpeed(2);
        bossRightHand.getRightHandPanel().setDirection(direction);
        bossRightHand.getRightHandPanel().setSpeed(2);
    }
    private void changeLeftDirection(Direction direction) {
        bossLeftHand.setDirection(direction);
        bossLeftHand.setSpeed(2);
        bossLeftHand.getLeftHandPanel().setDirection(direction);
        bossLeftHand.getLeftHandPanel().setSpeed(2);
    }
    private void changePunchDirection(Direction direction) {
        bossPunch.setDirection(direction);
        bossPunch.setSpeed(5);
        bossPunch.getPunchPanel().setDirection(direction);
        bossPunch.getPunchPanel().setSpeed(5);
    }

    public BossHead getBossHead() {
        return bossHead;
    }

    public void setBossHead(BossHead bossHead) {
        this.bossHead = bossHead;
    }

    public BossRightHand getBossRightHand() {
        return bossRightHand;
    }

    public void setBossRightHand(BossRightHand bossRightHand) {
        this.bossRightHand = bossRightHand;
    }

    public BossLeftHand getBossLeftHand() {
        return bossLeftHand;
    }

    public void setBossLeftHand(BossLeftHand bossLeftHand) {
        this.bossLeftHand = bossLeftHand;
    }

    public BossAttackType getBossAttackType() {
        return bossAttackType;
    }

    public void setBossAttackType(BossAttackType bossAttackType) {
        this.bossAttackType = bossAttackType;
    }

    public BossPunch getBossPunch() {
        return bossPunch;
    }

    public void setBossPunch(BossPunch bossPunch) {
        this.bossPunch = bossPunch;
    }
}
