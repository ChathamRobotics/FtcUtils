package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;

/*!
 * ftc-utils
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: Carson Storm
 * @Last Modified time: 5/26/2017
 */

/**
 * An abstract representation of a robot.
 */
public abstract class Robot {
    /**
     * The time in autonomous before the robot is allowed to cross the line.
     */
    public static final long AUTO_START_WAIT_TIME = 10000;

    /**
     * The robot's hardware map.
     */
    protected HardwareMap hardwareMap;

    /**
     * The telemetry object to write data to.
     */
    protected Telemetry telemetry;

    /**
     * The robot's driving system.
     */
    protected Driver driver;

    /**
     * The logger for the robot.
     */
    public RobotLogger logger;

//    private long timerEndTime;
//    private boolean isTiming;

    /**
     * Creates a instance of Robot.
     * @param opMode    Used to get all of the necessary components of the robot.
     */
    public Robot (OpMode opMode) {
        this(opMode.hardwareMap, opMode.telemetry);
    }

    /**
     * Creates a instance of Robot.
     * @param hardwareMap   Used to get all of the robot's hardware.
     * @param telemetry     The opmode's telemetry.
     */
    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        // use the class name for the tag so that if this is extended it matches that name
        this.logger = new RobotLogger(this.getClass().getSimpleName(), telemetry);

        this.initHardware();
    }

    /**
     * Initializes the robots hardware
     */
    public abstract void initHardware();

    /**
     * Starts robot. (eg: puts servos in start positions)
     */
    public abstract void start();

    /**
     * This method is used to stop the robot. This should set all motor powers to zero, and do anything else required to stop the robot.
     */
    public void stop() {
        this.logger.info("Stopping Robot...");

        // Stops all the motors. All robots should have this in common
        for (Map.Entry<String, DcMotor> entry : this.hardwareMap.dcMotor.entrySet()) {
            entry.getValue().setPower(0);
        }
    }

    /**
     * This method is used to update the telemetry and robot log.
     *
     * @param update    Whether or not to update telemetry.
     * @param looping   Whether or not this statement is contained in a loop.
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

        // Debug optical distance sensors
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


//    /**
//     * waits for given time duration
//     * @param duration  the time to do for
//     */
//    public boolean doUntil(long duration)  {
//        if(! isTiming) timerEndTime = System.currentTimeMillis() + duration;
//        isTiming = true;
//
//        if (System.currentTimeMillis() >= timerEndTime) {
//            isTiming = false;
//            return false;
//        }
//
//        return true;
//    }
//
//    public boolean isTimerUp() {
//        return System.currentTimeMillis() >= timerEndTime;
//    }
}
