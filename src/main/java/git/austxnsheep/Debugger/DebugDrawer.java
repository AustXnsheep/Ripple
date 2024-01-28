package git.austxnsheep.Debugger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

public class DebugDrawer extends btIDebugDraw {
    private ShapeRenderer shapeRenderer;
    private int debugMode;
    private PerspectiveCamera cam;

    public DebugDrawer(PerspectiveCamera cam) {
        shapeRenderer = new ShapeRenderer();
        this.cam = cam;
    }

    public void update() {
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    @Override
    public void drawLine(Vector3 from, Vector3 to, Vector3 color) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(color.x, color.y, color.z, 1f));
        shapeRenderer.line(from, to);
        shapeRenderer.end();
    }

    @Override
    public void setDebugMode(int debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public int getDebugMode() {
        return debugMode;
    }

}

