package models.server;

public class SpeedoBullet extends Bullet {
    public SpeedoBullet(models.Bullet bullet, float r, float g, float b) {
        super(bullet, r, g, b);
        if (bullet.decor.contains("SuperBullets")) {
            setSpeed(16).setDamage(5).setWidth(30).setHeight(30).setRange(800);
        } else {
            setSpeed(16).setDamage(5).setWidth(5).setHeight(5).setRange(800);
        }
    }
}
