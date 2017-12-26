package GraphicEntity;

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
