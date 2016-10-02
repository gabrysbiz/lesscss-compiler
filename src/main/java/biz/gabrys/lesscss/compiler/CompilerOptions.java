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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Options that control the <a href="http://lesscss.org/">Less</a> compilation process.
 * @since 1.0
 * @see <a href="http://lesscss.org/usage/index.html#command-line-usage-options">Less options</a>
 * @see LessCompiler
 */
public class CompilerOptions {

    private final List<Object> arguments;

    /**
     * Constructs a new instance with zero arguments.
     * @since 1.2
     */
    public CompilerOptions() {
        this(Collections.emptyList());
    }

    /**
     * Constructs a new instance.
     * @param arguments list with arguments.
     * @since 1.0
     */
    public CompilerOptions(final List<Object> arguments) {
        this.arguments = new ArrayList<Object>(arguments);
    }

    /**
     * Returns arguments for the <a href="http://lesscss.org/">Less</a> compiler. Returned list is a copy - changes in
     * the structure do not change the internal state.
     * @return arguments for the <a href="http://lesscss.org/">Less</a> compiler.
     * @since 1.0
     */
    public List<Object> getArguments() {
        return new ArrayList<Object>(arguments);
    }
}
