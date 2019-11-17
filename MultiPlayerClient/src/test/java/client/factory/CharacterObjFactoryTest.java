package client.factory;

import client.enumerators.SpaceshipType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CharacterObjFactoryTest {

    private static ArrayList<SpaceshipType> spaceshipTypeGenerator() {
        return new ArrayList<SpaceshipType>() {
            {
                add(SpaceshipType.SPEEDO);
                add(SpaceshipType.CRUISER);
                add(SpaceshipType.TANK);
            }
        };
    }

    @ParameterizedTest
    @MethodSource("spaceshipTypeGenerator")
    void testCreateCharacterObj(SpaceshipType type) {
        assertEquals(type, CharacterObjFactory.createCharacterObj(type, 0).type);
        assertEquals(10, CharacterObjFactory.createCharacterObj(type, 10).id);
    }
}