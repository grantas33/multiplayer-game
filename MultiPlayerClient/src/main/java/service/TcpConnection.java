package service;

import java.io.*;
import java.net.Socket;
import java.util.List;

import javax.xml.bind.JAXBException;

import models.Box;
import models.gameObjectsComposite.CharacterObj;
import models.ServerMessage;
import visitor.ServerMessagePrinter;


public class TcpConnection {

	private static final int GET_ID = 0;
	private static final int GET_MAP = 1;
	private static final int SEND_MAIN_CHARACTER = 2;
	private static final int GET_ID_IP_PORT = 3;
	private static final int REMOVE_CHARACTER = 4;
	
	private final int SERVER_PORT_TCP;
	
	private final String SERVER_IP;

	private ObjectOutput oos;
	private ObjectInput ois;
	
	private Socket socket;
	private MarshallerProxy marshallerProxy = new MarshallerProxy();

	TcpConnection(String ip, int port) {
		
		SERVER_PORT_TCP = port;
		SERVER_IP = ip;
		try {
			socket = new Socket(SERVER_IP, SERVER_PORT_TCP);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TcpConnection(String ip, int port, ObjectOutput oos, ObjectInput ois) {
		SERVER_PORT_TCP = port;
		SERVER_IP = ip;
		this.oos = oos;
		this.ois = ois;
	}
	
	/** Gets unique ID for player **/
	public long getIdFromServer() {
		
		try {
			ServerMessage sm = new ServerMessage(GET_ID);
			String data = marshallerProxy.marshall(sm);
			oos.writeObject(data);
			
			return ois.readLong();
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/** Downloads map from server **/
	public List<Box> getMapDetails() {
		
		try {
			ServerMessage sm = new ServerMessage(GET_MAP);
			String data = marshallerProxy.marshall(sm);
			oos.writeObject(data);
			
			String response = (String) ois.readObject();
			return marshallerProxy.unmarshall(response).realList;
			
		} catch (IOException | ClassNotFoundException | JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Sends data about the main character to server. Velocity, etc. */
	public void sendUpdatedVersion(CharacterObj character) {
		try {
			ServerMessage sm = new ServerMessage(SEND_MAIN_CHARACTER);
			sm.setCharacterData(character);

//			StringBuilder sb = new StringBuilder();
//			ServerMessagePrinter printer = new ServerMessagePrinter(sb);
//			sm.accept(printer);
//			System.out.println(sb);

			String data = marshallerProxy.marshall(sm);
			oos.writeObject(data);
//			((ObjectOutputStream) oos).reset();
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/** Sends IP and port of Udp connection **/
	public void sendIpIdPort(int port) {
		
		try {
			ServerMessage sm = new ServerMessage(GET_ID_IP_PORT);
			sm.setPort(port);
			String data = marshallerProxy.marshall(sm);
			oos.writeObject(data);
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/** Sends id of player to the server to inform that a player has left the game **/
	public void removeCharacter(long id) {
		
		try {
			ServerMessage sm = new ServerMessage(REMOVE_CHARACTER);
			sm.setId(id);
			String data = marshallerProxy.marshall(sm);
			oos.writeObject(data);
//			((ObjectOutputStream) oos).reset();
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
	}

}
