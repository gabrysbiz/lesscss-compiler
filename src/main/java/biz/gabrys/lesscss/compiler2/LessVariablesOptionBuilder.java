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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import biz.gabrys.lesscss.compiler2.util.StringUtils;

/**
 * <p>
 * Allows to create value of the {@link LessOptions#getGlobalVariables() global} and
 * {@link LessOptions#getModifyVariables() modify} variables options. The builder allows adding multiple variables with
 * the same name (they are not replaced).
 * </p>
 * <p>
 * Example code:
 * </p>
 * 
 * <pre>
 * Map&lt;String, String&gt; variablesMap = new LinkedHashMap&lt;String, String&gt;();
 * variablesMap.put("name1", "value1");
 * variablesMap.put("name2", "value2");
 * 
 * List&lt;{@link LessVariable}&gt; variables = new {@link #LessVariablesOptionBuilder() LessVariablesOptionBuilder}()
 *                                      .{@link #append(Map) append}(variablesMap)
 *                                      .{@link #append(CharSequence, CharSequence) append}("name3", "value3")
 *                                      .{@link #append(LessVariable) append}(new {@link LessVariable#LessVariable(String, String) LessVariable}("name2", "value4"))
 *                                      .{@link #build() build}();
 * 
 * System.out.println(variables.get(0)); // name1=value1
 * System.out.println(variables.get(1)); // name2=value2
 * System.out.println(variables.get(2)); // name3=value3
 * System.out.println(variables.get(3)); // name2=value4
 * </pre>
 * 
 * @since 2.0.0
 * @see LessOptions
 * @see LessVariable
 */
public class LessVariablesOptionBuilder {

    private final List<LessVariable> variables = new LinkedList<LessVariable>();

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public LessVariablesOptionBuilder() {
        // do nothing
    }

    /**
     * Appends variables.
     * @param variables the variables to add (cannot be {@code null}).
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the variables is {@code null} or at least one key or value is blank.
     * @since 2.0.0
     */
    public LessVariablesOptionBuilder append(final Map<? extends CharSequence, ? extends CharSequence> variables) {
        if (variables == null) {
            throw new IllegalArgumentException("Variables cannot be null");
        }
        for (final Entry<? extends CharSequence, ? extends CharSequence> variable : variables.entrySet()) {
            append(variable.getKey(), variable.getValue());
        }
        return this;
    }

    /**
     * Appends a variable with specified name and value.
     * @param name the variable name (cannot be blank).
     * @param value the variable name (cannot be blank).
     * @return {@code this} builder.
     * @throws IllegalArgumentException if key or value is blank.
     * @since 2.0.0
     */
    public LessVariablesOptionBuilder append(final CharSequence name, final CharSequence value) {
        getVariables().add(new LessVariable(StringUtils.toStringIfNotNull(name), StringUtils.toStringIfNotNull(value)));
        return this;
    }

    /**
     * Appends variables.
     * @param variables the variables to add (cannot be {@code null}).
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the variables or any contained variable is {@code null}.
     * @since 2.0.0
     */
    public LessVariablesOptionBuilder append(final Iterable<? extends LessVariable> variables) {
        if (variables == null) {
            throw new IllegalArgumentException("Variables cannot be null");
        }
        for (final LessVariable variable : variables) {
            append(variable);
        }
        return this;
    }

    /**
     * Appends variables.
     * @param variables the variables to add (cannot be {@code null}).
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the variables or any contained variable is {@code null}.
     * @since 2.0.0
     */
    public LessVariablesOptionBuilder append(final LessVariable... variables) {
        if (variables == null) {
            throw new IllegalArgumentException("Variables cannot be null");
        }
        for (final LessVariable variable : variables) {
            append(variable);
        }
        return this;
    }

    /**
     * Appends a variable.
     * @param variable the variable to add (cannot be {@code null}).
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the variable is {@code null}.
     * @since 2.0.0
     */
    public LessVariablesOptionBuilder append(final LessVariable variable) {
        if (variable == null) {
            throw new IllegalArgumentException("Variable cannot be null");
        }
        getVariables().add(variable);
        return this;
    }

    /**
     * Builds a new list with all appended variables.
     * @return the new list with all appended variables.
     * @since 2.0.0
     */
    public List<LessVariable> build() {
        return new ArrayList<LessVariable>(getVariables());
    }

    /**
     * Returns an internal state of the builder.
     * @return the internal state.
     * @since 2.0.0
     */
    protected List<LessVariable> getVariables() {
        return variables;
    }
}
