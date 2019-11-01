import builder.CruiserBuilder;
import builder.SpeedoBuilder;
import builder.TankBuilder;
import factory.MainCharacterFactory;

public class Main {
    public static void main(String[] args) {

        if (args.length != 1)
            throw new IllegalArgumentException("Bad input");

        MainCharacterFactory mcFactory = new MainCharacterFactory(
                new SpeedoBuilder(),
                new TankBuilder(),
                new CruiserBuilder()
        );

        service.Main main = new service.Main(
                Integer.parseInt(args[0]),
                mcFactory
        );
        main.start();
    }
}
