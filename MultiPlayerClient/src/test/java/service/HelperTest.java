package service;

import enumerators.SpaceshipType;
import models.Box;
import models.CharacterObj;
import models.ServerMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


class HelperTest {

    private static ServerMessage generateServerMessage(int messageType, SpaceshipType spaceshipType, long id) {
        CharacterObj charData = new CharacterObj(100, 100, spaceshipType, id);
        ServerMessage sm = new ServerMessage(messageType);
        sm.setId(1);
        sm.setPort(2);
        sm.setCharacterData(charData);

        return sm;
    }

    private static Stream<Arguments> serverMessageGenerator() {
        return Stream.of(
                Arguments.of(
                        generateServerMessage(0, SpaceshipType.CRUISER, 10),
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><serverMessage><characterData><xVel>100</xVel><yVel>100</yVel><type>CRUISER</type><id>10</id></characterData><messageType>0</messageType><id>1</id><port>2</port></serverMessage>"
                ),
                Arguments.of(
                        generateServerMessage(1, SpaceshipType.SPEEDO, 20),
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><serverMessage><characterData><xVel>100</xVel><yVel>100</yVel><type>SPEEDO</type><id>20</id></characterData><messageType>1</messageType><id>1</id><port>2</port></serverMessage>"
                ),
                Arguments.of(
                        generateServerMessage(2, SpaceshipType.TANK, 23),
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><serverMessage><characterData><xVel>100</xVel><yVel>100</yVel><type>TANK</type><id>23</id></characterData><messageType>2</messageType><id>1</id><port>2</port></serverMessage>"
                )
        );
    }

    private static Stream<Arguments> receivedMessageGenerator() {
        return Stream.of(
                Arguments.of(
                        "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><wrapperList><realList><x>30.0</x><y>120.0</y><w>50</w><h>50</h><xp>100</xp><r>0.61452997</r><g>0.56365216</g><b>0.17192775</b><id>1</id></realList></wrapperList>",
                        new ArrayList<Box>() {
                            {
                                add(new Box(30, 120, 50, 50, 0.61452997f, 0.56365216f, 0.17192775f, 1, 0));
                            }
                        }
                ),
                Arguments.of(
                        "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><wrapperList><realList><x>0.0</x><y>0.0</y><w>50</w><h>50</h><xp>100</xp><r>0.61452997</r><g>0.56365216</g><b>0.17192775</b><id>1</id></realList><realList><x>245.0</x><y>170.0</y><w>50</w><h>150</h><xp>200</xp><r>0.5091696</r><g>0.9744757</g><b>0.65207505</b><id>2</id></realList></wrapperList>",
                        new ArrayList<Box>() {
                            {
                                add(new Box(0, 0, 50, 50, 0.61452997f, 0.56365216f, 0.17192775f, 1, 0));
                                add(new Box(245, 170, 50, 150, 0.5091696f, 0.9744757f, 0.65207505f, 2, 0));
                            }
                        }
                ),
                Arguments.of(
                        "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><wrapperList><realList><x>0.0</x><y>0.0</y><w>50</w><h>50</h><xp>100</xp><r>0.61452997</r><g>0.56365216</g><b>0.17192775</b><id>1</id></realList><realList><x>443.50418</x><y>80.10693</y><w>10</w><h>10</h><xp>-1</xp><r>0.5091696</r><g>0.9744757</g><b>0.65207505</b><id>-1</id></realList><realList><x>493.09332</x><y>289.94666</y><w>10</w><h>10</h><xp>-1</xp><r>0.5091696</r><g>0.9744757</g><b>0.65207505</b><id>-1</id></realList><realList><x>245.0</x><y>235.0</y><w>50</w><h>150</h><xp>200</xp><r>0.5091696</r><g>0.9744757</g><b>0.65207505</b><id>2</id></realList></wrapperList>",
                        new ArrayList<Box>() {
                            {
                                add(new Box(0, 0, 50, 50, 0.61452997f, 0.56365216f, 0.17192775f, 1, 0));
                                add(new Box(443.50418f, 80.10693f, 10, 10, 0.5091696f, 0.9744757f, 0.65207505f, -1, 0));
                                add(new Box(493.09332f, 289.94666f, 10, 10, 0.5091696f, 0.9744757f, 0.65207505f, -1, 0));
                                add(new Box(245, 235, 50, 150, 0.5091696f, 0.9744757f, 0.65207505f, 2, 0));
                            }
                        }
                )
        );
    }

    @ParameterizedTest
    @MethodSource("serverMessageGenerator")
    void testMarshall(ServerMessage sm, String expected) throws JAXBException {
        assertEquals(expected, Helper.marshall(sm));
    }

    @ParameterizedTest
    @MethodSource("receivedMessageGenerator")
    void testUnmarshall(String message, List<Box> expected) throws JAXBException {
        assertArrayEquals(expected.toArray(), Helper.unmarshall(message).toArray());
    }
}
