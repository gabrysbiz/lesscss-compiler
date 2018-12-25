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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import biz.gabrys.lesscss.compiler2.util.ListWithoutEmptyValuesBuilder;
import biz.gabrys.lesscss.compiler2.util.StringUtils;

/**
 * <p>
 * Responsible for creating <a href="http://lesscss.org/usage/index.html#less-options">options</a> that control the
 * {@link NativeLessCompiler} compilation process.
 * </p>
 * <p>
 * Base options:
 * </p>
 * <ul>
 * <li>{@link #compress(boolean) compression} - whether a CSS code should be compressed (default: {@code false})</li>
 * <li>{@link #ieCompatibility(boolean) IE compatibility} - whether a CSS code should be compatible with Internet
 * Explorer browser (default: {@code true})</li>
 * <li>{@link #includePaths(List) included paths} - available include paths (default: {@code []})</li>
 * <li>{@link #inputFile(String) path} - an input (source) file path</li>
 * <li>{@link #javaScript(boolean) JavaScript} - whether a compiler should allow usage of JavaScript language (default:
 * {@code true})</li>
 * <li>{@link #lineNumbers(LineNumbersValue) line numbers} - whether a compiler should generate inline source-mapping
 * (default: {@link LineNumbersValue#OFF})</li>
 * <li>{@link #outputFile(File) output file} - an output (destination) file</li>
 * <li>{@link #relativeUrls(boolean) relative URLs} - whether a compiler should rewrite relative URLs (default:
 * {@code false})</li>
 * <li>{@link #rootPath(String) root path} - a path which will be added to every generated import and URL in CSS code
 * (default: {@code null})</li>
 * <li>{@link #silent(boolean) silent} - whether a compiler shouldn't log compilation warnings (default:
 * {@code false})</li>
 * <li>{@link #strictImports(boolean) strict imports} - whether a compiler should disallow an @import operation inside
 * of either @media blocks or other selector blocks (default: {@code false})</li>
 * <li>{@link #strictMath(boolean) strict math} - whether a compiler should try and process all maths in Less code
 * (default: {@code false})</li>
 * <li>{@link #strictUnits(boolean) strict units} - whether a compiler should guess units in Less code when it does
 * maths (default: {@code false})</li>
 * </ul>
 * <p>
 * Source Map options:
 * </p>
 * <ul>
 * <li>You can set where Source Map will be saved by using one of the options (mutually exclusive):
 * <ul>
 * <li>{@link #sourceMapDefault(boolean) default} - generate a Source Map file with a default name equal to
 * {@link #outputFile(File) output file name} with {@code map} extension (default: {@code false})</li>
 * <li>{@link #sourceMapFile(File) file} - generate a Source Map file (default: {@code null}).</li>
 * <li>{@link #sourceMapInline(boolean) inline} - generate a Source Map and put it to CSS code (default: {@code false})
 * </ul>
 * </li>
 * <li>{@link #sourceMapBasePath(String) base path} - a path that will be removed from each of the Less file paths
 * inside the Source Map and also from the path to the map file specified in your output CSS (default:
 * {@code null})</li>
 * <li>{@link #sourceMapLessInline(boolean) Less inline} - whether a compiler should include all of the Less files in to
 * the Source Map (default: {@code false})</li>
 * <li>{@link #sourceMapRootPath(String) root path} - a path that will be prepended to each of the Less file paths
 * inside the Source Map and also to the path to the map file specified in your output CSS (default: {@code null})</li>
 * <li>{@link #sourceMapUrl(String) URL} - a path which will overwrite the URL in the CSS that points at the Source Map
 * file (default: {@code null})</li>
 * </ul>
 * <p>
 * Additional options:
 * </p>
 * <ul>
 * <li>{@link #banner(String) banner} - a banner which will be inserted to a source file before the compilation</li>
 * <li>{@link #globalVariables(List) globalVariables} - variables that can be referenced by the files</li>
 * <li>{@link #modifyVariables(List) modifyVariables} - variables that can overwrite variables defined in the files</li>
 * </ul>
 * <p>
 * Non-standard options:
 * </p>
 * <ul>
 * <li>{@link #encoding(String) encoding} - an encoding used to read source files and save generated code (default:
 * {@code null} - means platform default encoding)</li>
 * <li>{@link #fileSystems(List) file systems} - a list with file systems (default:
 * {@link LessOptions#DEFAULT_FILE_SYSTEMS})</li>
 * </ul>
 * <p>
 * Example code:
 * </p>
 * 
 * <pre>
 * Collection&lt;String&gt; options = null;
 * 
 * // create options with an input file
 * options = new {@link #NativeLessOptionsBuilder() NativeLessOptionsBuilder}().{@link #inputFile(String) inputFile}("/less/file.less").{@link #build() build}();
 * 
 * // create options with an input file and compression enabled
 * options = new {@link #NativeLessOptionsBuilder() NativeLessOptionsBuilder}().{@link #inputFile(String) inputFile}("/less/file.less").{@link #compress(boolean) compress}(true).{@link #build() build}();
 * 
 * // use {@link LessOptions} object to create options 
 * {@link LessOptions} lessOptions = new {@link LessOptions#LessOptions() LessOptions}();
 * ... // modify options
 * options = new {@link #NativeLessOptionsBuilder() NativeLessOptionsBuilder}().{@link #options(LessOptions) options}(lessOptions).{@link #build() build}();
 * </pre>
 * 
 * @since 2.0.0
 * @see LessOptions
 */
public class NativeLessOptionsBuilder {

    private LessOptions options = new LessOptions();
    private String input;
    private File output;

    private boolean sourceMapDefault;
    private File sourceMapFile;
    private boolean sourceMapInline;

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder() {
        // do nothing
    }

    /**
     * Sets compiler options. This method overwrites values set by:
     * <ul>
     * <li>{@link #banner(String)}</li>
     * <li>{@link #compress(boolean)}</li>
     * <li>{@link #encoding(String)}</li>
     * <li>{@link #fileSystems(List)}</li>
     * <li>{@link #globalVariables(List)}</li>
     * <li>{@link #ieCompatibility(boolean)}</li>
     * <li>{@link #includePaths(List)}</li>
     * <li>{@link #javaScript(boolean)}</li>
     * <li>{@link #lineNumbers(LineNumbersValue)}</li>
     * <li>{@link #modifyVariables(List)}</li>
     * <li>{@link #relativeUrls(boolean)}</li>
     * <li>{@link #rootPath(String)}</li>
     * <li>{@link #silent(boolean)}</li>
     * <li>{@link #sourceMapBasePath(String)}</li>
     * <li>{@link #sourceMapLessInline(boolean)}</li>
     * <li>{@link #sourceMapRootPath(String)}</li>
     * <li>{@link #sourceMapUrl(String)}</li>
     * <li>{@link #strictImports(boolean)}</li>
     * <li>{@link #strictMath(boolean)}</li>
     * <li>{@link #strictUnits(boolean)}</li>
     * </ul>
     * @param options the options object.
     * @return {@code this} builder.
     * @throws IllegalArgumentException if the options object is {@code null}.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder options(final LessOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("Options cannot be null");
        }
        this.options = new LessOptions(options);
        return this;
    }

    /**
     * Sets an input (source) file path.
     * @param input the input file path.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder inputFile(final String input) {
        this.input = input;
        return this;
    }

    /**
     * Returns a command line option which represents an absolute path of the input file.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #inputFile(String)
     */
    protected String getInputFileOption() {
        if (input != null) {
            return input;
        } else {
            return "";
        }
    }

    /**
     * Sets an output (destination) file. This method must be used together with {@link #inputFile(String)}.
     * @param output the output file.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #inputFile(String)
     */
    public NativeLessOptionsBuilder outputFile(final File output) {
        this.output = output;
        return this;
    }

    /**
     * Returns a command line option which represents an absolute path of the output file.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #outputFile(File)
     */
    protected String getOutputFileOption() {
        if (output != null) {
            return output.getAbsolutePath();
        } else {
            return "";
        }
    }

    /**
     * Sets whether a compiler shouldn't log compilation warnings (default: {@code false}).
     * @param silent {@code true} whether the compiler shouldn't log compilation warnings, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder silent(final boolean silent) {
        options.setSilent(silent);
        return this;
    }

    /**
     * Returns a command line option which enables/disables logging compilation warnings.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #silent(boolean)
     */
    protected String getSilentOption() {
        if (options.isSilent()) {
            return "--silent";
        } else {
            return "";
        }
    }

    /**
     * Sets whether a compiler should disallow an &#64;import operation inside of either &#64;media blocks or other
     * selector blocks (default: {@code false}).
     * @param strictImports {@code true} whether the compiler should disallow the &#64;import operation inside of either
     *            &#64;media blocks or other selector blocks, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder strictImports(final boolean strictImports) {
        options.setStrictImports(strictImports);
        return this;
    }

    /**
     * Returns a command line option which enables/disables strict imports.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #strictImports(boolean)
     */
    protected String getStrictImportsOption() {
        if (options.isStrictImports()) {
            return "--strict-imports";
        } else {
            return "";
        }
    }

    /**
     * Sets whether a CSS code should be compressed (default: {@code false}).
     * @param compress {@code true} whether the CSS code should be compressed, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder compress(final boolean compress) {
        options.setCompress(compress);
        return this;
    }

    /**
     * Returns a command line option which enables/disables a CSS code compression.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #compress(boolean)
     */
    protected String getCompressOption() {
        if (options.isCompress()) {
            return "--compress";
        } else {
            return "";
        }
    }

    /**
     * Sets whether a CSS code should be compatible with Internet Explorer browser (default: {@code true}). Used for the
     * {@code data-uri} function to ensure that images aren't created that are too large for the browser to handle.
     * @param ieCompatibility {@code true} whether the CSS code should be compatible with Internet Explorer browser,
     *            otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder ieCompatibility(final boolean ieCompatibility) {
        options.setIeCompatibility(ieCompatibility);
        return this;
    }

    /**
     * Returns a command line option which enables/disables compatibility with IE browser.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #ieCompatibility(boolean)
     */
    protected String getIeCompatibilityOption() {
        if (options.isIeCompatibility()) {
            return "";
        } else {
            return "--no-ie-compat";
        }
    }

    /**
     * Sets whether a compiler should allow usage of JavaScript language (default: {@code true}).
     * @param javaScript {@code true} whether the compiler should allow usage of JavaScript language, otherwise
     *            {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder javaScript(final boolean javaScript) {
        options.setJavaScript(javaScript);
        return this;
    }

    /**
     * Returns a command line option which allows/disallows usage of JavaScript language.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #javaScript(boolean)
     */
    protected String getJavaScriptOption() {
        if (options.isJavaScript()) {
            return "";
        } else {
            return "--no-js";
        }
    }

    /**
     * Sets available include paths (default: {@code []}). If the file in an &#64;import rule does not exist at that
     * exact location, a compiler will look for it at the location(s) passed to this option. You might use this for
     * instance to specify a path to a library which you want to be referenced simply and relatively in the less files.
     * @param includePaths the available include paths.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder includePaths(final List<String> includePaths) {
        options.setIncludePaths(includePaths);
        return this;
    }

    /**
     * Returns command line options representation of the available include paths.
     * @return the command line options (never {@code null}).
     * @since 2.0.0
     * @see #includePaths(List)
     */
    protected String[] getIncludePathsOptions() {
        final List<String> includePaths = options.getIncludePaths();
        final List<String> commandLineOptions = new ArrayList<>(includePaths.size());
        for (final String includePath : includePaths) {
            if (StringUtils.isNotBlank(includePath)) {
                commandLineOptions.add("--include-path=" + includePath);
            }
        }
        return commandLineOptions.toArray(new String[0]);
    }

    /**
     * Sets whether a compiler should generate inline source-mapping (default: {@link LineNumbersValue#OFF}). This was
     * the only option before browsers started supporting Source Maps.
     * @param lineNumbers the line numbers value ({@code null} is treated as {@link LineNumbersValue#OFF}).
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder lineNumbers(final LineNumbersValue lineNumbers) {
        options.setLineNumbers(lineNumbers);
        return this;
    }

    /**
     * Returns a command line option which enables/disables generating inline source-mapping.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #javaScript(boolean)
     */
    protected String getLineNumbersOption() {
        final LineNumbersValue lineNumbers = options.getLineNumbers();
        if (lineNumbers == LineNumbersValue.OFF) {
            return "";
        } else {
            return "--line-numbers=" + lineNumbers.getValue();
        }
    }

    /**
     * Sets whether a compiler should generate a Source Map file with a default name equal to {@link #outputFile(File)
     * output file name} with {@code map} extension (default: {@code false}). It does not matter whether you call this
     * method before or after the {@link #outputFile(File)} method.
     * <p>
     * This method clears bulider's data set by the {@link #sourceMapFile(File)} and the
     * {@link #sourceMapInline(boolean)} methods.
     * </p>
     * @param enabled {@code true} whether the compiler should generate the Source Map file, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #outputFile(File)
     * @see #sourceMapFile(File)
     * @see #sourceMapInline(boolean)
     */
    public NativeLessOptionsBuilder sourceMapDefault(final boolean enabled) {
        sourceMapDefault = enabled;
        sourceMapFile = null;
        sourceMapInline = false;
        return this;
    }

    /**
     * Returns a command line option which enables/disables generating a Source Map file with default name.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #sourceMapDefault(boolean)
     */
    protected String getSourceMapDefaultOption() {
        if (sourceMapDefault) {
            return "--source-map";
        } else {
            return "";
        }
    }

    /**
     * Sets whether a compiler should generate a Source Map file (default: {@code null}).
     * <p>
     * This method clears bulider's data set by the {@link #sourceMapDefault(boolean)} and the
     * {@link #sourceMapInline(boolean)} methods.
     * </p>
     * @param sourceMap the Source Map file.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapDefault(boolean)
     * @see #sourceMapInline(boolean)
     */
    public NativeLessOptionsBuilder sourceMapFile(final File sourceMap) {
        sourceMapDefault = false;
        sourceMapFile = sourceMap;
        sourceMapInline = false;
        return this;
    }

    /**
     * Returns a command line option which enables/disables generating a Source Map file.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #sourceMapFile(File)
     */
    protected String getSourceMapFileOption() {
        if (sourceMapFile != null) {
            return "--source-map=" + sourceMapFile.getAbsolutePath();
        } else {
            return "";
        }
    }

    /**
     * Sets whether a compiler should generate a Source Map and put it to CSS code (default: {@code false}). This is not
     * recommended for production, but for development it allows the compiler to produce a single output file which in
     * browsers that support it, use the compiled CSS but show you the non-compiled Less source.
     * <p>
     * This method clears bulider's data set by the {@link #sourceMapDefault(boolean)} and the
     * {@link #sourceMapFile(File)} methods.
     * </p>
     * @param inline {@code true} whether the compiler should generate a Source Map, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapDefault(boolean)
     * @see #sourceMapFile(File)
     * @see #sourceMapLessInline(boolean)
     */
    public NativeLessOptionsBuilder sourceMapInline(final boolean inline) {
        sourceMapDefault = false;
        sourceMapFile = null;
        sourceMapInline = inline;
        return this;
    }

    /**
     * Returns command line options which enable/disable generating a Source Map which will be put to CSS code.
     * @return the command line options (never {@code null}).
     * @since 2.0.0
     * @see #sourceMapInline(boolean)
     */
    protected String[] getSourceMapInlineOptions() {
        if (sourceMapInline) {
            return new String[] { "--source-map", "--source-map-map-inline" };
        } else {
            return new String[0];
        }
    }

    /**
     * Sets whether a compiler should include all of the Less files in to the Source Map (default: {@code false}). This
     * means that you only need your map file to get to your original source.
     * <p>
     * This can be used in conjunction with the {@link #sourceMapInline(boolean) map inline} option so that you do not
     * need to have any additional external files at all.
     * </p>
     * @param inline {@code true} whether the compiler should include all of the Less files in to the Source Map,
     *            otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapInline(boolean)
     */
    public NativeLessOptionsBuilder sourceMapLessInline(final boolean inline) {
        options.setSourceMapLessInline(inline);
        return this;
    }

    /**
     * Returns a command line options which enables/disables generating a Source Map which will be put to CSS code.
     * @return the command line options (never {@code null}).
     * @since 2.0.0
     * @see #sourceMapLessInline(boolean)
     */
    protected String getSourceMapLessInlineOption() {
        if (options.isSourceMapLessInline()) {
            return "--source-map-less-inline";
        } else {
            return "";
        }
    }

    /**
     * Sets a path that will be prepended to each of the Less file paths inside the Source Map and also to the path to
     * the map file specified in your output CSS (default: {@code null}).
     * <p>
     * Use it if for instance you have a CSS file generated in the root on your web server but have your source
     * less/css/map files in a different folder.
     * </p>
     * <p>
     * Example: if you have a follow file structure:
     * </p>
     * 
     * <pre>
     * output.css
     * dev-files/output.css.map
     * dev-files/output.less
     * </pre>
     * <p>
     * then you should set the Source Map root path to {@code dev-files/}.
     * </p>
     * <p>
     * This option is the opposite of the {@link #sourceMapBasePath(String) Source Map base path} option.
     * </p>
     * @param rootPath the path which will be added.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapBasePath(String)
     * @see #sourceMapUrl(String)
     */
    public NativeLessOptionsBuilder sourceMapRootPath(final String rootPath) {
        options.setSourceMapRootPath(rootPath);
        return this;
    }

    /**
     * Returns a command line option representation of the Source Map root path.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #sourceMapRootPath(String)
     */
    protected String getSourceMapRootPathOption() {
        final String rootPath = options.getSourceMapRootPath();
        if (rootPath != null) {
            return "--source-map-rootpath=" + rootPath;
        } else {
            return "";
        }
    }

    /**
     * Sets a path that will be removed from each of the Less file paths inside the Source Map and also from the path to
     * the map file specified in your output CSS (default: {@code null}).
     * <p>
     * It specifies a path which should be removed from the output paths. For instance if you are compiling a file in
     * the less-files directory but the source files will be available on your web server in the root or current
     * directory, you can specify this to remove the additional less-files part of the path.
     * </p>
     * <p>
     * This option is the opposite of the {@link #sourceMapRootPath(String) Source Map root path} option.
     * </p>
     * @param basePath the path which will be removed.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapRootPath(String)
     * @see #sourceMapUrl(String)
     */
    public NativeLessOptionsBuilder sourceMapBasePath(final String basePath) {
        options.setSourceMapBasePath(basePath);
        return this;
    }

    /**
     * Returns a command line option representation of the Source Map base path.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #sourceMapBasePath(String)
     */
    protected String getSourceMapBasePathOption() {
        final String basePath = options.getSourceMapBasePath();
        if (basePath != null) {
            return "--source-map-basepath=" + basePath;
        } else {
            return "";
        }
    }

    /**
     * Sets a path which will overwrite the URL in the CSS that points at the Source Map file (default: {@code null}).
     * This is for cases when the {@link #sourceMapRootPath(String) root path} and the {@link #sourceMapBasePath(String)
     * base path} options are not producing exactly what you need.
     * @param url the Source Map URL.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapBasePath(String)
     * @see #sourceMapRootPath(String)
     */
    public NativeLessOptionsBuilder sourceMapUrl(final String url) {
        options.setSourceMapUrl(url);
        return this;
    }

    /**
     * Returns a command line option representation of the Source Map URL.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #sourceMapUrl(String)
     */
    protected String getSourceMapUrlOption() {
        final String url = options.getSourceMapUrl();
        if (url != null) {
            return "--source-map-url=" + url;
        } else {
            return "";
        }
    }

    /**
     * Sets a path which will be added to every generated import and URL in CSS code (default: {@code null}). This does
     * not affect less import statements that are processed, just ones that are left in the output CSS.
     * <p>
     * For instance, if all the images the CSS use are in a folder called resources, you can use this option to add this
     * on to the URL's and then have the name of that folder configurable.
     * </p>
     * @param rootPath the path which will be added.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder rootPath(final String rootPath) {
        options.setRootPath(rootPath);
        return this;
    }

    /**
     * Returns a command line option representation of the root path.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #rootPath(String)
     */
    protected String getRootPathOption() {
        final String rootPath = options.getRootPath();
        if (rootPath != null) {
            return "--rootpath=" + rootPath;
        } else {
            return "";
        }
    }

    /**
     * Sets whether a compiler should rewrite relative URLs (default: {@code false}). By default URLs are kept as-is, so
     * if you import a file in a sub-directory that references an image, exactly the same URL will be output in the CSS
     * code. This option allows you to re-write URLs in imported files so that the URL is always relative to the base
     * imported file. Example:
     * 
     * <pre>
     * # main.less
     * &#64;import "files/backgrounds.less";
     * 
     * # files/backgrounds.less
     * .icon-1 {
     *     background-image: url('images/lamp-post.png');
     * }
     * </pre>
     * <p>
     * this will output the following normally
     * </p>
     * 
     * <pre>
     * .icon-1 {
     *     background-image: url('images/lamp-post.png');
     * }
     * </pre>
     * <p>
     * but with this option on it will instead output
     * </p>
     * 
     * <pre>
     * .icon-1 {
     *     background-image: url('files/images/lamp-post.png');
     * }
     * </pre>
     * <p>
     * You may also want to consider using the {@code data-uri} function instead of this option, which will embed images
     * into the CSS.
     * </p>
     * @param relativeUrls {@code true} whether the compiler should rewrite relative URLs, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder relativeUrls(final boolean relativeUrls) {
        options.setRelativeUrls(relativeUrls);
        return this;
    }

    /**
     * Returns a command line option which enables/disables rewriting relative URLs.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #relativeUrls(boolean)
     */
    protected String getRelativeUrlsOption() {
        if (options.isRelativeUrls()) {
            return "--relative-urls";
        } else {
            return "";
        }
    }

    /**
     * Sets whether a compiler should try and process all maths in Less code (default: {@code false}). Without this
     * option on the compiler will try and process all maths in your Less e.g.:
     * 
     * <pre>
     * .class {
     *     height: calc(100% - 10px);
     * }
     * </pre>
     * <p>
     * will be processed currently.
     * </p>
     * <p>
     * With strict math on, only maths that is inside unnecessary parenthesis will be processed. Example:
     * </p>
     * 
     * <pre>
     * .class {
     *     width: calc(100% - ((10px  - 5px)));
     *     height: (100px / 4px);
     *     font-size: 1 / 4;
     * }
     * </pre>
     * <p>
     * will be compiled to
     * </p>
     * 
     * <pre>
     * .class {
     *     width: calc(100% - 5px);
     *     height: 25px;
     *     font-size: 1 / 4;
     * }
     * </pre>
     * 
     * @param strictMath {@code true} whether the compiler should try and process all maths in the Less code, otherwise
     *            {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictUnits(boolean)
     */
    public NativeLessOptionsBuilder strictMath(final boolean strictMath) {
        options.setStrictMath(strictMath);
        return this;
    }

    /**
     * Returns a command line option which enables/disables strict math.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #strictMath(boolean)
     */
    protected String getStrictMathOption() {
        if (options.isStrictMath()) {
            return "--strict-math=on";
        } else {
            return "";
        }
    }

    /**
     * Sets whether a compiler should guess units in Less code when it does maths (default: {@code false}). Without this
     * option, the compiler attempts to guess at the output unit when it does maths. For instance:
     * 
     * <pre>
     * .class {
     *     property: 1px * 2px;
     * }
     * </pre>
     * <p>
     * In this case, things are clearly not right - a length multiplied by a length gives an area, but CSS does not
     * support specifying areas. So we assume that the user meant for one of the values to be a value, not a unit of
     * length and Less outputs 2px. With strict units on, Less assumes this is a bug in the calculation and throw an
     * error.
     * </p>
     * @param strictUnits {@code true} whether the compiler should guess units in the Less code when it does maths,
     *            otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictMath(boolean)
     */
    public NativeLessOptionsBuilder strictUnits(final boolean strictUnits) {
        options.setStrictUnits(strictUnits);
        return this;
    }

    /**
     * Returns a command line option which enables/disables strict units.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #strictUnits(boolean)
     */
    protected String getStrictUnitsOption() {
        if (options.isStrictUnits()) {
            return "--strict-units=on";
        } else {
            return "";
        }
    }

    /**
     * Sets an encoding used to read source files and save generated code.
     * @param encoding the encoding.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder encoding(final String encoding) {
        options.setEncoding(encoding);
        return this;
    }

    /**
     * Returns a command line option which defines sources and output files encoding.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #encoding(String)
     */
    protected String getEncodingOption() {
        final String encoding = options.getEncoding();
        if (encoding != null) {
            return "--encoding=" + encoding;
        } else {
            return "";
        }
    }

    /**
     * Sets file systems used to fetch content of the source files.
     * @param fileSystems the file systems ({@code null} is treated as a collection with default values).
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder fileSystems(final List<FileSystemOption> fileSystems) {
        options.setFileSystems(fileSystems);
        return this;
    }

    /**
     * Returns command line options representation of the available file systems.
     * @return the command line options (never {@code null}).
     * @since 2.0.0
     * @see #fileSystems(List)
     */
    protected String[] getFileSystemsOptions() {
        final Collection<FileSystemOption> fileSystems = options.getFileSystems();
        if (fileSystems.isEmpty() || LessOptions.DEFAULT_FILE_SYSTEMS.equals(fileSystems)) {
            return new String[0];
        }
        final List<String> commandLineOptions = new ArrayList<>(fileSystems.size());
        for (final FileSystemOption fileSystem : fileSystems) {
            if (fileSystem != null) {
                final StringBuilder commandLineOption = new StringBuilder();
                commandLineOption.append("--file-system=");
                commandLineOption.append(fileSystem);
                commandLineOptions.add(commandLineOption.toString());
            }
        }
        return commandLineOptions.toArray(new String[0]);
    }

    /**
     * Sets a banner which will be inserted to a source file before the compilation.
     * @param banner the banner.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder banner(final String banner) {
        options.setBanner(banner);
        return this;
    }

    /**
     * Returns a command line option which sets a banner.
     * @return the command line option (never {@code null}).
     * @since 2.0.0
     * @see #banner(String)
     */
    protected String getBannerOption() {
        final String banner = options.getBanner();
        if (StringUtils.isNotBlank(banner)) {
            return "--banner=" + banner;
        } else {
            return "";
        }
    }

    /**
     * Sets global variables that can be referenced by the files (default: {@code []}. Effectively the declarations are
     * put at the top of your base Less file, meaning those variables can be used, but they also can be overridden if
     * variables with the same names are defined in the file.
     * @param globalVariables the global variables ({@code null} is treated as an empty collection).
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder globalVariables(final List<LessVariableOption> globalVariables) {
        options.setGlobalVariables(globalVariables);
        return this;
    }

    /**
     * Returns command line options which defines global variables.
     * @return the command line options (never {@code null}).
     * @since 2.0.0
     * @see #globalVariables(List)
     */
    protected String[] getGlobalVariablesOptions() {
        final List<LessVariableOption> variables = options.getGlobalVariables();
        final List<String> commandLineOptions = new ArrayList<>(variables.size());
        for (final LessVariableOption variable : variables) {
            if (variable != null) {
                final StringBuilder commandLineOption = new StringBuilder();
                commandLineOption.append("--global-var=");
                commandLineOption.append(variable);
                commandLineOptions.add(commandLineOption.toString());
            }
        }
        return commandLineOptions.toArray(new String[0]);
    }

    /**
     * Sets modify variables that can overwrite variables defined in the files (default: {@code []}. Effectively the
     * declarations are put at the bottom of your base Less file, meaning they will override anything defined in your
     * Less file.
     * @param modifyVariables the modify variables ({@code null} is treated as an empty collection).
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public NativeLessOptionsBuilder modifyVariables(final List<LessVariableOption> modifyVariables) {
        options.setModifyVariables(modifyVariables);
        return this;
    }

    /**
     * Returns command line options which defines modify variables.
     * @return the command line options (never {@code null}).
     * @since 2.0.0
     * @see #modifyVariables(List)
     */
    protected String[] getModifyVariablesOptions() {
        final List<LessVariableOption> variables = options.getModifyVariables();
        final List<String> commandLineOptions = new ArrayList<>(variables.size());
        for (final LessVariableOption variable : variables) {
            if (variable != null) {
                final StringBuilder commandLineOption = new StringBuilder();
                commandLineOption.append("--modify-var=");
                commandLineOption.append(variable);
                commandLineOptions.add(commandLineOption.toString());
            }
        }
        return commandLineOptions.toArray(new String[0]);
    }

    /**
     * Builds a collection with configuration options for the {@link NativeLessCompiler} compilation process.
     * @return the collection with configuration options.
     * @throws BuilderCreationException if you set an output file without setting an input file.
     * @since 2.0.0
     */
    public Collection<String> build() {
        final ListWithoutEmptyValuesBuilder<String> configurationOptions = new ListWithoutEmptyValuesBuilder<>(StringUtils::isNotBlank);
        configurationOptions.append(getSilentOption());
        configurationOptions.append(getStrictImportsOption());
        configurationOptions.append(getCompressOption());
        configurationOptions.append(getIeCompatibilityOption());
        configurationOptions.append(getJavaScriptOption());
        configurationOptions.append(getIncludePathsOptions());
        configurationOptions.append(getLineNumbersOption());

        configurationOptions.append(getSourceMapDefaultOption());
        configurationOptions.append(getSourceMapFileOption());
        configurationOptions.append(getSourceMapInlineOptions());
        configurationOptions.append(getSourceMapLessInlineOption());
        configurationOptions.append(getSourceMapRootPathOption());
        configurationOptions.append(getSourceMapBasePathOption());
        configurationOptions.append(getSourceMapUrlOption());

        configurationOptions.append(getRootPathOption());
        configurationOptions.append(getRelativeUrlsOption());
        configurationOptions.append(getStrictMathOption());
        configurationOptions.append(getStrictUnitsOption());

        configurationOptions.append(getBannerOption());
        configurationOptions.append(getGlobalVariablesOptions());
        configurationOptions.append(getModifyVariablesOptions());

        configurationOptions.append(getEncodingOption());
        configurationOptions.append(getFileSystemsOptions());

        final String inputPath = getInputFileOption();
        configurationOptions.append(inputPath);
        final String outputPath = getOutputFileOption();
        if (StringUtils.isNotBlank(outputPath)) {
            if (StringUtils.isBlank(inputPath)) {
                throw new BuilderCreationException("Input file is required when otuput file is set (see inputFile(String) method)");
            }
            configurationOptions.append(outputPath);
        }

        return configurationOptions.build();
    }
}
