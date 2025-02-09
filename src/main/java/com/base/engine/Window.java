package com.base.engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.Random;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
    private long window;
    private int width, height;
    private String title;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
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
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
        GL.createCapabilities();

    }

    private void createWindow() {
        window = glfwCreateWindow(this.width, this.height, this.title, 0L, 0L);
        if (window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create the GLFW window");
    }

    private void loop() {
        float red = 1.0f;
        float green = 1.0f;
        float blue = 1.0f;
        float alpha = 1.0f;


        while (!glfwWindowShouldClose(window)) {

            glClearColor(red, green, blue, alpha);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            if (KeyListener.isKeyDown(GLFW_KEY_UP)) {
                green = 0.0f;
                blue = 0.0f;
            } else if (KeyListener.isKeyDown(GLFW_KEY_DOWN)) {
                green = 1.0f;
                blue = 1.0f;
            }

            if (MouseListener.isDragging()) {
                red = 0.0f;
            } else {
                red = 1.0f;
            }

            if (MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_1)
                    | MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_2)
                    | MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_3)
                    | MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_4)
                    | MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_5)
                    | MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_6)
                    | MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_7)
                    | MouseListener.isMouseButtonDown(GLFW_MOUSE_BUTTON_8)) {
                red = 0.0f;
            } else {
                red = 1.0f;
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
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
