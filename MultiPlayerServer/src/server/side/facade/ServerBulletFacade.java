package server.side.facade;

import server.side.models.server.Bullet;
import server.side.models.server.SpeedoBullet;
import server.side.models.server.TankBullet;

public class ServerBulletFacade {

    public static Bullet createSpeedoBullet(server.side.models.Bullet bullet, float r, float g, float b)
    {
        return new SpeedoBullet(bullet, r, g, b);
    }

    public static Bullet createTankBullet(server.side.models.Bullet bullet, float r, float g, float b)
    {
        return new TankBullet(bullet, r, g, b);
    }

    public static Bullet createCruiserBullet(server.side.models.Bullet bullet, float r, float g, float b)
    {
        return new Bullet(bullet, r, g, b);
    }
}
