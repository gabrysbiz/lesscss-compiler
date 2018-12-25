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
 * Responsible for creating temporary files.
 * @since 2.0.0
 */
public interface TemporaryFileFactory {

    /**
     * Creates a temporary file.
     * @return the temporary file.
     * @throws IOException if the temporary file could not be created.
     * @since 2.0.0
     */
    File create() throws IOException;
}
