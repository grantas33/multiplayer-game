package Decorator;

public class SuperSaiyan extends CharacterDecorator {
    public SuperSaiyan(CharacterComponent component) {
        super(component);
    }

    @Override
    public String customMake(String innerDecor) {
        if (!innerDecor.contains("SuperSaiyan")) {
            return innerDecor + "SuperSaiyan";
        }
        return innerDecor;
    }
}
