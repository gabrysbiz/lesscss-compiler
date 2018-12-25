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
import java.io.IOException;

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
}
