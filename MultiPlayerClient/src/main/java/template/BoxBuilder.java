package template;

import models.Box;

abstract class BoxBuilder {

    Box box;

    public Box build() {
        box = new Box();
        if (needsCustomColor()) {
            setBoxColor();
        } else {
            box.r = 100;
            box.g = 100;
            box.b = 100;
        }
        setBoxPosition();
        setBoxSize();
        setBoxSpecialProperties();
        return box;
    }

    public abstract void setBoxPosition();

    public abstract void setBoxColor();

    public abstract void setBoxSize();

    public abstract void setBoxSpecialProperties();

    public abstract boolean needsCustomColor();
}
