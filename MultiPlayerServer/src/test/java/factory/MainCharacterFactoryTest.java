package factory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import models.composite.MainCharacter;
import builder.CharacterBuilderInterface;
import builder.CruiserBuilder;
import builder.SpeedoBuilder;
import builder.TankBuilder;
import enumerators.SpaceshipType;
import models.CharacterObj;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainCharacterFactoryTest {

    private SpeedoBuilder speedoBuilderMock = mock(SpeedoBuilder.class);
    private TankBuilder tankBuilderMock = mock(TankBuilder.class);
    private CruiserBuilder cruiserBuilderMock = mock(CruiserBuilder.class);

    private MainCharacterFactory mcFactory = new MainCharacterFactory(
           speedoBuilderMock,
           tankBuilderMock,
           cruiserBuilderMock
    );

    @BeforeAll
    void setup() {
        when(speedoBuilderMock.getMainCharacter()).thenReturn(createMainCharacterByType(SpaceshipType.SPEEDO));
        when(tankBuilderMock.getMainCharacter()).thenReturn(createMainCharacterByType(SpaceshipType.TANK));
        when(cruiserBuilderMock.getMainCharacter()).thenReturn(createMainCharacterByType(SpaceshipType.CRUISER));
    }

    @ParameterizedTest
    @EnumSource(SpaceshipType.class)
    void itShouldCreateMainCharacter(SpaceshipType type) {
        CharacterBuilderInterface builder = getBuilderForType(type);
        CharacterObj characterObj = createCharacterByType(type);
        MainCharacter mc = mcFactory.createMainCharacter(characterObj);

        verify(builder).buildDimensions();
        verify(builder).buildHp();
        verify(builder).buildData(characterObj);
        verify(builder).buildColor();
        assertEquals(mc, createMainCharacterByType(type));

    }

    private CharacterBuilderInterface getBuilderForType(SpaceshipType type) {
        switch (type) {
            case SPEEDO:
                return speedoBuilderMock;
            case TANK:
                return tankBuilderMock;
            case CRUISER:
                return cruiserBuilderMock;
            default:
                return null;
        }
    }

    private CharacterObj createCharacterByType(SpaceshipType type) {
        return new CharacterObj(0, 0, type, 0);
    }

    private MainCharacter createMainCharacterByType(SpaceshipType type) {
        MainCharacter character = new MainCharacter();
        character.setType(type);
        return character;
    }
}