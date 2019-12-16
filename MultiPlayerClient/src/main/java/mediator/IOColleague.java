package mediator;

public abstract class IOColleague {
    protected IOMediator mediator;

    public IOColleague(IOMediator mediator) {
        this.mediator = mediator;
    }

    public abstract void send(String msg);
    public abstract String receive();
    public abstract long receiveLong();
}
