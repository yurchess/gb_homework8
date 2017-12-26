package GraphicEntity;

import java.awt.*;

interface IEntity {
    abstract void move(double dx, double dy);
    abstract void draw(Graphics g);
    abstract WorldBasisBoundsRect getBoundsRect();
}
