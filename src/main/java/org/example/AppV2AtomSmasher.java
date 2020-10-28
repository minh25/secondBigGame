package org.example;

import engine.GameWorld;
import engine.Sprite;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Random;

public class AppV2AtomSmasher extends GameWorld {

    private final static Label NUM_SPRITES_FIELD = new Label();

    public AppV2AtomSmasher(int fps, String title) {
        super(fps, title);
    }

    @Override
    public void initialize(Stage primaryStage) {
        primaryStage.setTitle(getWindowTile());

        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 640, 580));
        primaryStage.setScene(getGameSurface());

        generateManySpheres(50);

        final Timeline gameLoop = getGameLoop();
        VBox stats = new VBox();
        stats.setSpacing(5);
        stats.setTranslateX(10);
        stats.setTranslateY(10);
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        Label label = new Label("Number of Particles: ");
        Button button1 = new Button();
        button1.setText("Regenerate");
        button1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                generateManySpheres(50);
            }
        });
        Button button2 = new Button();
        button2.setText("Freeze/Resume");
        button2.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                switch (gameLoop.getStatus()) {
                    case RUNNING:
                        gameLoop.stop();
                        break;
                    case STOPPED:
                        gameLoop.play();
                        break;
                }
            }
        });
        hBox.getChildren().addAll(label,
                NUM_SPRITES_FIELD,
                button1,
                button2);
        stats.getChildren().add(hBox);

        getSceneNodes().getChildren().add(stats);
    }

    private void generateManySpheres(int numSpheres) {
        Random rnd = new Random();
        Scene gameSurface = getGameSurface();

        for (int i = 0; i < numSpheres; i++) {
            Color color = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            AppV2Atom atom = new AppV2Atom(rnd.nextInt(15) + 5, color);
            Circle circle = atom.getAsCircle();

            atom.vX = (rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1);
            atom.vY = (rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1);

            double newX = rnd.nextInt((int) gameSurface.getWidth());
            double newY = rnd.nextInt((int) gameSurface.getHeight());

            if (newX > (gameSurface.getWidth() - (circle.getRadius() * 2))) {
                newX = gameSurface.getWidth() - (circle.getRadius() * 2);
            }
            if (newY > (gameSurface.getHeight() - (circle.getRadius() * 2))) {
                newY = gameSurface.getHeight() - (circle.getRadius() * 2);
            }

            circle.setTranslateX(newX);
            circle.setTranslateY(newY);
            circle.setVisible(true);
            circle.setId(atom.toString());

            getSpriteManager().addSprites(atom);
            getSceneNodes().getChildren().add(0, atom.node);
        }
    }

    @Override
    protected void handleUpdate(Sprite sprite) {
        if (sprite instanceof AppV2Atom) {
            AppV2Atom sphere = (AppV2Atom) sprite;

            sphere.update();

            if (sphere.node.getTranslateX() > (getGameSurface().getWidth() -
                    sphere.node.getBoundsInParent().getWidth()) ||
                    sphere.node.getTranslateX() < 0) {
                sphere.vX = sphere.vX * -1;
            }
            if (sphere.node.getTranslateY() > getGameSurface().getHeight() -
                    sphere.node.getBoundsInParent().getHeight() ||
                    sphere.node.getTranslateY() < 0) {
                sphere.vY = sphere.vY * -1;
            }
        }
    }

    @Override
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
        if (spriteA.collide(spriteB)) {
            ((AppV2Atom)spriteA).implode(this);
            ((AppV2Atom)spriteB).implode(this);
            getSpriteManager().addSpritesToBeRemoved(spriteA, spriteB);
            return true;
        }
        return false;
    }

    @Override
    protected void cleanupSprites() {
        super.cleanupSprites();

        NUM_SPRITES_FIELD.setText(String.valueOf(getSpriteManager().getAllSprites().size()));
    }
}
