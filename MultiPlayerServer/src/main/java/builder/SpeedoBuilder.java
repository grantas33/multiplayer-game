package builder;

import models.composite.MainCharacter;
import enumerators.SpaceshipType;
import factory.MainCharacterFactory;
import models.CharacterObj;
import models.composite.Bullet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SpeedoBuilder implements CharacterBuilderInterface {

    private MainCharacter mainCharacter;

    public SpeedoBuilder() {
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
        mainCharacter.setHeight(MainCharacterFactory.SPEEDO_HEIGHT);
        mainCharacter.setWidth(MainCharacterFactory.SPEEDO_WIDTH);
    }

    @Override
    public void buildHp() {
        mainCharacter.setHp(MainCharacterFactory.SPEEDO_HP);
        mainCharacter.setFullHp(MainCharacterFactory.SPEEDO_HP);
    }

    @Override
    public void buildData(CharacterObj data) {
        mainCharacter.setId(data.id);
        mainCharacter.setNickname(data.nickname);
        mainCharacter.setType(SpaceshipType.SPEEDO);
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
