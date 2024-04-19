package controller;

import model.charactersModel.EpsilonModel;
import view.charecterViews.EpsilonView;

import java.awt.geom.Point2D;

import static controller.constant.Constants.EPSILON_RADIUS;

public class Logic {
    EpsilonModel epsilon;
    EpsilonView epsilonView;
    public Logic() {
        createEpsilon();
    }
    public void createEpsilon() {
        epsilon = new EpsilonModel(new Point2D.Double(EPSILON_RADIUS, EPSILON_RADIUS));
        epsilonView = EpsilonView.getINSTANCE();
    }
}
