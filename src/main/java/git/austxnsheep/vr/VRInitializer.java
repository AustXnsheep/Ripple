package git.austxnsheep.vr;

import org.lwjgl.openvr.VR;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.openvr.OpenVR.VRSystem;

public class VRInitializer {

    public static boolean init() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer peError = stack.mallocInt(1);

            VR.VR_InitInternal(peError, VR.EVRApplicationType_VRApplication_Scene);
            int error = peError.get(0);
            if (error != 0) {
                System.err.println("Unable to initialize VR: " + VR.VR_GetVRInitErrorAsEnglishDescription(error));
                return false;
            }

            if (!VR.VR_IsHmdPresent()) {
                System.err.println("No VR headset detected.");
                VR.VR_ShutdownInternal();
                return false;
            }

            System.out.println("VR system initialized successfully.");
            return true;
        }
    }
    public static void shutdown() {
        VR.VR_ShutdownInternal();
    }
}
