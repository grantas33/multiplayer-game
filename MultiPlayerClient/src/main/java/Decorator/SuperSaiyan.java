package Decorator;

public class SuperSaiyan extends CharacterDecorator {
    public SuperSaiyan(CharacterComponent component) {
        super(component);
    }

    @Override
    public String customMake(String innerDecorated) {
        return innerDecorated + "SuperSaiyan";
    }
}
