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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Responsible for creating new instances of the {@link List} which stores not empty values.
 * @since 1.2
 */
public class ListWithoutEmptyValuesBuilder {

    private final List<Object> values = new LinkedList<Object>();

    /**
     * Constructs a new instance.
     * @since 1.2
     */
    public ListWithoutEmptyValuesBuilder() {
        // do nothing
    }

    /**
     * Appends value only if it is not equal to {@code null}.
     * @param value the appended value.
     * @return {@code this} builder.
     * @since 1.2
     */
    public ListWithoutEmptyValuesBuilder add(final Object value) {
        if (value != null) {
            values.add(value);
        }
        return this;
    }

    /**
     * Appends value only if it is not blank (empty or contains only whitespaces) or not equal to {@code null}.
     * @param value the appended value.
     * @return {@code this} builder.
     * @since 1.2
     */
    public ListWithoutEmptyValuesBuilder add(final String value) {
        if (StringUtils.isNotBlank(value)) {
            values.add(value);
        }
        return this;
    }

    /**
     * Creates a new instance of the {@link List}.
     * @return the new instance of the {@link List}.
     * @since 1.2
     */
    public List<Object> create() {
        return new ArrayList<Object>(values);
    }
}
