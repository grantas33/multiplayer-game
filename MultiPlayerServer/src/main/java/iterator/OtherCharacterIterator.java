package iterator;

import models.composite.MainCharacter;

import java.util.Iterator;
import java.util.List;

public class OtherCharacterIterator implements Iterator<MainCharacter> {

    private List<MainCharacter> list;
    private long currentCharId;
    private int currentIndex;

    public OtherCharacterIterator(List<MainCharacter> list, long currentCharId) {
        this.list = list;
        this.currentCharId = currentCharId;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        while (currentIndex < list.size()) {
            if (list.get(currentIndex).getID() != currentCharId) {
                return true;
            } else {
                currentIndex++;
            }
        }
        return false;
    }

    @Override
    public MainCharacter next() {
        return list.get(currentIndex++);
    }
}
