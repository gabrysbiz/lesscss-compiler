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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Responsible for creating new instances of the {@link List} which stores not empty values.
 * @param <T> the type of values.
 * @since 2.0.0
 */
public class ListWithoutEmptyValuesBuilder<T> {

    private final Checker<T> checker;
    private final List<T> values = new LinkedList<T>();

    /**
     * Constructs a new instance.
     * @param checker the checker responsible for testing if values are empty or not.
     * @throws IllegalArgumentException if the checker is {@code null}.
     * @since 2.0.0
     */
    public ListWithoutEmptyValuesBuilder(final Checker<T> checker) {
        if (checker == null) {
            throw new IllegalArgumentException("Checker cannot be null");
        }
        this.checker = checker;
    }

    /**
     * Appends all values which are not blank.
     * @param values the appended values.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public ListWithoutEmptyValuesBuilder<T> append(final T[] values) {
        if (values != null) {
            for (final T value : values) {
                append(value);
            }
        }
        return this;
    }

    /**
     * Appends a value only if it is not blank.
     * @param value the appended value.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public ListWithoutEmptyValuesBuilder<T> append(final T value) {
        if (checker.isNotEmpty(value)) {
            values.add(value);
        }
        return this;
    }

    /**
     * Builds a new instance of the {@link List}.
     * @return the new instance of the {@link List}.
     * @since 2.0.0
     */
    public List<T> build() {
        return new ArrayList<T>(values);
    }

    /**
     * Responsible for testing if values are empty or not.
     * @param <T> the type of values.
     * @since 2.0.0
     */
    public interface Checker<T> {

        /**
         * Tests whether a value is not empty.
         * @param value the tested value.
         * @return {@code true} whether the value is not empty, otherwise {@code false}.
         * @since 2.0.0
         */
        boolean isNotEmpty(T value);
    }
}
