package client;

public class Main {
    public static void main(String[] args) {

        if (args.length != 3){
            throw new IllegalArgumentException("Bad input. You need [REMOTE IP] [REMOTE TCP PORT] [LOCAL UDP PORT or -1 for random port]");
        }

        client.service.Main main = new client.service.Main(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        main.initOpenGl();
        main.init();
        main.start();
    }
}
