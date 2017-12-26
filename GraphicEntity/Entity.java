package GraphicEntity;

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
