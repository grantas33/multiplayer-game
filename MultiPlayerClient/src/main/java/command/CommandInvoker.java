package command;

import java.util.ArrayList;

public class CommandInvoker {
    private ArrayList<ICommand> commands = new ArrayList<ICommand>();

    public String addCommandAndExecute(ICommand cmd) {
        if (commands.size() < 3) {
            commands.add(cmd);
        }
        return cmd.execute();
    }

    public String undo() {
        if (commands.size() != 0){
            ICommand cmd = commands.get(commands.size() - 1);
            commands.remove(cmd);
            return cmd.undo();
        }
        return "";
    }
}
