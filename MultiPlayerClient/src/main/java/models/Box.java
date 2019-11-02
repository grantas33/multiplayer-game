package models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Box{
	
	public float x;
	public float y;
	public int w;
	public int h;
	
	public int hp;
	
	public float r;
	public float g;
	public float b;
	
	public long id;
	public String title;
	
	public Box(){}

	public Box(float x, float y, int width, int height, float r, float g, float b, long id, int hp) {
		this.r = r;
		this.b = b;
		this.g = g;

		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		
		this.id= id;
		this.hp = hp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Box box = (Box) o;

		return Float.compare(box.r, r) == 0 &&
				Float.compare(box.g, g) == 0 &&
				Float.compare(box.b, b) == 0 &&
				x == box.x &&
				y == box.y &&
				w == box.w &&
				h == box.h &&
				id == box.id &&
				hp == box.hp;
	}
}