package server.models;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Box implements Serializable{
	
	public float x;
	public float y;
	public int w;
	public int h;
	
	public int xp;
	
	public float r;
	public float g;
	public float b;
	
	public long id;
	
	public Box(){}
	
	public Box(float x, float y, int width, int height, float r, float g, float b, long id, int xp) {
		this.r = r;
		this.b = b;
		this.g = g;

		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		
		this.id= id;
		this.xp = xp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Box box = (Box) o;
		return Float.compare(box.x, x) == 0 &&
				Float.compare(box.y, y) == 0 &&
				w == box.w &&
				h == box.h &&
				xp == box.xp &&
				Float.compare(box.r, r) == 0 &&
				Float.compare(box.g, g) == 0 &&
				Float.compare(box.b, b) == 0 &&
				id == box.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, w, h, xp, r, g, b, id);
	}

	@Override
	public String toString() {
		return "Box{" +
				"x=" + x +
				", y=" + y +
				", w=" + w +
				", h=" + h +
				", xp=" + xp +
				", r=" + r +
				", g=" + g +
				", b=" + b +
				", id=" + id +
				'}';
	}
}