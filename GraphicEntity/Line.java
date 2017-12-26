package GraphicEntity;

import java.awt.*;

public class Line extends Entity {
    private double x1;
    private double y1;

    public Line(double xStart, double yStart, double xEnd, double yEnd) {
        super(xStart, yStart);
        x1 = xEnd;
        y1 = yEnd;
    }

    @Override
    public void move(double dx, double dy) {
        super.move(dx, dy);
        x1 += dx;
        y1 += dy;
    }

    @Override
    public void draw(Graphics g) {
        Point<Double> startPoint = new Point<Double>(x0, y0);
        Point<Double> endPoint = new Point<Double>(x1, y1);
        Point<Integer> startPixelPoint = getPixelCoordinates(startPoint, (int) g.getClipBounds().getHeight());
        Point<Integer> endPixelPoint = getPixelCoordinates(endPoint, (int) g.getClipBounds().getHeight());

        g.drawLine(startPixelPoint.getX(), startPixelPoint.getY(), endPixelPoint.getX(), endPixelPoint.getY());
    }

    @Override
    public WorldBasisBoundsRect getBoundsRect() {
        WorldBasisBoundsRect boundsRect = new WorldBasisBoundsRect();
        boundsRect.setLeft(Math.min(x0, x1));
        boundsRect.setRight(Math.max(x0, x1));
        boundsRect.setTop(Math.max(y0,y1));
        boundsRect.setBottom(Math.min(y0,y1));
        return boundsRect;
    }
}
