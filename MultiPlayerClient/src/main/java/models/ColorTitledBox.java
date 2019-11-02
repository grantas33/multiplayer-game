package models;

import org.joml.Vector4f;

public class ColorTitledBox implements ColorTitledObject {

    private Box box;

    public ColorTitledBox(Box box) {
        this.box = box;
    }

    @Override
    public float getX() {
        return box.x;
    }

    @Override
    public float getY() {
        return box.y;
    }

    @Override
    public Vector4f getTextColor() {
        float luminosity = 0.299f * box.r + 0.587f * box.g + 0.114f * box.b;
        return luminosity > 0.5 ? new Vector4f(0, 0, 0, 1) : new Vector4f(1, 1, 1, 1);
    }

    @Override
    public String getTitle() {
        return box.title;
    }
}
