package service;

import java.util.*;

import interfaces.Object2D;
import iterator.CollidedBoxIterator;
import sound.PlayerSounds;
import enumerators.SpaceshipType;
import facade.ServerBulletFacade;
import models.Box;
import models.Bullet;
import models.CharacterObj;

public class MainCharacter implements Cloneable, Object2D {

	private float r;
	private float g;
	private float b;

	private int x;
	private int y;
	private int width;
	private int height;
	private SpaceshipType type;
	private String nickname;
	//Thread safe list because bullets can be updated while 
	//iterating them which would resolve in an error.
	private List<models.server.Bullet> bullets;

	private long id;

	private int xVel;
	private int yVel;

	private int hp;
	private int fullHp;
	private int xp;

	private PlayerSounds playerSounds;

	private String decor;


	public void reduceHp(int reduce) {
		hp -= reduce;
	}

	public MainCharacter() {
		// playerSounds = new PlayerSounds();
	}

	/**
	 *
	 * @param width
	 * @param height
	 * @param hp
	 * @param id
	 * @param newBullets
	 */
	public MainCharacter(int width, int height, int hp, long id, List<Bullet> newBullets) {
		
		this.x = 0;
		this.y = 0;

		Random randomFloat = new Random(System.currentTimeMillis());
		this.r = randomFloat.nextFloat();
		this.b = randomFloat.nextFloat();
		this.g = randomFloat.nextFloat();

		xVel = 0;
		yVel = 0;
		this.width = width;
		this.height = height;
		this.hp = hp;
		fullHp = hp;
		this.id = id;

		bullets = Collections.synchronizedList(new ArrayList<models.server.Bullet>());
		addBullets(newBullets);
		// playerSounds = new PlayerSounds();
	}

	public MainCharacter clone() {
		MainCharacter clone = null;
		try {
			clone = (MainCharacter) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	/**
	 * If player already exists we need only to update velocity and add new bullets.
	 * @param data This is data that we get from client side and put on the server side
	 * to simulate game.
	 */
	
	void updateState(CharacterObj data) {
		
		xVel = data.xVel;
		yVel = data.yVel;
		decor = data.decor;
		if (decor.contains("SuperSaiyan")) {
			Random randomFloat = new Random(System.currentTimeMillis());
			this.r = randomFloat.nextFloat();
			this.b = randomFloat.nextFloat();
			this.g = randomFloat.nextFloat();
		}
		addBullets(data.newBullets);
	}	

	/**
	 * Function to add bullets that we get from client side to corresponding character.
	 * @param newBullets New bullets.
	 */

	public void addBullets(List<Bullet> newBullets){
		if (newBullets == null)	return;
		
		for (Bullet sb : newBullets) {
			switch (type) {
				case TANK:
					bullets.add(ServerBulletFacade.createTankBullet(sb, r, g, b));
					break;
				case SPEEDO:
					bullets.add(ServerBulletFacade.createSpeedoBullet(sb, r, g, b));
					break;
				case CRUISER:
					bullets.add(ServerBulletFacade.createCruiserBullet(sb, r, g, b));
					break;
				default:
					return;
			}
		}
	}
	
	/**
	 * This function updates character's and bullets' positions.d
	 * Deletes bullet if there is a collision, changes XPs of enemies.
	 * @param tiles 	Simple obstacles.
	 * @param fullCharacters	All players on-line.
	 * @return Returns main character and bullets in a Box class form. Box class contains
	 * only information necessary to render objects on the client side.
	 */
	
	List<Box> update(List<Box> tiles, Vector<MainCharacter> fullCharacters) {
		
		List<Box> boxes = new ArrayList<Box>();
		
		//updating bullets
		synchronized (bullets) {
			
			Iterator<models.server.Bullet> itr = bullets.listIterator();
			while (itr.hasNext()) {
				
				models.server.Bullet bullet = itr.next();
				if (bullet.update(tiles, fullCharacters, this)) {
					itr.remove();
				}
				else{
					boxes.add(new Box(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight(),
							bullet.getR(), bullet.getG(), bullet.getB(), -1L, -1, -1, null));
				}
			}
		}
		//updating character
		x += xVel;
		if (x < 0 || x + width > Main.MAP_WIDTH) {
			x -= xVel;
		}
		y += yVel;
		if (y < 0 || y + height > Main.MAP_HEIGTH) {
			y -= yVel;
		}
		
		//checking collision with obstacles
		Iterator<Box> collidedBoxes = new CollidedBoxIterator(tiles, this);
		while (collidedBoxes.hasNext()) {
			collidedBoxes.next();
			x -= xVel;
			y -= yVel;
		}
		
		//if hp is below 1 we reset player to its initial position
		if (hp < 1){
			// playerSounds.getExplosion().play();
			x = y = 0;
			hp = fullHp;
		}
		
		boxes.add(new Box(x, y, width, height, r, g, b, id, hp, xp, nickname));
		return boxes;
	}

	public long getID(){
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getHp() {
		return hp;
	}

	public int getFullHp() {
		return fullHp;
	}

	public float getR(){
		return r;
	}
	
	public float getG(){
		return g;
	}
	
	public float getB(){
		return b;
	}

	public List<models.server.Bullet> getBullets() {
		return bullets;
	}

    public int getxVel() {
        return xVel;
    }

    public int getyVel() {
        return yVel;
    }

    public void setR(float r) {
		this.r = r;
	}

	public void setG(float g) {
		this.g = g;
	}

	public void setB(float b) {
		this.b = b;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setBullets(List<models.server.Bullet> bullets) {
		this.bullets = bullets;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setFullHp(int fullHp) {
		this.fullHp = fullHp;
	}

	public SpaceshipType getType() {
		return type;
	}

	public void setType(SpaceshipType type) {
		this.type = type;
	}

    public void setxVel(int xVel) {
        this.xVel = xVel;
    }

    public void setyVel(int yVel) {
        this.yVel = yVel;
    }

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MainCharacter character = (MainCharacter) o;
		return Float.compare(character.r, r) == 0 &&
				Float.compare(character.g, g) == 0 &&
				Float.compare(character.b, b) == 0 &&
				x == character.x &&
				y == character.y &&
				width == character.width &&
				height == character.height &&
				id == character.id &&
				xVel == character.xVel &&
				yVel == character.yVel &&
				hp == character.hp &&
				fullHp == character.fullHp &&
				type == character.type &&
				Objects.equals(bullets, character.bullets);
	}

	@Override
	public int hashCode() {
		return Objects.hash(r, g, b, x, y, width, height, type, bullets, id, xVel, yVel, hp, fullHp);
	}
}
