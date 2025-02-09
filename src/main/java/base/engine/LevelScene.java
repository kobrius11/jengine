package base.engine;

public class LevelScene extends Scene{
    
    public LevelScene() {
    }

    @Override
    public void update(float dt) {
        Window.get().red = 0.5f;
        Window.get().green = 0.2f;
        Window.get().blue = 0.1f;
    }

    @Override
    public void render() {
    }

}
