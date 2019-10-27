package sound;


import enumerators.SoundFile;

public class PlayerSounds {

    private SoundEffect explosion;

    public PlayerSounds() {
        explosion = new SoundEffect(PlayerSounds.class.getClassLoader().getResource(SoundFile.EXPLOSION.getPath()).getFile());
    }

    public SoundEffect getExplosion() {
        return explosion;
    }
}
