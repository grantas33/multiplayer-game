package flyweight;

import models.gameObjectsComposite.Bullet;

import java.util.HashMap;

public class BulletsHashMap {
    private static final HashMap<Float, Bullet> BULLETS_BY_K = new HashMap<Float, Bullet>();

    public static Bullet getBullet(Float k, float xMain, float yMain,
                                   float c, float pnx, String decor) {
        Bullet bullet = BULLETS_BY_K.get(k);
        if (bullet == null) {
            bullet = new Bullet(xMain, yMain, k, c, pnx, decor);
            BULLETS_BY_K.put(k, bullet);
        } /*else {
            System.out.println("Bullet from HashMap");
        }*/
        return bullet;
    }
}
