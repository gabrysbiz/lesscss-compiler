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
package biz.gabrys.lesscss.compiler2.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import biz.gabrys.lesscss.compiler2.io.IOUtils;

/**
 * <p>
 * Represents a local file system of the current machine.
 * </p>
 * <p>
 * Example paths:
 * </p>
 * <ul>
 * <li>C:\Users\less\style.less</li>
 * <li>/home/user/less/style.less</li>
 * </ul>
 * <p>
 * <strong>Warning</strong>: the local file system in file systems option should be put as last or not at all.
 * </p>
 * @since 2.0.0
 */
public class LocalFileSystem implements FileSystem {

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public LocalFileSystem() {
        // do nothing
    }

    @Override
    public boolean isSupported(final String path) {
        return new File(path).exists();
    }

    @Override
    public String normalize(final String path) throws IOException {
        return new File(path).getCanonicalPath();
    }

    @Override
    public String expandRedirection(final String path) throws IOException {
        return new File(path).getCanonicalPath();
    }

    @Override
    public FileData fetch(final String file) throws IOException {
        return new FileData(IOUtils.toByteArray(new FileInputStream(file)));
    }
}
