package server.models;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerMessage implements Serializable{
	
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ServerMessage that = (ServerMessage) o;
		return messageType == that.messageType &&
				id == that.id &&
				port == that.port &&
				Objects.equals(characterData, that.characterData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(characterData, messageType, id, port);
	}

	@Override
	public String toString() {
		return "ServerMessage{" +
				"characterData=" + (characterData != null ? characterData.toString() : "null") +
				", messageType=" + messageType +
				", id=" + id +
				", port=" + port +
				'}';
	}
}
