package command;

import decorator.SuperSaiyan;
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
    public String undo() {
        return this.character.decor.replaceAll("SuperSaiyan", "");
    }
}
