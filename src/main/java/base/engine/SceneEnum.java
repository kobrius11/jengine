package base.engine;

public enum SceneEnum {
    LEVEL_EDITOR_SCENE(0, new LevelEditorScene()),
    LEVEL_SCENE(1, new LevelScene());

    private final int value;
    private final Scene scene;

    SceneEnum(int value, Scene scene) {
        this.value = value;
        this.scene = scene;
    }

    public int getValue() {
        return value;
    }

    public Scene getScene() {
        return scene;
    }

    public static SceneEnum fromValue(int value) {
        for (SceneEnum scene : values()) {
            if (scene.value == value) {
                return scene;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
