package base.engine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    private static KeyListener instance;
    private boolean[] keys = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        get().keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int keyCode) {
        return get().keys[keyCode];
    }
}
