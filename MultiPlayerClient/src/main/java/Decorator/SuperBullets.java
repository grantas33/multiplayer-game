package Decorator;

public class SuperBullets extends CharacterDecorator {
    public SuperBullets(CharacterComponent component) {
        super(component);
    }

    @Override
    public String customMake(String innerDecorated) {
        return innerDecorated + "SuperBullets";
    }
}
