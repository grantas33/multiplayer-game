package models.server;

import service.LogicHelper;
import service.Main;
import service.MainCharacter;
import models.Box;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * ServerBullet class represents bullets of main character
 */
public class Bullet {

    private float direc, speed; 		// y=kx+c going up or down
    private float k, c, x, y; 	// y=kx+c
    private int width, height;
    private float r, g,b;
    private int damage, range;

    public Bullet setDamage(int damage) {
        this.damage = damage;
        return this;
    }

    public Bullet setRange(int range) {
        this.range = range;
        return this;
    }

    public Bullet setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public Bullet setWidth(int width) {
        this.width = width;
        return this;
    }

    public Bullet setHeight(int height) {
        this.height = height;
        return this;
    }

    public float getDirec() {
        return direc;
    }

    public float getSpeed() {
        return speed;
    }

    public float getK() {
        return k;
    }

    public float getC() {
        return c;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public Bullet(models.Bullet bullet, float r, float g, float b){

        this.x = bullet.x;
        this.y = bullet.y;
        this.c = bullet.c;
        this.k = bullet.k;
        this.direc = bullet.pn;
        this.r = r;
        this.g = g;
        this.b = b;

        speed = 8;
        damage = 30;
        width = height = 10;
        range = 1000;
    }

    /**
     * Updates bullets state.
     * @param obstacles		Simple square obstacles .
     * @param fullCharacters	All characters.
     * @param id	Id of this character so we don't check collision with itself.
     * @return If there was a collision or bullet ran out of range returns true otherwise false.
     */

    public boolean update(List<Box> obstacles, Vector<MainCharacter> fullCharacters, long id) {

        if (range < 1) {
            return true;
        }

        //collision with tiles
        for (Box obs : obstacles) {
            if (LogicHelper.collision(x, y, x + width, y + height,
                    obs.x, obs.y, obs.x + obs.w, obs.y + obs.h)) {
                return true;
            }
        }
        //collision with enemies
        for (MainCharacter mc : fullCharacters){
            if (mc.getID() == id)
                continue;
            if (LogicHelper.collision(x, y, x + width, y + height,
                    mc.getX(), mc.getY(), mc.getX() + mc.getWidth(), mc.getY() + mc.getHeight())){
                mc.reduceHp(damage);
                return true;
            }
        }

        //collision with map
        if (x < 0 || x > Main.MAP_WIDTH || y < 0 || y > service.Main.MAP_HEIGTH) {
            return true;
        }

        /*
         * Super cool formula to find next x that is d (distance) away from
         * starting point.
         * Used distance formula and wolfram alpha to express next x position
         */
        x = (float) (-c * k + x + k * y - direc
                * Math.sqrt(-c * c + speed * speed + speed * speed * k * k - 2 * c * k * x
                - k * k * x * x + 2 * c * y + 2 * k * x * y - y * y))
                / (1 + k * k);
        y = k * x + c;
        range -= speed;

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bullet bullet = (Bullet) o;
        return Float.compare(bullet.direc, direc) == 0 &&
                Float.compare(bullet.speed, speed) == 0 &&
                Float.compare(bullet.k, k) == 0 &&
                Float.compare(bullet.c, c) == 0 &&
                Float.compare(bullet.x, x) == 0 &&
                Float.compare(bullet.y, y) == 0 &&
                width == bullet.width &&
                height == bullet.height &&
                Float.compare(bullet.r, r) == 0 &&
                Float.compare(bullet.g, g) == 0 &&
                Float.compare(bullet.b, b) == 0 &&
                damage == bullet.damage &&
                range == bullet.range;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direc, speed, k, c, x, y, width, height, r, g, b, damage, range);
    }
}