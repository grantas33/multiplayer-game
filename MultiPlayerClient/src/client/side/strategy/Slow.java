package client.side.strategy;

public class Slow extends Strategy {
    @Override
    public int speedIndicator() {
        return 1;
    }
}
