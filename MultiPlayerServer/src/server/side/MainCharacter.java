package server.side;


import java.util.*;

import server.side.enumerators.SpaceshipType;
import server.side.facade.ServerBulletFacade;
import server.side.models.Box;
import server.side.models.Bullet;
import server.side.models.CharacterObj;

public class MainCharacter{

	private float r;
	private float g;
	private float b;

	private int x;
	private int y;
	private int width;
	private int height;
	private SpaceshipType type;
	//Thread safe list because bullets can be updated while 
	//iterating them which would resolve in an error.
	private List<server.side.models.server.Bullet> bullets;

	private long id;

	private int xVel;
	private int yVel;

	private int hp;
	private int fullHp;

	public void reduceHp(int reduce) {
		hp -= reduce;
	}

	public MainCharacter() { }

	/**
	 *
	 * @param width
	 * @param height
	 * @param hp
	 * @param id
	 * @param newBullets
	 */
	public MainCharacter(int width, int height, int hp, long id, List<Bullet> newBullets) {
		
		x = 0;
		y = 0;
		
		Random randomFloat = new Random(System.currentTimeMillis());
		r = randomFloat.nextFloat();
		b = randomFloat.nextFloat();
		g = randomFloat.nextFloat();

		xVel = 0;
		yVel = 0;
		this.width = width;
		this.height = height;
		this.hp = hp;
		fullHp = hp;
		this.id = id;

		bullets = Collections.synchronizedList(new ArrayList<server.side.models.server.Bullet>());
		addBullets(newBullets);
	}
	
	/**
	 * If player already exists we need only to update velocity and add new bullets.
	 * @param data This is data that we get from client side and put on the server side
	 * to simulate game.
	 */
	
	void updateState(CharacterObj data) {
		
		xVel = data.xVel;
		yVel = data.yVel;
		addBullets(data.newBullets);
	}	

	/**
	 * Function to add bullets that we get from client side to corresponding character.
	 * @param newBullets New bullets.
	 */

	public void addBullets(List<Bullet> newBullets){
		if (newBullets == null)	return;
		
		for (Bullet sb : newBullets) {
//			bullets.add(ServerBulletFacade.createSpeedoBullet(sb, r, g, b));
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
			
			Iterator<server.side.models.server.Bullet> itr = bullets.listIterator();
			while (itr.hasNext()) {
				
				server.side.models.server.Bullet bullet = itr.next();
				if (bullet.update(tiles, fullCharacters, id)) {
					itr.remove();
				}
				else{
					boxes.add(new Box(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight(),
							bullet.getR(), bullet.getG(), bullet.getB(), -1L, -1));
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
		for (Box obs : tiles) {
			if (LogicHelper.collision(x, y, x + width, y + height,
					obs.x, obs.y, obs.x + obs.w, obs.y + obs.h)){
				x -= xVel;
				y -= yVel;
			}
		}
		
		//if xp is below 1 we reset player to its initial position
		if (hp < 1){
			x = y = 0;
			hp = fullHp;
		}
		
		boxes.add(new Box(x, y, width, height, r, g, b, id, hp));
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

	public List<server.side.models.server.Bullet> getBullets() {
		return bullets;
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

	public void setBullets(List<server.side.models.server.Bullet> bullets) {
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
