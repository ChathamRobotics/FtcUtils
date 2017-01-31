package org.chathamrobotics.ftcutils;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;

/**
 * A Object to abstract away the hardware aspects of the robot. We're building Karel! Robot.turnLeft!
 */

public abstract class Robot {
//    CONSTANTS         //

//    TOOLS             //

    protected HardwareMap hardwareMap;
    protected Telemetry telemetry;
    protected Driver driver;

//    STATEFUL          //
    private String TAG;
    private String lastLogLine;

    private long timerEndTime;
    private boolean isTiming;

//    CONSTRUCTORS      //
    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.TAG = this.getClass().getSimpleName();

        this.initHardware();
    }

//    ENUMS         //

    public enum Side {
        FRONT (OmniWheelDriver.FRONT_OFFSET, "Front"),
        LEFT (OmniWheelDriver.LEFT_OFFSET, "Left"),
        RIGHT (OmniWheelDriver.RIGHT_OFFSET, "Right"),
        BACK (OmniWheelDriver.BACK_OFFSET, "Back");

        public double angle;
        public int angleDeg;
        public String name;

        Side(double angle, String name) {
            this.angle = angle;
            this.angleDeg = (int)(angle * 180 / Math.PI);

            this.name = name;
        }
    }

//    ABSTRACT METHODS  //

    /**
     * initializes the robots hardware
     */
    public abstract void initHardware();

    /**
     * starts robot. (ex: puts servos in start positions)
     */
    public abstract void start();

//    METHODS           //

    /**
     * This method is used to stop the robot. This should set all motor powers to zero, and do anything else required to stop the robot.
     */
    public void stop() {
        this.log("Stopping Robot...");

        // Stops all the motors. All robots should have this in common
        for (Map.Entry<String, DcMotor> entry : this.hardwareMap.dcMotor.entrySet()) {
            entry.getValue().setPower(0);
        }
    }

    /**
     * This method is used to update the telemetry and robot log
     * @param update    whether or not to update the telemetry
     * @param teleOut  whether or not to output the data to the telemetry
     */
    public void debug(boolean update, boolean teleOut, boolean androidOut) {

        // Debug motor values
        for (Map.Entry<String, DcMotor> entry : this.hardwareMap.dcMotor.entrySet()) {
            // if the motor is moving
//            if(entry.getValue().isBusy()) {}

            log("Motor " + entry.getKey() + " Power",
                    entry.getValue().getController().getMotorPower(entry.getValue().getPortNumber()), teleOut, androidOut);
        }

        // Debug servo values
        for (Map.Entry<String, Servo> entry: this.hardwareMap.servo.entrySet()) {
            log("Servo" + entry.getKey() + " Position",
                    entry.getValue().getController().getServoPosition(entry.getValue().getPortNumber()), teleOut, androidOut);
        }


        // Optical Distance sensors
        for (Map.Entry<String, OpticalDistanceSensor> entry: this.hardwareMap.opticalDistanceSensor.entrySet()) {
            log("ODS " + entry.getKey() + " Light",
                    entry.getValue().getLightDetected(), teleOut, androidOut);
        }

        // update telemetry values if needed
        if(update) {
            this.telemetry.update();
        }
    }
    public void debug() {
        debug(true, true, true); // This is here just to make debug easier to call instead of having to do debug(true). If you don't want the telemetry to update when debug is called then do debug(false)
    }


    /**
     * logs
     * @param line          the line the output
     * @param teleOut       whether or not to output to telemetry
     * @param androidOut    whether or not to output to android facilities
     */
    public void log(String line, boolean teleOut, boolean androidOut) {
        // output to telemetry
        if(teleOut) this.telemetry.addLine(this.TAG + ":" + line);

        // prevent flooding in android logs
        if(line.equals(lastLogLine)) return;
        else lastLogLine = line;

        // output to android
        if(androidOut) Log.d(this.TAG, line);
    }
    public void log(String line) {log(line, true, true);}

    /**
     * logs
     * @param caption       the caption for the message.
     * @param value         the message
     * @param teleOut       whether or not to output to the telemetry
     * @param androidOut    whether or not to output to android
     */
    public void log(String caption, Object value, boolean teleOut, boolean androidOut) {
        log(caption + ": " + value.toString(), teleOut, androidOut);
    }
    public void log(String caption, Object value) {
        log(caption, value, true, true);
    }

    /**
     * waits for given time duration
     * @param duration  the time to do for
     */
    public boolean doUntil(long duration)  {
        if(! isTiming) timerEndTime = System.currentTimeMillis() + duration;
        isTiming = true;

        if (System.currentTimeMillis() >= timerEndTime) {
            isTiming = false;
            return true;
        }

        return false;
    }

    public boolean isTimerUp() {
        return System.currentTimeMillis() >= timerEndTime;
    }
}
