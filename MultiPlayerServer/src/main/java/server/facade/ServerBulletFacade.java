package server.facade;

import server.models.server.Bullet;
import server.models.server.SpeedoBullet;
import server.models.server.TankBullet;

public class ServerBulletFacade {

    public static Bullet createSpeedoBullet(server.models.Bullet bullet, float r, float g, float b)
    {
        return new SpeedoBullet(bullet, r, g, b);
    }

    public static Bullet createTankBullet(server.models.Bullet bullet, float r, float g, float b)
    {
        return new TankBullet(bullet, r, g, b);
    }

    public static Bullet createCruiserBullet(server.models.Bullet bullet, float r, float g, float b)
    {
        return new Bullet(bullet, r, g, b);
    }
}
