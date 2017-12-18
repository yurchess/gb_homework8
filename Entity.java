public class Entity implements IEntity {
    int x = 0;
    int y = 0;

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
}

interface IEntity {
    abstract void move(int dx, int dy);
}
