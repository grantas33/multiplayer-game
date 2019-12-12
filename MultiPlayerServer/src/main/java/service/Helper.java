package service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import models.Box;
import models.ServerMessage;

public class Helper {
	
	public static ServerMessage unmarshall(String data) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(ServerMessage.class);
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader sr = new StringReader(data);
		
		return (ServerMessage) unmarshaller.unmarshal(sr);
	}
	
	
	public static String marshall(WrapperList wl) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(WrapperList.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(wl, sw);
        
		return sw.toString();
	}
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WrapperList{
		List<Box> realList;
		int gamePhase;
		int currentTimer;
		
		public WrapperList(){
			realList = new ArrayList<Box>();
		}
		public void clear() {
			realList.clear();			
		}

		public void addAll(List<Box> update) {
			realList.addAll(update);
		}
		public void add(Box box) {
			realList.add(box);
		}

		public int getGamePhase() {
			return gamePhase;
		}

		public void setGamePhase(ServerGamePhase gamePhase) {
			this.gamePhase = gamePhase.ordinal();
		}

		public int getCurrentTimer() {
			return currentTimer;
		}

		public void setCurrentTimer(int currentTimer) {
			this.currentTimer = currentTimer;
		}
	}
}

enum ServerGamePhase {
	EMPTY_SERVER(0), LIVE_MATCH(30), END_SCORES(10);

	final int duration;

	ServerGamePhase(int duration) {
		this.duration = duration;
	}

}
