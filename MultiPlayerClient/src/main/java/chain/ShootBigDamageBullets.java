package chain;

import command.BigDamageBulletsCommand;
import command.CommandInvoker;
import command.ICommand;

public class ShootBigDamageBullets implements Chain {
    private Chain nextChain;
    CommandInvoker commandInvoker;

    public ShootBigDamageBullets(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public String calculateDecorAndReturn(ICommand request) {
        if (request.getClass() == BigDamageBulletsCommand.class) {
            System.out.println("Chain: Shoot Big Damage Bullets");
            return commandInvoker.addCommandAndExecute(request);
        } else {
            return this.nextChain.calculateDecorAndReturn(request);
        }
    }
}
