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
package biz.gabrys.lesscss.compiler2.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Provides tools for working with files.
 * @since 2.0.0
 */
public final class FileUtils {

    private FileUtils() {
        // blocks the possibility of create a new instance
    }

    /**
     * Writes a content to a file.
     * @param file the file (cannot be {@code null}).
     * @param content the content (cannot be {@code null}).
     * @param encoding the encoding to use (cannot be {@code null}).
     * @throws IllegalArgumentException if the file, content or encoding is {@code null}.
     * @throws IOException if an I/O error occurs.
     * @since 2.0.0
     */
    public static void write(final File file, final CharSequence content, final String encoding) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (encoding == null) {
            throw new IllegalArgumentException("Encoding cannot be null");
        }

        Writer writer = null;
        try {
            writer = new PrintWriter(file, encoding);
            writer.write(content.toString());
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
