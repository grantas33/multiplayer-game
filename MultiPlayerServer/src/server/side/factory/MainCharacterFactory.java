package server.side.factory;

import server.side.MainCharacter;
import server.side.builder.CharacterBuilderInterface;
import server.side.builder.CruiserBuilder;
import server.side.builder.SpeedoBuilder;
import server.side.builder.TankBuilder;
import server.side.enumerators.SpaceshipType;
import server.side.models.CharacterObj;

import java.security.InvalidParameterException;

import static server.side.enumerators.SpaceshipType.*;

public class MainCharacterFactory {

    public static final int SPEEDO_WIDTH = 50;
    public static final int SPEEDO_HEIGHT = 50;
    public static final int SPEEDO_HP = 100;

    public static final int TANK_WIDTH = 100;
    public static final int TANK_HEIGHT = 100;
    public static final int TANK_HP = 300;

    public static final int CRUISER_WIDTH = 50;
    public static final int CRUISER_HEIGHT = 150;
    public static final int CRUISER_HP = 200;


    public static MainCharacter createMainCharacter(CharacterObj data) {

        CharacterBuilderInterface characterBuilder;
        switch (data.type) {
            case SPEEDO:
                characterBuilder = new SpeedoBuilder();
                break;
            case TANK:
                characterBuilder = new TankBuilder();
                break;
            case CRUISER:
                characterBuilder = new CruiserBuilder();
                break;
            default:
                throw new InvalidParameterException("Spaceship type doesn't exist.");
        }

        characterBuilder.buildDimensions();
        characterBuilder.buildHp();
        characterBuilder.buildData(data);
        characterBuilder.buildColor();

        return characterBuilder.getMainCharacter();
    }
}
