package service;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import command.BigDamageBulletsCommand;
import command.CommandInvoker;
import command.BigBulletsCommand;
import command.SuperSaiyanCommand;
import decorator.BigBullets;
import decorator.BigDamageBullets;
import decorator.SuperSaiyan;
import enumerators.GamePhase;
import enumerators.SpaceshipType;
import factory.CharacterObjFactory;
import flyweight.BulletsHashMap;
import iterator.TitledBoxIterator;
import models.*;
import org.joml.Vector2i;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.animation.Animator;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.TextInput;
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

	private static final double[] XS = {100, 200, 300, 400, 500, 600, 700};
	private static final double[] YS = {100, 200, 300, 400, 500};

	static long ID = -1; // we get ID from the server side
	long window;
	DefaultInitializer guiInitializer;
	Animator guiAnimator;
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
	TextInput nameInput;
	List<Label> characterNicknames = new ArrayList<>();
	List<Label> scores = new ArrayList<>();

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
	private String decor = "";
	private CommandInvoker commandInvoker = new CommandInvoker();

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
		nameInput = new TextInput("Player1", 300, 50, 100, 20);

		guiFrame.getContainer().add(nameInput);
		guiInitializer = new DefaultInitializer(window, guiFrame);
		guiContext = guiInitializer.getContext();
		guiRenderer = guiInitializer.getRenderer();
		guiAnimator = Animator.getInstance();
		guiRenderer.initialize();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGTH, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	public Bullet constructBullet(double cursorX, double cursorY, boolean flyweight) {
		float xmouse = (float) cursorX + camera.x;
		float ymouse = DISPLAY_HEIGTH - (float) cursorY + camera.y;
		float pnx = 1;
		float xmain = updatedCharacter.x + updatedCharacter.w / (float) 2;
		float ymain = updatedCharacter.y + updatedCharacter.h / (float) 2;
		float k = (ymain - ymouse) / (xmain - xmouse);
		float c = ymain - k * xmain;
		if (xmouse > xmain) {
			pnx = -1;
		}

		if (flyweight) {
			return BulletsHashMap.getBullet(k, xmain, ymain, c, pnx, decor);

		} else {
			return new Bullet(xmain, ymain, k, c, pnx, decor);
		}
	}

	public long executeBulletsExplosion(boolean flyweight) {
		playerSounds.getFire().play();
		long timeWasted = 0;
		for (int i = 0; i < 18; i++) {
			int rndXs = new Random().nextInt(XS.length);
			int rndYs = new Random().nextInt(YS.length);
			double cursorX = XS[rndXs];
			double cursorY = YS[rndYs];
			long startTime = System.nanoTime();
			bullets.add(constructBullet(cursorX, cursorY, flyweight));
			long endTime = System.nanoTime();
			timeWasted += (endTime - startTime);
		}
		return timeWasted;
	}

	/** Setting up screen, establishing connections (TCP, UPD) with server, etc. */
	public void init() {

		guiInitializer.getCallbackKeeper().getChainCursorPosCallback().add(new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				cursorPos.x = xpos;
				cursorPos.y = DISPLAY_HEIGTH - ypos;
			}
		});

		guiInitializer.getCallbackKeeper().getChainMouseButtonCallback().add(new GLFWMouseButtonCallback() {
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
								double cursorX = cursorPos.x;
								double cursorY = cursorPos.y;
                                /*System.out.println(String.format("cursorX=%f, cursorY=%f",
										cursorX, cursorY));*/
								playerSounds.getFire().play();
								bullets.add(constructBullet(cursorX, cursorY, false));
							}
						}
						break;
				}

			}

		});

		guiInitializer.getCallbackKeeper().getChainKeyCallback().add(new GLFWKeyCallback() {
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
							decor = commandInvoker.addCommandAndExecute(saiyanCommand);
						}
					}
					if (key == GLFW_KEY_2) {
						if (action == GLFW_PRESS) {
							BigBulletsCommand bigBulletsCommand = new BigBulletsCommand(character);
							decor = commandInvoker.addCommandAndExecute(bigBulletsCommand);
							//decor = new BigBullets(new BigDamageBullets(new SuperSaiyan(character))).make();
						}
					}
					if (key == GLFW_KEY_3) {
						if (action == GLFW_PRESS) {
							BigDamageBulletsCommand bigDamageBulletsCommand = new BigDamageBulletsCommand (character);
							decor = commandInvoker.addCommandAndExecute(bigDamageBulletsCommand);
						}
					}
					if (key == GLFW_KEY_4) {
						if (action == GLFW_PRESS) {
							decor = commandInvoker.undo();
						}
					}
					if (key == GLFW_KEY_G) {
						if (action == GLFW_PRESS) {
							//gPressed = true;
							Runtime runtime = Runtime.getRuntime();
							runtime.gc();
							runtime.gc();
							runtime.gc();

							long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

							long timeWasted = executeBulletsExplosion(true);

							long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

							System.out.println(String.format("Flyweight pattern. Time wasted = %d, Memory consumption = %d",
									timeWasted / 1000, (usedMemoryAfter - usedMemoryBefore) / 100000));
						}
					}
					if (key == GLFW_KEY_B) {
						if (action == GLFW_PRESS) {
							//bPressed = true;
							Runtime runtime = Runtime.getRuntime();
							runtime.gc();
							runtime.gc();
							runtime.gc();

							long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

							long timeWasted = executeBulletsExplosion(false);

							long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

							System.out.println(String.format("Not flyweight. Time wasted: %d, Memory consumption = %d",
									timeWasted / 1000, (usedMemoryAfter - usedMemoryBefore) / 100000));
						}
					}
				}
			}

		});

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

			guiAnimator.runAnimations();

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
        Box speedo = new Box(100, 200, CharacterObjFactory.SPEEDO_WIDTH, CharacterObjFactory.SPEEDO_HEIGHT, 255, 255, 255, 0, 0, 0);
        Box tank = new Box(300, 200, CharacterObjFactory.TANK_WIDTH, CharacterObjFactory.TANK_HEIGHT, 100, 100, 100, 0, 0, 0);
        Box cruiser = new Box(500, 200, CharacterObjFactory.CRUISER_WIDTH, CharacterObjFactory.CRUISER_HEIGHT, 100, 100, 100, 0, 0, 0);

        drawSquare(speedo);
        drawSquare(tank);
        drawSquare(cruiser);

        if (selectedType != null) {
        	character = CharacterObjFactory.createCharacterObj(selectedType, nameInput.getTextState().getText(), ID);

			character.addStrategy(new Bolt());
			character.addStrategy(new Runner());
			character.addStrategy(new Normal());
			character.addStrategy(new Slow());
			counter = 0;

			spaceshipSelectLabels.forEach(it -> guiFrame.getContainer().remove(it));
			guiFrame.getContainer().remove(nameInput);
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
		drawTitles(movingObjects);
		drawScores(movingObjects);
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

	private void drawTitles(List<Box> ships) {
        characterNicknames.forEach(it -> guiFrame.getContainer().remove(it));
        characterNicknames = new ArrayList<>();

        Iterator<Box> boxesWithTitle = new TitledBoxIterator(ships);
        while (boxesWithTitle.hasNext()) {
            Box box = boxesWithTitle.next();
            Label label = drawColoredTitle(new ColorTitledBox(box));
            guiFrame.getContainer().add(label);
            characterNicknames.add(label);
        }
    }

    private Label drawColoredTitle(ColorTitledObject obj) {

        Label label = new Label(obj.getTitle(), obj.getX() - camera.x, obj.getY() - camera.y, 100, 50);
        label.getTextState().setTextColor(obj.getTextColor());
        return label;
    }

	private void drawScores(List<Box> ships) {

		scores.forEach(it -> guiFrame.getContainer().remove(it));
		scores = new ArrayList<>();
		int y = 20;
        Iterator<Box> boxesWithTitle = new TitledBoxIterator(ships);
        while (boxesWithTitle.hasNext()) {
            Box box = boxesWithTitle.next();
            String scoreTitle = box.title + ": " + box.xp;
            Label label = new Label(scoreTitle, DISPLAY_WIDTH - 100, y, 100, 20);
            y += 20;
            guiFrame.getContainer().add(label);
            scores.add(label);
		}
	}

	/** Function to send main characters data to server */
	public void sendCharacter() {

		character.newBullets = bullets;
		character.decor = decor;
		// System.out.println(decor);
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
			float xCam = Math.min(Math.max(0, (xnew + character.w / (float) 2) - DISPLAY_WIDTH / (float) 2),
					MAP_WIDTH - DISPLAY_WIDTH);
			float yCam = Math.min(Math.max(0, (ynew + character.h / (float) 2) - DISPLAY_HEIGTH / (float) 2),
					MAP_HEIGTH - DISPLAY_HEIGTH);

			xmov = xCam - x;
			x = xCam;

			ymov = yCam - y;
			y = yCam;
		}
	}
}
