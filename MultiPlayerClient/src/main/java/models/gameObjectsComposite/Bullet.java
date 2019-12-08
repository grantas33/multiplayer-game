package models.gameObjectsComposite;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Bullet extends XmlAdapter<Bullet, GameObject> implements GameObject {

    public float x;
    public float y;
    public float k;
    public float c;
    public float pn;
    public String decor;

    public Bullet() {
    }

    @Override
    public GameObject unmarshal(Bullet bullet) {
        return bullet;
    }

    @Override
    public Bullet marshal(GameObject gameObject) {
        return (Bullet) gameObject;
    }

    public Bullet(float x, float y, float k, float c, float pn, String decor) {
        this.x = x;
        this.y = y;
        this.k = k;
        this.c = c;
        this.pn = pn;
        this.decor = decor;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
