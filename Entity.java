interface IEntity {
    abstract void move(float dx, float dy);
}

public class Entity implements IEntity {
    private float x0 = 0;
    private float y0 = 0;

    Entity(float x, float y) {
        x0 = x;
        y0 = y;
    }

    @Override
    public void move(float dx, float dy) {
        x0 += dx;
        y0 += dy;
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
