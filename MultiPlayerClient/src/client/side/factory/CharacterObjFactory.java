package client.side.factory;

import client.side.enumerators.SpaceshipType;
import client.side.models.CharacterObj;

import java.security.InvalidParameterException;

import static client.side.enumerators.SpaceshipType.*;

public class CharacterObjFactory {
    public static final int SPEEDO_WIDTH = 50;
    public static final int SPEEDO_HEIGHT = 50;

    public static final int TANK_WIDTH = 100;
    public static final int TANK_HEIGHT = 100;

    public static final int CRUISER_WIDTH = 50;
    public static final int CRUISER_HEIGHT = 150;


    public static CharacterObj createCharacterObj(SpaceshipType type, long id) {
        CharacterObj character;
        switch (type) {
            case SPEEDO:
                character = new CharacterObj(0, 0, SPEEDO, id, 100, SPEEDO_WIDTH, SPEEDO_HEIGHT);
                break;
            case TANK:
                character = new CharacterObj(0, 0, TANK, id, 300, TANK_WIDTH, TANK_HEIGHT);
                break;
            case CRUISER:
                character = new CharacterObj(0, 0, CRUISER, id, 200, CRUISER_WIDTH, CRUISER_HEIGHT);
                break;
            default:
                throw new InvalidParameterException("Spaceship type doesn't exist.");
        }
        return character;
    }
}
