package factory;

import enumerators.SpaceshipType;
import models.gameObjectsComposite.CharacterObj;

import java.security.InvalidParameterException;

import static enumerators.SpaceshipType.*;

public class CharacterObjFactory {
    public static final int SPEEDO_WIDTH = 50;
    public static final int SPEEDO_HEIGHT = 50;

    public static final int TANK_WIDTH = 100;
    public static final int TANK_HEIGHT = 100;

    public static final int CRUISER_WIDTH = 50;
    public static final int CRUISER_HEIGHT = 150;


    public static CharacterObj createCharacterObj(SpaceshipType type, String nickname, long id) {
        CharacterObj character;
        switch (type) {
            case SPEEDO:
                character = new CharacterObj(0, 0, SPEEDO, nickname, id);
                break;
            case TANK:
                character = new CharacterObj(0, 0, TANK, nickname, id);
                break;
            case CRUISER:
                character = new CharacterObj(0, 0, CRUISER, nickname, id);
                break;
            default:
                throw new InvalidParameterException("Spaceship type doesn't exist.");
        }
        return character;
    }
}
