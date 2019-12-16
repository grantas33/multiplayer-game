package sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public abstract class AbstractSoundEffect {

    protected Clip clip;

    public AbstractSoundEffect(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        stop();
        clip.start();
    }

    public void stop()
    {
        clip.stop();
        clip.setFramePosition(0);
        clip.setMicrosecondPosition(0);
    }
}
