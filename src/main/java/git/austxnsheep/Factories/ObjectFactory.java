package git.austxnsheep.Factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import git.austxnsheep.Types.PhysicsModelInstance;

public interface ObjectFactory {
    ModelBuilder modelBuilder = new ModelBuilder();

    default PhysicsModelInstance createPhysicsCube(float width, float height, float depth, Color color, Vector3 position) {
        Model cubeModel = modelBuilder.createBox(width, height, depth,
                new Material(ColorAttribute.createDiffuse(color)),
                Usage.Position | Usage.Normal);
        ModelInstance cubeInstance = new ModelInstance(cubeModel);
        // Create physics shape
        btCollisionShape boxShape = new btBoxShape(new Vector3(width / 2, height / 2, depth / 2));
        Vector3 localInertia = new Vector3(0, 0, 0);
        boxShape.calculateLocalInertia(1f, localInertia);

        btRigidBody.btRigidBodyConstructionInfo boxBodyInfo = new btRigidBody.btRigidBodyConstructionInfo(1f, null, boxShape, localInertia);
        btRigidBody boxBody = new btRigidBody(boxBodyInfo);

        Matrix4 newTransform = new Matrix4().setToTranslation(position);
        boxBody.setWorldTransform(newTransform);

        return new PhysicsModelInstance(cubeInstance, boxBody);
    }

    default PhysicsModelInstance createPhysicsSphere(float radius, Color color, Vector3 position) {
        Model sphereModel = modelBuilder.createSphere(radius * 2, radius * 2, radius * 2, 32, 32,
                new Material(ColorAttribute.createDiffuse(color)),
                Usage.Position | Usage.Normal);
        ModelInstance sphereInstance = new ModelInstance(sphereModel);
        // Create physics shape
        btCollisionShape sphereShape = new btSphereShape(radius);
        Vector3 localInertia = new Vector3(0, 0, 0);
        sphereShape.calculateLocalInertia(1f, localInertia);

        btRigidBody.btRigidBodyConstructionInfo sphereBodyInfo = new btRigidBody.btRigidBodyConstructionInfo(1f, null, sphereShape, localInertia);
        btRigidBody sphereBody = new btRigidBody(sphereBodyInfo);
        //Move...
        Matrix4 newTransform = new Matrix4().setToTranslation(position);
        sphereBody.setWorldTransform(newTransform);

        return new PhysicsModelInstance(sphereInstance, sphereBody);
    }
    default ModelInstance createNonCollisionCube(float width, float height, float depth, Color color, Vector3 position) {
        Model cubeModel = modelBuilder.createBox(width, height, depth,
                new Material(ColorAttribute.createDiffuse(color)),
                Usage.Position | Usage.Normal);

        ModelInstance cubeInstance = new ModelInstance(cubeModel);

        cubeInstance.transform.setToTranslation(position);

        return cubeInstance;
    }
}
