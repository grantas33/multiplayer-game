package sound;


public class SoundEffect extends AbstractSoundEffect {
    public SoundEffect(String soundFileName) {
        super(soundFileName);
    }

    public void play() {
        stop();
        clip.start();
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
        clip.setMicrosecondPosition(0);
    }
}
