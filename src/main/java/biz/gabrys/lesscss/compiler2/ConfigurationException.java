/*
 * LessCSS Compiler
 * http://lesscss-compiler.projects.gabrys.biz/
 *
 * Copyright (c) 2015 Adam Gabryś
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 *  - a copy of the License at project page
 *  - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.lesscss.compiler2;

/**
 * Thrown to indicate that a compiler has been configured incorrectly (example: missing required options).
 * @since 2.0.0
 */
public class ConfigurationException extends CompilerException {

    private static final long serialVersionUID = -2386288381210804468L;

    /**
     * Constructs a new instance with the specified detail message.
     * @param message the detail message.
     * @since 2.0.0
     */
    public ConfigurationException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause.
     * @since 2.0.0
     */
    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance with the specified cause.
     * @param cause the cause.
     * @since 2.0.0
     */
    public ConfigurationException(final Throwable cause) {
        super(cause);
    }
}
