package Decorator;

public class SuperBullets extends CharacterDecorator {
    public SuperBullets(CharacterComponent component) {
        super(component);
    }

    @Override
    public String customMake(String innerDecor) {
        if (!innerDecor.contains("SuperBullets")) {
            return innerDecor + "SuperBullets";
        }
        return innerDecor;
    }
}
