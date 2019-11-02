package service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LogicHelperTest {

    private static Stream<Arguments> collisionDataProvider() {

        return Stream.of(
                Arguments.of(10, 10, 20, 20, 0, 0, 100, 100, true),
                Arguments.of(10, 10, 20, 20, 0, 0, 10, 10, false),
                Arguments.of(10, 10, 15, 15, 10, 10, 15, 15, true),
                Arguments.of(10, 10, 15, 15, 12, 12, 18, 18, true),
                Arguments.of(10, 10, 15, 15, 15, 15, 20, 20, false)
        );
    }

    @ParameterizedTest
    @MethodSource("collisionDataProvider")
    void itShouldCorrectlyDetermineCollision(float f, float h, float i, float j, float left, float top, float right, float bottom, boolean expected) {
        assertEquals(expected, LogicHelper.collision(f, h, i, j, left, top, right, bottom));
    }
}