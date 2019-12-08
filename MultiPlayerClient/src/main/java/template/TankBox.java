package template;

import factory.CharacterObjFactory;

public final class TankBox extends CruiserBox {
    @Override
    public void setBoxPosition() {
        box.x = 300;
        box.y = 200;
    }

    @Override
    public void setBoxColor() {
    }

    @Override
    public void setBoxSize() {
        box.h = CharacterObjFactory.TANK_HEIGHT;
        box.w = CharacterObjFactory.TANK_WIDTH;
    }
}
