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

/**
 * Responsible for checking operating system.
 * @since 1.0
 */
public interface OperatingSystemChecker {

    /**
     * Tests whether operating system is <a href="http://www.microsoft.com/">Windows</a>.
     * @return {@code true} whether operating system is Windows, otherwise {@code false}.
     * @since 1.0
     */
    boolean isWindows();
}
