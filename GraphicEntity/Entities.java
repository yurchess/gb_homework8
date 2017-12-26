package GraphicEntity;

import java.awt.*;
import java.util.ArrayList;

public class Entities extends Entity {
    ArrayList<Entity> entities = new ArrayList<>();

    public Entities() {

    }

    public double getScale() {
        return scale;
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
        entities.forEach(entity -> entity.setScale(scale));
    }

    @Override
    public void move(double dx, double dy) {
        entities.forEach(entity -> entity.move(dx, dy));
    }

    @Override
    public void draw(Graphics g) {
        entities.forEach(entity -> entity.draw(g));
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
