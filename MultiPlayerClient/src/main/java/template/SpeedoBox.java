package template;

import factory.CharacterObjFactory;

public final class SpeedoBox extends CruiserBox {
    @Override
    public boolean needsCustomColor() {
        return true;
    }

    @Override
    public void setBoxColor() {
        box.r = 255;
        box.g = 255;
        box.b = 255;
    }

    @Override
    public void setBoxPosition() {
        box.x = 100;
        box.y = 200;
    }

    @Override
    public void setBoxSize() {
        box.h = CharacterObjFactory.SPEEDO_HEIGHT;
        box.w = CharacterObjFactory.SPEEDO_WIDTH;
    }
}
