import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import factory.MainCharacterFactory;
import models.CharacterObj;
import service.Main;
import service.MainCharacter;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {

    private MainCharacterFactory mcFactoryMock = mock(MainCharacterFactory.class);

    private Main main = new Main(0, mcFactoryMock);

    @BeforeAll
    void setUp() {
        for(int i=0; i<4; i++) {
            when(
                    mcFactoryMock.createMainCharacter(
                            createCharacterObjWithId(i)
                    )
            ).thenReturn(
                    createMainCharacterWithId(i)
            );
        }
    }

    @BeforeEach
    void loadFullCharacters() {
        Vector<MainCharacter> fullCharacters = new Vector<>();
        for(int i=0; i<3; i++) {
            fullCharacters.add(
                    createMainCharacterWithId(i)
            );
        }
        main.setFullCharacters(fullCharacters);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 2})
    void itShouldUpdateCharacterStateIfCharacterAlreadyExists(long id) {
        main.includeCharacter(
                createCharacterObjWithId(id)
        );

        assertEquals(main.getFullCharacters().get((int)id).getxVel(), 1);
        assertEquals(main.getFullCharacters().size(), 3);
    }

    @ParameterizedTest
    @ValueSource(longs = {3, -1, 100})
    void itShouldAddNewCharacterIfCharacterDoesNotExist(long id) {
        CharacterObj characterObj = createCharacterObjWithId(id);
        main.includeCharacter(characterObj);

        verify(mcFactoryMock).createMainCharacter(characterObj);
        assertEquals(main.getFullCharacters().size(), 4);
    }

    @Test
    void itShouldRemoveCharacter() {
        main.removeCharacter(1);

        assertFalse(main.getFullCharacters().contains(
                createMainCharacterWithId(1)
        ));
        assertTrue(main.getFullCharacters().contains(
                createMainCharacterWithId(0)
        ));
        assertTrue(main.getFullCharacters().contains(
                createMainCharacterWithId(2)
        ));
    }

    MainCharacter createMainCharacterWithId(long id) {
        MainCharacter mc = new MainCharacter();
        mc.setId(id);
        mc.setxVel(0);
        mc.setyVel(0);
        return mc;
    }

    CharacterObj createCharacterObjWithId(long id) {
        CharacterObj characterObj = new CharacterObj();
        characterObj.id = id;
        characterObj.xVel = 1;
        characterObj.yVel = 1;
        return characterObj;
    }
}