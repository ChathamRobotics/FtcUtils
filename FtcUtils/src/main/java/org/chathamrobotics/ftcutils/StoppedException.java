package org.chathamrobotics.ftcutils;

/*!
 * ftc-utils
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: Carson Storm
 * @Last Modified time: 5/26/2017
 */

/**
 * t=Thrown when the stop button is called and the robot needs to be interrupted.
 */
public class StoppedException extends Exception {

    /**
     * Constructs a new {@code StoppedException} that includes the current stack trace.
     */
    public StoppedException() {
    }

    /**
     * Constructs a new {@code StoppedException} with the current stack
     * trace and the specified detail message.
     *
     * @param detailMessage the detail message for this exception.
     */
    public StoppedException(String detailMessage) {
        super(detailMessage);
    }
}
