package client.enumerators;

public enum SoundFile {
        SHOT("sound/player/shot.wav");

    private String path;

    SoundFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
