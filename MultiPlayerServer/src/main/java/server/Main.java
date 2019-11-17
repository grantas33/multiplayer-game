package server;

import server.builder.CruiserBuilder;
import server.builder.SpeedoBuilder;
import server.builder.TankBuilder;
import server.factory.MainCharacterFactory;

public class Main {
    public static void main(String[] args) {

        if (args.length != 1)
            throw new IllegalArgumentException("Bad input");

        MainCharacterFactory mcFactory = new MainCharacterFactory(
                new SpeedoBuilder(),
                new TankBuilder(),
                new CruiserBuilder()
        );

        server.service.Main main = new server.service.Main(
                Integer.parseInt(args[0]),
                mcFactory
        );
        main.start();
    }
}
