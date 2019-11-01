package service;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import Command.CommandController;
import Command.ICommand;
import Command.SuperBulletsCommand;
import Command.SuperSaiyanCommand;
import enumerators.GamePhase;
import enumerators.SpaceshipType;
import factory.CharacterObjFactory;
import org.joml.Vector2i;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.layout.LayoutManager;
import org.liquidengine.legui.system.renderer.Renderer;
import org.liquidengine.legui.theme.Themes;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import sound.PlayerSounds;
import strategy.Normal;

import models.Box;
import models.Bullet;
import models.CharacterObj;
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
	long window;
	DefaultInitializer guiInitializer;
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private final CursorPos cursorPos = new CursorPos();
	private boolean up = false;
	private boolean down = false;
	private boolean right = false;
	private boolean left = false;

	List<Label> spaceshipSelectLabels = new ArrayList<>();

	private Frame guiFrame;
	private Context guiContext;
	private Renderer guiRenderer;

	private TcpConnection connections; // establishing TCP connection

	private CharacterObj character; // data about the main character
	private List<Bullet> bullets; // bullets shot in every frame, also to server

	private List<Box> obstacles;
	private List<Box> movingObjects; // all players and bullets. We get this from server
	private Box updatedCharacter; // clients character that we get from server

  //  private UnicodeFont uf = null; // default font for rendering text
    private GamePhase gamePhase = GamePhase.SPACESHIP_SELECT; // current game phase
	SpaceshipType selectedType = null;

	private Camera camera;
	
	private String server_ip;
	private int server_port_tcp;
	private int client_port_udp;
	private int counter;
	private PlayerSounds playerSounds;
	private String decorated = "";
	private CommandController commandController = new CommandController();

	public Main(String ip, int portTcp, int portUdp){
		server_ip = ip;
		server_port_tcp = portTcp;
		client_port_udp = portUdp;
	}
	
	/** Initializing OpenGL functions */
	public void initOpenGl() {

		glfwInit();
		//Setup an error callback to print GLFW errors to the console.
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(DISPLAY_WIDTH, DISPLAY_HEIGTH, "Space shooter", 0, 0);
		if(window == 0) {
			throw new RuntimeException("Failed to create window");
		}
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glfwShowWindow(window);
		Themes.setDefaultTheme(Themes.FLAT_DARK);

		guiFrame = new Frame(DISPLAY_WIDTH, DISPLAY_HEIGTH);
		guiFrame.getContainer().getStyle().getBackground().setColor(ColorConstants.transparent());

		Label speedoLabel = new Label("Speedo", 100, 150, 100, 50);
		Label tankLabel = new Label("Tank", 300, 150, 100, 50);
		Label cruiserLabel = new Label("Cruiser", 500, 150, 100, 50);

		spaceshipSelectLabels.add(speedoLabel);
		spaceshipSelectLabels.add(tankLabel);
		spaceshipSelectLabels.add(cruiserLabel);
		spaceshipSelectLabels.forEach(it -> {
			it.getTextState().setFontSize(30);
			guiFrame.getContainer().add(it);
		});
		guiInitializer = new DefaultInitializer(window, guiFrame);
		guiContext = guiInitializer.getContext();
		guiRenderer = guiInitializer.getRenderer();
		guiRenderer.initialize();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGTH, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	/** Setting up screen, establishing connections (TCP, UPD) with server, etc. */
	public void init() {

		//Setup the cursor pos callback.
		glfwSetCursorPosCallback(window, (cursorPosCallback = new GLFWCursorPosCallback() {

			@Override
			public void invoke(long window, double xpos, double ypos) {
				cursorPos.x = xpos;
				cursorPos.y = DISPLAY_HEIGTH - ypos;
			}

		}));


		glfwSetMouseButtonCallback(window, (mouseButtonCallback = new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
				switch (gamePhase) {
					case SPACESHIP_SELECT:
						if(button == 0) {
							if(action == GLFW_PRESS) {
								double x = cursorPos.x;
								double y = cursorPos.y;
								if(x >= 100 && x <=100 + CharacterObjFactory.SPEEDO_WIDTH && y <=250 + CharacterObjFactory.SPEEDO_HEIGHT && y >= 250) {
									selectedType = SpaceshipType.SPEEDO;
								} else if (x >= 300 && x <= 300 + CharacterObjFactory.TANK_WIDTH && y <= 200 + CharacterObjFactory.TANK_HEIGHT && y >= 200) {
									selectedType = SpaceshipType.TANK;
								} else if (x >= 500 && x <= 500 + CharacterObjFactory.CRUISER_WIDTH && y <= 200 + CharacterObjFactory.CRUISER_HEIGHT && y >= 200) {
									selectedType = SpaceshipType.CRUISER;
								}
							}
						}
						break;
					case LIVE_MATCH:
						if(button == 0) {
							// new bullets shot
							if(action == GLFW_PRESS && updatedCharacter != null) {
								float xmouse = (float)cursorPos.x + camera.x;
								float ymouse = DISPLAY_HEIGTH - (float)cursorPos.y + camera.y;
								float pnx = 1;
								float xmain = updatedCharacter.x + updatedCharacter.w / 2;
								float ymain = updatedCharacter.y + updatedCharacter.h / 2;
								float k = (ymain - ymouse) / (xmain - xmouse);
								float c = ymain - k * xmain;

								if (xmouse > xmain) {
									pnx = -1;
								}
								playerSounds.getFire().play();
								bullets.add(new Bullet(xmain, ymain, k, c, pnx, decorated));
							}
						}
						break;
				}

			}

		}));


		glfwSetKeyCallback(window, (keyCallback = new GLFWKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (character != null) {
					int speedIndicator = character.speedIndicator();

					if (key == GLFW_KEY_W || key == GLFW_KEY_UP) {
						if (action == GLFW_PRESS || action == GLFW_REPEAT) {
							character.yVel = -5 * speedIndicator;
							up = true;
						} else {
							up = false;
							if (!down) {
								character.yVel = 0;
							}
						}
					}
					if (key == GLFW_KEY_S || key == GLFW_KEY_DOWN) {
						if (action == GLFW_PRESS || action == GLFW_REPEAT) {
							character.yVel = 5 * speedIndicator;
							down = true;
						} else {
							down = false;
							if (!up) {
								character.yVel = 0;
							}
						}
					}
					if (key == GLFW_KEY_D || key == GLFW_KEY_RIGHT) {
						if (action == GLFW_PRESS || action == GLFW_REPEAT) {
							character.xVel = 5 * speedIndicator;
							right = true;
						} else {
							right = false;
							if (!left) {
								character.xVel = 0;
							}
						}
					}
					if (key == GLFW_KEY_A || key == GLFW_KEY_LEFT) {
						if (action == GLFW_PRESS || action == GLFW_REPEAT) {
							character.xVel = -5 * speedIndicator;
							left = true;
						} else {
							left = false;
							if (!right) {
								character.xVel = 0;
							}
						}
					}
					if (key == GLFW_KEY_KP_ADD) {
						if (action == GLFW_PRESS) {
							if (counter == 3) {
								counter = 0;
							}
							else {
								counter++;
							}
							character.selectActiveStrategy(counter);
						}
					}
					if (key == GLFW_KEY_1) {
						if (action == GLFW_PRESS) {
							SuperSaiyanCommand saiyanCommand = new SuperSaiyanCommand(character);
							decorated = commandController.addCommandAndExecute(saiyanCommand);
						}
					}
					if (key == GLFW_KEY_2) {
						if (action == GLFW_PRESS) {
							SuperBulletsCommand bulletsCommand = new SuperBulletsCommand(character);
							decorated = commandController.addCommandAndExecute(bulletsCommand);
						}
					}
					if (key == GLFW_KEY_3) {
						if (action == GLFW_PRESS) {
							decorated = commandController.Undo(decorated);
						}
					}
				}
			}

		}));

		connections = new TcpConnection(server_ip, server_port_tcp);

		if ((ID = connections.getIdFromServer()) == -1) {
			System.err.println("cant get id for char");
		}
		
		obstacles = connections.getMapDetails();

		bullets = new ArrayList<Bullet>();
		camera = new Camera(0, 0);
		movingObjects = new ArrayList<Box>();

		playerSounds = new PlayerSounds();
		new Thread(new UdpConnection(this, connections, client_port_udp)).start();
	}

	/** Game loop */
	public void start() {

		SyncTimer timer  = new SyncTimer(SyncTimer.LWJGL_GLFW);
		while (!glfwWindowShouldClose(window)) {

			guiContext.updateGlfwWindow();
			Vector2i windowSize = guiContext.getFramebufferSize();
			glClearColor(0, 0, 0, 1);
			// Set viewport size
			glViewport(0, 0, windowSize.x, windowSize.y);
		 	glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			glEnable(GL_BLEND);
			glDisable(GL_DEPTH_TEST);

			if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
				closingOperations();
			}

            switch (gamePhase) {
                case SPACESHIP_SELECT:
                    spaceshipSelectGamePhaseLoop();
                case LIVE_MATCH:
                    liveMatchGamePhaseLoop();
            }

			guiRenderer.render(guiFrame, guiContext);

			glfwPollEvents();
			glfwSwapBuffers(window);

			// Now we need to handle events. Firstly we need to handle system events.
			// And we need to know to which frame they should be passed.
			guiInitializer.getSystemEventProcessor().processEvents(guiFrame, guiContext);

			// When system events are translated to GUI events we need to handle them.
			// This event processor calls listeners added to ui components
			guiInitializer.getGuiEventProcessor().processEvents();

			LayoutManager.getInstance().layout(guiFrame);


			try {
				timer.sync(FRAMES_PER_SECOND);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		closingOperations();
	}

	public void spaceshipSelectGamePhaseLoop() {
        Box speedo = new Box(100, 200, CharacterObjFactory.SPEEDO_WIDTH, CharacterObjFactory.SPEEDO_HEIGHT, 255, 255, 255, 0, 0);
        Box tank = new Box(300, 200, CharacterObjFactory.TANK_WIDTH, CharacterObjFactory.TANK_HEIGHT, 100, 100, 100, 0, 0);
        Box cruiser = new Box(500, 200, CharacterObjFactory.CRUISER_WIDTH, CharacterObjFactory.CRUISER_HEIGHT, 100, 100, 100, 0, 0);

        drawSquare(speedo);
        drawSquare(tank);
        drawSquare(cruiser);

        if (selectedType != null) {
        	character = CharacterObjFactory.createCharacterObj(selectedType, ID);

			character.addStrategy(new Bolt());
			character.addStrategy(new Runner());
			character.addStrategy(new Normal());
			character.addStrategy(new Slow());
			counter = 0;

			spaceshipSelectLabels.forEach(it -> guiFrame.getContainer().remove(it));
			gamePhase = GamePhase.LIVE_MATCH;
        }
    }

    public void liveMatchGamePhaseLoop() {
	    if (character != null) {
            sendCharacter();
            update();
            render();
        }
    }

	
	/** Updating camera's position */
	public void update() {
		if (updatedCharacter != null) {
			camera.update(updatedCharacter);
		}
	}

	
	/** Rendering obstacles, players and bullets */
	public void render() {

		glTranslatef(-camera.xmov, -camera.ymov, 0);	//camera's position

		for (Box box : obstacles) {
			drawSquare(box);
		}
		for (Box box : movingObjects) {
			drawSquare(box);
		}
	}

	/** Function to draw square */
	public void drawSquare(Box box) {

		GL11.glColor3f(box.r, box.g, box.b);
		GL11.glBegin(GL_QUADS);
			GL11.glVertex2f(box.x, box.y);
			GL11.glVertex2f(box.x + box.w, box.y);
			GL11.glVertex2f(box.x + box.w, box.y + box.h);
			GL11.glVertex2f(box.x, box.y + box.h);
		GL11.glEnd();
	}

	/** Function to send main characters data to server */
	public void sendCharacter() {

		character.newBullets = bullets;
		character.decorated = decorated;
		connections.sendUpdatedVersion(character);
		bullets.clear();
	}

	/** Closing game */
	private void closingOperations() {

		connections.removeCharacter(ID);
		guiRenderer.destroy();
		glfwDestroyWindow(window);
		glfwTerminate();
		System.exit(0);
	}

	/**
	 * Getting info about game play
	 * 
	 * @param objects Object can be either bullet or player
	 */
	public void updateListOfObjects(List<Box> objects) {
		if (objects == null)	return;
		movingObjects = objects;
		for (Box box : objects) {
			if (box.id == ID) {
				updatedCharacter = box;
				break;
			}
		}
	}

	public static class CursorPos {
		double x, y;
	}

	/**
	 * Camera shows map regarding main character's position
	 */
	public class Camera {

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
