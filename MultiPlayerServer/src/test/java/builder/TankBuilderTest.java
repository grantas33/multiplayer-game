package builder;

import enumerators.SpaceshipType;
import factory.MainCharacterFactory;
import models.server.Bullet;
import models.CharacterObj;
import models.server.TankBullet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.MainCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TankBuilderTest {
    private TankBuilder tankBuilder = spy(TankBuilder.class);

    @AfterEach
    void cleanCharacter() {
        tankBuilder.setMainCharacter(new MainCharacter());
    }


    @Test
    void itShouldBuildDimensions() {
        tankBuilder.buildDimensions();
        MainCharacter character = tankBuilder.getMainCharacter();
        assertEquals(character.getHeight(), MainCharacterFactory.TANK_HEIGHT);
        assertEquals(character.getWidth(), MainCharacterFactory.TANK_WIDTH);
    }

    @Test
    void itShouldBuildHp() {
        tankBuilder.buildHp();
        MainCharacter character = tankBuilder.getMainCharacter();
        assertEquals(character.getHp(), MainCharacterFactory.TANK_HP);
        assertEquals(character.getFullHp(), MainCharacterFactory.TANK_HP);
    }

    @Test
    void itShouldBuildData() {
        CharacterObj characterObj = createCharacterObjWithBullets(createBulletList());
        tankBuilder.buildData(characterObj);
        MainCharacter character = tankBuilder.getMainCharacter();
        assertEquals(character.getID(), 12);
        assertEquals(character.getType(), SpaceshipType.TANK);
        assertEquals(character.getBullets(), createServerBulletList());
    }

    @Test
    void itShouldBuildColor() {
        Random mockedRandom = mock(Random.class);
        when(mockedRandom.nextFloat()).thenReturn(0.25f, 0.70f, 0.99f);

        when(tankBuilder.makeRandom()).thenReturn(mockedRandom);
        tankBuilder.buildColor();
        MainCharacter character = tankBuilder.getMainCharacter();

        assertEquals(character.getR(), 0.25f);
        assertEquals(character.getB(), 0.70f);
        assertEquals(character.getG(), 0.99f);

    }

    List<models.Bullet> createBulletList() {
        List<models.Bullet> newBullets = new ArrayList<>();
        newBullets.add(new models.Bullet(0, 0, 2, 2, 5));
        return newBullets;
    }

    List<TankBullet> createServerBulletList() {
        List<TankBullet> newBullets = new ArrayList<>();
        newBullets.add(new TankBullet(new models.Bullet(0, 0, 2, 2, 5), 0, 0, 0));
        return newBullets;
    }

    CharacterObj createCharacterObjWithBullets(List<models.Bullet> bullets) {
        CharacterObj characterObj = new CharacterObj(1, 1, SpaceshipType.TANK, 12);
        characterObj.newBullets = bullets;
        return characterObj;
    }
}
