package models.server;

public class SpeedoBullet extends Bullet {
    public SpeedoBullet(models.Bullet bullet, float r, float g, float b) {
        super(bullet, r, g, b);
        if (bullet.decor.contains("BigBullets") && bullet.decor.contains("BigDamageBullets")) {
            setSpeed(16).setDamage(50).setWidth(20).setHeight(20).setRange(800);
        } else if (bullet.decor.contains("BigBullets")) {
            setSpeed(16).setDamage(10).setWidth(20).setHeight(20).setRange(800);
        } else if (bullet.decor.contains("BigDamageBullets")) {
            setSpeed(16).setDamage(50).setWidth(5).setHeight(5).setRange(800);
        } else {
            setSpeed(16).setDamage(10).setWidth(5).setHeight(5).setRange(800);
        }
    }
}
