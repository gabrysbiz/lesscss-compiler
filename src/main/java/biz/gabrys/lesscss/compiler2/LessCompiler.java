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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import biz.gabrys.lesscss.compiler2.io.FileUtils;
import biz.gabrys.lesscss.compiler2.io.SystemDefaultTemporaryFileFactory;
import biz.gabrys.lesscss.compiler2.io.TemporaryFileFactory;
import biz.gabrys.lesscss.compiler2.util.StringUtils;

/**
 * <p>
 * <a href="http://lesscss.org/">Less</a> compiler compatible with
 * <a href="https://github.com/less/less.js/releases/tag/v1.7.5">Less 1.7.5</a>.
 * </p>
 * <p>
 * This class is a facade for the {@link NativeLessCompiler}. List of methods with a developer-friendly API:
 * </p>
 * <ul>
 * <li>{@link #compile(CharSequence)} - compiles a Less source code to a CSS code</li>
 * <li>{@link #compile(CharSequence, LessOptions)} - compiles a Less source code with custom configuration options to a
 * CSS code</li>
 * <li>{@link #compile(File)} - compiles a Less source file to a CSS code</li>
 * <li>{@link #compile(File, Charset)} - compiles a Less source file to a CSS code using custom encoding</li>
 * <li>{@link #compile(File, LessOptions)} - compiles a Less source file with custom configuration options to a CSS
 * code</li>
 * <li>{@link #compile(File, File)} - compiles a Less source file to a CSS code and saves it in an output file</li>
 * <li>{@link #compile(File, File, Charset)} - compiles a Less source file to a CSS code and saves it in an output file
 * using custom encoding</li>
 * <li>{@link #compile(File, File, LessOptions)} - compiles a Less source file with custom configuration options and
 * saves result in an output file</li>
 * <li>{@link #compileAndCompress(CharSequence)} - compiles a Less source code to a compressed CSS code</li>
 * <li>{@link #compileAndCompress(File)} - compiles a Less source file to a compressed CSS code</li>
 * <li>{@link #compileAndCompress(File, Charset)} - compiles a Less source file to a compressed CSS code using custom
 * encoding</li>
 * <li>{@link #compileAndCompress(File, File)} - compiles a Less source file to a compressed CSS code and saves it in an
 * output file</li>
 * <li>{@link #compileAndCompress(File, File, Charset)} - compiles a Less source file to a compressed CSS code and saves
 * it in an output file using custom encoding</li>
 * <li>{@link #compileWithInlineSourceMap(CharSequence, LessOptions)} - compiles a Less source code with custom
 * configuration options to a CSS code with an inline Source Map</li>
 * <li>{@link #compileWithInlineSourceMap(File, File, LessOptions)} - compiles a Less source file with custom
 * configuration to a CSS code with an inline Source Map and saves it in an output file</li>
 * <li>{@link #compileWithInlineSourceMap(File, LessOptions)} - compiles a Less source file with custom configuration
 * options to a CSS code with an inline Source Map</li>
 * <li>{@link #compileWithSourceMap(File, File, LessOptions)} - compiles a Less source file with custom configuration to
 * a CSS code with a Source Map (Source Map file name is equal to the output file name with {@code map} extension)</li>
 * <li>{@link #compileWithSourceMap(File, File, File, LessOptions)} - compiles a Less source file with custom
 * configuration to a CSS code with a Source Map</li>
 * </ul>
 * <p>
 * Example code:
 * </p>
 * 
 * <pre>
 * String cssCode = null;
 * {@link LessOptions} options = null;
 * 
 * // create compiler
 * LessCompiler compiler = new {@link #LessCompiler() LessCompiler}();
 * 
 * // compile source code
 * cssCode = compiler.{@link #compile(CharSequence) compile}(".basic { display: block; }");
 * 
 * // compile source code with custom options
 * options = new {@link LessOptionsBuilder#LessOptionsBuilder() LessOptionsBuilder}().{@link LessOptionsBuilder#ieCompatibilityOff() ieCompatibilityOff}().{@link LessOptionsBuilder#build() build}();
 * cssCode = compiler.{@link #compile(CharSequence, LessOptions) compile}(".basic { display: block; }", options);
 * 
 * // compile source file
 * cssCode = compiler.{@link #compile(File) compile}(new File("source.less"));
 * 
 * // compile source file and save CSS code in an output file
 * compiler.{@link #compile(File, File) compile}(new File("source.less"), new File("output.css"));
 * 
 * // compile source file and compress CSS code
 * cssCode = compiler.{@link #compileAndCompress(File) compileAndCompress}(new File("source.less"));
 * 
 * // compile source code and generate inline source map
 * cssCode = compiler.{@link #compileWithInlineSourceMap(CharSequence, LessOptions) compileWithInlineSourceMap}(".basic { display: block; }", new {@link LessOptions#LessOptions() LessOptions}());
 * 
 * // compile source file and generate source map (save it in output.map file)
 * options = new {@link LessOptionsBuilder#LessOptionsBuilder() LessOptionsBuilder}().{@link LessOptionsBuilder#sourceMapBasePath(CharSequence) sourceMapBasePath}("basePath").{@link LessOptionsBuilder#build() build}();
 * compiler.{@link #compileWithSourceMap(File, File, File, LessOptions) compileWithSourceMap}(new File("source.less"), new File("output.css"), new File("output.map"), options);
 * 
 * // compile source file and generate source map (save it in output.css.map file)
 * compiler.{@link #compileWithSourceMap(File, File, LessOptions) compileWithSourceMap}(new File("source.less"), new File("output.css"), options);
 * </pre>
 * 
 * @since 2.0.0
 * @see LessOptionsBuilder
 */
public class LessCompiler {

    private final NativeLessCompiler compiler;
    private final TemporaryFileFactory fileFactory;

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public LessCompiler() {
        this(new NativeLessCompiler());
    }

    /**
     * Constructs a new instance.
     * @param compiler the native Less compiler used to compile source files (cannot be {@code null}).
     * @throws IllegalArgumentException if the native compiler is {@code null}.
     * @since 2.0.0
     */
    public LessCompiler(final NativeLessCompiler compiler) {
        this(compiler, new SystemDefaultTemporaryFileFactory());
    }

    /**
     * Constructs a new instance.
     * @param fileFactory the file factory used to create source files when Less code is passed by string parameter
     *            (cannot be {@code null}).
     * @throws IllegalArgumentException if the file factory is {@code null}.
     * @since 2.0.0
     */
    public LessCompiler(final TemporaryFileFactory fileFactory) {
        this(new NativeLessCompiler(), fileFactory);
    }

    /**
     * Constructs a new instance.
     * @param compiler the native Less compiler used to compile source files (cannot be {@code null}).
     * @param fileFactory the file factory used to create source files when Less code is passed by string parameter
     *            (cannot be {@code null}).
     * @throws IllegalArgumentException if the native compiler or file factory is {@code null}.
     * @since 2.0.0
     */
    public LessCompiler(final NativeLessCompiler compiler, final TemporaryFileFactory fileFactory) {
        if (compiler == null) {
            throw new IllegalArgumentException("Native Less compiler cannot be null");
        }
        if (fileFactory == null) {
            throw new IllegalArgumentException("Temporary file factory cannot be null");
        }
        this.compiler = compiler;
        this.fileFactory = fileFactory;
    }

    /**
     * Compiles a Less source code to a CSS code.
     * @param source the Less code (cannot be {@code null}).
     * @return the CSS code.
     * @throws IllegalArgumentException if the Less code is {@code null}.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if any &#64;import operation point to non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compile(final CharSequence source) {
        validateSourceCode(source);
        final String encoding = getDefaultPlatformEncoding();
        final File sourceFile = createTemporaryFileWithCode(source, encoding);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(sourceFile.getAbsolutePath()).encoding(encoding);
        final String css = compiler.execute(builder.build());
        deleteFile(sourceFile);
        return css;
    }

    /**
     * Compiles a Less source code with custom configuration options to a CSS code.
     * @param source the Less code (cannot be {@code null}).
     * @param options the configuration options (can be {@code null}).
     * @return the CSS code.
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if any &#64;import operation point to non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compile(final CharSequence source, final LessOptions options) {
        validateSourceCode(source);
        validateOptions(options);
        final String encoding = StringUtils.defaultString(options.getEncoding(), getDefaultPlatformEncoding());
        final File sourceFile = createTemporaryFileWithCode(source, encoding);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(sourceFile.getAbsolutePath()).options(options).encoding(encoding);
        final String css = compiler.execute(builder.build());
        deleteFile(sourceFile);
        return css;
    }

    /**
     * Compiles a Less source file to a CSS code.
     * @param source the source file (cannot be {@code null} and must exist).
     * @return the CSS code.
     * @throws IllegalArgumentException if the source file is {@code null} or does not exist.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compile(final File source) {
        validateSourceFile(source);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath());
        return compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file to a CSS code using custom encoding.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param encoding the encoding used to read the source file (cannot be {@code null}).
     * @return the CSS code.
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compile(final File source, final Charset encoding) {
        validateSourceFile(source);
        validateEncoding(encoding);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).encoding(encoding.name());
        return compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file to a CSS code and saves it in an output file.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param output the output file (cannot be {@code null}).
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public void compile(final File source, final File output) {
        validateSourceFile(source);
        validateOutputFile(output);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).outputFile(output);
        compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file to a CSS code and saves it in an output file using custom encoding.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param output the output file (cannot be {@code null}).
     * @param encoding the encoding used to read the source file and write the output file (cannot be {@code null}).
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public void compile(final File source, final File output, final Charset encoding) {
        validateSourceFile(source);
        validateOutputFile(output);
        validateEncoding(encoding);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).outputFile(output).encoding(encoding.name());
        compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file with custom configuration options to a CSS code.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param options the configuration options (can be {@code null}).
     * @return the CSS code.
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compile(final File source, final LessOptions options) {
        validateSourceFile(source);
        validateOptions(options);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).options(options);
        return compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file with custom configuration options and saves result in an output file.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param output the output file (cannot be {@code null}).
     * @param options the configuration options (can be {@code null}).
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public void compile(final File source, final File output, final LessOptions options) {
        validateSourceFile(source);
        validateOutputFile(output);
        validateOptions(options);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).outputFile(output).options(options);
        compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source code to a compressed CSS code.
     * @param source the Less code (cannot be {@code null}).
     * @return the compressed CSS code.
     * @throws IllegalArgumentException if the Less code is {@code null}.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if any &#64;import operation point to non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compileAndCompress(final CharSequence source) {
        validateSourceCode(source);
        final String encoding = getDefaultPlatformEncoding();
        final File sourceFile = createTemporaryFileWithCode(source, encoding);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(sourceFile.getAbsolutePath()).compress(true).encoding(encoding);
        final String css = compiler.execute(builder.build());
        deleteFile(sourceFile);
        return css;
    }

    /**
     * Compiles a Less source file to a compressed CSS code.
     * @param source the source code (cannot be {@code null}).
     * @return the compressed CSS code.
     * @throws IllegalArgumentException if the source file is {@code null} or does not exist.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compileAndCompress(final File source) {
        validateSourceFile(source);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).compress(true);
        return compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file to a compressed CSS code using custom encoding.
     * @param source the source code (cannot be {@code null}).
     * @param encoding the encoding used to read the source file (cannot be {@code null}).
     * @return the compressed CSS code.
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compileAndCompress(final File source, final Charset encoding) {
        validateSourceFile(source);
        validateEncoding(encoding);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).encoding(encoding.name()).compress(true);
        return compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file to a compressed CSS code and saves it in an output file.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param output the output file (cannot be {@code null}).
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public void compileAndCompress(final File source, final File output) {
        validateSourceFile(source);
        validateOutputFile(output);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).outputFile(output).compress(true);
        compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file to a compressed CSS code and saves it in an output file using custom encoding.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param output the output file (cannot be {@code null}).
     * @param encoding the encoding used to read the source file and write the output file (cannot be {@code null}).
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public void compileAndCompress(final File source, final File output, final Charset encoding) {
        validateSourceFile(source);
        validateOutputFile(output);
        validateEncoding(encoding);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).outputFile(output).encoding(encoding.name()).compress(true);
        compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source code with custom configuration options to a CSS code with an inline Source Map.
     * @param source the Less code (cannot be {@code null}).
     * @param options the configuration options (can be {@code null}).
     * @return the CSS code with inline Source Map.
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if any &#64;import operation point to non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compileWithInlineSourceMap(final CharSequence source, final LessOptions options) {
        validateSourceCode(source);
        validateOptions(options);
        final String encoding = StringUtils.defaultString(options.getEncoding(), getDefaultPlatformEncoding());
        final File sourceFile = createTemporaryFileWithCode(source, encoding);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(sourceFile.getAbsolutePath()).sourceMapInline(true).options(options).encoding(encoding);
        final String css = compiler.execute(builder.build());
        deleteFile(sourceFile);
        return css;
    }

    /**
     * Compiles a Less source file with custom configuration options to a CSS code with an inline Source Map.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param options the configuration options (can be {@code null}).
     * @return the CSS code with inline Source Map.
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public String compileWithInlineSourceMap(final File source, final LessOptions options) {
        validateSourceFile(source);
        validateOptions(options);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).sourceMapInline(true).options(options);
        return compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file with custom configuration to a CSS code with an inline Source Map and saves it in an
     * output file.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param output the output file (cannot be {@code null}).
     * @param options the configuration options (can be {@code null}).
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public void compileWithInlineSourceMap(final File source, final File output, final LessOptions options) {
        validateSourceFile(source);
        validateOutputFile(output);
        validateOptions(options);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).outputFile(output).sourceMapInline(true).options(options);
        compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file with custom configuration to a CSS code with a Source Map. The CSS code will be saved
     * in an output file and the Source Map in a file with name equal to the output file name with {@code map}
     * extension.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param output the output file (cannot be {@code null}).
     * @param options the configuration options (can be {@code null}).
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public void compileWithSourceMap(final File source, final File output, final LessOptions options) {
        validateSourceFile(source);
        validateOutputFile(output);
        validateOptions(options);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).outputFile(output).sourceMapDefault(true).options(options);
        compiler.execute(builder.build());
    }

    /**
     * Compiles a Less source file with custom configuration to a CSS code with a Source Map. The CSS code will be saved
     * in an output file and the Source Map in an output Source Map file.
     * @param source the source file (cannot be {@code null} and must exist).
     * @param output the output file (cannot be {@code null}).
     * @param outputSourceMap the output file for Source Map (cannot be {@code null}).
     * @param options the configuration options (can be {@code null}).
     * @throws IllegalArgumentException if any parameter is invalid.
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     */
    public void compileWithSourceMap(final File source, final File output, final File outputSourceMap, final LessOptions options) {
        validateSourceFile(source);
        validateOutputFile(output);
        validateOutputSourceMapFile(outputSourceMap);
        validateOptions(options);
        final NativeLessOptionsBuilder builder = createOptionsBuilder();
        builder.inputFile(source.getAbsolutePath()).outputFile(output).sourceMapFile(outputSourceMap).options(options);
        compiler.execute(builder.build());
    }

    void validateSourceCode(final CharSequence code) {
        if (code == null) {
            throw new IllegalArgumentException("Source code cannot be null");
        }
    }

    void validateSourceFile(final File file) {
        if (file == null) {
            throw new IllegalArgumentException("Source file cannot be null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("Source file does not exist: " + file.getAbsolutePath());
        }
    }

    void validateOutputFile(final File output) {
        if (output == null) {
            throw new IllegalArgumentException("Output file cannot be null");
        }
    }

    void validateOutputSourceMapFile(final File outputSourceMap) {
        if (outputSourceMap == null) {
            throw new IllegalArgumentException("Output Source Map file cannot be null");
        }
    }

    void validateOptions(final LessOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("Options cannot be null");
        }
    }

    void validateEncoding(final Charset encoding) {
        if (encoding == null) {
            throw new IllegalArgumentException("Encoding cannot be null");
        }
    }

    NativeLessOptionsBuilder createOptionsBuilder() {
        return new NativeLessOptionsBuilder();
    }

    String getDefaultPlatformEncoding() {
        return Charset.defaultCharset().toString();
    }

    File createTemporaryFileWithCode(final CharSequence code, final String encoding) {
        File file = null;
        try {
            file = fileFactory.create();
        } catch (final IOException e) {
            throw new CompilerException("Cannot create temporary file", e);
        }
        try {
            FileUtils.write(file, code, encoding);
        } catch (final IOException e) {
            throw new CompilerException("Cannot save source code in temporaty file: " + file.getAbsolutePath(), e);
        }
        return file;
    }

    void deleteFile(final File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.delete()) {
            throw new CompilerException("Cannot delete temporary file: " + file.getAbsolutePath());
        }
    }
}
