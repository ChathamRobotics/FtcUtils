package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*!
 * ftc-utils
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: Carson Storm
 * @Last Modified time: 5/26/2017
 */

/**
 * Driver to control a omniwheel drive setup
 */
public class OmniWheelDriver implements Driver {
    /**
     * The angle of transformation between the game pad the x-drive motor arraignment.
     */
    public static final double OMNIWHEEL_PLANE_TRANSFORM_ANGLE = Math.PI / 4;

    /**
     * The offset for the front of the robot.
     */
    public static final double FRONT_OFFSET = 0;

    /**
     * The offset for the left side of the robot.
     */
    public static final double LEFT_OFFSET = Math.PI/2;

    /**
     * The offset for the back of the robot.
     */
    public static final double BACK_OFFSET = Math.PI;

    /**
     * The offset for the right side of the robot.
     */
    public static final double RIGHT_OFFSET = 3* Math.PI / 2;

    /**
     * The tag used for the logger.
     */
    public static final String TAG = "OmniWheelDriver";

    /**
     * The logger used to debug values.
     */
    private RobotLogger logger;

    /**
     * The motors used for driving.
     */
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    /**
     * Whether or not to log debug messages.
     */
    private boolean silent;

    /**
     * The offset angle to use.
     */
    private double offsetAngle = 0;

    /**
     * Builds a new OmniWheelDriver using the default motor names (FrontLeft, FrontRight, BackLeft and BackRight).
     * @param opMode    Used to get all of the necessary components.
     * @return          The built OmniWheelDriver.
     */
    public static OmniWheelDriver build(OpMode opMode) {
        return new OmniWheelDriver(
                opMode.hardwareMap.dcMotor.get("FrontLeft"),
                opMode.hardwareMap.dcMotor.get("FrontRight"),
                opMode.hardwareMap.dcMotor.get("BackLeft"),
                opMode.hardwareMap.dcMotor.get("BackRight"),
                new RobotLogger(OmniWheelDriver.TAG, opMode.telemetry)
        );
    }

    /**
     * Builds a new OmniWheelDriver using the default motor names (FrontLeft, FrontRight, BackLeft and BackRight).
     * @param hardwareMap   Used to get all of the motors.
     * @param logger        The logger for used for debugging values.
     * @return              The built OmniWheelDriver.
     */
    public static  OmniWheelDriver build(HardwareMap hardwareMap, RobotLogger logger) {
        return new OmniWheelDriver(
                hardwareMap.dcMotor.get("FrontLeft"),
                hardwareMap.dcMotor.get("FrontRight"),
                hardwareMap.dcMotor.get("BackLeft"),
                hardwareMap.dcMotor.get("BackRight"),
                logger
        );
    }

    /**
     * Builds a new OmniWheelDriver using the default motor names (FrontLeft, FrontRight, BackLeft and BackRight).
     * @param hardwareMap   Used to get all of the required motors.
     * @param telemetry     Used to build the logger.
     * @return              The built OmniWheelDriver.
     */
    public static  OmniWheelDriver build(HardwareMap hardwareMap, Telemetry telemetry) {
        return new OmniWheelDriver(
                hardwareMap.dcMotor.get("FrontLeft"),
                hardwareMap.dcMotor.get("FrontRight"),
                hardwareMap.dcMotor.get("BackLeft"),
                hardwareMap.dcMotor.get("BackRight"),
                new RobotLogger(OmniWheelDriver.TAG, telemetry)
        );
    }

    /**
     * Creates an instance of OmniWheelDriver.
     * @param frontLeft     The motor located in the front left corner of the robot.
     * @param frontRight    The motor located in the front right corner of the robot.
     * @param backLeft      The motor located in the back left corner of the robot.
     * @param backRight     The motor located in the back right corner of the robot.
     * @param logger        The logger used to debugging values.
     */
    public OmniWheelDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft,
                           DcMotor backRight, RobotLogger logger) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.logger = logger;
    }

    /**
     * Sets the offset angle. The offset angle is used to change what is referenced as the front. It is recommended that only FRONT_OFFSET, LEFT_OFFSET, RIGHT_OFFSET and BACK_OFFSET are used
     * @param angle     The offset angle to use.
     */
    public void setOffsetAngle(double angle) {
        this.offsetAngle = angle;
    }

    /**
     * Sets silent. Whether or not to output debugging values. true for no logging and false for logging.
     * @param silent    Whether or not be silent.
     */
    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    /**
     * Sets the zero behavior of the drive motors to float or brake. Float behavior makes the changes in motor speed more gradual. This is especially useful when a lift is raised.
     * @param useFloat  Whether or not to use float behavior.
     */
    public void setFloatBehavior(boolean useFloat) {
        this.frontLeft.setZeroPowerBehavior(useFloat ? DcMotor.ZeroPowerBehavior.FLOAT : DcMotor.ZeroPowerBehavior.BRAKE);
        this.frontRight.setZeroPowerBehavior(useFloat ? DcMotor.ZeroPowerBehavior.FLOAT : DcMotor.ZeroPowerBehavior.BRAKE);
        this.backLeft.setZeroPowerBehavior(useFloat ? DcMotor.ZeroPowerBehavior.FLOAT : DcMotor.ZeroPowerBehavior.BRAKE);
        this.backRight.setZeroPowerBehavior(useFloat ? DcMotor.ZeroPowerBehavior.FLOAT : DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Uses the default controls and the gamepad values to drive the robot.
     * Default controls:
     * - The left joystick controls the movement. The robot will travel in the direction of the joystick.
     * - The right joystick x direction controls the rotation of the robot.
     * - The d-pad is used to change which side of the robot is referenced as the front. eg. Pressing the down direction on the d-pad makes the back of the robot the front.
     * @param gp    The game pad object.
     */
    public void driveWithControls(Gamepad gp) {
        // Any fancy driving controls should go here
        if(gp.dpad_up){setOffsetAngle(FRONT_OFFSET);}
        if(gp.dpad_left){setOffsetAngle(LEFT_OFFSET);}
        if(gp.dpad_down){setOffsetAngle(BACK_OFFSET);}
        if(gp.dpad_right){setOffsetAngle(RIGHT_OFFSET);}

        drive(-gp.left_stick_y, gp.left_stick_x, gp.right_stick_x);
    }

    /**
     * Drives in the direction specified by the given x and y value.
     * @param y    The Y portion of the direction.
     * @param x    The X portion of the direction.
     * @param rotation      The rotation to perform. Positive for right and negative for left.
     */
    public void drive(double y, double x, double rotation) {
        drive(y, x, rotation, Math.sqrt(x * x + y * y));
    }

    /**
     * Drives in the direction specified by the given x and y value.
     * @param y             The Y portion of the direction.
     * @param x             The X portion of the direction.
     * @param rotation      The rotation to perform. Positive for right and negative for left.
     * @param magnitude     The magnitude of the motor power.
     */
    public void drive(double y, double x, double rotation, double magnitude) {
        Range.throwIfRangeIsInvalid(x, -1, 1);
        Range.throwIfRangeIsInvalid(y, -1, 1);
        Range.throwIfRangeIsInvalid(rotation, -1, 1);

        if (! this.silent) {
            this.logger.debug("x direction", x, true);
            this.logger.debug("y direction", y, true);
        }

        move(Math.atan2(y, x), rotation, magnitude);
    }

    /**
     * Moves the robot in the direction specified.
     * @param direction     The direction(angle) for the robot to go measured radians.
     * @param rotation      The rotation to perform. Positive for right and negative for left.
     * @param magnitude     The magnitude of the motor power.
     */
    public void move(double direction, double rotation, double magnitude) {
        if (! this.silent) {
            this.logger.debug("rotation", rotation, true);
            this.logger.debug("direction", direction, true);
            this.logger.debug("direction(transformed)", direction + OMNIWHEEL_PLANE_TRANSFORM_ANGLE, true);
            this.logger.debug("direction(transformed) with offset", direction + OMNIWHEEL_PLANE_TRANSFORM_ANGLE + this.offsetAngle, true);
            this.logger.debug("magnitude", magnitude, true);
            this.logger.debug("offset angle", this.offsetAngle, true);
        }

        direction += OMNIWHEEL_PLANE_TRANSFORM_ANGLE + this.offsetAngle;

        this.frontLeft.setPower(calculateMotorPower(true, true, direction, rotation, magnitude));
        this.frontRight.setPower(calculateMotorPower(true, false, direction, rotation, magnitude));
        this.backLeft.setPower(calculateMotorPower(false, true, direction, rotation, magnitude));
        this.backRight.setPower(calculateMotorPower(false, false, direction, rotation, magnitude));
    }

    /**
     * Stops the motors controlled by the driver.
     */
    public void stop(){
        if (! this.silent) {
            this.logger.info("Stopping");
        }

        this.frontLeft.setPower(0);
        this.frontRight.setPower(0);
        this.backLeft.setPower(0);
        this.backRight.setPower(0);
    }

    /**
     * Calculates the power to set each of the motors based on the direction and their position.
     * @param isFront       Whether or not the motor is in the front.
     * @param isLeft        Whether or not the motor is on the left side.
     * @param direction     The direction(angle) for the robot to go measured radians.
     * @param rotation      The rotation to perform. Positive for right and negative for left.
     * @param magnitude     The magnitude of the motor power.
     * @return              The power to set.
     */
    private double calculateMotorPower(boolean isFront, boolean isLeft, double direction, double rotation, double magnitude) {
        double power = (isFront == isLeft ? Math.sin(direction) : Math.cos(direction)) * magnitude;

        if(isFront){
            power -= rotation;
        }
        else {
            power += rotation;
            power *= -1;
        }

        return Range.clip(power, -1, 1);
    }
}
