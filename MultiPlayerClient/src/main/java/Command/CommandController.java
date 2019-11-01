package Command;

import java.util.ArrayList;

public class CommandController {
    private ArrayList<ICommand> commands = new ArrayList<ICommand>();

    public String addCommandAndExecute(ICommand cmd) {
        if (commands.size() < 2) {
            commands.add(cmd);
        }
        return cmd.execute();
    }

    public String Undo(String decorated) {
        if (commands.size() != 0){
            ICommand cmd = commands.get(commands.size() - 1);
            commands.remove(cmd);
            return cmd.undo(decorated);
        }
        return "";
    }
}
