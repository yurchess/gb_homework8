package GraphicEntity;

public class WorldBasisBoundsRect {
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

    public double getLeft() {
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

    public double getBottom() {
        return bottom;
    }

    void setBottom(double bottom) {
        this.bottom = bottom;
    }

    public double getWidth() {
        return right - left;
    }

    public double getHeight() {
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