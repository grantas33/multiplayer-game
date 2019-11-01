package Command;

import Decorator.SuperBullets;
import models.CharacterObj;

public class SuperBulletsCommand implements ICommand {

    private CharacterObj character;

    public SuperBulletsCommand(CharacterObj character) {
        this.character = character;
    }

    @Override
    public String execute() {
        return new SuperBullets(character).make();
    }

    @Override
    public String undo(String decorated) {
        return decorated.replaceAll("SuperBullets", "");
    }
}
