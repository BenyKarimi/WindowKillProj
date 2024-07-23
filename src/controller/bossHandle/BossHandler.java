package controller.bossHandle;

import model.charactersModel.boss.BossAttackType;
import model.charactersModel.boss.BossHead;
import model.charactersModel.boss.BossLeftHand;
import model.charactersModel.boss.BossRightHand;

public class BossHandler {
    private BossHead bossHead;
    private BossRightHand bossRightHand;
    private BossLeftHand bossLeftHand;
    private BossAttackType bossAttackType;
    private int lastAttack;

    public BossHandler() {
        this.lastAttack = 0;
    }

    public void makeAttack(int time) {

    }

    private void makeSqueezeAttack() {

    }
    private void makeProjectileAttack() {

    }
    private void makeVomitAttack() {

    }


    public void updateAttack(int time) {

    }

    private void updateDirection() {

    }
    private void updateSqueezeDirection() {

    }
    private void updateProjectileDirection() {

    }
    private void updateVomitDirection() {

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
}
