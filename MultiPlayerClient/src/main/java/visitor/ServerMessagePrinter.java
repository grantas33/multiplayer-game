package visitor;

import models.ServerMessage;
import models.gameObjectsComposite.Bullet;
import models.gameObjectsComposite.CharacterObj;
import models.gameObjectsComposite.GameObject;

public class ServerMessagePrinter implements ServerMessageVisitor {

    StringBuilder sb;

    public ServerMessagePrinter(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void visit(ServerMessage sm) {
        sb.append("Message inludes ");
        if (sm.getCharacterData() != null) {
            sm.getCharacterData().accept(this);
        } else {
            sb.append("no character");
        }
    }

    @Override
    public void visit(CharacterObj character) {
        sb.append("character named ");
        sb.append(character.nickname);
        if (character.newBullets.size() > 0) {
            sb.append("with bullets in ");
            for(GameObject bullet : character.newBullets) {
                bullet.accept(this);
            }
        }
    }

    @Override
    public void visit(Bullet bullet) {
        sb.append("x: ");
        sb.append(bullet.x);
        sb.append(", y: ");
        sb.append(bullet.y);
        sb.append(" | ");
    }
}
