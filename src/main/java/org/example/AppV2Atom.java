package org.example;

import engine.GameWorld;
import engine.Sprite;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class AppV2Atom extends Sprite {

    public AppV2Atom(double radius, Color fill) {
        Circle sphere = new Circle();
        sphere.setCenterX(radius);
        sphere.setCenterY(radius);
        sphere.setRadius(radius);
        sphere.setCache(true);

        RadialGradient rgrad = new RadialGradient(0,
                                                    0.1,
                                                    sphere.getCenterX(),
                                                    sphere.getCenterY(),
                                                    sphere.getRadius(),
                                                    false,
                                                    CycleMethod.NO_CYCLE,
                                                    new Stop(0, fill),
                                                    new Stop(1, Color.BLACK));

        sphere.setFill(rgrad);

        node = sphere;
    }

    @Override
    public void update() {
        node.setTranslateX(node.getTranslateX() + vX);
        node.setTranslateY(node.getTranslateY() + vY);
    }

    @Override
    public boolean collide(Sprite other) {
        if (other instanceof AppV2Atom) {
            return collide((AppV2Atom)other);
        }
        return false;
    }

    private boolean collide(AppV2Atom other) {
        if (!node.isVisible() ||
            !other.node.isVisible() ||
            this == other) {
            return false;
        }

        Circle otherSphere = other.getAsCircle();
        Circle thisSphere = getAsCircle();
        double dx = otherSphere.getTranslateX() - thisSphere.getTranslateX();
        double dy = otherSphere.getTranslateY() - thisSphere.getTranslateY();
        double distance = Math.sqrt( dx * dx + dy * dy );
        double minDist  = otherSphere.getRadius() + thisSphere.getRadius() + 3;

        return (distance < minDist);
    }

    public Circle getAsCircle() {
        return (Circle)node;
    }

    public void implode(final GameWorld gameWorld) {
        vX = vY = 0;
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(node);
        fadeTransition.setDuration(Duration.millis(300));
        fadeTransition.setFromValue(node.getOpacity());
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isDead = true;
                gameWorld.getSceneNodes().getChildren().remove(node);
            }
        });
        fadeTransition.play();
    }
}
