package mediator;

import models.ServerMessage;

public interface IOMediator {
    public void writeToOutput(String data);
    public String readInputResponse();
    public String getRawServerMessage(ServerMessage sm);
}
