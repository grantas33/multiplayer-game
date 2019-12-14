package service;

import interfaces.ObserverInterface;
import interfaces.SubjectInterface;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;

public class UdpConnection implements SubjectInterface {

    DatagramSocket gamePlaySocket;
    private CopyOnWriteArrayList<ObserverInterface> activeClients = new CopyOnWriteArrayList<>();

    private static UdpConnection instance = null;

    private UdpConnection() {

        try {
            gamePlaySocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    synchronized static UdpConnection getInstance() {
        if (instance == null) {
            instance = new UdpConnection();
        }

        return instance;
    }

    @Override
    public void attach(ObserverInterface o) {
        activeClients.add(o);
    }

    @Override
    public void notifyObservers(DatagramPacket packet) {
        for(ObserverInterface observer: activeClients) {
            observer.update(packet);
        }
    }

    void sendGamePlay(Helper.WrapperList gamePlay) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(Helper.marshall(gamePlay));
            byte[] bytes = baos.toByteArray();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            System.out.println("sending packet of " + bytes.length + " bytes");

            notifyObservers(packet);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }
}