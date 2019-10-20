package server.side.factory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import server.side.MainCharacter;
import server.side.builder.CharacterBuilderInterface;
import server.side.builder.CruiserBuilder;
import server.side.builder.SpeedoBuilder;
import server.side.builder.TankBuilder;
import server.side.enumerators.SpaceshipType;
import server.side.models.CharacterObj;

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
    void createMainCharacter(SpaceshipType type) {
        CharacterObj characterObj = createCharacterByType(type);
        MainCharacter mc = mcFactory.createMainCharacter(characterObj);

        switch (type) {
            case SPEEDO:
                verifyForBuilder(speedoBuilderMock, characterObj);
                assertEquals(mc, createMainCharacterByType(SpaceshipType.SPEEDO));
                break;
            case TANK:
                verifyForBuilder(tankBuilderMock, characterObj);
                assertEquals(mc, createMainCharacterByType(SpaceshipType.TANK));
                break;
            case CRUISER:
                verifyForBuilder(cruiserBuilderMock, characterObj);
                assertEquals(mc, createMainCharacterByType(SpaceshipType.CRUISER));
        }
    }

    private void verifyForBuilder(CharacterBuilderInterface builder, CharacterObj characterData) {
        verify(builder).buildDimensions();
        verify(builder).buildHp();
        verify(builder).buildData(characterData);
        verify(builder).buildColor();
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