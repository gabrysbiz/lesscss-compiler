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
 * Default implementation of the {@link OperatingSystemChecker} based on the system properties.
 * @since 1.0
 * @see System#getProperty(String)
 */
public class OperatingSystemCheckerImpl implements OperatingSystemChecker {

    private final boolean windowsOs;

    /**
     * Constructs a new instance.
     * @since 1.0
     */
    public OperatingSystemCheckerImpl() {
        windowsOs = System.getProperty("os.name").startsWith("Windows");
    }

    public boolean isWindows() {
        return windowsOs;
    }
}
