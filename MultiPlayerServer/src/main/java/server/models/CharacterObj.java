package server.models;

import server.enumerators.SpaceshipType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterObj implements Serializable{

	public int xVel;
	public int yVel;
	public SpaceshipType type;
	public long id;
	
	public List<Bullet> newBullets;
	
	public CharacterObj(){}

	public CharacterObj(int xVel, int yVel, SpaceshipType type, long id) {
		this.xVel = xVel;
		this.yVel = yVel;
		this.type = type;
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CharacterObj that = (CharacterObj) o;
		return xVel == that.xVel &&
				yVel == that.yVel &&
				id == that.id &&
				type == that.type &&
				Objects.equals(newBullets, that.newBullets);
	}

	@Override
	public int hashCode() {
		return Objects.hash(xVel, yVel, type, id, newBullets);
	}

	@Override
	public String toString() {
		return "CharacterObj{" +
				"xVel=" + xVel +
				", yVel=" + yVel +
				", type=" + type +
				", id=" + id +
				", newBullets=" + newBullets +
				'}';
	}
}
