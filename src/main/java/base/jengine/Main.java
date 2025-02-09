package base.jengine;

import base.engine.Window;

public class Main {
    public static void main(String[] args) {
        Window.setTitle("Hello World");
        Window.setSize(640, 480);
        Window.get().run();
    }
}
