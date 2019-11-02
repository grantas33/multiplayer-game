package models;

import enumerators.SpaceshipType;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterObj implements Serializable {

    public int xVel;
    public int yVel;
    public SpaceshipType type;
    public String nickname;
    public long id;

    public List<Bullet> newBullets;
    public String decor;

    public CharacterObj() {
    }

    public CharacterObj(int xVel, int yVel,
                        SpaceshipType type, String nickname, long id) {
        this.xVel = xVel;
        this.yVel = yVel;
        this.type = type;
        this.nickname = nickname;
        this.id = id;
    }
}
