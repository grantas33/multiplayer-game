package Decorator;

public abstract class CharacterDecorator extends CharacterComponent {
    private CharacterComponent innerComponent;

    public CharacterDecorator(CharacterComponent component) {
        this.innerComponent = component;
    }

    @Override
    public final String make() {
        return customMake(this.innerComponent.make());
    }

    public abstract String customMake(String innerDecor);
}
