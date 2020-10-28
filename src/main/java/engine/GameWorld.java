package engine;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class GameWorld {
    private static Timeline gameLoop;
    private final int framesPerSecond;
    private final String windowTile;
    private final SpriteManager spriteManager = new SpriteManager();
    private final SoundManager soundManager = new SoundManager(3);
    private Scene gameSurface;
    private Group sceneNodes;


    public GameWorld(final int fps, final String title) {
        framesPerSecond = fps;
        windowTile = title;

        buildAndSetGameLoop();
    }

    protected static Timeline getGameLoop() {
        return gameLoop;
    }

    protected static void setGameLoop(Timeline gameLoop) {
        GameWorld.gameLoop = gameLoop;
    }

    protected final void buildAndSetGameLoop() {
        final Duration oneFrameAmt = Duration.millis(1000 / (float) getFramesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        updateSprites();
                        checkCollisions();
                        cleanupSprites();
                    }
                });

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(oneFrame);

        setGameLoop(timeline);
    }

    public abstract void initialize(final Stage primaryStage);

    public void beginGameLoop() {
        getGameLoop().play();
    }

    protected void updateSprites() {
        for (Sprite sprite: spriteManager.getAllSprites()) {
            handleUpdate(sprite);
        }
    }

    protected void handleUpdate(Sprite sprite) {
    }

    protected void checkCollisions() {
        spriteManager.resetCollisionsToCheck();

        for (Sprite spriteA: spriteManager.getCollisionsToCheck()) {
            for (Sprite spriteB: spriteManager.getAllSprites()) {
                if (handleCollision(spriteA, spriteB)) {
                    //TODO
                    break;
                }
            }
        }
    }

    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
        return false;
    }

    protected void cleanupSprites() {
        spriteManager.cleanupSprites();
    }

    protected int getFramesPerSecond() {
        return framesPerSecond;
    }

    protected String getWindowTile() {
        return windowTile;
    }

    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    public Scene getGameSurface() {
        return gameSurface;
    }

    protected void setGameSurface(Scene gameSurface) {
        this.gameSurface = gameSurface;
    }

    public Group getSceneNodes() {
        return sceneNodes;
    }

    public void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void shutdown() {
        getGameLoop().stop();
        getSoundManager().shutdown();
    }
}
