package server.side.factory;

import server.side.MainCharacter;
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
        MainCharacter character;
        switch (data.type) {
            case SPEEDO:
                character = new MainCharacter(SPEEDO_WIDTH, SPEEDO_HEIGHT, SPEEDO_HP, data.id, data.newBullets);
                break;
            case TANK:
                character = new MainCharacter(TANK_WIDTH, TANK_HEIGHT, TANK_HP, data.id, data.newBullets);
                break;
            case CRUISER:
                character = new MainCharacter(CRUISER_WIDTH, CRUISER_HEIGHT, CRUISER_HP, data.id, data.newBullets);
                break;
            default:
                throw new InvalidParameterException("Spaceship type doesn't exist.");
        }
        return character;
    }
}
