package template;

import factory.CharacterObjFactory;

public class CruiserBox extends BoxBuilder {

    @Override
    public void setBoxPosition() {
        box.x = 500;
        box.y = 200;
    }

    @Override
    public void setBoxColor() {
    }

    @Override
    public void setBoxSize() {
        box.h = CharacterObjFactory.CRUISER_HEIGHT;
        box.w = CharacterObjFactory.CRUISER_WIDTH;
    }

    @Override
    public final void setBoxSpecialProperties() {
        box.hp = 0;
        box.xp = 0;
        box.id = 0;
    }

    @Override
    public boolean needsCustomColor() {
        return false;
    }
}
