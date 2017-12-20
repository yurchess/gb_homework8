import java.awt.*;
import java.util.ArrayList;

interface IEntity {
    abstract void move(float dx, float dy);
    abstract void draw(Graphics g);
    abstract WorldBasisBoundsRect getBoundsRect();
}

abstract public class Entity implements IEntity {
    protected float x0 = 0;
    protected float y0 = 0;
    protected float scale = 1;

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

    public void setScale(float scale) {
        this.scale = scale;
    }
}

class Entities extends ArrayList<Entity> implements IEntity {
    private float scale = 1;

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        for (Entity entity : this)
            entity.setScale(scale);
    }

    @Override
    public void move(float dx, float dy) {
        for (Entity entity : this) {
            entity.move(dx, dy);
        }
    }

    @Override
    public void draw(Graphics g) {
        for (Entity entity : this)
            entity.draw(g);
//        g.drawRect((int) (getBoundsRect().getX() * scale), (int) (g.getClipBounds().getHeight() - getBoundsRect().getY() * scale), (int) (getBoundsRect().getWidth() * scale), (int) (getBoundsRect().getHeight() * scale));
    }

    @Override
    public WorldBasisBoundsRect getBoundsRect() {
        if (this.get(0) == null)
            return null;
        else {
            WorldBasisBoundsRect boundsRect = get(0).getBoundsRect();
            for (Entity entity : this)
                boundsRect = boundsRect.union(entity.getBoundsRect());
            return boundsRect;
        }
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
    protected float radius;

    Circle(float xCenter, float yCenter, float radius) {
        super(xCenter, yCenter);
        this.radius = radius;
    }

    @Override
    public void draw(Graphics g) {
        Point<Float> centerPoint = new Point<Float>(x0, y0);
        Point<Integer> centerPointPixel = getPixelCoordinates(centerPoint, (int) g.getClipBounds().getHeight());
        int i = Math.round(centerPointPixel.getX() - radius * scale);
        int i1 = Math.round(centerPointPixel.getY() - radius * scale);
        int i2 = Math.round(2 * radius * scale);
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
    private float startAngle;
    private float endAngle;

    Arc(float xCenter, float yCenter, float radius, float startAngle, float endAngle) {
        super(xCenter, yCenter, radius);
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    @Override
    public void draw(Graphics g) {
        Point<Float> centerPoint = new Point<Float>(x0, y0);
        Point<Integer> centerPointPixel = getPixelCoordinates(centerPoint, (int) g.getClipBounds().getHeight());
        int i = Math.round(centerPointPixel.getX() - radius * scale);
        int i1 = Math.round(centerPointPixel.getY() - radius * scale);
        int i2 = Math.round(2 * radius * scale);
        int i3 = i2;
        g.drawArc(i, i1, i2, i3, (int) startAngle, (int) (endAngle - startAngle));
//        g.drawRect((int) (getBoundsRect().getX() * scale), (int) (g.getClipBounds().getHeight() - getBoundsRect().getY() * scale), (int) (getBoundsRect().getWidth() * scale), (int) (getBoundsRect().getHeight() * scale));
    }

    private boolean isAngleInRange(float angle, float startAngle, float endAngle) {
        if (endAngle < startAngle)
            endAngle += 2 * Math.PI;

        if (angle >= startAngle && angle <= endAngle)
            return true;

        angle += 2 * Math.PI;
        if (angle >= startAngle && angle <= endAngle)
            return true;

        return false;
    }

    @Override
    public WorldBasisBoundsRect getBoundsRect() {
        float left = 0;
        float bottom = 0;
        float right = 0;
        float top = 0;
        float startAngleRad = (float) Math.toRadians(startAngle);
        float endAngleRad = (float) Math.toRadians(endAngle);
//        Left
        if (isAngleInRange((float) Math.toDegrees(Math.PI) , startAngle, endAngle))
            left = x0 - radius;
        else
            left = (float) Math.min((x0 + radius*Math.cos(startAngleRad)), (x0 + radius*Math.cos(endAngleRad)));
//        Top
        if (isAngleInRange((float) Math.toDegrees(Math.PI/2), startAngle, endAngle))
            top = y0 + radius;
        else
            top = (float) Math.max((y0 + radius*Math.sin(startAngleRad)), (y0 + radius*Math.sin(endAngleRad)));
//        Right
        if (isAngleInRange(0, startAngle, endAngle))
            right = x0 + radius;
        else
            right = (float) Math.max((x0 + radius*Math.cos(startAngleRad)), (x0 + radius*Math.cos(endAngleRad)));
//        Bottom
        if (isAngleInRange((float) Math.toDegrees(3*Math.PI/2), startAngle, endAngle))
            bottom = y0 - radius;
        else
            bottom = (float) Math.min((y0 + radius*Math.sin(startAngleRad)), (y0 + radius*Math.sin(endAngleRad)));
        
        return new WorldBasisBoundsRect(left, right, top, bottom);
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

class WorldBasisBoundsRect {
    private float left;
    private float right;
    private float top;
    private float bottom;

    WorldBasisBoundsRect() {}

    WorldBasisBoundsRect(float left, float right, float top, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getWidth() {
        return right - left;
    }

    public float getHeight() {
        return top - bottom;
    }

    public WorldBasisBoundsRect union(WorldBasisBoundsRect rect) {
        WorldBasisBoundsRect newRect = new WorldBasisBoundsRect();
        newRect.setLeft(Math.min(left, rect.getLeft()));
        newRect.setRight(Math.max(right, rect.getRight()));
        newRect.setTop(Math.max(top, rect.getTop()));
        newRect.setBottom(Math.min(bottom, rect.getBottom()));
        return newRect;
    }
}