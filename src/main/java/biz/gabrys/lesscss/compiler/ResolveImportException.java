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
 * Thrown to indicate that an error occurred while resolving imports.
 * @since 1.0
 */
public class ResolveImportException extends CompilerException {

    private static final long serialVersionUID = 5939444465067203699L;

    private final String fileName;

    /**
     * Constructs a new instance with the specified detail message and imported file name.
     * @param message the detail message.
     * @param fileName the imported file name.
     * @since 1.0
     */
    public ResolveImportException(final String message, final String fileName) {
        super(message);
        this.fileName = fileName;
    }

    /**
     * Constructs a new instance with the specified detail message, imported file name and cause.
     * @param message the detail message.
     * @param fileName the imported file name.
     * @param cause the cause.
     * @since 1.0
     */
    public ResolveImportException(final String message, final String fileName, final Throwable cause) {
        super(message, cause);
        this.fileName = fileName;
    }

    /**
     * Returns imported file name which caused the error.
     * @return imported file name.
     */
    public String getFileName() {
        return fileName;
    }
}
