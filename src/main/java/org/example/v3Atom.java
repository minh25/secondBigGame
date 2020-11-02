package org.example;

import engine.GameWorld;
import engine.Sprite;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class v3Atom extends Sprite {

    public v3Atom(double radius, Color fill, boolean gradientFill) {
        Circle sphere = new Circle();
        sphere.setCenterX(radius);
        sphere.setCenterY(radius);
        sphere.setRadius(radius);
        sphere.setFill(fill);
        sphere.setCache(true);
        sphere.setCacheHint(CacheHint.SPEED);

        if (gradientFill) {
            RadialGradient rgrad = new RadialGradient(0,
                                                        0.1,
                                                        sphere.getCenterX() - sphere.getRadius() / 3,
                                                        sphere.getCenterY() - sphere.getRadius() / 3,
                                                        sphere.getRadius(),
                                                        false,
                                                        CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.WHITE),
                                                        new Stop(1.0, fill));

            sphere.setFill(rgrad);
        }
        node = sphere;
        collisionBound = sphere;
    }

    @Override
    public void update() {
        node.setTranslateX(node.getTranslateX() + vX);
        node.setTranslateY(node.getTranslateY() + vY);
    }

    public Circle getAsCircle() {
        return (Circle) node;
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

    public void handleDeath(GameWorld gameWorld) {
        implode(gameWorld);
        super.handleDeath(gameWorld);
    }
}
