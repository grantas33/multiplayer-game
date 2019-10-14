package server.side.builder;

import server.side.MainCharacter;
import server.side.enumerators.SpaceshipType;
import server.side.factory.MainCharacterFactory;
import server.side.models.CharacterObj;
import server.side.models.server.Bullet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CruiserBuilder implements CharacterBuilderInterface {

    private MainCharacter mainCharacter;

    public CruiserBuilder() {
        this.mainCharacter = new MainCharacter();
    }

    @Override
    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }

    @Override
    public void buildDimensions() {
        mainCharacter.setHeight(MainCharacterFactory.CRUISER_HEIGHT);
        mainCharacter.setWidth(MainCharacterFactory.CRUISER_WIDTH);
    }

    @Override
    public void buildHp() {
        mainCharacter.setHp(MainCharacterFactory.CRUISER_HP);
        mainCharacter.setFullHp(MainCharacterFactory.CRUISER_HP);
    }

    @Override
    public void buildData(CharacterObj data) {
        mainCharacter.setId(data.id);
        mainCharacter.setType(SpaceshipType.CRUISER);
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
