package client.side;

import client.side.enumerators.SpaceshipType;
import client.side.factory.CharacterObjFactory;
import client.side.models.Box;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TcpConnectionTest {

    private TcpConnection tcp;
    private List<Box> map;

    @org.junit.jupiter.api.BeforeAll
    void setUp() throws IOException, ClassNotFoundException {
        map = new ArrayList<Box>() {
            {
                add(new Box(0, 0, 50, 50, 0.61452997f, 0.56365216f, 0.17192775f, 1, 0));
                add(new Box(245, 170, 50, 150, 0.5091696f, 0.9744757f, 0.65207505f, 2, 0));
            }
        };

        ObjectOutput oos = mock(ObjectOutput.class);
        ObjectInput ois = mock(ObjectInput.class);

        when(ois.readLong()).thenReturn((long) 1).thenReturn((long) 2).thenReturn((long) 3);
        doNothing().when(oos).writeObject(anyObject());
        when(ois.readObject()).thenReturn("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><wrapperList><realList><x>0.0</x><y>0.0</y><w>50</w><h>50</h><xp>100</xp><r>0.61452997</r><g>0.56365216</g><b>0.17192775</b><id>1</id></realList><realList><x>245.0</x><y>170.0</y><w>50</w><h>150</h><xp>200</xp><r>0.5091696</r><g>0.9744757</g><b>0.65207505</b><id>2</id></realList></wrapperList>");

        tcp = new TcpConnection("localhost", 1234, oos, ois);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void testGetIdFromServer(long expected) {
        assertEquals(expected, tcp.getIdFromServer());
    }

    @org.junit.jupiter.api.Test
    void testGetMapDetails() {
        assertArrayEquals(map.toArray(), tcp.getMapDetails().toArray());
    }

    @org.junit.jupiter.api.Test
    void testSendUpdatedVersion() {
        tcp.sendUpdatedVersion(CharacterObjFactory.createCharacterObj(SpaceshipType.CRUISER, 0));
    }

    @org.junit.jupiter.api.Test
    void testSendIpIdPort() {
        tcp.sendIpIdPort(1200);
    }

    @org.junit.jupiter.api.Test
    void testRemoveCharacter() {
        tcp.removeCharacter(0);
    }
}
