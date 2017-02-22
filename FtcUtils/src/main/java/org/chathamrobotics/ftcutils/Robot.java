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

// test

public abstract class Robot {
//    CONSTANTS         //
    public static final long AUTO_START_WAIT_TIME = 10000;

//    TOOLS             //

    protected HardwareMap hardwareMap;
    protected Telemetry telemetry;
    protected Driver driver;
    protected RobotLog logger;

//    STATEFUL          //
    private String lastLogLine;

    private long timerEndTime;
    private boolean isTiming;

//    CONSTRUCTORS      //
    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        // use the class name for the tag so that if this is extended it matches that name
        this.logger = new RobotLog(this.getClass().getSimpleName(), telemetry);

        this.initHardware();
    }

//    ENUMS         //

    public enum Side {
        FRONT (Math.PI/2,OmniWheelDriver.FRONT_OFFSET, "Front"),
        LEFT (Math.PI, OmniWheelDriver.LEFT_OFFSET, "Left"),
        RIGHT (0, OmniWheelDriver.RIGHT_OFFSET, "Right"),
        BACK (3 * Math.PI / 2, OmniWheelDriver.BACK_OFFSET, "Back");

        public double angle;
        public int angleDeg;
        public double offset;
        public String name;

        Side(double angle, double offset, String name) {
            this.angle = angle;
            this.angleDeg = (int)(angle * 180 / Math.PI);
            this.offset = offset;

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
        this.logger.info("Stopping Robot...");
//        this.log("Stopping Robot...");

        // Stops all the motors. All robots should have this in common
        for (Map.Entry<String, DcMotor> entry : this.hardwareMap.dcMotor.entrySet()) {
            entry.getValue().setPower(0);
        }
    }

    /**
     * This method is used to update the telemetry and robot log.
     *
     * @param update    whether or not to update telemetry.
     * @param looping   whether or not in a loop.
     */
    public void debug(boolean update, boolean looping) {

        // Debug motor values
        for (Map.Entry<String, DcMotor> entry : this.hardwareMap.dcMotor.entrySet()) {
            this.logger.debug("Motor " + entry.getKey() + " Power",
                    entry.getValue().getController().getMotorPower(entry.getValue().getPortNumber()), looping);
        }

        // Debug servo values
        for (Map.Entry<String, Servo> entry: this.hardwareMap.servo.entrySet()) {
            this.logger.debug("Servo" + entry.getKey() + " Position",
                    entry.getValue().getController().getServoPosition(entry.getValue().getPortNumber()), looping);
        }


        // Optical Distance sensors
        for (Map.Entry<String, OpticalDistanceSensor> entry: this.hardwareMap.opticalDistanceSensor.entrySet()) {
            this.logger.debug("ODS " + entry.getKey() + " Light", entry.getValue().getLightDetected(), looping);
        }

        // update telemetry values if needed
        if(update) {
            this.telemetry.update();
        }
    }

    /**
     * Debugs hardware values
     */
    public void debug() {
        debug(true, true); // This is here just to make debug easier to call instead of having to do debug(true). If you don't want the telemetry to update when debug is called then do debug(false)
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
            return false;
        }

        return true;
    }

    public boolean isTimerUp() {
        return System.currentTimeMillis() >= timerEndTime;
    }
}
