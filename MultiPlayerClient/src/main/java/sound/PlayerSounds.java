package sound;

import enumerators.SoundFile;

public class PlayerSounds {

    private SoundEffect fire;

    public PlayerSounds() {
        fire = new SoundEffect(PlayerSounds.class.getClassLoader().getResource(SoundFile.SHOT.getPath()).getFile());
    }

    public SoundEffect getFire() {
        return fire;
    }
}
