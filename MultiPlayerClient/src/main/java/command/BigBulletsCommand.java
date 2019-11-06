package command;

import decorator.BigBullets;
import models.CharacterObj;

public class BigBulletsCommand implements ICommand {

    private CharacterObj character;

    public BigBulletsCommand(CharacterObj character) {
        this.character = character;
    }

    @Override
    public String execute() {
        return new BigBullets(character).make();
    }

    @Override
    public String undo() {
        return this.character.decor.replaceAll("BigBullets", "");
    }
}
