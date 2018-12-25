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
 * Available values of the {@code line numbers} option.
 * @since 2.0.0
 */
public enum LineNumbersValue {

    /**
     * Line numbers won't be put in CSS code.
     * @since 2.0.0
     */
    OFF(""),
    /**
     * Line numbers will be put in CSS comments blocks.
     * @since 2.0.0
     */
    COMMENTS("comments"),
    /**
     * Line numbers will be put in &#64;media queries.
     * @since 2.0.0
     */
    MEDIA_QUERY("mediaquery"),
    /**
     * Line numbers will be put in CSS comments blocks and &#64;media queries.
     * @since 2.0.0
     */
    ALL("all");

    private final String value;

    LineNumbersValue(final String value) {
        this.value = value;
    }

    /**
     * Returns a value assigned to {@code this} object.
     * @return the value assigned to {@code this} object.
     * @since 2.0.0
     */
    public String getValue() {
        return value;
    }
}
