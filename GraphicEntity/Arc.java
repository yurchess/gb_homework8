package GraphicEntity;

import java.awt.*;

public class Arc extends Circle {
    private double startAngle;
    private double endAngle;

    public Arc(double xCenter, double yCenter, double radius, double startAngle, double endAngle) {
        super(xCenter, yCenter, radius);
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    @Override
    public void draw(Graphics g) {
        Point<Double> centerPoint = new Point<Double>(x0, y0);
        Point<Integer> centerPointPixel = getPixelCoordinates(centerPoint, (int) g.getClipBounds().getHeight());
        int i = (int) Math.round(centerPointPixel.getX() - radius * scale);
        int i1 = (int) Math.round(centerPointPixel.getY() - radius * scale);
        int i2 = (int) Math.round(2 * radius * scale);
        double endAngleN = endAngle;
        if (endAngleN < startAngle) {
            endAngleN += 360;
        }
        g.drawArc(i, i1, i2, i2, (int) startAngle, (int) (endAngleN - startAngle));
    }

    private boolean isAngleInRange(double angle, double startAngle, double endAngle) {
        if (endAngle < startAngle) {
            endAngle += 2 * Math.PI;
        }

        if (angle >= startAngle && angle <= endAngle) {
            return true;
        }

        angle += 2 * Math.PI;
        return angle >= startAngle && angle <= endAngle;
    }

    @Override
    public WorldBasisBoundsRect getBoundsRect() {
        double left = 0;
        double bottom = 0;
        double right = 0;
        double top = 0;
        double startAngleRad = (double) Math.toRadians(startAngle);
        double endAngleRad = (double) Math.toRadians(endAngle);
//        Left
        if (isAngleInRange((double) Math.toDegrees(Math.PI) , startAngle, endAngle))
            left = x0 - radius;
        else
            left = (double) Math.min((x0 + radius*Math.cos(startAngleRad)), (x0 + radius*Math.cos(endAngleRad)));
//        Top
        if (isAngleInRange((double) Math.toDegrees(Math.PI/2), startAngle, endAngle))
            top = y0 + radius;
        else
            top = (double) Math.max((y0 + radius*Math.sin(startAngleRad)), (y0 + radius*Math.sin(endAngleRad)));
//        Right
        if (isAngleInRange(0, startAngle, endAngle))
            right = x0 + radius;
        else
            right = (double) Math.max((x0 + radius*Math.cos(startAngleRad)), (x0 + radius*Math.cos(endAngleRad)));
//        Bottom
        if (isAngleInRange((double) Math.toDegrees(3*Math.PI/2), startAngle, endAngle))
            bottom = y0 - radius;
        else
            bottom = (double) Math.min((y0 + radius*Math.sin(startAngleRad)), (y0 + radius*Math.sin(endAngleRad)));

        return new WorldBasisBoundsRect(left, right, top, bottom);
    }
}
