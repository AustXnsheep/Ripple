package git.austxnsheep.Listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import git.austxnsheep.Main;
import git.austxnsheep.Types.Entities.NoodleMan;
import git.austxnsheep.Types.Entity;

public class KeyboardProcessor implements InputProcessor {

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            System.out.println("Left click detected!");
            for (Entity entity : Main.entityList) {
                if (entity instanceof NoodleMan noodleMan) {
                    noodleMan.lookAt(entity.getPosition(), noodleMan.getOrientationFromModelInstance(noodleMan.head), new Vector3(5, 5, 5));
                }
            }
        }
        return false;
    }

    // Other input methods
    @Override
    public boolean keyDown(int keycode) {
        float cameraSpeed = 5.0f;

        if (keycode == Input.Keys.UP) {
            Main.cam.position.add(0, 0, -cameraSpeed);
        } else if (keycode == Input.Keys.DOWN) {
            Main.cam.position.add(0, 0, cameraSpeed);
        } else if (keycode == Input.Keys.LEFT) {
            Main.cam.position.add(-cameraSpeed, 0, 0);
        } else if (keycode == Input.Keys.RIGHT) {
            Main.cam.position.add(cameraSpeed, 0, 0);
        }

        Main.cam.update();
        return true;
    }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Zoom in or out
        float zoomSpeed = 1.0f;
        Main.cam.fieldOfView += amountY * zoomSpeed;
        Main.cam.update();
        return true;
    }
}
