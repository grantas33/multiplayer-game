package command;

import decorator.BigBullets;
import decorator.BigDamageBullets;
import models.CharacterObj;

public class BigDamageBulletsCommand implements ICommand {

    private CharacterObj character;

    public BigDamageBulletsCommand(CharacterObj character) {
        this.character = character;
    }

    @Override
    public String execute() {
        return new BigDamageBullets(character).make();
    }

    @Override
    public String undo() {
        return this.character.decor.replaceAll("BigDamageBullets", "");
    }
}
