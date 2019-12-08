package models.server;

public class TankBullet extends Bullet {
    public TankBullet(models.Bullet bullet, float r, float g, float b) {
        super(bullet, r, g, b);
        if (bullet.decor.contains("BigBullets") && bullet.decor.contains("BigDamageBullets")) {
            setSpeed(6).setDamage(100).setWidth(30).setHeight(30).setRange(800);
        } else if (bullet.decor.contains("BigBullets")) {
            setSpeed(6).setDamage(50).setWidth(30).setHeight(30).setRange(800);
        } else if (bullet.decor.contains("BigDamageBullets")) {
            setSpeed(6).setDamage(100).setWidth(15).setHeight(15).setRange(800);
        } else {
            setSpeed(6).setDamage(50).setWidth(15).setHeight(15).setRange(800);
        }
    }
}
