package service;

import builder.CruiserBuilder;
import builder.SpeedoBuilder;
import builder.TankBuilder;
import factory.MainCharacterFactory;
import interfaces.ObserverInterface;
import models.Box;
import models.CharacterObj;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


public class Main {
	public static final int MAP_WIDTH = 1500;
	public static final int MAP_HEIGTH = 900;

	private static final String TILES_FILE = "/gameInfo.txt";
	
	 // refreshing game state and sending data to clients every x ms
	private static final long RESHRESH_GAP = 30;

	private static int SERVER_PORT_TCP;
	
	private static long IDs = 0L;
	
	//thread safe array because while one thread is reading another
	//might add delete some entries
	private Vector<MainCharacter> fullCharacters;

	private Helper.WrapperList tiles;
	private Helper.WrapperList gamePlay;
	
	private UdpConnection udpSend;

	private MainCharacterFactory mcFactory;

	private MainCharacter mainCharacterPrototype;

    public Vector<MainCharacter> getFullCharacters() {
        return fullCharacters;
    }

    public void setFullCharacters(Vector<MainCharacter> fullCharacters) {
        this.fullCharacters = fullCharacters;
    }
	
	public Main(int tcpPort, MainCharacterFactory mcFactory){
		
		SERVER_PORT_TCP = tcpPort;
		tiles = new Helper.WrapperList();
		gamePlay = new Helper.WrapperList();
		udpSend = UdpConnection.getInstance();
		fullCharacters = new Vector<MainCharacter>();
		mainCharacterPrototype = new MainCharacter();
		this.mcFactory = mcFactory;
	}

	public void start(){
			
		gameStateRefresher();

		InputStream ff = Main.class.getResourceAsStream(TILES_FILE);
		try (Scanner fileReader = new Scanner(ff);
		ServerSocket serverSocket = new ServerSocket(SERVER_PORT_TCP)){

			while(fileReader.hasNext()){
				tiles.add(new Box(fileReader.nextInt(), fileReader.nextInt(),
						fileReader.nextInt(), fileReader.nextInt(), 1f,
						1f, 1f, -1L, -1, -1, null));
			}
			
			Socket clientSocket;
			while((clientSocket = serverSocket.accept()) != null){
				new Thread(new service.TcpConnection(this, clientSocket)).start();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void gameStateRefresher(){
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				updateGamePlay();
				udpSend.sendGamePlay(gamePlay);
			}

			private void updateGamePlay() {
				gamePlay.clear();
				for (MainCharacter mc : fullCharacters){
					gamePlay.addAll(mc.update(tiles.realList, fullCharacters));
				}
			}
			
		},0, RESHRESH_GAP);
	}

	synchronized long getId(){
		return IDs++;
	}
	
	/**
	 * This function is called when new data about character arrives.
	 * If this is a new character we update its state otherwise
	 * we simply update velocity and etc.
	 * @param data that we get from client
	 */

	public void includeCharacter(CharacterObj data){
		
		long specId = data.id;
		for (MainCharacter mc : fullCharacters){
			
			//if character already exists we just update its status
			if (specId == mc.getID()){
			 	mc.updateState(data);
				return ;
			}
		}
		//if it is new character then we add it to the list

		MainCharacter newMc = mcFactory.createMainCharacter(data, mainCharacterPrototype.clone());
		fullCharacters.add(newMc);
	}
	
	public void removeCharacter(long id){
		
		Iterator<MainCharacter> i = fullCharacters.iterator();
		while(i.hasNext()){
			
			MainCharacter mc = i.next();
			if (mc.getID() == id){
				i.remove();
				return;
			}
		}
	}
	
	public static class IpPort implements ObserverInterface {
		
		InetAddress address;
		int port;
		
		IpPort(InetAddress address, int port){
			this.address = address;
			this.port = port;
		}

		@Override
		public void update(DatagramPacket packet) {
			packet.setAddress(this.address);
			packet.setPort(this.port);
			try {
				service.UdpConnection.getInstance().gamePlaySocket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Helper.WrapperList getMap(){
		return tiles;
	}

}