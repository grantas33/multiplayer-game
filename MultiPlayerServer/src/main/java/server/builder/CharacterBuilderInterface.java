package server.builder;

import server.service.MainCharacter;
import server.models.CharacterObj;

public interface CharacterBuilderInterface {

    MainCharacter getMainCharacter();

    void setMainCharacter(MainCharacter mc);

    void buildDimensions();

    void buildHp();

    void buildData(CharacterObj data);

    void buildColor();
}
