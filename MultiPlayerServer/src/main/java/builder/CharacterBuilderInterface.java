package builder;

import service.MainCharacter;
import models.CharacterObj;

public interface CharacterBuilderInterface {

    MainCharacter getMainCharacter();

    void setMainCharacter(MainCharacter mc);

    void buildDimensions();

    void buildHp();

    void buildData(CharacterObj data);

    void buildColor();
}
