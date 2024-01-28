package git.austxnsheep;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import git.austxnsheep.Debugger.DebugDrawer;
import git.austxnsheep.Factories.ObjectFactory;
import git.austxnsheep.Listeners.KeyboardProcessor;
import git.austxnsheep.Types.Entities.NoodleMan;
import git.austxnsheep.Types.Entities.Player;
import git.austxnsheep.Types.Entity;
import git.austxnsheep.Types.PhysicsModelInstance;
import git.austxnsheep.vr.VRInitializer;

public class Main extends ApplicationAdapter implements ObjectFactory {
    private btCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;
    private btBroadphaseInterface broadphase;
    private btSequentialImpulseConstraintSolver solver;
    private Player player;
    private ModelBatch modelBatch;
    private Environment environment;
    private CameraInputController camController;
    private Array<Player> playerInstances = new Array<>();
    public int recommendedHeight;
    public int recommendedWidth;
    private final boolean developerMode = true;
    private final boolean vrMode = false;
    private DebugDrawer debugDrawer;
    private InputProcessor inputProcessor;
    public static Array<PhysicsModelInstance> instances = new Array<>();
    public static Array<Entity> entityList = new Array<>();
    public static PerspectiveCamera cam;
    public static btDiscreteDynamicsWorld dynamicsWorld;

    public static void main(String[] args) {
        //This game is about to be the BIGGEST memory hog
        System.out.println("Starting Ripple...");
        new LwjglApplication(new Main(), "Ripple", 800, 600);
    }

    @Override
    public void create() {
        if (developerMode) {
            startClient();
            startServer();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        float deltaTime = Gdx.graphics.getDeltaTime();
        dynamicsWorld.stepSimulation(deltaTime, 5);

        for (PhysicsModelInstance instance : instances) {
            instance.update();
        }

        for (Entity entity : entityList) {
            entity.update();
            if (entity instanceof NoodleMan noodleMan) {
                Vector3 noodleManHeadPosition = noodleMan.getHeadPosition();
                Quaternion noodleManOrientation = noodleMan.getHeadOrientation();

                cam.position.set(noodleManHeadPosition);

                System.out.println("Camera Position: " + cam.position);
                System.out.println("Camera Direction: " + cam.direction);
                Vector3 direction = new Vector3(0, 0, -1);
                noodleManOrientation.transform(direction);
                cam.direction.set(direction);

                cam.update();
            }
        }

        if (vrMode) {
            for (Player player : playerInstances) {
                player.render();
            }
        } else {
            renderCamInstance(cam);
        }
    }

    @Override
    public void dispose() {
        modelBatch.dispose();

        for (PhysicsModelInstance instance : instances) {
            dynamicsWorld.removeRigidBody(instance.body);
            instance.disposePhysics();
        }
        if (debugDrawer != null) {
            debugDrawer.dispose();
        }
        dynamicsWorld.dispose();
        solver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
    }
    public void startClient() {
        System.out.println("Render Setup...");
        cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(5f, 2f, 2f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        //cam.update();

        Bullet.init();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        solver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfig);

        //Objects
        //Note to self, please make a better way to store all this.
        //Ideas:
        //Make a .MAPFILE extension or use an existing one. It would help players make their own maps to. So
        //many upsides.
        System.out.println("Creating Objects...");

        PhysicsModelInstance cubeInstance = createPhysicsCube(1f, 1f, 1f, Color.GREEN, new Vector3(1f, 15f, 1f));
        instances.add(cubeInstance);
        dynamicsWorld.addRigidBody(cubeInstance.body);
        cubeInstance.setMass(1);

        PhysicsModelInstance sphereInstance = createPhysicsSphere(1f, Color.BLUE, new Vector3(1.2f, 10f, 1f));
        instances.add(sphereInstance);
        dynamicsWorld.addRigidBody(sphereInstance.body);

        PhysicsModelInstance basePlate = createPhysicsCube(20f, 1f, 20f, Color.RED, new Vector3(0f, 0f, 0f)); // Baseplate
        instances.add(basePlate);
        dynamicsWorld.addRigidBody(basePlate.body);
        basePlate.setStatic(true);

        PhysicsModelInstance Wall1 = createPhysicsCube(20f, 1f, 1f, Color.RED, new Vector3(0f, 1f, 20f)); // Wall1
        instances.add(Wall1);
        dynamicsWorld.addRigidBody(Wall1.body);
        Wall1.setStatic(true);

        PhysicsModelInstance anchor = createPhysicsSphere(0.1f, Color.BLUE, new Vector3(0f, 0f, 0f)); //Center Sphere
        instances.add(anchor);
        dynamicsWorld.addRigidBody(anchor.body);
        anchor.setStatic(true);

        entityList.add(new NoodleMan(new Vector3(1, 5, 1)));

        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        System.out.println("Setting up IO Listeners...");
        inputProcessor = new KeyboardProcessor();
        Gdx.input.setInputProcessor(inputProcessor);

        //Jesus take the wheel
        if (vrMode) {
            initVR();
        }
        //Debugging hitboxes (I hate physics)

        if (developerMode) {
            debugDrawer = new DebugDrawer(cam);
            dynamicsWorld.setDebugDrawer(debugDrawer);
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
        }
    }
    public void startServer() {

    }
    public void renderCamInstance(PerspectiveCamera cam) {
        modelBatch.begin(cam);
        for (PhysicsModelInstance instance : instances) {
            modelBatch.render(instance, environment);
        }
        if (debugDrawer != null) {
            dynamicsWorld.debugDrawWorld();
            debugDrawer.update();
        }
        modelBatch.end();
    }
    //VR stuff
    private void initVR() {
        VRInitializer.init();
        player = new Player(createPhysicsCube(1f, 4f, 1f, Color.YELLOW, new Vector3(0f, 1f, 20f)));
        playerInstances.add(player);
    }
}