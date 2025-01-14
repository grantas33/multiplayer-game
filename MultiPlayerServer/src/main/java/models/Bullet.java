package models;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Bullet implements Serializable{
	
	public float x;
	public float y;
	public float k;
	public float c;
	public float pn;
	public String decor;
	
	public Bullet(){}

	
	public Bullet(float x, float y, float k, float c, float pn,
				  int width, int height, String decor){
		this.x = x;
		this.y = y;
		this.k = k;
		this.c = c;
		this.pn = pn;
		this.decor = decor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Bullet bullet = (Bullet) o;
		return Float.compare(bullet.x, x) == 0 &&
				Float.compare(bullet.y, y) == 0 &&
				Float.compare(bullet.k, k) == 0 &&
				Float.compare(bullet.c, c) == 0 &&
				Float.compare(bullet.pn, pn) == 0 &&
				bullet.decor.equals(decor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, k, c, pn, decor);
	}
}
