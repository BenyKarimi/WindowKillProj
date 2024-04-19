package controller;

import controller.constant.Constants;
import model.charactersModel.EpsilonModel;
import view.container.GamePanel;
import view.container.GlassFrame;

import javax.swing.*;
import java.awt.*;

import static controller.constant.Constants.*;

public class Updater {
    boolean startGame = false;
    EpsilonModel epsilon;
    public Updater() {
        Timer viewUpdater = new Timer((int) FRAME_UPDATE_TIME, e -> updateView()){{setCoalesce(true);}};
        viewUpdater.start();
        Timer modelUpdater = new Timer((int) MODEL_UPDATE_TIME, e -> updateModel()){{setCoalesce(true);}};
        modelUpdater.start();
    }
    public void updateView() {
        Rectangle tmp = GamePanel.getINSTANCE().getBounds();
        updateEpsilonView();

        if (!startGame) {
            if (tmp.width > GAME_PANEL_START_DIMENSION.width && tmp.height > GAME_PANEL_START_DIMENSION.height)
                GamePanel.getINSTANCE().setBounds(tmp.x + 2, tmp.y + 2, tmp.width - 4, tmp.height - 4);
            else startGame = true;
        }
        else {

        }
        GlassFrame.getINSTANCE().repaint();
    }
    public void updateModel() {
        TypedActionHandel.doMove();
        epsilon.adjustLocation(new Dimension(GamePanel.getINSTANCE().getWidth(), GamePanel.getINSTANCE().getHeight()));
    }
    private void updateEpsilonView() {
        Controller.getINSTANCE().logic.epsilonView.setCurrentCenter(epsilon.getCenter());
        Controller.getINSTANCE().logic.epsilonView.setCurrentHp(epsilon.getHp());
        Controller.getINSTANCE().logic.epsilonView.setCurrentXp(epsilon.getXp());
        Controller.getINSTANCE().logic.epsilonView.setCurrentVertices(epsilon.getVertices());
    }
}
