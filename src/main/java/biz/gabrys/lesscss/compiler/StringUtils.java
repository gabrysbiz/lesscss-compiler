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
 * Provides functionality for working with texts.
 * @since 1.2
 */
public final class StringUtils {

    private StringUtils() {
        // blocks the possibility of create a new instance
    }

    /**
     * Checks whether a text is not empty (""), not {@code null} and not stores only whitespace.
     * @param text the text to check.
     * @return {@code true} if the text is not empty (""), not {@code null} and not stores only whitespace, otherwise
     *         {@code false}.
     * @since 1.2
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
}
