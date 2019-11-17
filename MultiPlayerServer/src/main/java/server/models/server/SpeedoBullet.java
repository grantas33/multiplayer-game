package server.models.server;

public class SpeedoBullet extends Bullet {
    public SpeedoBullet(server.models.Bullet bullet, float r, float g, float b) {
        super(bullet, r, g, b);
        setSpeed(16).setDamage(5).setWidth(5).setHeight(5).setRange(800);
    }
}
