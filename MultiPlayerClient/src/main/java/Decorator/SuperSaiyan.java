package Decorator;

public class SuperSaiyan extends CharacterDecorator {
    public SuperSaiyan(CharacterComponent component) {
        super(component);
    }

    @Override
    public String customMake(String innerDecor) {
        return innerDecor + "SuperSaiyan";
    }
}
