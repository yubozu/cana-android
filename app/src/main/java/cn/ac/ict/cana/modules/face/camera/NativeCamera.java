/*
 *  Copyright 2016 Jeroen Mols
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */



package cn.ac.ict.cana.modules.face.camera;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Wrapper around the native camera class so all camera access
 * can easily be mocked.
 * <p/>
 * Created by Jeroen Mols on 06/12/15.
 */
public class
NativeCamera {

    private Camera     camera = null;
    private Parameters params = null;

    public Camera getNativeCamera() {
        return camera;
    }

    public void openNativeCamera() throws RuntimeException {
        camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
    }

    public void unlockNativeCamera() {
        camera.unlock();
    }

    public void releaseNativeCamera() {
        camera.release();
    }

    public void setNativePreviewDisplay(SurfaceHolder holder) throws IOException {
        camera.setPreviewDisplay(holder);
    }

    public void startNativePreview() {
        camera.startPreview();
    }

    public void stopNativePreview() {
        camera.stopPreview();
    }

    public void clearNativePreviewCallback() {
        camera.setPreviewCallback(null);
    }

    public Parameters getNativeCameraParameters() {
        if (params == null) {
            params = camera.getParameters();
        }
        return params;
    }

    public void updateNativeCameraParameters(Parameters params) {
        this.params = params;
        camera.setParameters(params);
    }

    public void setDisplayOrientation(int degrees) {
        camera.setDisplayOrientation(degrees);
    }

    public int getCameraOrientation() {
        CameraInfo camInfo = new CameraInfo();
        Camera.getCameraInfo(getFrontFacingCameraId(), camInfo);

        return camInfo.orientation;
    }

    private int getBackFacingCameraId() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
    private int getFrontFacingCameraId() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
}
