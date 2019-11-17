package server.interfaces;

import java.net.DatagramPacket;

public interface ObserverInterface {

    void update(DatagramPacket packet);
}
