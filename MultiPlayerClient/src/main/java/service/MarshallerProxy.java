package service;

import java.io.StringReader;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mediator.IOMediator;
import mediator.MarshallerColleague;
import models.Box;
import models.ServerMessage;

/**
 * 
 * @author Titas Skrebe
 *
 * Proxy class for (un)marshalling data
 * 
 */
public class MarshallerProxy extends MarshallerColleague {

	private Marshaller marshaller = null;

	private Unmarshaller unmarshaller = null;

	public MarshallerProxy(IOMediator mediator) {
		super(mediator);
	}
	
	/**
	 * Marshalls ServerMessage class to a string.
	 * 
	 * @param sm ServerMessage class
	 * @return an XML string 
	 * @throws JAXBException
	 */
	
	public String marshall(ServerMessage sm) throws JAXBException {
		
		JAXBContext jc = JAXBContext.newInstance(ServerMessage.class);
        if (this.marshaller == null) {
			marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		}
        
        StringWriter sw = new StringWriter();
        marshaller.marshal(sm, sw);

        
		return sw.toString();
	}
	
	/**
	 * Unmarshall a list of Boxes in XML to a actual object
	 * 
	 * @param data
	 * @return A list of boxes
	 * @throws JAXBException
	 */
	
	public WrapperList unmarshall(String data) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(WrapperList.class);
		
		if (unmarshaller == null) {
			unmarshaller = jc.createUnmarshaller();
		}
		StringReader sr = new StringReader(data);

		return (WrapperList) unmarshaller.unmarshal(sr);
	}

	@Override
	public WrapperList receiveWrapperList() {
		try {
			return this.unmarshall(mediator.readInputResponse());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void sendServerMessage(ServerMessage msg) {
		try {
			mediator.writeToOutput(this.marshall(msg));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wrapper class for marshalling/unmarshalling a list of boxes
	 */
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WrapperList{
		
		List<Box> realList;
		int gamePhase;
		int currentTimer;

		public WrapperList(){}
		
	}

}
