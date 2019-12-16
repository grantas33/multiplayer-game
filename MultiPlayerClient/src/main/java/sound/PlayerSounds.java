package sound;

import enumerators.SoundFile;

public class PlayerSounds {

    private AbstractSoundEffect fire;

    public PlayerSounds() {
        fire = initSoundEffect(SoundFile.SHOT.getPath());
    }

    private AbstractSoundEffect initSoundEffect(String path) {
        try {
           return new SoundEffect(PlayerSounds.class.getClassLoader().getResource(path).getFile());
        } catch (NullPointerException e) {
            return new NullSoundEffect(null);
        }
    }

    public AbstractSoundEffect getFire() {
        return fire;
    }
}
