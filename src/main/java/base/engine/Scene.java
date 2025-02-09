package base.engine;

public abstract class Scene {
    public Scene() {
    }

    public abstract void update(float dt);

    public abstract void render();
}
