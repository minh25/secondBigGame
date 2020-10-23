package org.example;

//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
///**
// * JavaFX App
// */
//public class App extends Application {
//
//    private static Scene scene;
//
//    @Override
//    public void start(Stage stage) throws IOException {
//        scene = new Scene(loadFXML("primary"));
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    static void setRoot(String fxml) throws IOException {
//        scene.setRoot(loadFXML(fxml));
//    }
//
//    private static Parent loadFXML(String fxml) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
//        return fxmlLoader.load();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//
//}

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
//            long oldFrameTime = frameTimes[frameTimeIndex] ;
//            frameTimes[frameTimeIndex] = now ;
//            frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
//            if (frameTimeIndex == 0) {
//                arrayFilled = true ;
//            }
//            if (arrayFilled) {
//                long elapsedNanos = now - oldFrameTime ;
//                long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
//                double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
//                label.setText(String.format("Current frame rate: %.3f", frameRate));
//            }

                layer.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), Insets.EMPTY)));

                layer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        CircleNFx circleNFx = new CircleNFx(mouseEvent.getSceneX(), mouseEvent.getSceneY());
//                        CircleNFx circleNFx = new CircleNFx();
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
