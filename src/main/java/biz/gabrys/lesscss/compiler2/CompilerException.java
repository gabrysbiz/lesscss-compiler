/*
 * LessCSS Compiler
 * http://lesscss-compiler.projects.gabrys.biz/
 *
 * Copyright (c) 2015 Adam Gabrys
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 *  - a copy of the License at project page
 *  - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.lesscss.compiler2;

/**
 * Thrown to indicate that an error occurred during compilation process.
 * @since 2.0.0
 */
public class CompilerException extends RuntimeException {

    private static final long serialVersionUID = -818505762769749946L;

    /**
     * Constructs a new instance with the specified detail message.
     * @param message the detail message.
     * @since 2.0.0
     */
    public CompilerException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause.
     * @since 2.0.0
     */
    public CompilerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance with the specified cause.
     * @param cause the cause.
     * @since 2.0.0
     */
    public CompilerException(final Throwable cause) {
        super(cause);
    }
}
