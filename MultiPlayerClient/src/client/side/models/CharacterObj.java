package client.side.models;

import client.side.enumerators.SpaceshipType;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterObj implements Serializable{

	public int xVel;
	public int yVel;

	public int hp;
	public int width;
	public int height;

	public SpaceshipType type;
	
	public long id;
	
	public List<Bullet> newBullets;
	
	public CharacterObj(){}

	public CharacterObj(int xVel, int yVel, SpaceshipType type, long id, int hp, int width, int height) {
		this.xVel = xVel;
		this.yVel = yVel;
		this.type = type;
		this.id = id;
		this.hp = hp;
		this.height = height;
		this.width = width;
	}
}
