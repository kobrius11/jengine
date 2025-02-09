package base.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import base.utils.Time;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;


public class Window {
    private static Window instance;
    private static long window;
    private static int width, height;
    private static String title;
    public float red, green, blue, alpha = 1.0f;

    private Window() {
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
        this.alpha = 1.0f;
    }

    public static Window get() {
        if (Window.instance == null) {
            Window.instance = new Window();
        }
        return instance;
    }

    public static void setTitle(String title) {
        Window.title = title;
    }

    public static void setSize(int width, int height) {
        Window.width = width;
        Window.height = height;
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        createWindow();

        glfwSetCursorPosCallback(window, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);

        glfwSetKeyCallback(window, KeyListener::keyCallback);

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            //GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            // glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
            //         (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
        GL.createCapabilities();

        SceneManager.get();
        SceneManager.changeScene(0);
    }

    private void createWindow() {
        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create the GLFW window");
    }


    private void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;


        while (!glfwWindowShouldClose(window)) {

            glClearColor(red, green, blue, alpha);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (dt >= 0) {
                SceneManager.currentScene.update(dt);;
            }

            glfwSwapBuffers(window);
            glfwPollEvents();

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

}
