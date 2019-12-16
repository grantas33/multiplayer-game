package service;

import mediator.IOColleague;
import mediator.IOMediator;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class OutputStream extends IOColleague {

    private ObjectOutputStream oos;

    public OutputStream(IOMediator mediator, ObjectOutputStream oos) {
        super(mediator);
        this.oos = oos;
    }

    @Override
    public void send(String msg) {
        try {
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String receive() {
        return null;
    }

    @Override
    public long receiveLong() {
        return 0;
    }
}
