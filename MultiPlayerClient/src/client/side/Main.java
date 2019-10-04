package client.side;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import client.side.enumerators.GamePhase;
import client.side.enumerators.SpaceshipType;
import client.side.factory.CharacterObjFactory;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import client.side.models.Box;
import client.side.models.Bullet;
import client.side.models.CharacterObj;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.TextureImpl;
import strategy.Bolt;
import strategy.Runner;
import strategy.Slow;

/**
 * 
 * @author Titas Skrebe
 *
 * This is the main class of a client side for an online multiplayer game.
 * 
 * Go to www.tskrebe.me for more info 
 * 
 */
public class Main {

	private static final int DISPLAY_WIDTH = 700;
	private static final int DISPLAY_HEIGTH = 500;

	private static final int MAP_WIDTH = 1500;
	private static final int MAP_HEIGTH = 900;

	private static final int FRAMES_PER_SECOND = 30;

	static long ID = -1; // we get ID from the server side

	private TcpConnection connections; // establishing TCP connection

	private CharacterObj character; // data about the main character
	private List<Bullet> bullets; // bullets shot in every frame, also to server

	private List<Box> obstacles;
	private List<Box> movingObjects; // all players and bullets. We get this from server
	private Box updatedCharacter; // clients character that we get from server

    private UnicodeFont uf = null; // default font for rendering text
    private GamePhase gamePhase = GamePhase.SPACESHIP_SELECT; // current game phase

	private Camera camera;
	
	private String server_ip;
	private int server_port_tcp;
	private int client_port_udp;
	private int counter;
	
	public static void main(String[] args) {
		
		if (args.length != 3){
			throw new IllegalArgumentException("Bad input. You need [REMOTE IP] [REMOTE TCP PORT] [LOCAL UDP PORT or -1 for random port]");
		}
		
		Main main = new Main(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		main.initOpenGl();
		main.init();
		main.start();
	}
	
	public Main(String ip, int portTcp, int portUdp){
		server_ip = ip;
		server_port_tcp = portTcp;
		client_port_udp = portUdp;
	}
	
	/** Initializing OpenGL functions */
	private void initOpenGl() {

		try {
			Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGTH));
			Display.setResizable(true);
			Display.create();

		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGTH, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	/** Setting up screen, establishing connections (TCP, UPD) with server, etc. */
	private void init() {
		connections = new TcpConnection(this, server_ip, server_port_tcp);

		if ((ID = connections.getIdFromServer()) == -1) {
			System.err.println("cant get id for char");
		}
		
		obstacles = connections.getMapDetails();

		bullets = new ArrayList<Bullet>();
		camera = new Camera(0, 0);
		movingObjects = new ArrayList<Box>();

		try {
            uf = new UnicodeFont("fonts/Mansalva-Regular.ttf", 50, false, false);
            uf.getEffects().add(new ColorEffect(Color.WHITE)); // set the default color to white
            uf.addAsciiGlyphs();
            uf.loadGlyphs(); // load glyphs from font file
        } catch (SlickException exception) {
            exception.printStackTrace();
        }

		new Thread(new UdpConnection(this, connections, client_port_udp)).start();
	}

	/** Game loop */
	private void start() {

		while (!Display.isCloseRequested()) {

		 	glClear(GL_COLOR_BUFFER_BIT);

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				closingOperations();
			}

            switch (gamePhase) {
                case SPACESHIP_SELECT:
                    spaceshipSelectGamePhaseLoop();
                case LIVE_MATCH:
                    liveMatchGamePhaseLoop();
            }

		 	Display.update();
			Display.sync(FRAMES_PER_SECOND);
		}
		closingOperations();
	}

	private void spaceshipSelectGamePhaseLoop() {
        Box speedo = new Box(100, 200, CharacterObjFactory.SPEEDO_WIDTH, CharacterObjFactory.SPEEDO_HEIGHT, 255, 255, 255, 0, 0);
        Box tank = new Box(300, 200, CharacterObjFactory.TANK_WIDTH, CharacterObjFactory.TANK_HEIGHT, 100, 100, 100, 0, 0);
        Box cruiser = new Box(500, 200, CharacterObjFactory.CRUISER_WIDTH, CharacterObjFactory.CRUISER_HEIGHT, 100, 100, 100, 0, 0);

        SpaceshipType selectedType = null;

        drawSquare(speedo);
        drawSquare(tank);
        drawSquare(cruiser);
        drawText(100, 100, "Speedo");
        drawText(300, 100, "Tank");
        drawText(500, 100, "Cruiser");
        TextureImpl.bindNone();

        selectedType = pollInputForSpaceshipType();
        if (selectedType != null) {
        	character = CharacterObjFactory.createCharacterObj(selectedType, ID);

        	Bolt bolt = new Bolt();
			Runner runner = new Runner();
			Slow slow = new Slow();
			character.addStrategy(bolt);
			character.addStrategy(runner);
			character.addStrategy(slow);
			counter = 0;

			gamePhase = GamePhase.LIVE_MATCH;
        }
    }

    private void liveMatchGamePhaseLoop() {
	    if (character != null) {
            handlingEvents();
            sendCharacter();
            update();
            render();
        }
    }

	
	/** Updating camera's position */
	private void update() {

		if (updatedCharacter != null) {
			camera.update(updatedCharacter);
		}
	}

	
	/** Rendering obstacles, players and bullets */
	private void render() {

		glTranslatef(-camera.xmov, -camera.ymov, 0);	//camera's position

		for (Box box : obstacles) {
			drawSquare(box);
		}
		for (Box box : movingObjects) {
			drawSquare(box);
		}
	}

	private void drawText(float x, float y, String text) {
		uf.drawString(x, y, text);
	}

	/** Function to draw square */
	private void drawSquare(Box box) {

		glColor3f(box.r, box.g, box.b);
		glBegin(GL_QUADS);
			glVertex2f(box.x, box.y);
			glVertex2f(box.x + box.w, box.y);
			glVertex2f(box.x + box.w, box.y + box.h);
			glVertex2f(box.x, box.y + box.h);
		glEnd();
	}

	private SpaceshipType pollInputForSpaceshipType() {
		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = Mouse.getY();

            System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);

			if(x >= 100 && x <=100 + CharacterObjFactory.SPEEDO_WIDTH && y <=250 + CharacterObjFactory.SPEEDO_HEIGHT && y >= 250) {
				return SpaceshipType.SPEEDO;
			} else if (x >= 300 && x <= 300 + CharacterObjFactory.TANK_WIDTH && y <= 200 + CharacterObjFactory.TANK_HEIGHT && y >= 200) {
			    return SpaceshipType.TANK;
            } else if (x >= 500 && x <= 500 + CharacterObjFactory.CRUISER_WIDTH && y <= 200 + CharacterObjFactory.CRUISER_HEIGHT && y >= 200) {
				return SpaceshipType.CRUISER;
			}
		}
        return null;
	}

	/** Function to send main characters data to server */
	private void sendCharacter() {

		character.newBullets = bullets;
		connections.sendUpdatedVersion(character);
		bullets.clear();
	}

	/** Closing game */
	private void closingOperations() {

		connections.removeCharacter(ID);
		Display.destroy();
		System.exit(0);
	}

	/**
	 * Getting info about game play
	 * 
	 * @param objects Object can be either bullet or player
	 */
	void updateListOfObjects(List<Box> objects) {
		if (objects == null)	return;
		movingObjects = objects;
		for (Box box : objects) {
			if (box.id == ID) {
				updatedCharacter = box;
				break;
			}
		}
	}
	

	
	private boolean up = false;
	private boolean down = false;
	private boolean right = false;
	private boolean left = false;

	private void handlingEvents() {

		if (Display.isActive()) { // if display is focused events are handled
			// new bullets shot
			while (Mouse.next()) {
				
				if (Mouse.getEventButtonState() && updatedCharacter != null) {	

					float xmouse = Mouse.getX() + camera.x;
					float ymouse = DISPLAY_HEIGTH - Mouse.getY() + camera.y;
					float pnx = 1;
					float xmain = updatedCharacter.x + updatedCharacter.w / 2;
					float ymain = updatedCharacter.y + updatedCharacter.h / 2;
					float k = (ymain - ymouse) / (xmain - xmouse);
					float c = ymain - k * xmain;

					if (xmouse > xmain) {
						pnx = -1;
					}
					bullets.add(new Bullet(xmain, ymain, k, c, pnx));
				}
			}
			
			// character's moves and change speed
			while (Keyboard.next()) {
				int speedIndicator = character.speedIndicator();

				if (Keyboard.getEventKey() == Keyboard.KEY_W
						|| Keyboard.getEventKey() == Keyboard.KEY_UP) {
					if (Keyboard.getEventKeyState()) {
						character.yVel = -5 * speedIndicator;
						up = true;
					} else {
						up = false;
						if (!down) {
							character.yVel = 0;
						}
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S
						|| Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					if (Keyboard.getEventKeyState()) {
						character.yVel = 5 * speedIndicator;
						down = true;
					} else {
						down = false;
						if (!up) {
							character.yVel = 0;
						}
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D
						|| Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
					if (Keyboard.getEventKeyState()) {
						character.xVel = 5 * speedIndicator;
						right = true;
					} else {
						right = false;
						if (!left) {
							character.xVel = 0;
						}
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_A
						|| Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
					if (Keyboard.getEventKeyState()) {
						character.xVel = -5 * speedIndicator;
						left = true;
					} else {
						left = false;
						if (!right) {
							character.xVel = 0;
						}
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_ADD) {
					if (Keyboard.getEventKeyState()) {
						if (counter == 2) {
							counter = 0;
						}
						else {
							counter++;
						}
						character.selectActiveStrategy(counter);
					}
				}
			}
		} else {
			character.xVel = 0;
			character.yVel = 0;
		}
	}

	/**
	 * Camera shows map regarding main character's position
	 */
	private class Camera {

		private float x;
		private float y;

		private float xmov;
		private float ymov;

		Camera(float x, float y) {

			this.x = x;
			this.y = y;
			xmov = 0;
			ymov = 0;
		}

		private void update(Box character) {

			float xnew = character.x, ynew = character.y;
			float xCam = Math.min(Math.max(0, (xnew + character.w / 2) - DISPLAY_WIDTH / 2),
					MAP_WIDTH - DISPLAY_WIDTH);
			float yCam = Math.min(Math.max(0, (ynew + character.h / 2) - DISPLAY_HEIGTH / 2),
					MAP_HEIGTH - DISPLAY_HEIGTH);

			xmov = xCam - x;
			x = xCam;

			ymov = yCam - y;
			y = yCam;
		}
	}
}
