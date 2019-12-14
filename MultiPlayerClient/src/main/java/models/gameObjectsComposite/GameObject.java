package models.gameObjectsComposite;

import visitor.AcceptsServerMessageVisit;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(Bullet.class)
public interface GameObject extends AcceptsServerMessageVisit {
    float getX();
    float getY();
    int getWidth();
    int getHeight();
}
