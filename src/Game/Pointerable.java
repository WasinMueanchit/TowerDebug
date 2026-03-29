
package Game;

import java.awt.Rectangle;

public interface Pointerable {
    public Rectangle getSolidArea();
    public int getX();
    public int getY();
    public void setCollisionOn(boolean collisionOn);
}
