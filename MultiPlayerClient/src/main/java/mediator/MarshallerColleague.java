package mediator;

import models.ServerMessage;
import service.MarshallerProxy;

public abstract class MarshallerColleague {
    protected IOMediator mediator;

    public MarshallerColleague(IOMediator mediator) {
        this.mediator = mediator;
    }

    public abstract MarshallerProxy.WrapperList receiveWrapperList();
    public abstract void sendServerMessage(ServerMessage msg);
}
