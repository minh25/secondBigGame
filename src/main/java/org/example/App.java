package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private boolean arrayFilled = false ;

    public static double pref_height = 400;
    public static double pref_width = 600;

    @Override
    public void start(Stage primaryStage) {

        Layer layer = new Layer();
        Scene scene = new Scene(layer, pref_width, pref_height);

        AnimationTimer frameRateMeter = new AnimationTimer() {
            @Override
            public void handle(long now) {

                layer.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));

                layer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        CircleNFx circleNFx = new CircleNFx(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                        layer.getChildren().add(circleNFx);
                        System.out.println(layer.getChildren().size());
                    }
                });

                ManagerShape.update();
            }
        };

        frameRateMeter.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
