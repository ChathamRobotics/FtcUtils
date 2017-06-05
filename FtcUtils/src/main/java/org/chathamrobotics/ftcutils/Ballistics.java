package org.chathamrobotics.ftcutils;

/*!
 * ftc-utils
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: Carson Storm
 * @Last Modified time: 5/26/2017
 */

/**
 * A collection of methods for ballistics calculations.
 */
public final class Ballistics {
    /**
     * The acceleration of gravity on earth.
     */
    public static final double GRAVITY_ACCELERATION = 9.81;

    /**
     * Finds the required velocity in order fire a projectile at a given target.
     * @param distance  The horizontal displacement to the target.
     * @param height    The vertical displacement to the target.
     * @param fireAngle The angle the project is being fired at.
     * @return          The required velocity.
     */
    public double requiredVelocity(double distance, double height, double fireAngle) {
        return (distance * GRAVITY_ACCELERATION) /
                Math.sqrt(2 * distance * GRAVITY_ACCELERATION * Math.cos(fireAngle) * Math.sin(fireAngle)
                        - 2 * GRAVITY_ACCELERATION * height * Math.pow(Math.cos(fireAngle), 2));
    }
}
