package service;

import java.io.*;
import java.net.Socket;
import java.util.List;

import mediator.TCPIOMediator;
import models.Box;
import models.gameObjectsComposite.CharacterObj;
import models.ServerMessage;


public class TcpConnection {

	private static final int GET_ID = 0;
	private static final int GET_MAP = 1;
	private static final int SEND_MAIN_CHARACTER = 2;
	private static final int GET_ID_IP_PORT = 3;
	private static final int REMOVE_CHARACTER = 4;

	private InputStream inputStream;

	private MarshallerProxy marshallerProxy;

	TcpConnection(String ip, int port) {

		try {
			Socket socket = new Socket(ip, port);
			TCPIOMediator mediator = new TCPIOMediator();
			OutputStream outputStream = new OutputStream(mediator, new ObjectOutputStream(socket.getOutputStream()));
			inputStream = new InputStream(mediator, new ObjectInputStream(socket.getInputStream()));
			marshallerProxy = new MarshallerProxy(mediator);
			mediator.setOis(inputStream);
			mediator.setOos(outputStream);
			mediator.setMarshallerProxy(marshallerProxy);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Gets unique ID for player **/
	public long getIdFromServer() {
		ServerMessage sm = new ServerMessage(GET_ID);
		marshallerProxy.sendServerMessage(sm);
		return inputStream.receiveLong();
	}
	
	/** Downloads map from server **/
	public List<Box> getMapDetails() {
		ServerMessage sm = new ServerMessage(GET_MAP);
		marshallerProxy.sendServerMessage(sm);
		return marshallerProxy.receiveWrapperList().realList;
	}
	
	/** Sends data about the main character to server. Velocity, etc. */
	public void sendUpdatedVersion(CharacterObj character) {
		ServerMessage sm = new ServerMessage(SEND_MAIN_CHARACTER);
		sm.setCharacterData(character);
		marshallerProxy.sendServerMessage(sm);
	}
	
	/** Sends IP and port of Udp connection **/
	public void sendIpIdPort(int port) {
		ServerMessage sm = new ServerMessage(GET_ID_IP_PORT);
		sm.setPort(port);
		marshallerProxy.sendServerMessage(sm);
	}
	
	/** Sends id of player to the server to inform that a player has left the game **/
	public void removeCharacter(long id) {
		ServerMessage sm = new ServerMessage(REMOVE_CHARACTER);
		sm.setId(id);
		marshallerProxy.sendServerMessage(sm);
	}

}
