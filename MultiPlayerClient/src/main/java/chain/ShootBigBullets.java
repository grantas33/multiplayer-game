package chain;

import command.BigBulletsCommand;
import command.CommandInvoker;
import command.ICommand;

public class ShootBigBullets implements Chain {
    private Chain nextChain;
    CommandInvoker commandInvoker;

    public ShootBigBullets(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public String calculateDecorAndReturn(ICommand request) {
        if (request.getClass() == BigBulletsCommand.class) {
            System.out.println("Chain: Shoot Big Bullets");
            return commandInvoker.addCommandAndExecute(request);
        } else {
            return this.nextChain.calculateDecorAndReturn(request);
        }
    }
}
