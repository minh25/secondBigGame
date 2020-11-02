package org.example;

import engine.Sprite;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class v3Ship extends Sprite {

    private final static int TWO_PI_DEGREES = 360;
    private final static int NUM_DIRECTIONS = 32;
    private final static float UNIT_ANGLE_PER_FRAME = ((float) TWO_PI_DEGREES / NUM_DIRECTIONS);
    private final static int MILLIS_TURN_SHIP_180_DEGREES = 300;
    private final static float MILLIS_PER_FRAME = (float) MILLIS_TURN_SHIP_180_DEGREES / (NUM_DIRECTIONS / 2);

    private enum DIRECTION {
        CLOCKWISE, COUNTER_CLOCKWISE, NEITHER
    }

    private final static float THRUST_AMOUNT = 4.3f;
    private final static float MISSILE_THRUST_AMOUNT = 6.3F;
    private DIRECTION turnDirection = DIRECTION.NEITHER;
    private v3Vec u; // current or start vector
    private final List<v3RotatedShipImage> directionalShips = new ArrayList<>();
    private Timeline rotateShipTimeline;
    private int uIndex = 0;
    private int vIndex = 0;
    private final Circle stopArea = new Circle();
    private final Group flipBook = new Group();
    private KeyCode keyCode;
    private boolean shieldOn;
    private Circle shield;
    FadeTransition shieldFade;
    private Circle hitBounds;

    public v3Ship() throws Exception {
        Image shipImage;

        String file = "C:/Users/minh0/IdeaProjects/BigGame/src/main/resources/org/example/ship.png";
        shipImage = new Image(new FileInputStream(file));

        stopArea.setRadius(40);
        stopArea.setStroke(Color.ORANGE);
        v3RotatedShipImage prev = null;

        for (int i = 0; i < NUM_DIRECTIONS; i++) {
            v3RotatedShipImage imageView = new v3RotatedShipImage();
            imageView.setImage(shipImage);
            imageView.setRotate(-1 * i * UNIT_ANGLE_PER_FRAME);
            imageView.setCache(true);
            imageView.setCacheHint(CacheHint.SPEED);
            imageView.setManaged(false);

            imageView.prev = prev;
            imageView.setVisible(false);
            directionalShips.add(imageView);
            if (prev != null) {
                prev.next = imageView;
            }
            prev = imageView;
            flipBook.getChildren().add(imageView);
        }

        v3RotatedShipImage firstShip = directionalShips.get(0);
        firstShip.prev = prev;
        prev.next = firstShip;

        firstShip.setVisible(true);
        node = flipBook;
        flipBook.setTranslateX(200);
        flipBook.setTranslateY(300);
        flipBook.setCache(true);
        flipBook.setCacheHint(CacheHint.SPEED);
        flipBook.setManaged(false);
        flipBook.setAutoSizeChildren(false);
        initHitZone();
    }

    public void initHitZone() {
        if (hitBounds == null) {
            double hZoneCenterX = 55;
            double hZoneCenterY = 34;
            hitBounds = new Circle();
            hitBounds.setCenterX(hZoneCenterX);
            hitBounds.setCenterY(hZoneCenterY);
            hitBounds.setStroke(Color.PINK);
            hitBounds.setFill(Color.RED);
            hitBounds.setRadius(15);
            hitBounds.setOpacity(0);
            flipBook.getChildren().add(hitBounds);
            collisionBound = hitBounds;
        }
    }

    @Override
    public void update() {
        flipBook.setTranslateX(flipBook.getTranslateX() + vX);
        flipBook.setTranslateY(flipBook.getTranslateY() + vY);

        if (stopArea.contains(getCenterX(), getCenterY())) {
            vX = 0;
            vY = 0;
        }
    }

    private v3RotatedShipImage getCurrentShipImage() {
        return directionalShips.get(uIndex);
    }

    public double getCenterX() {
        v3RotatedShipImage shipImage = getCurrentShipImage();
        return node.getTranslateX() + (shipImage.getBoundsInLocal().getWidth() / 2);
    }

    public double getCenterY() {
        v3RotatedShipImage shipImage = getCurrentShipImage();
        return node.getTranslateY() + (shipImage.getBoundsInLocal().getHeight() / 2);
    }

    public void plotCourse(double screenX, double screenY, boolean thrust) {
        double sx = getCenterX();
        double sy = getCenterY();

        v3Vec v = new v3Vec(screenX, screenY, sx, sy);
        if (u == null) {
            u = new v3Vec(1, 0);
        }

        double atan2RadiansU = Math.atan2(u.y, u.x);
        double atan2DegreesU = Math.toDegrees(atan2RadiansU);

        double atan2RadiansV = Math.atan2(v.y, v.x);
        double atan2DegreesV = Math.toDegrees(atan2RadiansV);

        double angleBetweenUV = atan2DegreesV - atan2DegreesU;

        double absAngleBetUV = Math.abs(angleBetweenUV);
        boolean goOtherWay = false;
        if (absAngleBetUV > 180) {
            if (angleBetweenUV < 0) {
                turnDirection = DIRECTION.COUNTER_CLOCKWISE;
                goOtherWay = true;
            } else if (angleBetweenUV > 0) {
                turnDirection = DIRECTION.CLOCKWISE;
            } else {
                turnDirection = DIRECTION.NEITHER;
            }
        } else {
            if (angleBetweenUV < 0) {
                turnDirection = DIRECTION.CLOCKWISE;
            } else if (angleBetweenUV > 0) {
                turnDirection = DIRECTION.COUNTER_CLOCKWISE;
            } else {
                turnDirection = DIRECTION.NEITHER;
            }
        }

        double degreesToMove = absAngleBetUV;
        if (goOtherWay) {
            degreesToMove = TWO_PI_DEGREES - absAngleBetUV;
        }

        uIndex = Math.round((float) (atan2DegreesU / UNIT_ANGLE_PER_FRAME));
        if (uIndex < 0) {
            uIndex = NUM_DIRECTIONS + uIndex;
        }
        vIndex = Math.round((float) (atan2DegreesV / UNIT_ANGLE_PER_FRAME));
        if (vIndex < 0) {
            vIndex = NUM_DIRECTIONS + vIndex;
        }

        String debugMsg = turnDirection +
                " U [m(" + u.mx + ", " + u.my + ")  => c(" + u.x + ", " + u.y + ")] " +
                " V [m(" + v.mx + ", " + v.my + ")  => c(" + v.x + ", " + v.y + ")] " +
                " start angle: " + atan2DegreesU +
                " end angle:" + atan2DegreesV +
                " Angle between: " + degreesToMove +
                " Start index: " + uIndex +
                " End index: " + vIndex;

        System.out.println(debugMsg);

        if (thrust) {
            vX = Math.cos(atan2RadiansV) * THRUST_AMOUNT;
            vY = -Math.sin(atan2RadiansV) * THRUST_AMOUNT;
        }
        turnShip();

        u = v;
    }

    private void turnShip() {
        final Duration oneFrameAmt = Duration.millis(MILLIS_PER_FRAME);
        v3RotatedShipImage startImage = directionalShips.get(uIndex);
        v3RotatedShipImage endImage = directionalShips.get(vIndex);
        List<KeyFrame> frames = new ArrayList<>();

        v3RotatedShipImage currImage = startImage;

        int i = 1;
        while (true) {
            final Node displayNode = currImage;

            KeyFrame oneFrame = new KeyFrame(oneFrameAmt.multiply(i),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            for (v3RotatedShipImage shipImg : directionalShips) {
                                shipImg.setVisible(false);
                            }

                            displayNode.setVisible(true);
                        }
                    });
            frames.add(oneFrame);

            if (currImage == endImage) {
                break;
            }
            if (turnDirection == DIRECTION.CLOCKWISE) {
                currImage = currImage.prev;
            }
            if (turnDirection == DIRECTION.COUNTER_CLOCKWISE) {
                currImage = currImage.next;
            }
            i++;
        }

        if (rotateShipTimeline != null) {
            rotateShipTimeline.stop();
            rotateShipTimeline.getKeyFrames().clear();
            rotateShipTimeline.getKeyFrames().addAll(frames);
        } else {
            rotateShipTimeline = new Timeline();
            rotateShipTimeline.getKeyFrames().addAll(frames);
        }
        rotateShipTimeline.playFromStart();
    }

    public void applyTheBrakes(double screenX, double screenY) {
        stopArea.setCenterX(screenX);
        stopArea.setCenterY(screenY);
    }

    public v3MisSile fire() {
        v3MisSile m1;
        double slowDownAmt = 0;
        int scaleBeginningMissile;

        if (KeyCode.DIGIT2 == keyCode) {
            m1 = new v3MisSile(9, Color.BLUE);
            ;
            slowDownAmt = 1.3f;
            scaleBeginningMissile = 11;
        } else {
            m1 = new v3MisSile(Color.RED);
            scaleBeginningMissile = 8;
        }

        m1.vX = Math.cos(Math.toRadians(uIndex * UNIT_ANGLE_PER_FRAME)) * (MISSILE_THRUST_AMOUNT - slowDownAmt);
        m1.vY = -Math.sin(Math.toRadians(uIndex * UNIT_ANGLE_PER_FRAME)) * (MISSILE_THRUST_AMOUNT - slowDownAmt);

        v3RotatedShipImage shipImage = directionalShips.get(uIndex);

        double offsetX = (shipImage.getBoundsInLocal().getWidth() - m1.node.getBoundsInLocal().getWidth()) / 2;
        double offsetY = (shipImage.getBoundsInLocal().getHeight() - m1.node.getBoundsInLocal().getHeight()) / 2;

        m1.node.setTranslateX(node.getTranslateX() + (offsetX + (m1.vX * scaleBeginningMissile)));
        m1.node.setTranslateY(node.getTranslateY() + (offsetY + (m1.vY * scaleBeginningMissile)));

        return m1;
    }

    public void changWeapon(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    public void shieldToggle() {
        if (shield == null) {
            v3RotatedShipImage shipImage = getCurrentShipImage();
            double x = shipImage.getBoundsInLocal().getWidth() / 2;
            double y = shipImage.getBoundsInLocal().getHeight() / 2;

            shield = new Circle();
            shield.setRadius(60);
            shield.setStrokeWidth(5);
            shield.setStroke(Color.LIMEGREEN);
            shield.setCenterX(x);
            shield.setCenterY(y);
            shield.setOpacity(0.7);

            collisionBound = shield;

            shieldFade = new FadeTransition();
            shieldFade.setFromValue(1);
            shieldFade.setToValue(0.4);
            shieldFade.setDuration(Duration.millis(1000));
            shieldFade.setCycleCount(12);
            shieldFade.setAutoReverse(true);
            shieldFade.setNode(shield);
            shieldFade.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    shieldOn = false;
                    flipBook.getChildren().remove(shield);
                    shieldFade.stop();
                    collisionBound = hitBounds;
                }
            });
            shieldFade.playFromStart();
        }
        shieldOn = !shieldOn;
        if (shieldOn) {
            collisionBound = shield;
            flipBook.getChildren().add(0, shield);
            shieldFade.playFromStart();
        } else {
            flipBook.getChildren().remove(shield);
            shieldFade.stop();
            collisionBound = hitBounds;
        }
    }
}
