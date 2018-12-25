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
package biz.gabrys.lesscss.compiler2;

import biz.gabrys.lesscss.compiler2.util.StringUtils;

/**
 * Represents {@link LessOptions#getGlobalVariables() global} and {@link LessOptions#getModifyVariables() modify}
 * variables.
 * @since 2.0.0
 */
public class LessVariable {

    private final String name;
    private final String value;

    /**
     * Constructs a new instance.
     * @param name the variable name (cannot be blank).
     * @param value the variable value (cannot be blank).
     * @throws IllegalArgumentException if name/value is blank.
     * @since 2.0.0
     */
    public LessVariable(final String name, final String value) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Variable name cannot be blank");
        }
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Variable value cannot be blank");
        }
        this.name = name;
        this.value = value;
    }

    /**
     * Returns a variable name.
     * @return the variable name (never blank).
     * @since 2.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a variable value.
     * @return the variable value (never blank).
     * @since 2.0.0
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     * @since 2.0.0
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = prime + name.hashCode();
        return prime * result + value.hashCode();
    }

    /**
     * {@inheritDoc}
     * @since 2.0.0
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final LessVariable other = (LessVariable) obj;
        return name.equals(other.name) && value.equals(other.value);
    }

    /**
     * Returns a string representation of the variable in the following format: {@code name=value}.
     * @return the string representation of the variable.
     * @since 2.0.0
     */
    @Override
    public String toString() {
        return name + '=' + value;
    }
}
