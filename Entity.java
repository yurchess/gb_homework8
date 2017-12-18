import java.awt.*;

interface IEntity {
    abstract void move(float dx, float dy);
    abstract void draw(Graphics g);
}

abstract public class Entity implements IEntity {
    protected float x0 = 0;
    protected float y0 = 0;
    private float scale = 1;

    Entity(float x, float y) {
        x0 = x;
        y0 = y;
    }

    @Override
    public void move(float dx, float dy) {
        x0 += dx;
        y0 += dy;
    }

    protected Point<Integer> getPixelCoordinates(Point<Float> inCoords, int height) {
        Point<Integer> pixelCoord = new Point<Integer>();
        pixelCoord.setX(Math.round(inCoords.getX() * scale));
        pixelCoord.setY((height - 1) - Math.round(inCoords.getY() * scale));
        return pixelCoord;
    }
}

class Line extends Entity {
    private float x1;
    private float y1;

    Line(float xStart, float yStart, float xEnd, float yEnd) {
        super(xStart, yStart);
        x1 = xEnd;
        y1 = yEnd;
    }

    @Override
    public void move(float dx, float dy) {
        super.move(dx, dy);
        x1 += dx;
        y1 += dy;
    }

    @Override
    public void draw(Graphics g) {
        Point<Float> startPoint = new Point<Float>(x0, y0);
        Point<Float> endPoint = new Point<Float>(x1, y1);
        Point<Integer> startPixelPoint = getPixelCoordinates(startPoint, (int) g.getClipBounds().getHeight());
        Point<Integer> endPixelPoint = getPixelCoordinates(endPoint, (int) g.getClipBounds().getHeight());

        g.drawLine(startPixelPoint.getX(), startPixelPoint.getY(), endPixelPoint.getX(), endPixelPoint.getY());
    }
}

class Circle extends Entity {
    private float radius;

    Circle(float xCenter, float yCenter, float radius) {
        super(xCenter, yCenter);
        this.radius = radius;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
    }
}

class Arc extends Circle {
    private float angle0;
    private float angle1;

    Arc(float xCenter, float yCenter, float radius, float angle0, float angle1) {
        super(xCenter, yCenter, radius);
        this.angle0 = angle0;
        this.angle1 = angle1;
    }
}

class Point<T> {
    private T x;
    private T y;

    Point() {

    }

    Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public void setX(T x) {
        this.x = x;
    }

    public void setY(T y) {
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }
}