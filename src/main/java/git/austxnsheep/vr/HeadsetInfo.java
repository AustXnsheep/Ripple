package git.austxnsheep.vr;

import org.lwjgl.openvr.TrackedDevicePose;
import org.lwjgl.openvr.VR;
import org.lwjgl.openvr.VRCompositor;


public class HeadsetInfo {
    private static final TrackedDevicePose.Buffer trackedDevicePoses = TrackedDevicePose.create(VR.k_unMaxTrackedDeviceCount);
    public static TrackedDevicePose.Buffer getDevicePose() {
        VRCompositor.VRCompositor_WaitGetPoses(trackedDevicePoses, null);
        return trackedDevicePoses;
    }
}
