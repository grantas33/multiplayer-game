package models;

import Decorator.CharacterComponent;
import enumerators.SpaceshipType;
import strategy.Strategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterObj extends CharacterComponent implements Serializable {

    public int xVel;
    public int yVel;

    public SpaceshipType type;
    public String nickname;
    public long id;

    public List<Bullet> newBullets;

    private Strategy strategy;
    private ArrayList<Strategy> strategies = new ArrayList<Strategy>();

    public String decor = "";

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

    public void addStrategy(Strategy s) {
        this.strategies.add(s);
        this.strategy = s;
    }

    public void selectActiveStrategy(int index) {
        try {
            this.strategy = this.strategies.get(index);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int speedIndicator() {
        return strategy.speedIndicator();
    }


    @Override
    public String make() {
        return decor;
    }
}
