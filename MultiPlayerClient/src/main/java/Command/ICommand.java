package Command;

public interface ICommand {
    String execute();

    String undo(String decorated);
}
