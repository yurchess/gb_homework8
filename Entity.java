import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

interface IEntity {
    abstract void move(double dx, double dy);
    abstract void draw(Graphics g);
    abstract WorldBasisBoundsRect getBoundsRect();
}

abstract public class Entity implements IEntity {
    double x0 = 0;
    double y0 = 0;
    double scale = 1;

    Entity() {

    }

    Entity(double x, double y) {
        x0 = x;
        y0 = y;
    }

    @Override
    public void move(double dx, double dy) {
        x0 += dx;
        y0 += dy;
    }

    Point<Integer> getPixelCoordinates(Point<Double> inCoords, int height) {
        Point<Integer> pixelCoord = new Point<Integer>();
        pixelCoord.setX((int) Math.round(inCoords.getX() * scale));
        pixelCoord.setY((height - 1) - (int) Math.round(inCoords.getY() * scale));
        return pixelCoord;
    }

    void setScale(double scale) {
        this.scale = scale;
    }
}

class Entities extends Entity {
    ArrayList<Entity> entities = new ArrayList<>();

    Entities() {

    }

    double getScale() {
        return scale;
    }

    void setScale(double scale) {
        this.scale = scale;
        for (Entity entity : entities)
            entity.setScale(scale);
    }

    @Override
    public void move(double dx, double dy) {
        for (Entity entity : entities) {
            entity.move(dx, dy);
        }
    }

    @Override
    public void draw(Graphics g) {
        for (Entity entity : entities)
            entity.draw(g);
//        g.drawRect((int) (getBoundsRect().getX() * scale), (int) (g.getClipBounds().getHeight() - getBoundsRect().getY() * scale), (int) (getBoundsRect().getWidth() * scale), (int) (getBoundsRect().getHeight() * scale));
    }

    @Override
    public WorldBasisBoundsRect getBoundsRect() {
        if (this.get(0) == null)
            return null;
        else {
            WorldBasisBoundsRect boundsRect = get(0).getBoundsRect();
            for (Entity entity : entities)
                boundsRect = boundsRect.union(entity.getBoundsRect());
            return boundsRect;
        }
    }

    public int getSize() {
        return entities.size();
    }

    public void add(Entity entity) {
        entities.add(entity);
    }

    public Entity get(int index) {
        return entities.get(index);
    }
}

class Line extends Entity {
    private double x1;
    private double y1;

    Line(double xStart, double yStart, double xEnd, double yEnd) {
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
//        g.drawRect((int) (getBoundsRect().getX() * scale), (int) (g.getClipBounds().getHeight() - getBoundsRect().getY() * scale), (int) (getBoundsRect().getWidth() * scale), (int) (getBoundsRect().getHeight() * scale));
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

class Circle extends Entity {
    double radius;

    Circle(double xCenter, double yCenter, double radius) {
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
//        g.drawRect((int) (getBoundsRect().getX() * scale), (int) (g.getClipBounds().getHeight() - getBoundsRect().getY() * scale), (int) (getBoundsRect().getWidth() * scale), (int) (getBoundsRect().getHeight() * scale));
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

class Arc extends Circle {
    private double startAngle;
    private double endAngle;

    Arc(double xCenter, double yCenter, double radius, double startAngle, double endAngle) {
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
        g.drawArc(i, i1, i2, i2, (int) startAngle, (int) (endAngle - startAngle));
//        g.drawRect((int) (getBoundsRect().getX() * scale), (int) (g.getClipBounds().getHeight() - getBoundsRect().getY() * scale), (int) (getBoundsRect().getWidth() * scale), (int) (getBoundsRect().getHeight() * scale));
    }

    private boolean isAngleInRange(double angle, double startAngle, double endAngle) {
        if (endAngle < startAngle) {
            endAngle += 2 * Math.PI;
        }

        if (angle >= startAngle && angle <= endAngle) {
            return true;
        }

        angle += 2 * Math.PI;
        if (angle >= startAngle && angle <= endAngle) {
            return true;
        }

        return false;
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

class Point<T> {
    private T x;
    private T y;

    Point() {}

    Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    void setX(T x) {
        this.x = x;
    }

    void setY(T y) {
        this.y = y;
    }

    T getX() {
        return x;
    }

    T getY() {
        return y;
    }
}

class WorldBasisBoundsRect {
    private double left;
    private double right;
    private double top;
    private double bottom;

    WorldBasisBoundsRect() {}

    WorldBasisBoundsRect(double left, double right, double top, double bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    double getLeft() {
        return left;
    }

    void setLeft(double left) {
        this.left = left;
    }

    double getRight() {
        return right;
    }

    void setRight(double right) {
        this.right = right;
    }

    double getTop() {
        return top;
    }

    void setTop(double top) {
        this.top = top;
    }

    double getBottom() {
        return bottom;
    }

    void setBottom(double bottom) {
        this.bottom = bottom;
    }

    double getWidth() {
        return right - left;
    }

    double getHeight() {
        return top - bottom;
    }

    WorldBasisBoundsRect union(WorldBasisBoundsRect rect) {
        WorldBasisBoundsRect newRect = new WorldBasisBoundsRect();
        newRect.setLeft(Math.min(left, rect.getLeft()));
        newRect.setRight(Math.max(right, rect.getRight()));
        newRect.setTop(Math.max(top, rect.getTop()));
        newRect.setBottom(Math.min(bottom, rect.getBottom()));
        return newRect;
    }
}