package GraphicEntity;

import java.awt.*;

public class Circle extends Entity {
    double radius;

    public Circle(double xCenter, double yCenter, double radius) {
        super(xCenter, yCenter);
        this.radius = radius;
    }

    @Override
    public void draw(Graphics g) {
        Point<Double> centerPoint = new Point<Double>(x0, y0);
        Point<Integer> centerPointPixel = getPixelCoordinates(centerPoint, (int) g.getClipBounds().getHeight());
        int i = (int) Math.round(centerPointPixel.getX() - radius * scale);
        int i1 = (int) Math.round(centerPointPixel.getY() - radius * scale);
        int i2 = (int) Math.round(2 * radius * scale);
        int i3 = i2;
        g.drawArc(i, i1, i2, i3, 0, 360);
    }

    @Override
    public WorldBasisBoundsRect getBoundsRect() {
        WorldBasisBoundsRect boundsRect = new WorldBasisBoundsRect();
        boundsRect.setLeft(x0 - radius);
        boundsRect.setRight(x0 + radius);
        boundsRect.setTop(y0 + radius);
        boundsRect.setBottom(y0 - radius);
        return boundsRect;
    }
}
