package client.side;

import client.side.factory.CharacterObjFactory;
import client.side.models.Box;
import client.side.models.Bullet;
import client.side.models.CharacterObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.internal.util.reflection.Whitebox;
import org.newdawn.slick.UnicodeFont;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {
    private Main main;

    @BeforeAll
    void setUp() {
        main = new Main("0", 1000, 1234);
        Whitebox.setInternalState(main, "uf", mock(UnicodeFont.class));
        Whitebox.setInternalState(main, "camera", mock(Main.Camera.class));
        Whitebox.setInternalState(main, "obstacles", Arrays.asList(mock(Box.class), mock(Box.class), mock(Box.class)));
        Whitebox.setInternalState(main, "movingObjects", Arrays.asList(mock(Box.class), mock(Box.class), mock(Box.class)));
        Whitebox.setInternalState(main, "bullets", new ArrayList(){{
                    add(mock(Bullet.class));
                    add(mock(Bullet.class));
                    add(mock(Bullet.class));
                }}
        );
        Whitebox.setInternalState(main, "character", mock(CharacterObj.class));
        Whitebox.setInternalState(main, "connections", mock(TcpConnection.class));
        main.initOpenGl();
    }


    @org.junit.jupiter.api.Test
    void testInitOpenGl() {
        try {
            main.initOpenGl();
        } catch (IllegalStateException ignored) { }
    }

    @org.junit.jupiter.api.Test
    void testLiveMatchGamePhaseLoop() {
        main.liveMatchGamePhaseLoop();
    }

    @org.junit.jupiter.api.Test
    void testUpdate() {
        main.update();
    }

    @org.junit.jupiter.api.Test
    void testRender() {
        main.render();
    }

    @org.junit.jupiter.api.Test
    void testDrawText() {
        main.drawText(10f, 10f, "Content");
    }

    @org.junit.jupiter.api.Test
    void testDrawSquare() {
        main.drawSquare(new Box(100, 200, CharacterObjFactory.SPEEDO_WIDTH, CharacterObjFactory.SPEEDO_HEIGHT, 255, 255, 255, 0, 0));
    }

    @org.junit.jupiter.api.Test
    void testPollInputForSpaceshipType() {
        assertNull(main.pollInputForSpaceshipType());
    }

    @org.junit.jupiter.api.Test
    void testSpaceshipSelectGamePhaseLoop() {
        main.spaceshipSelectGamePhaseLoop();
    }

    @org.junit.jupiter.api.Test
    void testSendCharacter() {
        main.sendCharacter();
    }

    @org.junit.jupiter.api.Test
    void updateListOfObjects() {
        ArrayList objs = new ArrayList(){{
            add(mock(Box.class));
            add(mock(Box.class));
            add(mock(Box.class));
        }};

        main.updateListOfObjects(objs);
        assertEquals(objs, Whitebox.getInternalState(main, "movingObjects"));
    }
}