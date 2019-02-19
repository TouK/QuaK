package pl.touk.liero.gdx

import com.badlogic.gdx.graphics.glutils.ShaderProgram

fun createShaderProgram(vertexShader: String, fragmentShader: String): ShaderProgram {
    val shader = ShaderProgram(vertexShader, fragmentShader)
    if (!shader.isCompiled) {
        throw IllegalStateException("Failed to compile shader:\n" + shader.log)
    }
    return shader
}