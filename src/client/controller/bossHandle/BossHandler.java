package client.controller.bossHandle;

import client.controller.updater.Utils;
import client.controller.constant.Constants;
import client.controller.constant.GameValues;
import client.controller.constant.KeyActions;
import client.controller.random.RandomHelper;
import client.model.charactersModel.boss.*;
import client.model.movement.Direction;
import client.model.movement.ImpactMechanism;
import client.model.panelModel.PanelModel;
import client.model.panelModel.Rigid;
import client.model.panelModel.WallSideIndicator;

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
    private boolean powerPunchHappened;
    private int rapidFireStart;
    private boolean quakeHappened;
    private int quakeMakeTime;

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
        if (lastAttack != 0 && time - lastAttack < 15000) return;
        if (bossHead == null) return;

        bossHead.getAoeCenters().clear();
        powerPunchHappened = false;
        quakeHappened = false;
        makeHeadStop();
        makeRightHandStop();
        makeLeftHandStop();
        makePunchStop();

        if (!firstRoundFinish) {
            if (bossRightHand != null && bossLeftHand != null && bossHead.getHeadPanel().getCenter().getY() <= bossRightHand.getRightHandPanel().getCenter().getY()
                    && bossHead.getHeadPanel().getCenter().getY() <= bossLeftHand.getLeftHandPanel().getCenter().getY()) {
                bossAttackType = RandomHelper.makeFirstRoundBossAttack();
                if (bossAttackType.equals(BossAttackType.SQUEEZE)) makeSqueezeAttack();
                else makeProjectileAttack();
            }
            else if (bossLeftHand != null || bossRightHand != null) {
                bossAttackType = BossAttackType.PROJECTILE;
                makeProjectileAttack();
            }
            else {
                bossAttackType = null;
                bossHead.setCanInjure(true);
            }
        }
        else {
            if (bossRightHand != null && bossLeftHand != null && bossHead.getHeadPanel().getCenter().getY() <= bossRightHand.getRightHandPanel().getCenter().getY()
                    && bossHead.getHeadPanel().getCenter().getY() <= bossLeftHand.getLeftHandPanel().getCenter().getY()) {
                bossAttackType = RandomHelper.makeSecondRoundBossAttack(0, 6);
            }
            else if (bossLeftHand != null || bossRightHand != null) {
                bossAttackType = RandomHelper.makeSecondRoundBossAttack(1, 6);
            }
            else {
                bossAttackType = RandomHelper.makeSecondRoundBossAttack(2, 6);
            }

            if (bossAttackType == null) return;
            if (bossAttackType.equals(BossAttackType.SQUEEZE)) {
                makeSqueezeAttack();
            }
            else if (bossAttackType.equals(BossAttackType.PROJECTILE)) {
                makeProjectileAttack();
            }
            else if (bossAttackType.equals(BossAttackType.VOMIT)) {
                makeVomitAttack(time);
            }
            else if (bossAttackType.equals(BossAttackType.POWER_PUNCH)) {
                makePowerPunchAttack();
            }
            else if (bossAttackType.equals(BossAttackType.QUAKE)) {
                makeQuakeAttack();
            }
            else if (bossAttackType.equals(BossAttackType.RAPID_FIRE)) {
                makeRapidFireAttack(time);
            }
            else if (bossAttackType.equals(BossAttackType.SLAP)) {
                makeSlapAttack();
            }
        }
        lastAttack = time;
    }

    private void makeSqueezeAttack() {
        makeAttacks(true, false, false);
    }
    private void makeProjectileAttack() {
        makeAttacks(false, true, true);
    }
    private void makeVomitAttack(int time) {
        makeAttacks(true, false, false);
        vomitAttackMade = time;
    }
    private void makePowerPunchAttack() {
        makeAttacks(true, true, true);
    }
    private void makeQuakeAttack() {
        makeAttacks(false, false, false);
    }
    private void makeRapidFireAttack(int time) {
        makeAttacks(true, false, false);
        rapidFireStart = time;
    }
    private void makeSlapAttack() {
        makeAttacks(true, false, false);
    }


    public void updateAttack(int time, Point2D epsilonCenter, PanelModel epsilonPanel) {
        if (bossAttackType == null) return;
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
        if (bossAttackType.equals(BossAttackType.QUAKE) && quakeHappened) {
            if (time - quakeMakeTime < 8000) {
                KeyActions.UP = 40;
                KeyActions.DOWN = 38;
                KeyActions.LEFT = 39;
                KeyActions.RIGHT = 37;
                GameValues.isBossQuakeAttack = true;
            }
            else {
                KeyActions.UP = 38;
                KeyActions.DOWN = 40;
                KeyActions.LEFT = 37;
                KeyActions.RIGHT = 39;
                GameValues.isBossQuakeAttack = false;
            }
        }
        if (bossAttackType.equals(BossAttackType.RAPID_FIRE) && time - rapidFireStart > 2000) {
            if (bossHead != null) bossHead.shootBullets();
            rapidFireStart = time;
        }
        updateDirection(time, epsilonCenter, epsilonPanel);
    }

    private void updateDirection(int time, Point2D epsilonCenter, PanelModel epsilonPanel) {
        if (bossAttackType.equals(BossAttackType.SQUEEZE)) updateSqueezeDirection(epsilonPanel);
        if (bossAttackType.equals(BossAttackType.PROJECTILE)) updateProjectileDirection(epsilonCenter);
        if (bossAttackType.equals(BossAttackType.VOMIT)) updateVomitDirection();
        if (bossAttackType.equals(BossAttackType.POWER_PUNCH) && !powerPunchHappened) updatePowerPunchDirection(epsilonPanel);
        if (bossAttackType.equals(BossAttackType.QUAKE) && !quakeHappened) updateQuakeDirection(time, epsilonPanel);
        if (bossAttackType.equals(BossAttackType.RAPID_FIRE)) updateRapidFireDirection();
        if (bossAttackType.equals(BossAttackType.SLAP)) updateSlapDirection(epsilonCenter);
    }
    private void updateSqueezeDirection(PanelModel panel) {
        Point2D rightDest = new Point2D.Double(panel.getX() + panel.getWidth() + bossRightHand.getRightHandPanel().getWidth() / 2 + Constants.ERROR, panel.getY() + panel.getHeight() / 2);
        Point2D leftDest = new Point2D.Double(panel.getX() - bossLeftHand.getLeftHandPanel().getWidth() / 2 - Constants.ERROR, panel.getY() + panel.getHeight() / 2);

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
            if (bossRightHand != null) {
                Point2D rightDelta = new Point2D.Double(epsilon.getX() - bossRightHand.getRightHandPanel().getCenter().getX(), epsilon.getY() - bossRightHand.getRightHandPanel().getCenter().getY());
                rightDelta = new Point2D.Double(rightDelta.getX() * Math.cos(Math.PI / 2) + rightDelta.getY() * Math.sin(Math.PI / 2), rightDelta.getX() * -Math.sin(Math.PI / 2) + rightDelta.getY() * Math.cos(Math.PI / 2));
                Direction rightToPoint = new Direction(rightDelta);
                changeRightDirection(new Direction(rightToPoint.getDirectionVector()));
            }

            if (bossLeftHand != null) {
                Point2D leftDelta = new Point2D.Double(epsilon.getX() - bossLeftHand.getLeftHandPanel().getCenter().getX(), epsilon.getY() - bossLeftHand.getLeftHandPanel().getCenter().getY());
                leftDelta = new Point2D.Double(leftDelta.getX() * Math.cos(Math.PI / 2) + leftDelta.getY() * Math.sin(Math.PI / 2), leftDelta.getX() * -Math.sin(Math.PI / 2) + leftDelta.getY() * Math.cos(Math.PI / 2));
                Direction leftToPoint = new Direction(leftDelta);
                changeLeftDirection(new Direction(leftToPoint.getDirectionVector()));
            }

            if (bossHead != null) {
                Point2D headDelta = new Point2D.Double(epsilon.getX() - bossHead.getHeadPanel().getCenter().getX(), epsilon.getY() - bossHead.getHeadPanel().getCenter().getY());
                headDelta = new Point2D.Double(headDelta.getX() * Math.cos(Math.PI / 2) + headDelta.getY() * Math.sin(Math.PI / 2), headDelta.getX() * -Math.sin(Math.PI / 2) + headDelta.getY() * Math.cos(Math.PI / 2));
                Direction headToPoint = new Direction(headDelta);
                changeHeadDirection(new Direction(headToPoint.getDirectionVector()));
            }
        }
    }
    private void updateVomitDirection() {
        makeHeadStop();
        makePunchStop();
        makeLeftHandStop();
        makeRightHandStop();
    }
    private void updatePowerPunchDirection(PanelModel panel) {
        Point2D punchDest = new Point2D.Double(panel.getX() - bossPunch.getPunchPanel().getWidth() / 2, panel.getY() + panel.getHeight() / 2);

        if (Utils.pointsApproxEqual(punchDest, bossPunch.getPunchPanel().getCenter())) {
            powerPunchHappened = true;
            panel.bulletHit(WallSideIndicator.RIGHT);
            panel.bulletHit(WallSideIndicator.RIGHT);
            panel.bulletHit(WallSideIndicator.RIGHT);
            if (bossRightHand != null) bossRightHand.getRightHandPanel().setRigid(Rigid.YES);
            if (bossLeftHand != null) bossLeftHand.getLeftHandPanel().setRigid(Rigid.YES);
            makePunchStop();
        }
        else {
            Point2D delta = new Point2D.Double(punchDest.getX() - bossPunch.getPunchPanel().getCenter().getX(), punchDest.getY() - bossPunch.getPunchPanel().getCenter().getY());
            Direction toPoint = new Direction(delta);
            changePunchDirection(new Direction(toPoint.getDirectionVector()), 8);
        }

        makeHeadStop();
        makeLeftHandStop();
        makeRightHandStop();
    }
    private void updateQuakeDirection(int time, PanelModel panel) {
        Point2D punchDest = new Point2D.Double(panel.getX() + panel.getWidth() / 2, panel.getY() + panel.getHeight() + bossPunch.getPunchPanel().getHeight() / 2);

        if (Utils.pointsApproxEqual(punchDest, bossPunch.getPunchPanel().getCenter())) {
            makePunchStop();
            quakeHappened = true;
            quakeMakeTime = time;
            Point2D impactPoint = new Point2D.Double(bossPunch.getPunchPanel().getX() + bossPunch.getPunchPanel().getWidth() / 2, bossPunch.getPunchPanel().getY());
            ImpactMechanism.applyImpact(impactPoint, 10);
        }
        else {
            Point2D delta = new Point2D.Double(punchDest.getX() - bossPunch.getPunchPanel().getCenter().getX(), punchDest.getY() - bossPunch.getPunchPanel().getCenter().getY());
            Direction toPoint = new Direction(delta);
            changePunchDirection(new Direction(toPoint.getDirectionVector()), 8);
        }

        makeHeadStop();
        makeLeftHandStop();
        makeRightHandStop();
    }
    private void updateRapidFireDirection() {
        makeHeadStop();
        makePunchStop();
        makeLeftHandStop();
        makeRightHandStop();
    }
    private void updateSlapDirection(Point2D point) {
        Point2D delta = new Point2D.Double(point.getX() - bossPunch.getPunchPanel().getCenter().getX(), point.getY() - bossPunch.getPunchPanel().getCenter().getY());
        Direction toPoint = new Direction(delta);
        double dist = bossPunch.getPunchPanel().getCenter().distance(point);
        changePunchDirection(new Direction(toPoint.getDirectionVector()), (dist < 300 ? 0.25 : 5));
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
    private void changePunchDirection(Direction direction, double speed) {
        bossPunch.setDirection(direction);
        bossPunch.setSpeed(speed);
        bossPunch.getPunchPanel().setDirection(direction);
        bossPunch.getPunchPanel().setSpeed(speed);
    }
    private void makeAttacks(boolean head, boolean right, boolean left) {
        if (bossHead != null) {
            bossHead.setCanInjure(head);
            bossHead.getHeadPanel().setRigid(Rigid.NO);
        }
        if (bossRightHand != null) {
            bossRightHand.setCanInjure(right);
            bossRightHand.getRightHandPanel().setRigid(Rigid.NO);
        }
        if (bossLeftHand != null) {
            bossLeftHand.setCanInjure(left);
            bossLeftHand.getLeftHandPanel().setRigid(Rigid.NO);
        }
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
