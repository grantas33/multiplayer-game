package interfaces;

import java.net.DatagramPacket;

public interface SubjectInterface {

    void attach(ObserverInterface o);

    void notifyObservers(DatagramPacket packet);
}
