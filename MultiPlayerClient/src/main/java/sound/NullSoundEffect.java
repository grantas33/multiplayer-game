package sound;

public class NullSoundEffect extends AbstractSoundEffect {
    public NullSoundEffect(String soundFileName) {
        super(soundFileName);
    }

    public void play() {
    }

    public void stop() {
    }
}
