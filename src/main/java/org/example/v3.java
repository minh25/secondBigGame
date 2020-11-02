package org.example;

import engine.GameWorld;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class v3 extends Application {

    GameWorld gameWorld = new v3TheExpanse(59, "v3");

    public v3() {
    }

    public  static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameWorld.initialize(primaryStage);
        gameWorld.beginGameLoop();
        primaryStage.show();
    }

    @Override
    public void stop() {
        Platform.exit();
        gameWorld.shutdown();
    }
}
