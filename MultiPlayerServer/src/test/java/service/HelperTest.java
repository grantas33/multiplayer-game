package service;

import enumerators.SpaceshipType;
import models.Box;
import models.Bullet;
import models.CharacterObj;
import models.ServerMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.bind.JAXBException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {

    private static ServerMessage generateServerMessage(SpaceshipType spaceshipType, long id, List<Bullet> bullets) {
        CharacterObj charData = new CharacterObj(0, 0, spaceshipType, id);
        charData.newBullets = bullets;
        ServerMessage sm = new ServerMessage(2);
        sm.id = 0;
        sm.port = 0;
        sm.setCharacterData(charData);

        return sm;
    }

    private static Helper.WrapperList generateWrapperList(List<Box> boxes) {
        Helper.WrapperList wl = new Helper.WrapperList();
        wl.addAll(boxes);

        return wl;
    }

    private static Stream<Arguments> unmarshallDataProvider() {

        Bullet bullet1 = new Bullet(230.0f, 280.0f, -0.565f, 409.95f, -1.0f);

        return Stream.of(
                Arguments.of(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                                "<serverMessage><characterData><xVel>0</xVel><yVel>0</yVel>" +
                                "<type>CRUISER</type><id>0</id><newBullets><x>230.0</x><y>280.0" +
                                "</y><k>-0.565</k><c>409.95</c><pn>-1.0</pn></newBullets><strategy/>" +
                                "<strategies/><strategies/><strategies/><strategies/></characterData>" +
                                "<messageType>2</messageType><id>0</id><port>0</port></serverMessage>",
                        generateServerMessage(SpaceshipType.CRUISER, 0, Collections.singletonList(bullet1))
                ),
                Arguments.of(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><serverMessage>" +
                                "<characterData><xVel>0</xVel><yVel>0</yVel><type>SPEEDO</type><id>1" +
                                "</id><strategy/><strategies/><strategies/><strategies/><strategies/>" +
                                "</characterData><messageType>2</messageType><id>0</id><port>0</port>" +
                                "</serverMessage>\n",
                        generateServerMessage(SpaceshipType.SPEEDO, 1, null)
                )
        );
    }

    private static Stream<Arguments> marshallDataProvider() {

        Box box1 = new Box(255.36542f, 247.02377f, 5, 5, 0.96497494f, 0.03887391f, 0.42293137f, -1L, -1);
        Box box2 = new Box(125.0f, 260.0f, 50, 50, 0.96497494f, 0.03887391f, 0.42293137f, 0L, 100);

        return Stream.of(
                Arguments.of(
                        generateWrapperList(Arrays.asList(box1, box2)),
                        "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>" +
                                "<wrapperList><realList><x>255.36542</x><y>247.02377</y>" +
                                "<w>5</w><h>5</h><xp>-1</xp><r>0.96497494</r><g>0.03887391</g>" +
                                "<b>0.42293137</b><id>-1</id></realList><realList><x>125.0</x><y>260.0</y>" +
                                "<w>50</w><h>50</h><xp>100</xp><r>0.96497494</r><g>0.03887391" +
                                "</g><b>0.42293137</b><id>0</id></realList></wrapperList>"
                ),
                Arguments.of(
                        generateWrapperList(Collections.singletonList(box2)),
                        "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><wrapperList>" +
                                "<realList><x>125.0</x><y>260.0</y><w>50</w><h>50</h><xp>100</xp>" +
                                "<r>0.96497494</r><g>0.03887391</g><b>0.42293137</b><id>0</id></realList>" +
                                "</wrapperList>"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("unmarshallDataProvider")
    void itShouldUnmarshallData(String data, ServerMessage expected) {
        try {
            assertEquals(expected, Helper.unmarshall(data));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("marshallDataProvider")
    void itShouldMarshallData(Helper.WrapperList data, String expected) {
        try {
            assertEquals(expected, Helper.marshall(data));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}