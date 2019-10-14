package server.side;

public class LogicHelper {

    /**
     * Function to check if two square objects collide.
     */
    public static boolean collision(float f, float h, float i, float j, float left,
                              float top, float right, float buttom) {
        return f < right && i > left && h < buttom && j > top;
    }
}
