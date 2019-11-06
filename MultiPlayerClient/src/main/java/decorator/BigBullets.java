package decorator;

public class BigBullets extends CharacterDecorator {
    public BigBullets(CharacterComponent component) {
        super(component);
    }

    @Override
    public String customMake(String innerDecor) {
        if (!innerDecor.contains("BigBullets")) {
            return innerDecor + "BigBullets";
        }
        return innerDecor;
    }
}
