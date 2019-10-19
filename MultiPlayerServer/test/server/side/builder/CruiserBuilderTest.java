package server.side.builder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import server.side.MainCharacter;
import server.side.enumerators.SpaceshipType;
import server.side.factory.MainCharacterFactory;
import server.side.models.CharacterObj;
import server.side.models.server.Bullet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CruiserBuilderTest {

    private CruiserBuilder cruiserBuilder = spy(CruiserBuilder.class);

    @Test
    void buildDimensions() {
        cruiserBuilder.buildDimensions();
        MainCharacter character = cruiserBuilder.getMainCharacter();
        assertEquals(character.getHeight(), MainCharacterFactory.CRUISER_HEIGHT);
        assertEquals(character.getWidth(), MainCharacterFactory.CRUISER_WIDTH);
    }

    @Test
    void buildHp() {
        cruiserBuilder.buildHp();
        MainCharacter character = cruiserBuilder.getMainCharacter();
        assertEquals(character.getHp(), MainCharacterFactory.CRUISER_HP);
        assertEquals(character.getFullHp(), MainCharacterFactory.CRUISER_HP);
    }

    @Test
    void buildData() {
        CharacterObj characterObj = createCharacterObjWithBullets(createBulletList());
        cruiserBuilder.buildData(characterObj);
        MainCharacter character = cruiserBuilder.getMainCharacter();
        assertEquals(character.getID(), 12);
        assertEquals(character.getType(), SpaceshipType.CRUISER);
        assertEquals(character.getBullets(), createServerBulletList());
    }

    @Test
    void buildColor() {
        Random mockedRandom = mock(Random.class);
        when(mockedRandom.nextFloat()).thenReturn(0.25f, 0.70f, 0.99f);

        when(cruiserBuilder.makeRandom()).thenReturn(mockedRandom);
        cruiserBuilder.buildColor();
        MainCharacter character = cruiserBuilder.getMainCharacter();

        assertEquals(character.getR(), 0.25f);
        assertEquals(character.getB(), 0.70f);
        assertEquals(character.getG(), 0.99f);

    }

    List<server.side.models.Bullet> createBulletList() {
        List<server.side.models.Bullet> newBullets = new ArrayList<>();
        newBullets.add(new server.side.models.Bullet(0, 0, 2, 2, 5));
        return newBullets;
    }

    List<Bullet> createServerBulletList() {
        List<Bullet> newBullets = new ArrayList<>();
        newBullets.add(new Bullet(new server.side.models.Bullet(0, 0, 2, 2, 5), 0, 0, 0));
        return newBullets;
    }

    CharacterObj createCharacterObjWithBullets(List<server.side.models.Bullet> bullets) {
        CharacterObj characterObj = new CharacterObj(1, 1, SpaceshipType.CRUISER, 12);
        characterObj.newBullets = bullets;
        return characterObj;
    }
}