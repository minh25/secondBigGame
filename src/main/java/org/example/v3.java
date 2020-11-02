package org.example;

import engine.GameWorld;
import javafx.application.Application;
import javafx.stage.Stage;

public class v3 extends Application {

    GameWorld gameWorld = new v3TheExpanse(59, "v3");

    public v3() throws Exception {
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
}
