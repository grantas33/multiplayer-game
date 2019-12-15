package chain;

import command.CommandInvoker;
import command.ICommand;
import command.SuperSaiyanCommand;

public class BecomeSuperSaiyan implements Chain {
    private Chain nextChain;
    CommandInvoker commandInvoker;

    public BecomeSuperSaiyan(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public String calculateDecorAndReturn(ICommand request) {
        if (request.getClass() == SuperSaiyanCommand.class) {
            System.out.println("Chain: Become a Super Saiyan");
            return commandInvoker.addCommandAndExecute(request);
        } else {
            return this.nextChain.calculateDecorAndReturn(request);
        }
    }
}
