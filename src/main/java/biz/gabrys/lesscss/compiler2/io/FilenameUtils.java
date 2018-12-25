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

/**
 * Provides tools for working with files names.
 * @since 2.0.0
 */
public final class FilenameUtils {

    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';

    private FilenameUtils() {
        // blocks the possibility of create a new instance
    }

    /**
     * Returns a file name extracted from a path.
     * @param path the path (cannot be {@code null}).
     * @return the file name (name + extension).
     * @throws IllegalArgumentException if the path is {@code null}.
     * @since 2.0.0
     */
    public static String getName(final String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        final int lastUnixPos = path.lastIndexOf(UNIX_SEPARATOR);
        final int lastWindowsPos = path.lastIndexOf(WINDOWS_SEPARATOR);
        final int index = Math.max(lastUnixPos, lastWindowsPos);
        return path.substring(index + 1);
    }

    /**
     * Returns a file extension extracted from a path.
     * @param path the path (cannot be {@code null}).
     * @return the file extension or empty text (if file does not have extension).
     * @throws IllegalArgumentException if the path is {@code null}.
     * @since 2.0.0
     */
    public static String getExtension(final String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        final String name = getName(path);
        final int index = name.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return name.substring(index + 1);
        }
    }
}
