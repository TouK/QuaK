package pl.touk.liero.fx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.math.Vector2
import pl.touk.liero.gdx.createShaderProgram
import pl.touk.liero.utils.FloatArray3d
import pl.touk.liero.utils.deg_rad
import pl.touk.liero.utils.rnd

class TriangleParticles(val camera: Camera, particleSize: Float = 0.2f, particleLifeTimeSec: Float = 2.0f) {
    private val maxParticles = 1000
    private val maxVertices = maxParticles * 3
    private val particleLifeTimeSec = 2.0f

    private var newParticle = 0
    private var oldParticle = 0
    private var dirtyParticles = 0

    private var attrCount = 0
    private val POS_X = attrCount++
    private val POS_Y = attrCount++
    private val VEL_X = attrCount++
    private val VEL_Y = attrCount++
    private val ANGLE = attrCount++
    private val OMEGA = attrCount++
    private val TIME_0 = attrCount++
    private val COLOR = attrCount++

    private var currTimeSec = 0f

    private val mesh = Mesh(false, maxVertices, 0,
            VertexAttribute(Usage.Position, 2, "a_position"),
            VertexAttribute(Usage.Position, 2, "a_vel"),
            VertexAttribute(Usage.Generic, 1, "a_angle"),
            VertexAttribute(Usage.Position, 1, "a_omega"),
            VertexAttribute(Usage.Generic, 1, "a_startTime"),
            VertexAttribute(Usage.ColorPacked, 4, "a_color"))

    private val vertexShader = """
        #version 100
        attribute vec2 a_position;
        attribute vec2 a_vel;
        attribute float a_angle;
        attribute float a_omega;
        attribute float a_startTime;
        attribute vec4 a_color;

        uniform mat4 u_projTrans;
        uniform float u_time;

		varying vec4 v_color;

        float size = $particleSize;

		void main() {
            float time = u_time - a_startTime;

            v_color = a_color;
            float alpha = 1.0 - time / $particleLifeTimeSec;
            v_color.a = v_color.a * alpha * (255.0/254.0);



            float a = a_angle + a_omega * time;
            mat2 rot = mat2( cos(a), sin(a),
                            -sin(a), cos(a) );

            vec2 pos = a_position + a_vel * time + rot * (size * vec2(0.0, 1.0));
			gl_Position = u_projTrans * vec4(pos, 0.0, 1.0);
        }
        """

    private val fragmentShader = """
        #version 100
        #ifdef GL_ES
            #define LOWP lowp
            precision mediump float;
        #else
            #define LOWP
        #endif
        varying LOWP vec4 v_color;
        void main() {
            gl_FragColor = v_color;
        }
        """

    private val shader = createShaderProgram(vertexShader, fragmentShader)
    private val vertices = FloatArray3d(maxParticles, 3, attrCount)

    fun emit(pos: Vector2, vel: Vector2, color: Color) {
        emit(pos.x, pos.y, vel.x, vel.y, rnd(360.deg_rad()), rnd(60.deg_rad()), color)
    }

    fun emit(pos: Vector2, vel: Vector2, angle: Float, omega: Float, color: Color) {
        emit(pos.x, pos.y, vel.x, vel.y, angle, omega, color)
    }

    fun emit(x: Float, y: Float, vx: Float, vy: Float, angle: Float, omega: Float, color: Color) {

        for (v in 0..2) {
            vertices[newParticle, v, POS_X] = x
            vertices[newParticle, v, POS_Y] = y
            vertices[newParticle, v, VEL_X] = vx
            vertices[newParticle, v, VEL_Y] = vy
            vertices[newParticle, v, ANGLE] = angle + v * 120.deg_rad()
            vertices[newParticle, v, OMEGA] = omega
            vertices[newParticle, v, TIME_0] = currTimeSec
            vertices[newParticle, v, COLOR] = color.toFloatBits()
        }

        newParticle = next(newParticle)
        if (newParticle == oldParticle) {
            oldParticle = next(oldParticle)
        }
        dirtyParticles++
    }

    fun render(deltaTimeSec: Float) {
        removeOldParticles()

        if (dirtyParticles > 0) {
            // todo [optimization] update only dirty vertices
            mesh.setVertices(vertices.array)
            dirtyParticles = 0
        }

        if (oldParticle != newParticle) {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
            shader.begin()
            shader.setUniformMatrix("u_projTrans", camera.combined)
            shader.setUniformf("u_time", currTimeSec)
            if (oldParticle < newParticle) {
                // * (old -> new)
                //  ----#############--------
                //      |            |
                //    old particle   new particle
                mesh.render(shader, GL20.GL_TRIANGLES, oldParticle * 3, (newParticle - oldParticle) * 3)
            } else {
                // * (0 -> new)
                // * (old -> max)
                //
                //  ####-------------#########
                //      |            |
                //  new particle     old particle
                mesh.render(shader, GL20.GL_TRIANGLES, 0, newParticle * 3)
                mesh.render(shader, GL20.GL_TRIANGLES, oldParticle * 3, (maxParticles - oldParticle) * 3)
            }
            shader.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)

            currTimeSec += deltaTimeSec
        } else {
            // Jeśli nie ma particles, możemy wyzerować czas, żeby nie narastał w nieskończoność (i indeksy, żeby nie zapętlać)
            clear()
        }
    }

    fun clear() {
        oldParticle = 0
        newParticle = 0
        currTimeSec = 0f
    }

    fun dispose() {
        mesh.dispose()
    }

    private fun removeOldParticles() {
        while (oldParticle != newParticle &&
                vertices[oldParticle, 0, TIME_0] + particleLifeTimeSec < currTimeSec) {
            oldParticle = ++oldParticle % maxParticles
        }
    }

    private fun next(index: Int): Int {
        if (index + 1 >= maxParticles)
            return 0
        else
            return index + 1
    }
}