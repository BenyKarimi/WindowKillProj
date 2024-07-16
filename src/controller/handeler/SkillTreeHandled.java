package controller.handeler;

import controller.Controller;
import controller.constant.Constants;
import controller.constant.KeyActions;
import model.charactersModel.EpsilonModel;
import view.container.GamePanel;
import view.container.GlassFrame;
import view.gameTimerView.GameTimer;

public class SkillTreeHandled {

    public static boolean canAttack = false;
    public static boolean canDefence = false;
    public static boolean canProteus = false;

    private static int lastAttack = 0;
    private static int lastDefence = 0;
    private static int lastProteus = 0;

    private static int lastAddedHP = 0;
    private static int addingHpNumber = 0;
    private static GameTimer timer;
    private static EpsilonModel epsilon;
    public SkillTreeHandled() {
        timer = GlassFrame.getINSTANCE().getTimer();
        epsilon = Controller.getINSTANCE().logic.epsilon;
    }
    public static void handlePressedKeys(int keyKode) {
        if (epsilon.getXp() < 100) return;
        if (keyKode == KeyActions.skillTreeAttack && canAttack) {
            if (lastAttack == 0 || timer.getSeconds() - lastAttack >= 150) {
                epsilon.setXp(epsilon.getXp() - 100);
                lastAttack = timer.getSeconds();
                handleAttackFunction();
            }
        }
        else if (keyKode == KeyActions.skillTreeDefence && canDefence) {
            if (lastDefence == 0 || timer.getSeconds() - lastDefence >= 150) {
                epsilon.setXp(epsilon.getXp() - 100);
                lastDefence = timer.getSeconds();
                handleDefenceFunction();
            }
        }
        else if (keyKode == KeyActions.skillTreeProteus && canProteus) {
            if (lastProteus == 0 || timer.getSeconds() - lastProteus >= 150) {
                epsilon.setXp(epsilon.getXp() - 100);
                lastProteus = timer.getSeconds();
                handleProteusFunction();
            }
        }
    }

    private static void handleAttackFunction() {
        Constants.BULLET_REDUCE_HP += 2;
        Constants.EPSILON_REDUCE_HP += 2;
    }
    private static void handleDefenceFunction() {
        addingHpNumber++;
    }
    private static void handleProteusFunction() {
        epsilon.setVerticesNumber(epsilon.getVerticesNumber() + 1);
    }

    public static void addHpByTime() {
        if (lastAddedHP == 0 || timer.getSeconds() - lastAddedHP >= 1) {
            lastAddedHP = timer.getSeconds();
            epsilon.setHp(epsilon.getHp() + addingHpNumber);
        }
    }

    public static void makeAllRestart() {
        lastAttack = 0;
        lastDefence = 0;
        lastProteus = 0;
        lastAddedHP = 0;
        addingHpNumber = 0;
    }
}
