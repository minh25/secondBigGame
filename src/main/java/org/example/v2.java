package org.example;

import engine.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class v2 extends Application {

    GameWorld gameWorld = new v2AtomSmasher(60, "v2.0");

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameWorld.initialize(primaryStage);
        gameWorld.beginGameLoop();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
