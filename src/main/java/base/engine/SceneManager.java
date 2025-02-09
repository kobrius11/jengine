package base.engine;

public class SceneManager {
    private static SceneManager instance;
    public static Scene currentScene;

    private SceneManager() {}

    public static SceneManager get() {
        if (SceneManager.instance == null) {
            SceneManager.instance = new SceneManager();
        }
        return SceneManager.instance;
    }

    public static void changeScene(int newScene) {
        SceneEnum sceneEnum = SceneEnum.fromValue(newScene);
        currentScene = sceneEnum.getScene();
        currentScene.init();
    }

}
