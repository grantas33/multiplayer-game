package server.side.builder;

import server.side.MainCharacter;
import server.side.models.CharacterObj;

public interface CharacterBuilderInterface {

    MainCharacter getMainCharacter();

    void setMainCharacter(MainCharacter mc);

    void buildDimensions();

    void buildHp();

    void buildData(CharacterObj data);

    void buildColor();
}
