package model.movement;

import java.awt.*;
import java.awt.geom.Point2D;

public class Direction {
    private boolean isUpward = false;
    private boolean isDownward = false;
    private double slope;
    private DirectionState state = DirectionState.neutral;

    public Direction(Point2D deltaPos) {
        if (deltaPos.getX() == 0 && deltaPos.getY() < 0) isUpward = true;
        else if (deltaPos.getX() == 0 && deltaPos.getY() > 0) isDownward = true;
        else if (deltaPos.getX() == 0) this.state = DirectionState.neutral;
        else {
            this.slope = deltaPos.getY() / deltaPos.getX();
            if (deltaPos.getX() > 0) this.state = DirectionState.positive;
            else this.state = DirectionState.negative;
        }
    }

    public Point2D getDirectionVector() {
        if (state == DirectionState.neutral) return new Point(0, 0);
        if (isUpward) return new Point2D.Double(0, -1);
        if (isDownward) return new Point2D.Double(0, 1);
        double magnitude = Math.sqrt(1 + slope * slope);
        Point2D.Double normalVector = new Point2D.Double(1 / magnitude,slope / magnitude);
        if (state == DirectionState.negative) normalVector = new Point2D.Double(-normalVector.getX(),-normalVector.getY());
        return normalVector;
    }

    public int getState() {
        if (state.equals(DirectionState.positive)) return 1;
        else if (state.equals(DirectionState.negative)) return -1;
        return 0;
    }

    public enum DirectionState{
        negative,positive,neutral
    }
}
