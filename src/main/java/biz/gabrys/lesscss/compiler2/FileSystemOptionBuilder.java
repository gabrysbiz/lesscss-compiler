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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;
import biz.gabrys.lesscss.compiler2.util.StringUtils;

/**
 * <p>
 * Responsible for creating new instances of {@link FileSystemOption}.
 * </p>
 * <p>
 * Example code:
 * </p>
 * 
 * <pre>
 * {@link FileSystemOption} fileSystem;
 * // create a file system with no parameters
 * fileSystem = new {@link #FileSystemOptionBuilder() FileSystemOptionBuilder}().{@link #withClass(String) withClass}("org.example.FileSystem").{@link #build() build}();
 * 
 * // create a file system with parameters
 * Map&lt;String, String&gt; parameters = new HashMap&lt;String, String&gt;();
 * parameters.put("param1", "value1");
 * parameters.put("param2", "value2");
 * fileSystem = new {@link #FileSystemOptionBuilder() FileSystemOptionBuilder}() //
 *                      .{@link #withClass(Class) withClass}(org.example.FileSystem.class) //
 *                      .{@link #appendParameters(Map) appendParameters}(parameters) //
 *                      .{@link #appendParameter(CharSequence, CharSequence) appendParameter}("param3", "value3") //
 *                      .{@link #build() build}();
 * </pre>
 * 
 * @see FileSystemOptionsBuilder
 * @see LessOptions
 */
public class FileSystemOptionBuilder {

    private String className;
    private final Map<String, String> parameters = new LinkedHashMap<String, String>();

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public FileSystemOptionBuilder() {
        // do nothing
    }

    /**
     * Sets a file system class name.
     * @param className the file system class name (cannot be blank).
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the file system class name is blank.
     * @since 2.0.0
     * @see #withClass(Class)
     */
    public FileSystemOptionBuilder withClass(final String className) {
        if (StringUtils.isBlank(className)) {
            throw new IllegalArgumentException("Class name cannot be blank");
        }
        this.className = className;
        return this;
    }

    /**
     * Sets a file system class.
     * @param clazz the file system class (cannot be {@code null}).
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the file system class is {@code null}.
     * @since 2.0.0
     * @see #withClass(String)
     */
    public FileSystemOptionBuilder withClass(final Class<? extends FileSystem> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        className = clazz.getName();
        return this;
    }

    /**
     * Appends file system parameters.
     * @param parameters the map with parameters (cannot be {@code null}).
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the parameters map or any parameter name is {@code null}.
     * @since 2.0.0
     * @see #appendParameter(CharSequence, CharSequence)
     */
    public FileSystemOptionBuilder appendParameters(final Map<? extends CharSequence, ? extends CharSequence> parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Paremeters cannot be null");
        }
        for (final Entry<? extends CharSequence, ? extends CharSequence> parameter : parameters.entrySet()) {
            appendParameter(parameter.getKey(), parameter.getValue());
        }
        return this;
    }

    /**
     * Appends a file system parameter.
     * @param name the parameter name (cannot be {@code null}).
     * @param value the parameter value.
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the parameters name is {@code null}.
     * @since 2.0.0
     * @see #appendParameters(Map)
     */
    public FileSystemOptionBuilder appendParameter(final CharSequence name, final CharSequence value) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name cannot be null");
        }
        parameters.put(name.toString(), StringUtils.toStringIfNotNull(value));
        return this;
    }

    /**
     * Builds a new file system option.
     * @return the file system option.
     * @throws BuilderCreationException if withClass method has not been called before this method.
     * @since 2.0.0
     * @see #withClass(Class)
     * @see #withClass(String)
     */
    public FileSystemOption build() {
        if (className == null) {
            throw new BuilderCreationException("withClass method has not been called before build method");
        }
        return new FileSystemOption(className, parameters);
    }
}
