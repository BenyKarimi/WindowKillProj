package model.collision;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Line {
    private Point2D start, end;
    public Line(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;
    }
    public static Point2D findIntersection(Line l1, Line l2) {
        double s1_x, s1_y, s2_x, s2_y;
        s1_x = l1.end.getX() - l1.start.getX();
        s1_y = l1.end.getY() - l1.start.getY();
        s2_x = l2.end.getX() - l2.start.getX();
        s2_y = l2.end.getY() - l2.start.getY();

        double s, t;
        s = (-s1_y * (l1.start.getX() - l2.start.getX()) + s1_x * (l1.start.getY() - l2.start.getY())) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (l1.start.getY() - l2.start.getY()) - s2_y * (l1.start.getX() - l2.start.getX())) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            return new Point2D.Double(l1.start.getX() + (t * s1_x), l1.start.getY() + (t * s1_y));
        }
        return null;
    }

    public Point2D getStart() {
        return start;
    }

    public Point2D getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(start, line.start) && Objects.equals(end, line.end);
    }
}
