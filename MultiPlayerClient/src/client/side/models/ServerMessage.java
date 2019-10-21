package client.side.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerMessage{
	
	public CharacterObj characterData;
	
	public int messageType;
	public long id;
	public int port;
	
	public ServerMessage(){}
	
	public ServerMessage(int msgType){
		messageType = msgType;
	}
	
	public void setCharacterData(CharacterObj data){
		characterData = data;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
