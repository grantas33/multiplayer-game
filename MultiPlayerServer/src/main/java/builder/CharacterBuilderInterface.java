package builder;

import models.composite.MainCharacter;
import models.CharacterObj;

public interface CharacterBuilderInterface {

    MainCharacter getMainCharacter();

    void setMainCharacter(MainCharacter mc);

    void buildDimensions();

    void buildHp();

    void buildData(CharacterObj data);

    void buildColor();
}
