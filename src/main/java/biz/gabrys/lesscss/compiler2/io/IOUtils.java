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
package biz.gabrys.lesscss.compiler2.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Provides tools for working with I/O operations.
 * @since 2.0.0
 */
public final class IOUtils {

    private IOUtils() {
        // blocks the possibility of create a new instance
    }

    /**
     * Closes a {@link Cloneable} unconditionally.
     * @param closeable the object to close (can be {@code null} or already closed).
     * @since 2.0.0
     */
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (final IOException e) {
            // do nothing
        }
    }

    /**
     * Writes a content to a file.
     * @param file the file (cannot be {@code null}).
     * @param content the content (cannot be {@code null}).
     * @throws IllegalArgumentException if the file or the content is {@code null}.
     * @throws IOException if an I/O error occurs.
     * @since 2.0.0
     */
    public static void write(final File file, final CharSequence content) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }

        Writer writer = null;
        try {
            writer = new PrintWriter(file, "UTF-8");
            writer.write(content.toString());
        } finally {
            closeQuietly(writer);
        }
    }
}
