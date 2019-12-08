package iterator;

import models.composite.Object2D;
import models.Box;
import service.LogicHelper;

import java.util.Iterator;
import java.util.List;

public class CollidedBoxIterator implements Iterator<Box> {

    private List<Box> list;
    private Object2D objectToCollideWith;
    private int currentIndex;

    public CollidedBoxIterator(List<Box> list, Object2D objectToCollideWith) {
        this.list = list;
        this.objectToCollideWith = objectToCollideWith;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        while (currentIndex < list.size()) {
            Box curr = list.get(currentIndex);
            if (LogicHelper.collision(
                    objectToCollideWith.getX(),
                    objectToCollideWith.getY(),
                    objectToCollideWith.getX() + objectToCollideWith.getWidth(),
                    objectToCollideWith.getY() + objectToCollideWith.getHeight(),
                    curr.x, curr.y, curr.x + curr.w, curr.y + curr.h)) {
                return true;
            } else {
                currentIndex++;
            }
        }
        return false;
    }

    @Override
    public Box next() {
        return list.get(currentIndex++);
    }
}
