package model.movement;

import java.awt.geom.Point2D;

public class Direction {
    boolean isUpward = false;
    boolean isDownward = false;
    double slope;
    DirectionState state = DirectionState.neutral;

    public Direction(Point2D point) {

    }
    public enum DirectionState{
        negative,positive,neutral
    }
}
