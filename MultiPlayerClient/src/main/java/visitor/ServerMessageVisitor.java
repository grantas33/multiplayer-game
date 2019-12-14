package visitor;

import models.ServerMessage;
import models.gameObjectsComposite.Bullet;
import models.gameObjectsComposite.CharacterObj;

public interface ServerMessageVisitor {
    void visit(ServerMessage sm);
    void visit(CharacterObj character);
    void visit(Bullet bullet);

}
