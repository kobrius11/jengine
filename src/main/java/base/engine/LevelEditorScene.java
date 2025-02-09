package base.engine;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" + //
            "\n" + //
            "layout(location = 0) in vec3 aPos;\n" + //
            "layout(location = 1) in vec4 aColor;\n" + //
            "\n" + //
            "out vec4 fColor;\n" + //
            "void main()\n" + //
            "{\n" + //
            "    fColor = aColor;\n" + //
            "    gl_Position = vec4(aPos, 1.0);\n" + //
            "}";
    private String fragmentShaderSrc = "#version 330 core\n" + //
            "\n" + //
            "in vec4 fColor;\n" + //
            "out vec4 color;\n" + //
            "\n" + //
            "void main()\n" + //
            "{\n" + //
            "    color = fColor;\n" + //
            "}";

    private int vertexId, fragmentId, shaderProgram;

    private float[] vertexArray = {
        // positions            // colors
         0.5f,  -0.5f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f, // bottom right 0
        -0.5f,   0.5f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f, // top left     1
         0.5f,   0.5f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f, // top right    2
        -0.5f,  -0.5f, 0.0f,    1.0f, 1.0f, 0.0f, 0.0f, // bottom left  3
    };

    // must be in counter-clockwise order
    private int[] elementArray = {
        2, 1, 0, // top right triangle
        0, 1, 3 // bottom left triangle
    };

    private int vaoId, vboId, eboId;

    public LevelEditorScene() {}

    @Override
    public void update(float dt) {
        // bind shader program
        glUseProgram(shaderProgram);

        glBindVertexArray(vaoId);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glUseProgram(0);

    }

    @Override
    public void init() {
        // load and compile vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // pass shader source to the GPU
        glShaderSource(vertexId, vertexShaderSrc);
        glCompileShader(vertexId);

        // check for errors
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            System.exit(1);
        }

        // load and compile fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // pass shader source to the GPU
        glShaderSource(fragmentId, fragmentShaderSrc);
        glCompileShader(fragmentId);

        // check for errors
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            System.exit(1);
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexId);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);

        //Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            System.exit(1);
        }

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length).put(vertexArray).flip();

        // create VBO
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW); 

        // create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length).put(elementArray).flip();
        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void render() {}
}
