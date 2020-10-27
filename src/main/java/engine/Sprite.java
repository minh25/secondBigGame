package engine;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

public abstract class Sprite {

    // current display node
    public Node node;

    // velocity vector x, y
    public double vX = 0;
    public double vy = 0;

    // dead?
    public boolean isDead = false;

    // collision shape
    public Circle collisionBound;

    // update per frame
    public abstract void update();

    public boolean collide(Sprite other) {
        if (collisionBound == null || other.collisionBound == null) {
            return false;
        }

        Circle otherSphere = other.collisionBound;
        Circle thisSphere = collisionBound;
        Point2D otherCenter = otherSphere.localToScene(otherSphere.getCenterX(), otherSphere.getCenterY());
        Point2D thisCenter = thisSphere.localToScene(thisSphere.getCenterX(), thisSphere.getCenterY());
        double dx = otherCenter.getX() - thisCenter.getX();
        double dy = otherCenter.getY() - thisCenter.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDist = otherSphere.getRadius() + thisSphere.getRadius();

        return (distance < minDist);
    }

    public void handleDeath(GameWorld gameWorld) {
        gameWorld.getSpriteManager().addSpritesToBeRemoved(this);
    }
}
