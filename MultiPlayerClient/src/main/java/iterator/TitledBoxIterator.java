package iterator;

import models.Box;

import java.util.Iterator;
import java.util.List;

public class TitledBoxIterator implements Iterator<Box> {

    private List<Box> list;
    private int currentIndex;

    public TitledBoxIterator(List<Box> list) {
        this.list = list;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        while (currentIndex < list.size()) {
            if (list.get(currentIndex).title != null) {
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
