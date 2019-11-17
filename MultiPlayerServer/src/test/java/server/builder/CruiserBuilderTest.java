package server.builder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import server.service.MainCharacter;
import server.enumerators.SpaceshipType;
import server.factory.MainCharacterFactory;
import server.models.CharacterObj;
import server.models.server.Bullet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CruiserBuilderTest {

    private CruiserBuilder cruiserBuilder = spy(CruiserBuilder.class);

    @AfterEach
    void cleanCharacter() {
        cruiserBuilder.setMainCharacter(new MainCharacter());
    }


    @Test
    void itShouldBuildDimensions() {
        cruiserBuilder.buildDimensions();
        MainCharacter character = cruiserBuilder.getMainCharacter();
        assertEquals(character.getHeight(), MainCharacterFactory.CRUISER_HEIGHT);
        assertEquals(character.getWidth(), MainCharacterFactory.CRUISER_WIDTH);
    }

    @Test
    void itShouldBuildHp() {
        cruiserBuilder.buildHp();
        MainCharacter character = cruiserBuilder.getMainCharacter();
        assertEquals(character.getHp(), MainCharacterFactory.CRUISER_HP);
        assertEquals(character.getFullHp(), MainCharacterFactory.CRUISER_HP);
    }

    @Test
    void itShouldBuildData() {
        CharacterObj characterObj = createCharacterObjWithBullets(createBulletList());
        cruiserBuilder.buildData(characterObj);
        MainCharacter character = cruiserBuilder.getMainCharacter();
        assertEquals(character.getID(), 12);
        assertEquals(character.getType(), SpaceshipType.CRUISER);
        assertEquals(character.getBullets(), createServerBulletList());
    }

    @Test
    void itShouldBuildColor() {
        Random mockedRandom = mock(Random.class);
        when(mockedRandom.nextFloat()).thenReturn(0.25f, 0.70f, 0.99f);

        when(cruiserBuilder.makeRandom()).thenReturn(mockedRandom);
        cruiserBuilder.buildColor();
        MainCharacter character = cruiserBuilder.getMainCharacter();

        assertEquals(character.getR(), 0.25f);
        assertEquals(character.getB(), 0.70f);
        assertEquals(character.getG(), 0.99f);

    }

    List<server.models.Bullet> createBulletList() {
        List<server.models.Bullet> newBullets = new ArrayList<>();
        newBullets.add(new server.models.Bullet(0, 0, 2, 2, 5));
        return newBullets;
    }

    List<Bullet> createServerBulletList() {
        List<Bullet> newBullets = new ArrayList<>();
        newBullets.add(new Bullet(new server.models.Bullet(0, 0, 2, 2, 5), 0, 0, 0));
        return newBullets;
    }

    CharacterObj createCharacterObjWithBullets(List<server.models.Bullet> bullets) {
        CharacterObj characterObj = new CharacterObj(1, 1, SpaceshipType.CRUISER, 12);
        characterObj.newBullets = bullets;
        return characterObj;
    }
}