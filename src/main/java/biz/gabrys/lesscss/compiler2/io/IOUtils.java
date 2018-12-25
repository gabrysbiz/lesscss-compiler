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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides tools for working with I/O operations.
 * @since 2.0.0
 */
public final class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int END_OF_FILE = -1;

    private IOUtils() {
        // blocks the possibility of create a new instance
    }

    /**
     * Reads data from an input stream and closes the stream at the end of the operation.
     * @param inputStream the input stream to read from (cannot be {@code null}).
     * @return bytes stored by the input stream.
     * @throws IllegalArgumentException if the file, content or encoding is {@code null}.
     * @throws IOException if an I/O error occurs.
     * @since 2.0.0
     */
    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            for (int readBytes = inputStream.read(buffer); readBytes != END_OF_FILE; readBytes = inputStream.read(buffer)) {
                outputStream.write(buffer, 0, readBytes);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } finally {
            closeQuietly(inputStream);
        }
    }

    /**
     * Closes a {@link Closeable} unconditionally.
     * @param closeable the object to close (can be {@code null} or already closed).
     * @since 2.0.0
     */
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (final Exception e) {
            // do nothing
        }
    }
}
