package client.side;

import client.side.enumerators.SpaceshipType;
import client.side.models.CharacterObj;
import client.side.models.ServerMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.bind.JAXBException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {

    private static ServerMessage generateServerMessage() {
        CharacterObj charData = new CharacterObj(100, 100, SpaceshipType.CRUISER, 10);
        ServerMessage sm = new ServerMessage(2);
        sm.setId(1);
        sm.setPort(2);
        sm.setCharacterData(charData);

        return sm;
    }

    private static ArrayList<ServerMessage> serverMessageGenerator() {
        return new ArrayList<ServerMessage>() {
            {
                add(generateServerMessage());
                add(generateServerMessage());
                add(generateServerMessage());
            }
        };
    }

    @ParameterizedTest
    @MethodSource("serverMessageGenerator")
    void marshall() throws JAXBException {
        CharacterObj charData = new CharacterObj(100, 100, SpaceshipType.CRUISER, 10);
        ServerMessage sm = new ServerMessage(2);
        sm.setId(1);
        sm.setPort(2);
        sm.setCharacterData(charData);

        Helper.marshall(sm);


    }

    @org.junit.jupiter.api.Test
    void unmarshall() {
//        String data = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><wrapperList><realList><x>0.0</x><y>0.0</y><w>50</w><h>50</h><xp>80</xp><r>0.84502995</r><g>0.6242632</g><b>0.0062342286</b><id>0</id></realList><realList><x>195.0</x><y>325.0</y><w>50</w><h>50</h><xp>100</xp><r>0.49343354</r><g>0.73024106</g><b>0.012767494</b><id>1</id></realList></wrapperList>"
    }
}