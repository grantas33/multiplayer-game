package command;

public interface ICommand {
    String execute();

    String undo();
}
