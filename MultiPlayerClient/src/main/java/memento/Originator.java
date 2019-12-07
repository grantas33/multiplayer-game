package memento;

public class Originator {
    String decor;

    public Originator() {
        this.decor = "";
    }

    public void restoreDecor(Memento memento) {
        memento.getDecor(this);
    }

    public String getDecor() {
        return decor;
    }

    public void setDecor(String newDecor) {
        this.decor = newDecor;
    }

    public Memento saveDecorInMementoAndReturnMemento() {
        return new Memento(this.decor);
    }
}
