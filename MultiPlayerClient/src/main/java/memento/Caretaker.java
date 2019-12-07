package memento;

import java.util.ArrayList;

public class Caretaker {
    ArrayList<Memento> mementos;

    public Caretaker() {
        this.mementos = new ArrayList<Memento>();
    }

    public void addMemento(Memento m) {
        this.mementos.add(m);
    }

    public void removeMemento(int index) {
        this.mementos.remove(index);
    }

    public Memento getMemento(int index) {
        return this.mementos.get(index);
    }

    public int mementosCount() {
        return this.mementos.size();
    }
}
