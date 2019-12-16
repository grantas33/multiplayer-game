package mediator;


import models.ServerMessage;

public class TCPIOMediator implements IOMediator {
    private IOColleague oos;
    private IOColleague ois;
    private MarshallerColleague marshallerProxy;

    public void setMarshallerProxy(MarshallerColleague marshallerProxy) {
        this.marshallerProxy = marshallerProxy;
    }

    public void setOos(IOColleague oos) {
        this.oos = oos;
    }

    public void setOis(IOColleague ois) {
        this.ois = ois;
    }

    @Override
    public void writeToOutput(String data) {
        oos.send(data);
    }

    @Override
    public String readInputResponse() {
        return ois.receive();
    }

    @Override
    public String getRawServerMessage(ServerMessage sm) {
        return marshallerProxy.receiveWrapperList().toString();
    }
}
