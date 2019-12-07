package memento;

public class Memento {

    String decor;

    public Memento(String newDecor) {
        this.decor = newDecor;
    }

    public void getDecor(Originator originator) {
        originator.setDecor(this.decor);
    }
}
