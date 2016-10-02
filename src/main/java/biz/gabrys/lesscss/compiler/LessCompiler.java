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

import java.io.File;

/**
 * Responsible for compiling <a href="http://lesscss.org/">Less</a> sources to the
 * <a href="http://www.w3.org/Style/CSS/">CSS</a> code.
 * @since 1.0
 */
public interface LessCompiler {

    /**
     * Compiles a <a href="http://lesscss.org/">Less</a> source file to the
     * <a href="http://www.w3.org/Style/CSS/">CSS</a> code with default options.
     * @param source the source file.
     * @return the <a href="http://www.w3.org/Style/CSS/">CSS</a> code.
     * @throws CompilerException if an error occurred during compilation process.
     * @since 1.0
     * @see CompilerOptions
     */
    String compile(File source) throws CompilerException;

    /**
     * Compiles a <a href="http://lesscss.org/">Less</a> source file to the
     * <a href="http://www.w3.org/Style/CSS/">CSS</a> code.
     * @param source the source file.
     * @param options the compiler options.
     * @return the <a href="http://www.w3.org/Style/CSS/">CSS</a> code.
     * @throws CompilerException if an error occurred during compilation process.
     * @since 1.0
     */
    String compile(File source, CompilerOptions options) throws CompilerException;
}
