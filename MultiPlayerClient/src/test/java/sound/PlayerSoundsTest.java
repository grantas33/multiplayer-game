package sound;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerSoundsTest {

    @Test
    void constructorTest() {
        PlayerSounds ps = new PlayerSounds();
        assertEquals(SoundEffect.class, ps.getFire().getClass());
    }
}
