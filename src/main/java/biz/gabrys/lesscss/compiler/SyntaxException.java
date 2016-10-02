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
package biz.gabrys.lesscss.compiler;

/**
 * Thrown to indicate that a syntax error occurred during source file compilation.
 * @since 1.0
 */
public class SyntaxException extends CompilerException {

    private static final long serialVersionUID = -4880113730827461145L;

    /**
     * Constructs a new instance with the specified detail message.
     * @param message the detail message.
     * @since 1.0
     */
    public SyntaxException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause.
     * @since 1.0
     */
    public SyntaxException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance with the specified cause.
     * @param cause the cause.
     * @since 1.0
     */
    public SyntaxException(final Throwable cause) {
        super(cause);
    }
}
