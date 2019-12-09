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

import models.Box;
import models.ServerMessage;

/**
 * 
 * @author Titas Skrebe
 *
 * Proxy class for (un)marshalling data
 * 
 */
public class MarshallerProxy {

	private Marshaller marshaller = null;

	private Unmarshaller unmarshaller = null;
	
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
	
	public List<Box> unmarshall(String data) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(WrapperList.class);
		
		if (unmarshaller == null) {
			unmarshaller = jc.createUnmarshaller();
		}
		StringReader sr = new StringReader(data);
		
		WrapperList wrapList = (WrapperList) unmarshaller.unmarshal(sr);
		return wrapList.realList;
	}
	
	/**
	 * Wrapper class for marshalling/unmarshalling a list of boxes
	 */
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WrapperList{
		
		List<Box> realList;
		public WrapperList(){}
		
	}

}
