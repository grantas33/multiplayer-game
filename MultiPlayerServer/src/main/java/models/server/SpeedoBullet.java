package models.server;

public class SpeedoBullet extends Bullet {
    public SpeedoBullet(models.Bullet bullet, float r, float g, float b) {
        super(bullet, r, g, b);
        if (bullet.decor.contains("BigBullets") && bullet.decor.contains("BigDamageBullets")) {
            setSpeed(16).setDamage(15).setWidth(8).setHeight(8).setRange(800);
        } else if (bullet.decor.contains("BigBullets")) {
            setSpeed(16).setDamage(10).setWidth(8).setHeight(8).setRange(800);
        } else if (bullet.decor.contains("BigDamageBullets")) {
            setSpeed(16).setDamage(15).setWidth(5).setHeight(5).setRange(800);
        } else {
            setSpeed(16).setDamage(10).setWidth(5).setHeight(5).setRange(800);
        }
    }
}
