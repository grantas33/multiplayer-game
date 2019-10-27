package enumerators;

public enum SoundFile {
        EXPLOSION("sound/player/explosion.wav");

    private String path;

    SoundFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
