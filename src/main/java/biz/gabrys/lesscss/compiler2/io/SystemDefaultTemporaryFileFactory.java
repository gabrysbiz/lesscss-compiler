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
package biz.gabrys.lesscss.compiler2.io;

import java.io.File;
import java.io.IOException;

/**
 * Responsible for creating temporary files in a system default temporary directory.
 * @since 2.0.0
 */
public class SystemDefaultTemporaryFileFactory implements TemporaryFileFactory {

    /**
     * The prefix string used in generating the temporary files names.
     * @since 2.0.0
     */
    public static final String FILENAME_PREFIX = "gabrys-lesscss-compiler-";
    /**
     * The suffix string used in generating the temporary files names.
     * @since 2.0.0
     */
    public static final String FILENAME_SUFFIX = ".less";

    @Override
    public File create() throws IOException {
        return File.createTempFile(FILENAME_PREFIX, FILENAME_SUFFIX);
    }
}
