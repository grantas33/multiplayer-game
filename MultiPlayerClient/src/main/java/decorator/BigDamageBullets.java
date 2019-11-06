package decorator;

public class BigDamageBullets extends CharacterDecorator {
    public BigDamageBullets(CharacterComponent component) {
        super(component);
    }

    @Override
    public String customMake(String innerDecor) {
        if (!innerDecor.contains("BigDamageBullets")) {
            return innerDecor + "BigDamageBullets";
        }
        return innerDecor;
    }
}
