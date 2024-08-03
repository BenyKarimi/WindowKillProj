package client.controller.handeler;

import client.controller.updater.Pair;
import client.controller.constant.Constants;
import client.controller.constant.KeyActions;
import client.controller.random.RandomHelper;
import client.model.charactersModel.EpsilonModel;
import client.view.container.GlassFrame;
import client.view.gameTimerView.GameTimer;

import java.util.ArrayList;

public class SkillTreeHandled {

    public static boolean canAttack = false;
    public static boolean canDefence = false;
    public static boolean canTransformation = false;
    public static boolean astrapeAttack = false;
    public static boolean cerberusAttack = false;
    public static boolean melampusAttack = false;
    public static boolean chironAttack = false;

    public static ArrayList<Integer> skillsOpened = new ArrayList<>();

    private static boolean madeTwoRandomForDolus = false;
    private static int firstDolusInd;
    private static int secondDolusInd;

    public static SkillTreeAttackType skillTreeAttackType;
    public static SkillTreeDefenceType skillTreeDefenceType;
    public static SkillTreeTransformationType skillTreeTransformationType;

    private static int lastAttack = 0;
    private static int lastDefence = 0;
    private static int lastProteus = 0;

    private static int lastAddedHP = 0;
    private static int addingHpNumber = 0;
    private static GameTimer timer;
    private static EpsilonModel epsilon;
    public SkillTreeHandled() {
        timer = GlassFrame.getINSTANCE().getTimer();
        epsilon = EpsilonModel.epsilonModelsList.get(0);
    }
    public static void handlePressedKeys(int keyKode) {
        if (epsilon.getXp() < 100) return;
        if (keyKode == KeyActions.skillTreeAttack && canAttack) {
            if (lastAttack == 0 || timer.getSeconds() - lastAttack >= 150) {
                if (!astrapeAttack && !cerberusAttack) epsilon.setXp(epsilon.getXp() - 100);
                lastAttack = timer.getSeconds();
                handleAttackFunction();
            }
        }
        else if (keyKode == KeyActions.skillTreeDefence && canDefence) {
            if (lastDefence == 0 || timer.getSeconds() - lastDefence >= 150) {
                if (!melampusAttack && !chironAttack) epsilon.setXp(epsilon.getXp() - 100);
                lastDefence = timer.getSeconds();
                handleDefenceFunction();
            }
        }
        else if (keyKode == KeyActions.skillTreeProteus && canTransformation) {
            if (lastProteus == 0 || timer.getSeconds() - lastProteus >= 150) {
                epsilon.setXp(epsilon.getXp() - 100);
                lastProteus = timer.getSeconds();
                handleTransformationFunction();
            }
        }
    }

    private static void handleAttackFunction() {
        if (skillTreeAttackType.equals(SkillTreeAttackType.ARES)) {
            Constants.BULLET_REDUCE_HP += 2;
            Constants.EPSILON_REDUCE_HP += 2;
        }
        else if (skillTreeAttackType.equals(SkillTreeAttackType.ASTRAPE)) {
            astrapeAttack = true;
        }
        else {
            cerberusAttack = true;
        }
    }
    private static void handleDefenceFunction() {
        if (skillTreeDefenceType.equals(SkillTreeDefenceType.ACESO)) {
            addingHpNumber++;
        }
        else if (skillTreeDefenceType.equals(SkillTreeDefenceType.MELAMPUS)) {
            melampusAttack = true;
        }
        else {
            chironAttack = true;
        }
    }
    private static void handleTransformationFunction() {
        if (skillTreeTransformationType.equals(SkillTreeTransformationType.PROTEUS)) {
            epsilon.setVerticesNumber(epsilon.getVerticesNumber() + 1);
        }
        else if (skillTreeTransformationType.equals(SkillTreeTransformationType.EMPUSA)) {
            epsilon.setRadius(epsilon.getRadius() * 9 / 10);
        }
        else {
            makeDolusRandom();
            makeDolusSkill(skillsOpened.get(firstDolusInd));
            makeDolusSkill(skillsOpened.get(secondDolusInd));
        }
    }
    private static void makeDolusSkill(int type) {
        if (type == 0) {
            Constants.BULLET_REDUCE_HP += 2;
            Constants.EPSILON_REDUCE_HP += 2;
        }
        else if (type == 1) {
            astrapeAttack = true;
        }
        else if (type == 2) {
            cerberusAttack = true;
        }
        else if (type == 3) {
            addingHpNumber++;
        }
        else if (type == 4) {
            melampusAttack = true;
        }
        else if (type == 5) {
            chironAttack = true;
        }
        else if (type == 6) {
            epsilon.setVerticesNumber(epsilon.getVerticesNumber() + 1);
        }
        else if (type == 7) {
            epsilon.setRadius(epsilon.getRadius() * 9 / 10);
        }
    }
    private static void makeDolusRandom() {
        if (madeTwoRandomForDolus) return;
        Pair<Integer, Integer> tmp = RandomHelper.makeDolusRandom(skillsOpened.size());
        firstDolusInd = tmp.getFirst();
        secondDolusInd = tmp.getSecond();
        madeTwoRandomForDolus = true;
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
        astrapeAttack = false;
        cerberusAttack = false;
        melampusAttack = false;
        chironAttack = false;
        madeTwoRandomForDolus = false;
    }
}
