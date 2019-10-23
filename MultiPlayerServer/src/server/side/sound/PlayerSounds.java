package server.side.sound;


import server.side.enumerators.SoundFile;

public class PlayerSounds {

    private SoundEffect explosion;

    public PlayerSounds() {
        explosion = new SoundEffect(SoundFile.EXPLOSION.getPath());
    }

    public SoundEffect getExplosion() {
        return explosion;
    }
}
