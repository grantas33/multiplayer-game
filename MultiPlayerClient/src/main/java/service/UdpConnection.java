package service;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.xml.bind.JAXBException;

import mediator.TCPIOMediator;
import models.ServerMessage;

/**
* This class establishes UDP connection with server and receives data about
* the game state
*/
class UdpConnection implements Runnable {
	
		private Main main;
		
		private byte[] buffer = new byte[1024 * 3];
		
		private DatagramSocket datagramSocket;
		
		private TcpConnection tcpConnection;

		private MarshallerProxy marshallerProxy;
		
		//set udp port you want get game-play though. Make sure
		//router port forwards it.
		//private final int UDP_PORT;

		private final int UDP_PORT;

		UdpConnection(Main main, TcpConnection tcpConnection, int client_port_udp) {
			
			this.main = main;
			this.tcpConnection = tcpConnection;
			UDP_PORT = client_port_udp;
		}

		/** Listens to server, reads sent data and passes it to main class */
		@Override
		public void run() {
			TCPIOMediator mediator = new TCPIOMediator();

			try {
				if (UDP_PORT < 0 || UDP_PORT > 65535){
					datagramSocket = new DatagramSocket();
					System.err.append(UDP_PORT + "port is not possible. Random port assigned");
				}
				else{
					datagramSocket = new DatagramSocket(UDP_PORT);
				}

				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					OutputStream outputStream = new OutputStream(mediator, new ObjectOutputStream(baos));
					marshallerProxy = new MarshallerProxy(mediator);
					mediator.setOos(outputStream);
					mediator.setMarshallerProxy(marshallerProxy);

					ServerMessage sm = new ServerMessage(datagramSocket.getLocalPort());
					sm.setPort(datagramSocket.getLocalPort());
					marshallerProxy.sendServerMessage(sm);

					byte[] bytes = baos.toByteArray();
					DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

					packet.setAddress(InetAddress.getByName(main.server_ip));
					packet.setPort(15001);
					try {
						datagramSocket.send(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}


				// send info about UDP to server
				tcpConnection.sendIpIdPort(datagramSocket.getLocalPort());
				System.err.println(datagramSocket.getLocalPort());



				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				while (true) {

					String data;
					try {
						datagramSocket.receive(packet);
						ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
						InputStream inputStream = new InputStream(mediator, new ObjectInputStream(bais));
						mediator.setOis(inputStream);

						data = inputStream.receive();
					} catch (IOException e1) {
						 e1.printStackTrace();
						continue;
					}
					MarshallerProxy.WrapperList wrapperList = null;
					try {
						wrapperList = marshallerProxy.unmarshall(data);
					} catch (JAXBException e) {
						e.printStackTrace();
					}
					main.updateListOfObjects(wrapperList);
					packet.setData(buffer);
					packet.setLength(buffer.length);
				}

			} catch ( SocketException e) {
				e.printStackTrace();
			}

		}

}
