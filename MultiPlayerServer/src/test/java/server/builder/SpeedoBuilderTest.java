package server.builder;

import server.enumerators.SpaceshipType;
import server.factory.MainCharacterFactory;
import server.models.CharacterObj;
import server.models.server.SpeedoBullet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import server.service.MainCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class SpeedoBuilderTest {
    private SpeedoBuilder speedoBuilder = spy(SpeedoBuilder.class);

    @AfterEach
    void cleanCharacter() {
        speedoBuilder.setMainCharacter(new MainCharacter());
    }


    @Test
    void itShouldBuildDimensions() {
        speedoBuilder.buildDimensions();
        MainCharacter character = speedoBuilder.getMainCharacter();
        assertEquals(character.getHeight(), MainCharacterFactory.SPEEDO_HEIGHT);
        assertEquals(character.getWidth(), MainCharacterFactory.SPEEDO_WIDTH);
    }

    @Test
    void itShouldBuildHp() {
        speedoBuilder.buildHp();
        MainCharacter character = speedoBuilder.getMainCharacter();
        assertEquals(character.getHp(), MainCharacterFactory.SPEEDO_HP);
        assertEquals(character.getFullHp(), MainCharacterFactory.SPEEDO_HP);
    }

    @Test
    void itShouldBuildData() {
        CharacterObj characterObj = createCharacterObjWithBullets(createBulletList());
        speedoBuilder.buildData(characterObj);
        MainCharacter character = speedoBuilder.getMainCharacter();
        assertEquals(character.getID(), 12);
        assertEquals(character.getType(), SpaceshipType.SPEEDO);
        assertEquals(character.getBullets(), createServerBulletList());
    }

    @Test
    void itShouldBuildColor() {
        Random mockedRandom = mock(Random.class);
        when(mockedRandom.nextFloat()).thenReturn(0.25f, 0.70f, 0.99f);

        when(speedoBuilder.makeRandom()).thenReturn(mockedRandom);
        speedoBuilder.buildColor();
        MainCharacter character = speedoBuilder.getMainCharacter();

        assertEquals(character.getR(), 0.25f);
        assertEquals(character.getB(), 0.70f);
        assertEquals(character.getG(), 0.99f);

    }

    List<server.models.Bullet> createBulletList() {
        List<server.models.Bullet> newBullets = new ArrayList<>();
        newBullets.add(new server.models.Bullet(0, 0, 2, 2, 5));
        return newBullets;
    }

    List<SpeedoBullet> createServerBulletList() {
        List<SpeedoBullet> newBullets = new ArrayList<>();
        newBullets.add(new SpeedoBullet(new server.models.Bullet(0, 0, 2, 2, 5), 0, 0, 0));
        return newBullets;
    }

    CharacterObj createCharacterObjWithBullets(List<server.models.Bullet> bullets) {
        CharacterObj characterObj = new CharacterObj(1, 1, SpaceshipType.SPEEDO, 12);
        characterObj.newBullets = bullets;
        return characterObj;
    }
}
