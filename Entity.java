import java.awt.*;

interface IEntity {
    abstract void move(float dx, float dy);
    abstract void draw(Graphics g);
}

abstract public class Entity implements IEntity {
    private float x0 = 0;
    private float y0 = 0;
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

    private static PointInt getPixelCoordinates(PointF inCoords) {
        PointInt pixelCoord = new PointInt();
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
    }
}

class Circle extends Entity {
    private float radius;

    Circle(float xCenter, float yCenter, float radius) {
        super(xCenter, yCenter);
        this.radius = radius;
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

class PointF {
    private float xf;
    private float yf;

    public void setXf(float xf) {
        this.xf = xf;
    }

    public void setYf(float yf) {
        this.yf = yf;
    }

    public float getXf() {
        return xf;
    }

    public float getYf() {
        return yf;
    }
}

class PointInt {
    private int x;
    private int y;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
