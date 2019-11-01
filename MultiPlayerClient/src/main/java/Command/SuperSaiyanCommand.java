package Command;

import Decorator.SuperSaiyan;
import models.CharacterObj;

public class SuperSaiyanCommand implements ICommand{
    private CharacterObj character;

    public SuperSaiyanCommand(CharacterObj character) {
        this.character = character;
    }

    @Override
    public String execute() {
        return new SuperSaiyan(character).make();
    }

    @Override
    public String undo(String decor) {
        return decor.replaceAll("SuperSaiyan", "");
    }
}
