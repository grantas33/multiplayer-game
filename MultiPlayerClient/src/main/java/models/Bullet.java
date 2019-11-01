package models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Bullet {

    public float x;
    public float y;
    public float k;
    public float c;
    public float pn;
    public String decorated;

    public Bullet() {
    }

    public Bullet(float x, float y, float k,
                  float c, float pn, String decorated) {
        this.x = x;
        this.y = y;
        this.k = k;
        this.c = c;
        this.pn = pn;
        this.decorated = decorated;
    }
}
