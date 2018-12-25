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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;
import biz.gabrys.lesscss.compiler2.util.StringUtils;

/**
 * <p>
 * Represents {@code --file-system} option. The option should be defined in the following format:
 * {@code classname(,parameterslist)} where:
 * </p>
 * <ul>
 * <li>classname - java class name (example {@code org.example.FileSystem})</li>
 * <li>parameterslist - comma separated list of parameters (example: {@code param1=value1,param2=value2})</li>
 * </ul>
 * <p>
 * Special characters used in parameters names or values have to be escaped in parameters list by adding underscore
 * character before:
 * </p>
 * <ul>
 * <li>underscore "_" -&gt; two underscores "__"</li>
 * <li>comma "," -&gt; underscore + comma "_,"</li>
 * <li>equal "=" -&gt; underscore + equal "_="</li>
 * </ul>
 * @since 2.0.0
 * @see FileSystem
 * @see FileSystemOptionBuilder
 */
public class FileSystemOption {

    private static final String UNDERSCORE_SIGN = "_";
    private static final char COMMA_SIGN = ',';
    private static final char EQUAL_SIGN = '=';

    private static final Pattern UNDERSCORE_PATTERN = Pattern.compile(UNDERSCORE_SIGN);
    private static final Pattern COMMA_PATTERN = Pattern.compile("" + COMMA_SIGN);
    private static final Pattern EQUAL_PATTERN = Pattern.compile("" + EQUAL_SIGN);

    private final String className;
    private final Map<String, String> parameters = new LinkedHashMap<>();

    /**
     * Constructs a new instance with a file system class and no parameters.
     * @param clazz the file system class (cannot be {@code null}).
     * @throws IllegalArgumentException if the file system class is {@code null}.
     * @since 2.0.0
     */
    public FileSystemOption(final Class<? extends FileSystem> clazz) {
        this(clazz, Collections.<String, String>emptyMap());
    }

    /**
     * Constructs a new instance with a file system class and parameters.
     * @param clazz the files system class (cannot be {@code null}).
     * @param parameters the parameters which would be used to configure the file system (cannot be {@code null}).
     * @throws IllegalArgumentException if the file system class or parameters is {@code null}.
     * @since 2.0.0
     */
    public FileSystemOption(final Class<? extends FileSystem> clazz, final Map<String, String> parameters) {
        if (clazz == null) {
            throw new IllegalArgumentException("File system class cannot be null");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        className = clazz.getName();
        this.parameters.putAll(parameters);
    }

    /**
     * Constructs a new instance with a file system class and no parameters.
     * @param className the file system class name (cannot be blank).
     * @throws IllegalArgumentException if the file system class name is blank.
     * @since 2.0.0
     */
    public FileSystemOption(final String className) {
        this(className, Collections.<String, String>emptyMap());
    }

    /**
     * Constructs a new instance with a file system class and parameters.
     * @param className the files system class name (cannot be blank).
     * @param parameters the parameters which would be used to configure the file system (cannot be {@code null}).
     * @throws IllegalArgumentException if the file system class name or parameters is {@code null}.
     * @since 2.0.0
     */
    public FileSystemOption(final String className, final Map<String, String> parameters) {
        if (StringUtils.isBlank(className)) {
            throw new IllegalArgumentException("Class name cannot be blank");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        this.className = className;
        this.parameters.putAll(parameters);
    }

    /**
     * Returns a file system class name.
     * @return the file system class name (never {@code null}).
     * @since 2.0.0
     */
    public String getClassName() {
        return className;
    }

    /**
     * Returns file system parameters.
     * @return the file system parameters (never {@code null}).
     * @since 2.0.0
     */
    public Map<String, String> getParameters() {
        return new LinkedHashMap<>(parameters);
    }

    /**
     * {@inheritDoc}
     * @since 2.0.0
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = prime + className.hashCode();
        return prime * result + parameters.hashCode();
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
        final FileSystemOption other = (FileSystemOption) obj;
        return className.equals(other.className) && parameters.equals(other.parameters);
    }

    /**
     * Returns a string representation of the option in the following format:
     * {@code classname,param1=value1,param2=value2...}. Special characters in parameters list are escaped:
     * <ul>
     * <li>underscore "_" -&gt; two underscores "__"</li>
     * <li>comma "," -&gt; underscore + comma "_,"</li>
     * <li>equal "=" -&gt; underscore + equal "_="</li>
     * </ul>
     * @return the string representation of the option.
     * @since 2.0.0
     */
    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder();
        text.append(className);
        for (final Entry<String, String> entry : parameters.entrySet()) {
            text.append(COMMA_SIGN);
            text.append(escapeSpecialCharacters(entry.getKey()));
            text.append(EQUAL_SIGN);
            if (entry.getValue() != null) {
                text.append(escapeSpecialCharacters(entry.getValue()));
            }
        }
        return text.toString();
    }

    private static String escapeSpecialCharacters(final String text) {
        String escaped = UNDERSCORE_PATTERN.matcher(text).replaceAll(UNDERSCORE_SIGN + UNDERSCORE_SIGN);
        escaped = COMMA_PATTERN.matcher(escaped).replaceAll(UNDERSCORE_SIGN + COMMA_SIGN);
        return EQUAL_PATTERN.matcher(escaped).replaceAll(UNDERSCORE_SIGN + EQUAL_SIGN);
    }
}
