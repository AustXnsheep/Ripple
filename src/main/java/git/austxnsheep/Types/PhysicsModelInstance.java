package git.austxnsheep.Types;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class PhysicsModelInstance extends ModelInstance {
    public boolean isStatic = false;
    public btRigidBody body;

    public PhysicsModelInstance(ModelInstance instance, btRigidBody body) {
        super(instance.model);
        this.transform.set(instance.transform);
        this.body = body;
    }

    public void update() {
        if (body != null) {
            transform.set(body.getWorldTransform());
        }
    }
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        if (isStatic) {
            setDynamic(0);
        } else {
            setDynamic(1);
        }
    }

    public void disposePhysics() {
        if (body != null) {
            body.dispose();
            body = null;
        }
    }
    public void setDynamic(float mass) {
        if (body != null && mass >= 0) {
            Vector3 localInertia = new Vector3(0, 0, 0);
            body.getCollisionShape().calculateLocalInertia(mass, localInertia);
            body.setMassProps(mass, localInertia);
            body.setLinearVelocity(Vector3.Zero);
            body.setAngularVelocity(Vector3.Zero);
            body.activate();
        }
    }
    public void setMass(float mass) {
        body.setMassProps(mass, body.getLocalInertia());
    }
    public void teleportModelToRigidBody() {
        if (body != null) {
            Matrix4 transform = new Matrix4();
            body.getWorldTransform(transform);

            this.transform.set(transform);
        }
    }
    public void updateModelInstanceTransform() {
        if (body != null) {
            Matrix4 transform = new Matrix4();
            body.getWorldTransform(transform);
            this.transform.set(transform);
        }
    }

}
