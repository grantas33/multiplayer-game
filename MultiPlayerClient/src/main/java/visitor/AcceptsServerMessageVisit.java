package visitor;

public interface AcceptsServerMessageVisit {
    void accept(ServerMessageVisitor visitor);
}
