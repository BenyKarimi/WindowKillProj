package controller;

import controller.constant.Constants;
import controller.constant.GameValues;
import controller.handeler.SkillTreeHandled;
import controller.handeler.StoreActionHandle;
import controller.handeler.TypedActionHandle;
import controller.random.RandomHelper;
import model.bulletModel.RigidBulletModel;
import model.bulletModel.NonRigidBulletModel;
import model.charactersModel.*;
import model.collectibleModel.Collectible;
import model.collision.Collidable;
import model.collision.CollisionPoint;
import model.movement.Direction;
import model.movement.ImpactMechanism;
import model.movement.Movable;
import model.panelModel.PanelModel;
import model.panelModel.WallSideIndicator;
import view.bulletView.BulletView;
import view.bulletView.EnemyNonRigidBulletView;
import view.charecterViews.*;
import view.collectibleView.CollectibleView;
import view.container.GamePanel;
import view.container.GlassFrame;
import view.container.InformationPanel;
import view.gameTimerView.GameTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static controller.constant.Constants.*;

public class Updater {
    private boolean startGame = false;
    private boolean wonGame = false;
    private boolean epsilonGoesBigger = false;
    private int epsilonGoesBiggerTime;
    EpsilonModel epsilon;
    Timer viewUpdater;
    Timer modelUpdater;
    private final GameTimer gameTimer;
    private int startTime;
    public Updater() {
        gameTimer = GlassFrame.getINSTANCE().getTimer();
        viewUpdater = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()){{setCoalesce(true);}};
        viewUpdater.start();
        modelUpdater = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()){{setCoalesce(true);}};
        modelUpdater.start();
    }
    public void updateView() {
        updateEpsilonView();
        updateTriangleEnemyView();
        updateSquareEnemyView();
        updateOmenoctEnemyView();
        updateNecropickEnemyView();
        updateArchmireEnemyView();
        updateWyrmEnemyView();
        updateBlackOrbMiniBoss();
        updateBarricadosEnemyView();
        updateCollectibleView();
        updateBulletView();
        updateNonRigidBulletView();
        updatePanelView();
        for (GamePanel panel : GamePanel.gamePanelList) {
            panel.repaint();
        }
        GlassFrame.getINSTANCE().revalidate();
        GlassFrame.getINSTANCE().repaint();
        InformationPanel.getINSTANCE().repaint();
    }
    public void updateModel() {
        epsilon = EpsilonModel.epsilonModelsList.get(0);
        updateGameValues();
        updateEpsilonModel();
        reduceHealthByEpsilonAoe();
        updatePanelModel();
        SkillTreeHandled.addHpByTime();
        updateEnemiesModel();
        updateCollectibleModel();
        reduceHealthByBlackOrb();
        reduceHealthByArchmire();
        updateCollisionAndImpact();
        checkCollisionBulletAndPanels();
        moveAllModels();
        Controller.getINSTANCE().logic.updateLocalSave(gameTimer.getMiliSecond());
        Controller.getINSTANCE().logic.checkGameOver();
        updateWave();
    }
    private void updateGameValues() {
        GameValues.waveLengthTime = gameTimer.getMiliSecond() - GameValues.waveStartTime;
        GameValues.progressRate = 10 * (epsilon.getXp() * (GameValues.totalProgressTime + GameValues.waveNumber * GameValues.waveLengthTime / 1000) / epsilon.getHp());
    }
    /// model functions
    private void updateWave() {
        if (Controller.getINSTANCE() == null) return;
        if (PanelModel.panelModelList.isEmpty()) return;
        if (startGame) {
            boolean isWave = false;
            if (!GameValues.firstRoundFinish && !epsilonGoesBigger) {
                PanelModel pm = PanelModel.panelModelList.get(0);
                isWave = Controller.getINSTANCE().logic.checkCanMakeWave();
                if (GameValues.waveNumber + 1 == 2 && isWave) {
                    epsilonGoesBigger = true;
                    epsilonGoesBiggerTime = gameTimer.getSeconds();
                }
                else if (isWave) {
                    Controller.getINSTANCE().logic.makeFirstRoundWave(pm.getX(), pm.getY(), pm.getWidth(), pm.getHeight());
                    startWave.play();
                    GameValues.waveStartTime = gameTimer.getMiliSecond();
                }
            }
            else if (GameValues.firstRoundFinish) {
                isWave = Controller.getINSTANCE().logic.checkCanMakeWave();
                if (GameValues.waveNumber + 1 == 5 && isWave) {
                    if (PanelModel.panelModelList.get(0).moveToCenter()) {
                        PanelModel.panelModelList.get(0).setDirection(new Direction(new Point2D.Double(0, 0)));
                        PanelModel.panelModelList.get(0).setSpeed(0);
                        epsilon.getAoeCenters().clear();
                        Controller.getINSTANCE().logic.showFinishGame();
                    }
                    return;
                }
                else if (isWave) {
                    Controller.getINSTANCE().logic.updateSecondRoundWave(gameTimer.getMiliSecond());
                    startWave.play();
                }
                Controller.getINSTANCE().logic.updateSpawn(gameTimer.getMiliSecond());
            }
        }
    }
    private void updateEpsilonModel() {
        epsilon.updateVertices();
        epsilon.updateMainPanels(PanelModel.panelModelList);
        if (SkillTreeHandled.cerberusAttack) epsilon.updateAoeAttack();

        if (epsilonGoesBigger) {
            PanelModel firstMainPanel = PanelModel.panelModelList.get(0);
            double addRate = Math.max(firstMainPanel.getHeight(), firstMainPanel.getWidth()) / 75;
            epsilon.setRadius(epsilon.getRadius() + addRate);

            Point2D lst = epsilon.getCenter();
            epsilon.setCenter(new Point2D.Double(lst.getX() - addRate / 3, lst.getY() - addRate / 3));

            if (Utils.unionPanels(PanelModel.panelModelList, epsilon.getCenter(), epsilon.getRadius()).isEmpty()  || gameTimer.getSeconds() - epsilonGoesBiggerTime >= 10) {
                wonGame = true;
            }
            return;
        }
        TypedActionHandle.doMove();
        epsilon.adjustLocation(PanelModel.panelModelList);
        if (epsilon.getSpeed() > 0) {
            epsilon.setSpeed(epsilon.getSpeed() - (epsilon.getSpeed() / 10));
        }
    }
    private void reduceHealthByEpsilonAoe() {
        if (gameTimer.getSeconds() - epsilon.getLastAoeAttack() > 15 || epsilon.getLastAoeAttack() == 0) {
            ArrayList<Enemy> tmp = epsilon.getEnemiesInsideAoe();
            for (int i = 0; i < tmp.size(); i++) {
                enemyHealthReduction(tmp.get(i), SKILL_TREE_CERBERUS_DAMAGE);
            }
            epsilon.setLastAoeAttack(gameTimer.getSeconds());
        }
    }
    private void updatePanelModel() {
        for (PanelModel panel : PanelModel.panelModelList) {
            if (!startGame) {
                panel.startShrinkValue();
                if (panel.getWidth() <= Constants.GAME_PANEL_START_DIMENSION_ERROR.width) {
                    startGame = true;
                    startTime = gameTimer.getSeconds();
                    panel.setLeftSpeed(0);
                    panel.setRightSpeed(0);
                    panel.setUpSpeed(0);
                    panel.setDownSpeed(0);
                }
            }
            else if (wonGame) {
                panel.endGameShrinkValue();
                if ((panel.getWidth() <= GAME_PANEL_END_SIZE.width && panel.getHeight() <= GAME_PANEL_END_SIZE.height)) {
                    startGame = false;
                    wonGame = false;
                    epsilonGoesBigger = false;
                    Controller.getINSTANCE().logic.showFinishGame();
                    return;
                }
            }

            else if (gameTimer.getSeconds() - startTime >= 10 && gameTimer.getMiliSecond() - GameValues.waveStartTime >= 3000) {
                panel.inGameShrinkValue();
            }
            panel.shrink(wonGame);
            panel.updateCenter();
        }
    }
    private void updateEnemiesModel() {
        for (SquareEnemy ptr : SquareEnemy.squareEnemyList) {
            ptr.updateDirection(epsilon.getCenter());
            ptr.calculateVertices();
            ptr.updateSpeed();
        }
        for (TriangleEnemy ptr : TriangleEnemy.triangleEnemyList) {
            ptr.updateDirection(epsilon.getCenter());
            ptr.calculateVertices();
            ptr.updateSpeed(epsilon.getCenter());
        }
        for (OmenoctEnemy ptr : OmenoctEnemy.omenoctEnemyList) {
            ptr.updateDirection(ptr.findTargetPanel(epsilon.getMainPanels(), epsilon.getCenter()));
            ptr.makeAttack(gameTimer.getSeconds(), epsilon.getCenter());
            ptr.calculateVertices();
        }
        for (NecropickEnemy ptr : NecropickEnemy.necropickEnemiesList) {
            ptr.updateStation(gameTimer.getMiliSecond());
            ptr.updateDirection(epsilon.getCenter());
            ptr.updateSpeed(epsilon.getCenter());
            ptr.calculateVertices();
        }
        for (ArchmireEnemy ptr : ArchmireEnemy.archmireEnemiesList) {
            ptr.updateDirection(epsilon.getCenter());
            ptr.updateEntitiesInsideDrown(gameTimer.getMiliSecond());
            ptr.updateEntitiesInsideAOE(gameTimer.getMiliSecond());
            ptr.updateCentersMemory(gameTimer.getMiliSecond());
            ptr.calculateVertices();
        }
        for (WyrmEnemy ptr : WyrmEnemy.wyrmEnemiesList) {
            ptr.calculateVertices();
            ptr.makeAttack(gameTimer.getSeconds(), epsilon.getCenter());
            ptr.updateDirection(epsilon.getCenter());
        }
        for (int i = 0; i < BlackOrbMiniBoss.blackOrbMiniBossesList.size(); i++) {
            BlackOrbMiniBoss ptr = BlackOrbMiniBoss.blackOrbMiniBossesList.get(i);
            ptr.makeOrbs(gameTimer.getMiliSecond());
            ptr.updateEntitiesLaserCollision(gameTimer.getMiliSecond());
            if (ptr.isFullyMade() && ptr.getOrbEnemies().isEmpty()) {
                BlackOrbMiniBoss.blackOrbMiniBossesList.remove(i--);
            }
        }
        for (int i = 0; i < BarricadosEnemy.barricadosEnemiesList.size(); i++) {
            BarricadosEnemy ptr = BarricadosEnemy.barricadosEnemiesList.get(i);
            if (ptr.checkToRemove(gameTimer.getMiliSecond())) {
                BarricadosEnemy.removeFromAllList(ptr.getId());
                ptr.removePanel();
                i--;
            }
        }
    }
    private void reduceHealthByBlackOrb() {
        boolean done = false;

        while (!done) {
            done = true;
            ArrayList<BlackOrbMiniBoss> blackOrbs = BlackOrbMiniBoss.blackOrbMiniBossesList;
            for (int j = 0; j < blackOrbs.size(); j++) {
                BlackOrbMiniBoss blackOrb = blackOrbs.get(j);
                ArrayList<Pair<Enemy, Integer>> tmp = blackOrb.getEnemiesLaserCollision();
                for (int i = 0; i < tmp.size(); i++) {
                    if (gameTimer.getMiliSecond() - tmp.get(i).getSecond() >= 1000) {
                        enemyHealthReduction(tmp.get(i).getFirst(), ORB_ENEMY_LASER_REDUCER_HP);
                        done = false;
                        tmp.remove(i);
                        break;
                    }
                }
                ArrayList<Pair<EpsilonModel, Integer>> tmp2 = blackOrb.getEpsilonLaserCollision();
                for (int i = 0; i < tmp2.size(); i++) {
                    if (gameTimer.getMiliSecond() - tmp2.get(i).getSecond() >= 1000) {
                        epsilonHealthReduction(tmp2.get(i).getFirst(), ORB_ENEMY_LASER_REDUCER_HP);
                        done = false;
                        tmp2.remove(i);
                        break;
                    }
                }
            }
        }
    }
    private void reduceHealthByArchmire() {
        boolean done = false;

        while(!done){
            done = true;
            ArrayList<ArchmireEnemy> archmires = ArchmireEnemy.archmireEnemiesList;
            for (int j = 0; j < archmires.size(); j++) {
                ArchmireEnemy archmire = archmires.get(j);
                ArrayList<Pair<Enemy, Integer>> tmp = archmire.getEnemiesInsideDrown();
                for (int i = 0; i < tmp.size(); i++) {
                    if (gameTimer.getMiliSecond() - tmp.get(i).getSecond() >= 1000) {
                        enemyHealthReduction(tmp.get(i).getFirst(), ARCHMIRE_ENEMY_DROWN_REDUCER_HP);
                        done = false;
                        tmp.remove(i);
                        break;
                    }
                }
                tmp = archmire.getEnemiesInsideAOE();
                for (int i = 0; i < tmp.size(); i++) {
                    if (gameTimer.getMiliSecond() - tmp.get(i).getSecond() >= 1000) {
                        enemyHealthReduction(tmp.get(i).getFirst(), ARCHMIRE_ENEMY_AOE_REDUCER_HP);
                        done = false;
                        tmp.remove(i);
                        break;
                    }
                }
                ArrayList<Pair<EpsilonModel, Integer>> tmp2 = archmire.getEpsilonsInsideDrown();
                for (int i = 0; i < tmp2.size(); i++) {
                    if (gameTimer.getMiliSecond() - tmp2.get(i).getSecond() >= 1000) {
                        epsilonHealthReduction(tmp2.get(i).getFirst(), ARCHMIRE_ENEMY_DROWN_REDUCER_HP);
                        done = false;
                        tmp2.remove(i);
                        break;
                    }
                }
                tmp2 = archmire.getEpsilonsInsideAOE();
                for (int i = 0; i < tmp2.size(); i++) {
                    if (gameTimer.getMiliSecond() - tmp2.get(i).getSecond() >= 1000) {
                        epsilonHealthReduction(tmp2.get(i).getFirst(), ARCHMIRE_ENEMY_AOE_REDUCER_HP);
                        done = false;
                        tmp2.remove(i);
                        break;
                    }
                }
            }
        }
    }
    private void updateCollectibleModel() {
        int ptr = 0;
        Rectangle epsilonRect = new Rectangle((int)(epsilon.getCenter().getX() - epsilon.getRadius())
                , (int)(epsilon.getCenter().getY() - epsilon.getRadius())
                , (int)(2 * epsilon.getRadius()), (int)(2 * epsilon.getRadius()));

        while (ptr < Collectible.collectibleList.size()) {
            Collectible now = Collectible.collectibleList.get(ptr);
            boolean hasIntersect = epsilonRect.intersects(new Rectangle((int)(now.getCenter().getX() - (now.getSize()) / 2),
                    (int)(now.getCenter().getY() - (now.getSize()) / 2), (int)now.getSize(), (int)now.getSize()));
            now.setExistence(now.getExistence() - modelUpdater.getDelay());
            if (hasIntersect) {
                addCollectible.play();
                epsilon.setXp(epsilon.getXp() + now.getAddedXp());
                Collectible.removeFromAllList(Collectible.collectibleList.get(ptr).getId());
            }
            else {
                if (now.getExistence() <= 0) {
                    Collectible.removeFromAllList(Collectible.collectibleList.get(ptr).getId());
                }
                else ptr++;
            }
        }
    }
    private void updateCollisionAndImpact() {
        for (int i = 0; i < Collidable.collidables.size(); i++) {
            Collidable first = Collidable.collidables.get(i);
            for (int j = 0; j < Collidable.collidables.size(); j++) {
                if (i == j) continue;
                Collidable second = Collidable.collidables.get(j);
                CollisionPoint tmp = first.collides(second);
                Point2D point = null;
                if (tmp != null) point = tmp.getCollisionPoint();

                if (point != null) {
                    double impactLevel = 0;
                    if (first instanceof NecropickEnemy && second instanceof NecropickEnemy) continue;
                    if (first instanceof WyrmEnemy) {
                        ((WyrmEnemy) first).reverseClockwise(gameTimer.getMiliSecond());
                        if (second instanceof WyrmEnemy) {
                            ((WyrmEnemy) second).reverseClockwise(gameTimer.getMiliSecond());
                            continue;
                        }
                    }
                    if (first instanceof EpsilonModel && second instanceof Enemy) {
                        if (SkillTreeHandled.astrapeAttack) {
                            enemyHealthReduction((Enemy) second, SKILL_TREE_ASTRAPE_DAMAGE);
                        }
                        boolean firstVer = false;
                        boolean secondVer = false;
                        for (Point2D ptr : first.getVertices()) {
                            if (Utils.approxEqual(ptr.getX(), point.getX()) && Utils.approxEqual(point.getY(), ptr.getY())) firstVer = true;
                        }
                        for (Point2D ptr : second.getVertices()) if (ptr.equals(point)) secondVer = true;
                        if (firstVer && !secondVer) {
                            if (!(second instanceof ArchmireEnemy) && !(second instanceof WyrmEnemy)) {
                                if (SkillTreeHandled.chironAttack) epsilon.setHp(epsilon.getHp() + SKILL_TREE_CHIRON_ADD_HP);
                                enemyHealthReduction((Enemy) second, EPSILON_REDUCE_HP);
                            }
                        }
                        else if (secondVer && !firstVer) {
                            boolean canDamage = true;
                            if (SkillTreeHandled.melampusAttack) canDamage = RandomHelper.melampusRandom();
                            if (canDamage) epsilonHealthReduction(epsilon, ((Enemy) second).getReducerHp());
                        }
                        impactLevel = 5;
                    }
                    else if ((first instanceof RigidBulletModel || first instanceof NonRigidBulletModel) && second instanceof Enemy) {
                        impactLevel = 5;
                        if (first instanceof RigidBulletModel && !second.getId().equals(((RigidBulletModel) first).getShooterEntity())) {
                            if (((RigidBulletModel) first).getShooterEntity().equals(epsilon.getId())) {
                                if (SkillTreeHandled.chironAttack) epsilon.setHp(epsilon.getHp() + SKILL_TREE_CHIRON_ADD_HP);
                                GameValues.successfulBullet++;
                            }
                            enemyHealthReduction((Enemy) second, ((RigidBulletModel) first).getReduceHp());
                            RigidBulletModel.removeFromAllList(first.getId());
                        }
                        else if (first instanceof NonRigidBulletModel && !second.getId().equals(((NonRigidBulletModel) first).getShooterEnemy())) {
                            enemyHealthReduction((Enemy) second, ((NonRigidBulletModel) first).getReduceHp());
                            NonRigidBulletModel.removeFromAllList(first.getId());
                        }
                        else impactLevel = 0;
                    }
                    else if (first instanceof EpsilonModel && (second instanceof NonRigidBulletModel || second instanceof RigidBulletModel)) {
                        if (second instanceof RigidBulletModel && ((RigidBulletModel) second).getShooterEntity().equals(first.getId())) continue;
                        if (second instanceof NonRigidBulletModel) {
                            epsilonHealthReduction(epsilon, ((NonRigidBulletModel) second).getReduceHp());
                            NonRigidBulletModel.removeFromAllList(second.getId());
                        }
                        if (second instanceof RigidBulletModel) {
                            epsilonHealthReduction(epsilon, ((RigidBulletModel) second).getReduceHp());
                            RigidBulletModel.removeFromAllList(second.getId());
                        }
                        impactLevel = 5;
                    }
                    else if (first instanceof RigidBulletModel && second instanceof NonRigidBulletModel) {
                        NonRigidBulletModel.removeFromAllList(second.getId());
                        RigidBulletModel.removeFromAllList(first.getId());
                        impactLevel = 2;
                    }
                    else if (first instanceof NonRigidBulletModel && second instanceof NonRigidBulletModel) {
                        NonRigidBulletModel.removeFromAllList(second.getId());
                        NonRigidBulletModel.removeFromAllList(first.getId());
                        impactLevel = 2;
                    }
                    else if (first instanceof RigidBulletModel && second instanceof RigidBulletModel) {
                        RigidBulletModel.removeFromAllList(second.getId());
                        RigidBulletModel.removeFromAllList(first.getId());
                        impactLevel = 2;
                    }
                    else impactLevel = 2;
                    if (impactLevel != 0) ImpactMechanism.applyImpact(point, impactLevel);
                }
            }
        }
    }
    private boolean enemyHealthReduction(Enemy enemy, int reduceHp) {
        enemy.setHp(enemy.getHp() - reduceHp);
        if (enemy.getHp() <= 0) {
            GameValues.enemyKilled++;
            enemyDeath.play();
            GameValues.temporaryEnemyKilledNumber++;
            Controller.getINSTANCE().logic.createCollectible(enemy.getCollectibleNumber(), enemy.getCollectibleXp(), enemy.getCenter(), enemy.getSize());
            if (enemy instanceof TriangleEnemy) TriangleEnemy.removeFromAllList(enemy.getId());
            if (enemy instanceof SquareEnemy) SquareEnemy.removeFromAllList(enemy.getId());
            if (enemy instanceof OmenoctEnemy) OmenoctEnemy.removeFromAllList(enemy.getId());
            if (enemy instanceof NecropickEnemy) NecropickEnemy.removeFromAllList(enemy.getId());
            if (enemy instanceof ArchmireEnemy) ArchmireEnemy.removeFromAllList(enemy.getId());
            if (enemy instanceof WyrmEnemy) {
                ((WyrmEnemy) enemy).removePanel();
                WyrmEnemy.removeFromAllList(enemy.getId());
            }
            if (enemy instanceof OrbEnemy) {
                ((OrbEnemy) enemy).removePanel();
                OrbEnemy.removeFromAllList(enemy.getId());
                ((OrbEnemy) enemy).removeFromParentBoss();
            }
            return true;
        }
        return false;
    }
    private void epsilonHealthReduction(EpsilonModel epsilon, int reduceHp) {
        if (reduceHp != 0) injured.play();
        epsilon.setHp(epsilon.getHp() - reduceHp);
    }
    private void omenoctWallHealthReduction(WallSideIndicator wallSideIndicator, double pl) {
        for (int i = 0; i < OmenoctEnemy.omenoctEnemyList.size(); i++) {
            OmenoctEnemy omenoct = OmenoctEnemy.omenoctEnemyList.get(i);
            if (omenoct.isConnectedToWall(wallSideIndicator, pl)) {
                if (enemyHealthReduction(omenoct, BULLET_REDUCE_HP)) i--;
            }
        }
    }
    private void checkCollisionBulletAndPanels() {
        for (int i = 0; i < RigidBulletModel.rigidBulletModelList.size(); i++) {
            RigidBulletModel ptr = RigidBulletModel.rigidBulletModelList.get(i);
            ArrayList<PanelModel> coveredPanels = ptr.hasCollisions(PanelModel.panelModelList);

            if (coveredPanels == null) return;

            for (PanelModel panel : coveredPanels) {
                if (ptr.getCenter().getX() + ptr.getRadius() >= panel.getX() + panel.getWidth()) {
                    panel.bulletHit(WallSideIndicator.RIGHT);
                    omenoctWallHealthReduction(WallSideIndicator.RIGHT, panel.getX() + panel.getWidth());
                }
                if (ptr.getCenter().getX() - ptr.getRadius() <= panel.getX()) {
                    panel.bulletHit(WallSideIndicator.LEFT);
                    omenoctWallHealthReduction(WallSideIndicator.LEFT, panel.getX());
                }
                if (ptr.getCenter().getY() + ptr.getRadius() >= panel.getY() + panel.getHeight()) {
                    panel.bulletHit(WallSideIndicator.DOWN);
                    omenoctWallHealthReduction(WallSideIndicator.DOWN, panel.getY() + panel.getHeight());
                }
                if (ptr.getCenter().getY() - ptr.getRadius() <= panel.getY()) {
                    panel.bulletHit(WallSideIndicator.UP);
                    omenoctWallHealthReduction(WallSideIndicator.UP, panel.getY());
                }
            }

            RigidBulletModel.removeFromAllList(ptr.getId());
        }
    }
    private void moveAllModels() {
        for (Movable ptr : Movable.movable) {
            if (StoreActionHandle.isFreezing()) {
                if (gameTimer.getSeconds() - StoreActionHandle.getStartFreezing() < 10) {
                    if (!(ptr instanceof RigidBulletModel && ((RigidBulletModel) ptr).getShooterEntity().equals(epsilon.getId()))){
                        continue;
                    }
                }
                else StoreActionHandle.setFreezing(false);
            }

            if (StoreActionHandle.isBanishHovering()) {
                if (gameTimer.getSeconds() - StoreActionHandle.getStartBanish() < 10) {
                    if (ptr instanceof Enemy && !((Enemy) ptr).isHovering()) continue;
                }
                else StoreActionHandle.setBanishHovering(false);
            }

            if (ptr instanceof PanelModel && ((PanelModel) ptr).collidesWithOtherPanel()) continue;

            ptr.move();
        }
    }

    /// view functions
    private void updateEpsilonView() {
        for (EpsilonView ptr : EpsilonView.epsilonViewsList) {
            EpsilonModel tmp = Controller.getINSTANCE().logic.findEpsilonModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentXp(tmp.getXp());
            ptr.setCurrentHp(tmp.getHp());
            ptr.setCurrentVertices(tmp.getVertices());
            ptr.setCurrentRadius(tmp.getRadius());
            ptr.setAoeCenters(tmp.getAoeCenters());
        }
    }
    private void updateTriangleEnemyView() {
        for (TriangleEnemyView ptr : TriangleEnemyView.triangleEnemyViewList) {
            TriangleEnemy tmp = Controller.getINSTANCE().logic.findTriangleEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateSquareEnemyView() {
        for (SquareEnemyView ptr : SquareEnemyView.squareEnemyViewList) {
            SquareEnemy tmp = Controller.getINSTANCE().logic.findSquareEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateOmenoctEnemyView() {
        for (OmenoctEnemyView ptr : OmenoctEnemyView.omenoctEnemyViewList) {
            OmenoctEnemy tmp = Controller.getINSTANCE().logic.findOmenoctEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateNecropickEnemyView() {
        for (NecropickEnemyView ptr : NecropickEnemyView.necropickEnemyViewsList) {
            NecropickEnemy tmp = Controller.getINSTANCE().logic.findNecropickEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
            ptr.setStationedType(tmp.getStationedType());
        }
    }
    private void updateArchmireEnemyView() {
        for (ArchmireEnemyView ptr : ArchmireEnemyView.archmireEnemyViewsList) {
            ArchmireEnemy tmp = Controller.getINSTANCE().logic.findArchmireEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
            ptr.setCurrentCentersPointMemory(tmp.getCentersPointMemory());
        }
    }
    private void updateWyrmEnemyView() {
        for (WyrmEnemyView ptr : WyrmEnemyView.wyrmEnemyViewsList) {
            WyrmEnemy tmp = Controller.getINSTANCE().logic.findWyrmEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateBlackOrbMiniBoss() {
        for (BlackOrbMiniBossView ptr : BlackOrbMiniBossView.blackOrbMiniBossViewsList) {
            BlackOrbMiniBoss tmp = Controller.getINSTANCE().logic.findBlackOrbMiniBossModel(ptr.getId());
            ptr.setCurrentOrbLocations(tmp.getOrbsLocation());
            ptr.setOrbRadius(tmp.getOrbRadius());
            ptr.setCurrentOrbLasers(tmp.getOrbLasers());
        }
    }
    private void updateBarricadosEnemyView() {
        for (BarricadosEnemyView ptr : BarricadosEnemyView.barricadosEnemyViewsList) {
            BarricadosEnemy tmp = Controller.getINSTANCE().logic.findBarricadosEnemyModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateCollectibleView() {
        for (CollectibleView ptr : CollectibleView.collectibleViewList) {
            Collectible tmp = Controller.getINSTANCE().logic.findCollectibleModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentSize(tmp.getSize());
        }
    }
    private void updateBulletView() {
        for (BulletView ptr : BulletView.bulletViewList) {
            RigidBulletModel tmp = Controller.getINSTANCE().logic.findBulletModel(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentRadius(tmp.getRadius());
        }
    }
    private void updateNonRigidBulletView() {
        for (EnemyNonRigidBulletView ptr : EnemyNonRigidBulletView.nonRigidBulletViewsList) {
            NonRigidBulletModel tmp = Controller.getINSTANCE().logic.findNonRigidBullet(ptr.getId());
            ptr.setCurrentCenter(tmp.getCenter());
            ptr.setCurrentRadius(tmp.getRadius());
        }
    }
    private void updatePanelView() {
        for (GamePanel ptr : GamePanel.gamePanelList) {
            PanelModel tmp = Controller.getINSTANCE().logic.findPanelModel(ptr.getId());
            ptr.setUpLeftX(tmp.getX());
            ptr.setUpLeftY(tmp.getY());
            ptr.setWidth(tmp.getWidth());
            ptr.setHeight(tmp.getHeight());
            ptr.updateBound();
        }
    }

    public Timer getViewUpdater() {
        return viewUpdater;
    }

    public Timer getModelUpdater() {
        return modelUpdater;
    }
}
