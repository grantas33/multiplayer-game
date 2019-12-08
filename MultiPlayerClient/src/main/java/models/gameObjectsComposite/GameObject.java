package models.gameObjectsComposite;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(Bullet.class)
interface GameObject {
    float getX();
    float getY();
    int getWidth();
    int getHeight();
}
