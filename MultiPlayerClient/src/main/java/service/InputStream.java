package service;

import mediator.IOColleague;
import mediator.IOMediator;

import java.io.IOException;
import java.io.ObjectInputStream;

public class InputStream extends IOColleague {

    private ObjectInputStream ois;

    public InputStream(IOMediator mediator, ObjectInputStream ois) {
        super(mediator);
        this.ois = ois;

    }

    @Override
    public void send(String msg) {
    }

    @Override
    public String receive() {
        try {
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public long receiveLong() {
        try {
            return ois.readLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
