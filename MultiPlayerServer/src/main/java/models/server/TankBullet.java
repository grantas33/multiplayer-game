package models.server;

public class TankBullet extends Bullet {
    public TankBullet(models.Bullet bullet, float r, float g, float b) {
        super(bullet, r, g, b);
        setSpeed(6).setDamage(60).setWidth(15).setHeight(15).setRange(1200);
    }
}
