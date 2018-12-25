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
package biz.gabrys.lesscss.compiler2.util;

/**
 * Provides functionality for working with texts.
 * @since 2.0.0
 */
public final class StringUtils {

    private StringUtils() {
        // blocks the possibility of create a new instance
    }

    /**
     * Checks whether a text is {@code null}, empty or stores only whitespace.
     * @param text the text to check.
     * @return {@code true} if the text is empty, {@code null} or stores only whitespace, otherwise {@code false}.
     * @since 2.0.0
     */
    public static boolean isBlank(final String text) {
        return !isNotBlank(text);
    }

    /**
     * Checks whether a text is not {@code null}, not empty and not stores only whitespace.
     * @param text the text to check.
     * @return {@code true} if the text is not empty, not {@code null} and not stores only whitespace, otherwise
     *         {@code false}.
     * @since 2.0.0
     */
    public static boolean isNotBlank(final String text) {
        if (text == null || "".equals(text)) {
            return false;
        }
        for (int i = 0; i < text.length(); ++i) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a text representation of an object or {@code null} if the object is {@code null}.
     * @param object the object.
     * @return the text representation of the object or {@code null}.
     * @since 2.0.0
     */
    public static String toStringIfNotNull(final Object object) {
        if (object != null) {
            return object.toString();
        }
        return null;
    }
}
