package facade;

import models.composite.Bullet;
import models.server.SpeedoBullet;
import models.server.TankBullet;

public class ServerBulletFacade {

    public static Bullet createSpeedoBullet(models.Bullet bullet, float r, float g, float b)
    {
        return new SpeedoBullet(bullet, r, g, b);
    }

    public static Bullet createTankBullet(models.Bullet bullet, float r, float g, float b)
    {
        return new TankBullet(bullet, r, g, b);
    }

    public static Bullet createCruiserBullet(models.Bullet bullet, float r, float g, float b)
    {
        return new Bullet(bullet, r, g, b);
    }
}
