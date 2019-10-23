package client.side.sound;

import client.side.enumerators.SoundFile;

public class PlayerSounds {

    private SoundEffect fire;

    public PlayerSounds() {
        fire = new SoundEffect(SoundFile.SHOT.getPath());
    }

    public SoundEffect getFire() {
        return fire;
    }
}
