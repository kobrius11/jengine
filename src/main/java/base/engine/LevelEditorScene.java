package base.engine;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    private boolean isSceneChanging = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
    }

    @Override
    public void update(float dt) {
        if (KeyListener.isKeyDown(GLFW_KEY_SPACE) && !isSceneChanging) {
            isSceneChanging = true;
        }

        if (isSceneChanging && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().red -= dt * 5.0f;
            Window.get().green -= dt * 5.0f;
            Window.get().blue -= dt * 5.0f;
        } else if (isSceneChanging) {
            SceneManager.changeScene(1);
        }
    }

    @Override
    public void render() {
    }
}
