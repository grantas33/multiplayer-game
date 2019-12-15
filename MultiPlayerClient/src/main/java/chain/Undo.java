package chain;

import command.CommandInvoker;
import command.ICommand;
import command.UndoCommand;
import service.Main;

public class Undo implements Chain {
    private Chain nextChain;
    CommandInvoker commandInvoker;

    public Undo(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public String calculateDecorAndReturn(ICommand request) {
        if (request.getClass() == UndoCommand.class) {
            System.out.println("Chain: UNDO");
            return commandInvoker.undo();
        } else {
            System.out.println("Chain end: There is no such command.");
            return Main.decor;
        }
    }
}
