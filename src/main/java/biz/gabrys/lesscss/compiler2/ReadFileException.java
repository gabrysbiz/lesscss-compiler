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
 * Thrown to indicate that a compiler cannot read file content.
 * @since 2.0.0
 */
public class ReadFileException extends CompilerException {

    private static final long serialVersionUID = 3711806897577477826L;

    private final String filePath;

    /**
     * Constructs a new instance with the specified detail message and file path.
     * @param message the detail message.
     * @param filePath the file path.
     * @since 2.0.0
     */
    public ReadFileException(final String message, final String filePath) {
        super(message);
        this.filePath = filePath;
    }

    /**
     * Constructs a new instance with the specified detail message, cause and file path.
     * @param message the detail message.
     * @param cause the cause.
     * @param filePath the file path.
     * @since 2.0.0
     */
    public ReadFileException(final String message, final Throwable cause, final String filePath) {
        super(message, cause);
        this.filePath = filePath;
    }

    /**
     * Constructs a new instance with the cause and file path.
     * @param cause the cause.
     * @param filePath the file path.
     * @since 2.0.0
     */
    public ReadFileException(final Throwable cause, final String filePath) {
        super(cause);
        this.filePath = filePath;
    }

    /**
     * Returns a file path.
     * @return the file path.
     * @since 2.0.0
     */
    public String getFilePath() {
        return filePath;
    }
}
