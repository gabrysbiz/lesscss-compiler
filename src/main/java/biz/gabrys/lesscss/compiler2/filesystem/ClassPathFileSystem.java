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
package biz.gabrys.lesscss.compiler2.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import biz.gabrys.lesscss.compiler2.io.IOUtils;

/**
 * <p>
 * Represents a file system accessible via class path ({@code classpath} protocol).
 * </p>
 * <p>
 * Example paths:
 * </p>
 * <ul>
 * <li>classpath://style.less</li>
 * <li>classpath://styles/main.less</li>
 * </ul>
 * @since 2.0.0
 */
public class ClassPathFileSystem implements FileSystem {

    private static final String PROTOCOL_PREFIX = "classpath://";

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public ClassPathFileSystem() {
        // do nothing
    }

    @Override
    public void configure(final Map<String, String> parameters) {
        // do nothing
    }

    @Override
    public boolean isSupported(final String path) {
        return path.startsWith(PROTOCOL_PREFIX);
    }

    @Override
    public String normalize(final String path) throws URISyntaxException {
        return new URI(path).normalize().toString();
    }

    @Override
    public String expandRedirection(final String path) {
        return path;
    }

    @Override
    public boolean exists(final String path) {
        final String name = path.substring(PROTOCOL_PREFIX.length());
        final ClassLoader classLoader = getClassLoader();
        return classLoader.getResource(name) != null;
    }

    @Override
    public FileData fetch(final String path) throws IOException {
        final String name = path.substring(PROTOCOL_PREFIX.length());
        final InputStream stream = getClassLoader().getResourceAsStream(name);
        if (stream == null) {
            throw new IOException(String.format("cannot find resource \"%s\" in classpath", name));
        }
        return new FileData(IOUtils.toByteArray(stream));
    }

    /**
     * Returns the context {@link ClassLoader} for the currently executing thread object. The reason why this method has
     * been extracted is allow stubbing and mocking class loader objects in tests.
     * @return the context {@link ClassLoader}.
     * @since 2.0.0
     */
    protected ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
