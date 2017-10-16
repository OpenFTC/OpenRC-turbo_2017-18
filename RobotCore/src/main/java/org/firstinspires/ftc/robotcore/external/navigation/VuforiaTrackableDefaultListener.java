/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.robotcore.external.navigation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vuforia.Matrix34F;
import com.vuforia.TrackableResult;
import com.vuforia.VuMarkTarget;
import com.vuforia.VuMarkTargetResult;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaPoseMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link VuforiaTrackableDefaultListener} is the default listener used for {@link VuforiaTrackable}
 * implementations. This listener facilitates polling for results of the tracking. (Advanced:) Alternate
 * listeners could make use of event-driven results by taking actions in the {@link VuforiaTrackable.Listener}
 * methods.
 *
 * @see VuforiaTrackable
 * @see VuforiaTrackable#getListener()
 */
@SuppressWarnings("WeakerAccess")
public class VuforiaTrackableDefaultListener implements VuforiaTrackable.Listener
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public final static String TAG = "Vuforia";

    protected VuforiaTrackable trackable;
    protected boolean newPoseAvailable;
    protected boolean newLocationAvailable;
    protected Matrix34F currentPose;
    protected Matrix34F lastTrackedPose;
    protected VuMarkInstanceId vuMarkInstanceId = null;
    protected OpenGLMatrix phoneLocationOnRobotInverted;
    protected VuforiaLocalizer.CameraDirection cameraDirection;
    protected final Map<VuforiaLocalizer.CameraDirection, OpenGLMatrix> poseCorrectionMatrices;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public VuforiaTrackableDefaultListener(VuforiaTrackable trackable)
        {
        this.trackable = trackable;
        this.newPoseAvailable = false;
        this.newLocationAvailable = false;
        this.currentPose = this.lastTrackedPose = null;
        this.vuMarkInstanceId = null;
        this.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        this.poseCorrectionMatrices = new HashMap<VuforiaLocalizer.CameraDirection, OpenGLMatrix>();
        this.poseCorrectionMatrices.put(VuforiaLocalizer.CameraDirection.BACK, new OpenGLMatrix(new float[]
            {
                0, -1,  0,  0,
               -1,  0,  0,  0,
                0,  0, -1,  0,
                0,  0,  0,  1
            }));
        this.poseCorrectionMatrices.put(VuforiaLocalizer.CameraDirection.FRONT, new OpenGLMatrix(new float[]
            {
                0,  1,  0,  0,
               -1,  0,  0,  0,
                0,  0,  1,  0,
                0,  0,  0,  1
            }));
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    /**
     * Informs the listener of the location of the phone on the robot and the identity of
     * the camera being used. This information is needed in order to compute the robot location.
     *
     * @param phoneLocationOnRobot the location of the phone on the robot
     * @param cameraDirection which camera on the phone is use
     * @see #getPhoneLocationOnRobot()
     * @see #getCameraDirection()
     * @see #getRobotLocation()
     */
    public synchronized void setPhoneInformation(@NonNull OpenGLMatrix phoneLocationOnRobot, @NonNull VuforiaLocalizer.CameraDirection cameraDirection)
        {
        this.phoneLocationOnRobotInverted = phoneLocationOnRobot.inverted();
        this.cameraDirection = cameraDirection;
        }

    /**
     * Returns the location of the phone on the robot, as previously set, or null if that
     * has never been set.
     * @return the location of the phone on the robot
     * @see #setPhoneInformation(OpenGLMatrix, VuforiaLocalizer.CameraDirection)
     */
    public synchronized OpenGLMatrix getPhoneLocationOnRobot()
        {
        return this.phoneLocationOnRobotInverted == null ? null : this.phoneLocationOnRobotInverted.inverted();
        }

    /**
     * Returns the identity of the camera in use
     * @return the identity of the camera in use
     * @see #setPhoneInformation(OpenGLMatrix, VuforiaLocalizer.CameraDirection)
     */
    public synchronized VuforiaLocalizer.CameraDirection getCameraDirection()
        {
        return this.cameraDirection;
        }

    /**
     * Returns the {@link OpenGLMatrix} transform that represents the location of the robot
     * on the field, or null if that cannot be computed.
     *
     * <p>The pose will be null if the trackable is not currently visible. The location of the trackable
     * will be null if a location wasn't previously provided with {@link VuforiaTrackable#setLocation(OpenGLMatrix)}.
     * The phone location on the robot will be null if {@link #setPhoneInformation(OpenGLMatrix, VuforiaLocalizer.CameraDirection)}
     * has not been called. All three must be non-null for the location to be computable.</p>
     *
     * @return the location of the robot on the field
     * @see #getUpdatedRobotLocation()
     * @see #getPose()
     * @see #setPhoneInformation(OpenGLMatrix, VuforiaLocalizer.CameraDirection)
     * @see VuforiaTrackable#setLocation(OpenGLMatrix)
     */
    public synchronized @Nullable OpenGLMatrix getRobotLocation()
        {
        // Capture the location in order to avoid races with concurrent updates
        OpenGLMatrix trackableLocationOnField = trackable.getLocation();
        OpenGLMatrix pose = this.getPose();
        if (pose != null && trackableLocationOnField != null && this.phoneLocationOnRobotInverted != null)
            {
            /**
             * The target is visible and we know the target's location on the field. Put
             * together the overall transformation matrix that computes the robot's
             * location on the field.
             */
            OpenGLMatrix result =
                    trackableLocationOnField
                            .multiplied(pose.inverted())
                            .multiplied(phoneLocationOnRobotInverted);
            return result;
            }
        else
            return null;
        }

    /**
     * The {@link #poseCorrectionMatrices} correct for the different coordinate systems used
     * in Vuforia and our phone coordinate system here. Here, with the phone in flat front of you
     * in portrait mode (as it is when running the robot controller app), Z is pointing upwards, up
     * out of the screen, X points to your right, and Y points away from you.
     * @see #setPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection, OpenGLMatrix)
     */
    public @NonNull OpenGLMatrix getPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection direction)
        {
        synchronized (this.poseCorrectionMatrices)
            {
            return this.poseCorrectionMatrices.get(direction);
            }
        }

    /** @see #getPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection) */
    public void setPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection direction, @NonNull OpenGLMatrix matrix)
        {
        synchronized (this.poseCorrectionMatrices)
            {
            this.poseCorrectionMatrices.put(direction, matrix);
            }
        }

    /**
     * Returns the location of the robot, but only if a new location has been detected since
     * the last call to {@link #getUpdatedRobotLocation()}.
     *
     * @return the location of the robot
     * @see #getRobotLocation()
     */
    public synchronized OpenGLMatrix getUpdatedRobotLocation()
        {
        if (this.newLocationAvailable)
            {
            this.newLocationAvailable = false;
            return getRobotLocation();
            }
        else
            return null;
        }

    /**
     * Returns the pose of the trackable if it is currently visible. If it is not currently
     * visible, null is returned. The pose of the trackable is the location of the trackable
     * in the phone's coordinate system.
     *
     * <p>Note that whether a trackable is visible or not is constantly dynamically changing
     * in the background as the phone is moved about. Thus, just because one call to getPose()
     * returns a non-null matrix doesn't a second call a short time later will return the same
     * result.</p>
     *
     * @return the pose of the trackable, if visible; if not visible, then null is returned.
     * @see #getPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection)
     * @see #getRawPose()
     * @see #isVisible()
     */
    public synchronized @Nullable OpenGLMatrix getPose()
        {
        OpenGLMatrix pose = getRawPose();
        return pose==null ? null : this.getPoseCorrectionMatrix(this.cameraDirection).multiplied(pose);
        }

    /**
     * Returns the raw pose of the trackable as reported by Vuforia. This differs from the
     * value reported by {@link #getPose()} since Vuforia and we here have different notions
     * of the phone coordinate system.
     * @return the raw pose of the trackable as reported by Vuforia
     * @see #getPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection)
     */
    public synchronized @Nullable OpenGLMatrix getRawPose()
        {
        if (this.currentPose != null)
            {
            OpenGLMatrix result = new VuforiaPoseMatrix(this.currentPose).toOpenGL();
            // RobotLog.vv(TAG, "rawPose -> %s", result.toString());
            return result;
            }
        else
            return null;
        }

    /**
     * Returns the raw pose of the trackable, but only if a new pose is available since the last call
     * to {@link #getRawUpdatedPose()}.
     *
     * @return the raw pose of the trackable
     * @see #getRawPose()
     */
    public synchronized @Nullable OpenGLMatrix getRawUpdatedPose()
        {
        if (this.newPoseAvailable)
            {
            this.newPoseAvailable = false;
            return getRawPose();
            }
        else
            return null;
        }

    /**
     * Answers whether the associated trackable is currently visible or not
     *
     * @return whether the associated trackable is currently visible or not
     * @see #getPose()
     */
    public boolean isVisible()
        {
        return getPose() != null;
        }

    /**
     * Returns the pose associated with the last known tracked location of this trackable, if any.
     * This can be used to recall the last pose seen, even if the trackable is no longer visible.
     *
     * @return the pose associated with the last known tracked location of this trackable, if any.
     * @see #getRawPose()
     */
    public synchronized OpenGLMatrix getLastTrackedRawPose()
        {
        return this.lastTrackedPose == null ? null : new VuforiaPoseMatrix(this.lastTrackedPose).toOpenGL();
        }

    /**
     * Returns the instance id of the currently visible VuMark associated with this
     * VuMark template, if any presently exists.
     *
     * @return the instance id of the currently visible VuMark
     * @see <a href="https://library.vuforia.com/content/vuforia-library/en/reference/java/classcom_1_1vuforia_1_1VuMarkTemplate.html">VuMark template</a>
     */
    public synchronized @Nullable VuMarkInstanceId getVuMarkInstanceId()
        {
        return vuMarkInstanceId;
        }

    /**
     * {@link #onTracked(TrackableResult, VuforiaTrackable) onTracked} is called by the system to notify the
     * listener that its associated trackable is currently visible.
     *
     * @param trackableResult the Vuforia trackable result object in which we were located
     */
    @Override public synchronized void onTracked(TrackableResult trackableResult, VuforiaTrackable child)
        {
        this.currentPose = trackableResult.getPose();
        this.newPoseAvailable = true;
        this.newLocationAvailable = true;
        this.lastTrackedPose = this.currentPose;

        if (trackableResult.isOfType(VuMarkTargetResult.getClassType()))
            {
            VuMarkTargetResult vuMarkTargetResult = (VuMarkTargetResult)trackableResult;
            VuMarkTarget vuMarkTarget = (VuMarkTarget) vuMarkTargetResult.getTrackable();
            vuMarkInstanceId = new VuMarkInstanceId(vuMarkTarget.getInstanceId());
            }
        }

    /**
     * Called by the system to inform the trackable that it is no longer visible.
     */
    @Override public synchronized void onNotTracked()
        {
        this.currentPose = null;
        this.newPoseAvailable = true;
        this.newLocationAvailable = true;
        this.vuMarkInstanceId = null;
        }
    }
