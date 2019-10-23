package client.side.sound;

import org.lwjgl.Sys;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class SoundEffect {

    private Clip clip;

    public SoundEffect(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
//            URL soundFile = this.getClass().getResource("shot.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.setFramePosition(0);
        clip.setMicrosecondPosition(0);
        clip.start();
        System.out.println("should play sound");
//        while(clip.isRunning()){}
//        clip.close();
    }

//    public void setVolume(float vol) {
//        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
//        System.out.println(volume.getValue());
////        volume.setValue(vol);
//    }

    public void stop()
    {
        clip.stop();
        clip.setFramePosition(0);
    }
}