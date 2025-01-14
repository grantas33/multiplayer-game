package builder;

import service.MainCharacter;
import enumerators.SpaceshipType;
import factory.MainCharacterFactory;
import models.CharacterObj;
import models.server.Bullet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TankBuilder implements CharacterBuilderInterface {

    private MainCharacter mainCharacter;

    public TankBuilder() {
        this.mainCharacter = new MainCharacter();
    }

    @Override
    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }

    @Override
    public void setMainCharacter(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
    }

    @Override
    public void buildDimensions() {
        mainCharacter.setHeight(MainCharacterFactory.TANK_HEIGHT);
        mainCharacter.setWidth(MainCharacterFactory.TANK_WIDTH);
    }

    @Override
    public void buildHp() {
        mainCharacter.setHp(MainCharacterFactory.TANK_HP);
        mainCharacter.setFullHp(MainCharacterFactory.TANK_HP);
    }

    @Override
    public void buildData(CharacterObj data) {
        mainCharacter.setId(data.id);
        mainCharacter.setNickname(data.nickname);
        mainCharacter.setType(SpaceshipType.TANK);
        mainCharacter.setBullets(Collections.synchronizedList(new ArrayList<Bullet>()));
        mainCharacter.addBullets(data.newBullets);
    }

    @Override
    public void buildColor() {
        Random randomFloat = new Random(System.currentTimeMillis());
        mainCharacter.setR(randomFloat.nextFloat());
        mainCharacter.setB(randomFloat.nextFloat());
        mainCharacter.setG(randomFloat.nextFloat());
    }
}
