package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/*!
 * ftc-utils
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: Carson Storm
 * @Last Modified time: 5/26/2017
 */

/**
 * {@link AutonomousOpMode} provides utilities for working with Autonomous opmodes.
 * These utilities include instantiating and storing a robot class, handling stopping and waiting
 * for stop. The purpose of this class is allow the developer to focus on the autonomous procedure,
 * and to not have to worry about the setup.
 *
 * <p>Here is an example of how one might implement {@link AutonomousOpMode}:</p>
 *
 * <pre>
 *     public class AutoOpMode extends AutonomousOpMode{@code<Robot9853>} {
 *         public AutoOpMode(boolean isRedTeam) {
 *             super(isRedTeam, Robot9853.class);
 *         }
 *
 *         {@code @Override}
 *         public void runRobot() throws StoppedException, InterruptedException {
 *             this.robot.doSomething();
 *
 *             // Your autonomous code
 *         }
 *     }
 * </pre>
 *
 * @param <ROBOT_TYPE>  The type to use as the robot object.
 */
public abstract class AutonomousOpMode<ROBOT_TYPE extends Robot> extends LinearOpMode {
    /**
     * Whether the current team is red.
     */
    protected boolean isRedTeam;

    /**
     * The robot object.
     */
    protected ROBOT_TYPE robot;

    /**
     * Called on start
     */
    abstract public void runRobot() throws StoppedException, InterruptedException;

    /**
     * Creates a new instance of AutonomousOpMode
     * @param isRedTeam     Whether the current team is red or not.
     * @param robotClazz    The class of the robot.
     */
    public AutonomousOpMode (boolean isRedTeam, Class<ROBOT_TYPE> robotClazz) {
        this.isRedTeam = isRedTeam;

        try {
            this.robot = robotClazz.getDeclaredConstructor(OpMode.class).newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs op mode. Duh!
     * @throws InterruptedException     Thrown when the thread is interrupted. This is usually thrown on stop.
     */
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        this.robot.debug();
        this.robot.start();

        try {
            runRobot();
        } catch (StoppedException error) {
            // Just ignore
        } finally {
            this.robot.debug();
            this.robot.stop();
        }
    }

    /**
     * Checks the status of the robot and the opmode.
     * @throws StoppedException     Thrown if the opmode has stopped.
     */
    public void status() throws StoppedException{
        this.robot.debug();
        checkForStop();
    }

    /**
     * Checks to make sure the opmode has not been stopped.
     * @throws StoppedException     Thrown if the opmode has been stopped.
     */
    protected void checkForStop() throws StoppedException{
        if (! opModeIsActive()) throw new StoppedException();
    }
}
