package sound;

import enumerators.SoundFile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SoundEffectTest {

    SoundEffect se;

    @BeforeAll
    void setUp() {
        se = new SoundEffect(PlayerSounds.class.getClassLoader().getResource(SoundFile.SHOT.getPath()).getFile());
    }

    @Test
    void playTest() {
        se.play();
    }

    @Test
    void stopTest() {
        se.stop();
    }
}
