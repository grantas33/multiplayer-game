package chain;

import command.ICommand;

public interface Chain {
    public void setNextChain(Chain nextChain);

    public String calculateDecorAndReturn(ICommand request);
}
