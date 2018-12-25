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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import biz.gabrys.lesscss.compiler2.filesystem.LocalFileSystem;

/**
 * <p>
 * Represents <a href="http://lesscss.org/usage/index.html#less-options">Less compiler options</a> responsible for
 * controlling the {@link LessCompiler} compilation process.
 * </p>
 * <p>
 * Base options:
 * </p>
 * <ul>
 * <li>{@link #isCompress() compress} - whether a CSS code should be compressed (default: {@code false})</li>
 * <li>{@link #isIeCompatibility() IE compatibility} - whether a CSS code should be compatible with Internet Explorer
 * browser (default: {@code true})</li>
 * <li>{@link #getIncludePaths() included paths} - available include paths (default: {@code []})</li>
 * <li>{@link #isJavaScript() JavaScript} - whether a compiler should allow usage of JavaScript language (default:
 * {@code true})</li>
 * <li>{@link #getLineNumbers() line numbers} - whether a compiler should generate inline source-mapping (default:
 * {@link LineNumbersValue#OFF})</li>
 * <li>{@link #isRelativeUrls() relative URLs} - whether a compiler should rewrite relative URLs (default:
 * {@code false})</li>
 * <li>{@link #getRootPath() root path} - a path which will be added to every generated import and URL in CSS code
 * (default: {@code null})</li>
 * <li>{@link #isSilent() silent} - whether a compiler shouldn't log compilation warnings (default: {@code false})</li>
 * <li>{@link #isStrictImports() strict imports} - whether a compiler should disallow an @import operation inside of
 * either @media blocks or other selector blocks (default: {@code false})</li>
 * <li>{@link #isStrictMath() strict math} - whether a compiler should try and process all maths in Less code (default:
 * {@code false})</li>
 * <li>{@link #isStrictUnits() strict units} - whether a compiler should guess units in Less code when it does maths
 * (default: {@code false})</li>
 * </ul>
 * <p>
 * Source Map options:
 * </p>
 * <ul>
 * <li>{@link #getSourceMapBasePath() base path} - a path that will be removed from each of the Less file paths inside
 * the Source Map and also from the path to the map file specified in your output CSS (default: {@code null})</li>
 * <li>{@link #isSourceMapLessInline() Less inline} - whether a compiler should include all of the Less files in to the
 * Source Map (default: {@code false})</li>
 * <li>{@link #getSourceMapRootPath() root path} - a path that will be prepended to each of the Less file paths inside
 * the Source Map and also to the path to the map file specified in your output CSS (default: {@code null})</li>
 * <li>{@link #getSourceMapUrl() URL} - a path which will overwrite the URL in the CSS that points at the Source Map
 * file (default: {@code null})</li>
 * </ul>
 * <p>
 * Additional options:
 * </p>
 * <ul>
 * <li>{@link #getBanner() banner} - a banner which will be inserted to a source file before the compilation</li>
 * <li>{@link #getGlobalVariables() globalVariables} - variables that can be referenced by the files</li>
 * <li>{@link #getModifyVariables() modifyVariables} - variables that can overwrite variables defined in the files</li>
 * </ul>
 * <p>
 * Non-standard options:
 * </p>
 * <ul>
 * <li>{@link #getEncoding() encoding} - an encoding used to read source files and save generated code (default:
 * {@code null} - means platform default encoding)</li>
 * <li>{@link #getFileSystems() file systems} - a list with file systems (default:
 * <code>["biz.gabrys.lesscss.compiler2.filesystem.{@link LocalFileSystem}"]</code>)</li>
 * </ul>
 * @since 2.0.0
 * @see LessOptionsBuilder
 * @see FileSystemOptionsBuilder
 * @see LessVariableOptionsBuilder
 */
public class LessOptions {

    /**
     * Stores a default value of the file systems option
     * (<code>["biz.gabrys.lesscss.compiler2.filesystem.{@link LocalFileSystem}"]</code>).
     * @since 2.0.0
     */
    public static final List<FileSystemOption> DEFAULT_FILE_SYSTEMS = Collections
            .unmodifiableList(Arrays.asList(new FileSystemOption(LocalFileSystem.class)));

    private boolean silent;
    private boolean strictImports;
    private boolean compress;
    private boolean ieCompatibility;
    private boolean javaScript;
    private List<String> includePaths;
    private LineNumbersValue lineNumbers;
    private String rootPath;
    private boolean relativeUrls;
    private boolean strictMath;
    private boolean strictUnits;

    private String sourceMapRootPath;
    private String sourceMapBasePath;
    private boolean sourceMapLessInline;
    private String sourceMapUrl;

    private String banner;
    private List<LessVariableOption> globalVariables;
    private List<LessVariableOption> modifyVariables;

    private String encoding;
    private List<FileSystemOption> fileSystems;

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public LessOptions() {
        ieCompatibility = true;
        javaScript = true;
        includePaths = Collections.emptyList();
        lineNumbers = LineNumbersValue.OFF;

        globalVariables = Collections.emptyList();
        modifyVariables = Collections.emptyList();

        fileSystems = DEFAULT_FILE_SYSTEMS;
    }

    /**
     * Constructs a new instance as a copy of the another options object.
     * @param options the another options object (cannot be {@code null}).
     * @throws IllegalArgumentException if options is {@code null}.
     * @since 2.0.0
     */
    public LessOptions(final LessOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("Options cannot be null");
        }
        silent = options.silent;
        strictImports = options.strictImports;
        compress = options.compress;
        ieCompatibility = options.ieCompatibility;
        javaScript = options.javaScript;
        includePaths = new ArrayList<>(options.includePaths);
        lineNumbers = options.lineNumbers;
        rootPath = options.rootPath;
        relativeUrls = options.relativeUrls;
        strictMath = options.strictMath;
        strictUnits = options.strictUnits;

        sourceMapRootPath = options.sourceMapRootPath;
        sourceMapBasePath = options.sourceMapBasePath;
        sourceMapLessInline = options.sourceMapLessInline;
        sourceMapUrl = options.sourceMapUrl;

        banner = options.banner;
        globalVariables = new ArrayList<>(options.globalVariables);
        modifyVariables = new ArrayList<>(options.modifyVariables);

        encoding = options.encoding;
        fileSystems = new ArrayList<>(options.fileSystems);
    }

    /**
     * Checks whether a compiler shouldn't log compilation warnings (default: {@code false}).
     * @return {@code true} whether the compiler shouldn't log compilation warnings, otherwise {@code false}.
     * @since 2.0.0
     */
    public boolean isSilent() {
        return silent;
    }

    /**
     * Sets whether a compiler shouldn't log compilation warnings (default: {@code false}).
     * @param silent {@code true} whether the compiler shouldn't log compilation warnings, otherwise {@code false}.
     * @since 2.0.0
     */
    public void setSilent(final boolean silent) {
        this.silent = silent;
    }

    /**
     * Checks whether a compiler should disallow an &#64;import operation inside of either &#64;media blocks or other
     * selector blocks (default: {@code false}).
     * @return {@code true} whether the compiler should disallow the &#64;import operation inside of either &#64;media
     *         blocks or other selector blocks, otherwise {@code false}.
     * @since 2.0.0
     */
    public boolean isStrictImports() {
        return strictImports;
    }

    /**
     * Sets whether a compiler should disallow an &#64;import operation inside of either &#64;media blocks or other
     * selector blocks (default: {@code false}).
     * @param strictImports {@code true} whether the compiler should disallow the &#64;import operation inside of either
     *            &#64;media blocks or other selector blocks, otherwise {@code false}.
     * @since 2.0.0
     */
    public void setStrictImports(final boolean strictImports) {
        this.strictImports = strictImports;
    }

    /**
     * Checks whether a CSS code should be compressed (default: {@code false}).
     * @return {@code true} whether the CSS code should be compressed, otherwise {@code false}.
     * @since 2.0.0
     */
    public boolean isCompress() {
        return compress;
    }

    /**
     * Sets whether a CSS code should be compressed (default: {@code false}).
     * @param compress {@code true} whether the CSS code should be compressed, otherwise {@code false}.
     * @since 2.0.0
     */
    public void setCompress(final boolean compress) {
        this.compress = compress;
    }

    /**
     * Sets whether a CSS code should be compatible with Internet Explorer browser (default: {@code true}). Used for the
     * {@code data-uri} function to ensure that images aren't created that are too large for the browser to handle.
     * @return {@code true} whether the CSS code should be compatible with Internet Explorer browser, otherwise
     *         {@code false}.
     * @since 2.0.0
     */
    public boolean isIeCompatibility() {
        return ieCompatibility;
    }

    /**
     * Sets whether a CSS code should be compatible with Internet Explorer browser (default: {@code true}). Used for the
     * {@code data-uri} function to ensure that images aren't created that are too large for the browser to handle.
     * @param ieCompatibility {@code true} whether the CSS code should be compatible with Internet Explorer browser,
     *            otherwise {@code false}.
     * @since 2.0.0
     */
    public void setIeCompatibility(final boolean ieCompatibility) {
        this.ieCompatibility = ieCompatibility;
    }

    /**
     * Checks whether a compiler should allow usage of JavaScript language (default: {@code true}).
     * @return {@code true} whether the compiler should allow usage of JavaScript language, otherwise {@code false}.
     * @since 2.0.0
     */
    public boolean isJavaScript() {
        return javaScript;
    }

    /**
     * Sets whether a compiler should allow usage of JavaScript language (default: {@code true}).
     * @param javaScript {@code true} whether the compiler should allow usage of JavaScript language, otherwise
     *            {@code false}.
     * @since 2.0.0
     */
    public void setJavaScript(final boolean javaScript) {
        this.javaScript = javaScript;
    }

    /**
     * Returns available include paths (default: {@code []}). If the file in an &#64;import rule does not exist at that
     * exact location, a compiler will look for it at the location(s) passed to this option. You might use this for
     * instance to specify a path to a library which you want to be referenced simply and relatively in the less files.
     * @return the available include paths (never {@code null}).
     * @since 2.0.0
     */
    public List<String> getIncludePaths() {
        return new ArrayList<>(includePaths);
    }

    /**
     * Sets available include paths (default: {@code []}). If the file in an &#64;import rule does not exist at that
     * exact location, a compiler will look for it at the location(s) passed to this option. You might use this for
     * instance to specify a path to a library which you want to be referenced simply and relatively in the less files.
     * @param includePaths the available include paths ({@code null} is treated as an empty collection).
     * @since 2.0.0
     */
    public void setIncludePaths(final List<String> includePaths) {
        if (includePaths == null) {
            this.includePaths = Collections.emptyList();
        } else {
            this.includePaths = new ArrayList<>(includePaths);
        }
    }

    /**
     * Returns whether a compiler should generate inline source-mapping (default: {@link LineNumbersValue#OFF}). This
     * was the only option before browsers started supporting Source Maps.
     * @return the line numbers value (never {@code null}).
     * @since 2.0.0
     */
    public LineNumbersValue getLineNumbers() {
        return lineNumbers;
    }

    /**
     * Sets whether a compiler should generate inline source-mapping (default: {@link LineNumbersValue#OFF}). This was
     * the only option before browsers started supporting Source Maps.
     * @param lineNumbers the line numbers value ({@code null} is treated as {@link LineNumbersValue#OFF}).
     * @since 2.0.0
     */
    public void setLineNumbers(final LineNumbersValue lineNumbers) {
        if (lineNumbers == null) {
            this.lineNumbers = LineNumbersValue.OFF;
        } else {
            this.lineNumbers = lineNumbers;
        }
    }

    /**
     * Returns a path which will be added to every generated import and URL in CSS code (default: {@code null}). This
     * does not affect less import statements that are processed, just ones that are left in the output CSS.
     * <p>
     * For instance, if all the images the CSS use are in a folder called resources, you can use this option to add this
     * on to the URL's and then have the name of that folder configurable.
     * </p>
     * @return the path which will be added.
     * @since 2.0.0
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * Sets a path which will be added to every generated import and URL in CSS code (default: {@code null}). This does
     * not affect less import statements that are processed, just ones that are left in the output CSS.
     * <p>
     * For instance, if all the images the CSS use are in a folder called resources, you can use this option to add this
     * on to the URL's and then have the name of that folder configurable.
     * </p>
     * @param rootPath the path which will be added.
     * @since 2.0.0
     */
    public void setRootPath(final String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * Checks whether a compiler should rewrite relative URLs (default: {@code false}). By default URLs are kept as-is,
     * so if you import a file in a sub-directory that references an image, exactly the same URL will be output in the
     * CSS code. This option allows you to re-write URLs in imported files so that the URL is always relative to the
     * base imported file. Example:
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
     * @return {@code true} whether the compiler should rewrite relative URLs, otherwise {@code false}.
     * @since 2.0.0
     */
    public boolean isRelativeUrls() {
        return relativeUrls;
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
     * @since 2.0.0
     */
    public void setRelativeUrls(final boolean relativeUrls) {
        this.relativeUrls = relativeUrls;
    }

    /**
     * Checks whether a compiler should try and process all maths in Less code (default: {@code false}). By default the
     * compiler will try and process all maths in your Less e.g.:
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
     * With this option enabled, only maths that is inside unnecessary parenthesis will be processed. Example:
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
     * @return {@code true} whether the compiler should try and process all maths in the Less code, otherwise
     *         {@code false}.
     * @since 2.0.0
     * @see #isStrictUnits()
     */
    public boolean isStrictMath() {
        return strictMath;
    }

    /**
     * Sets whether a compiler should try and process all maths in Less code (default: {@code false}). By default the
     * compiler will try and process all maths in your Less e.g.:
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
     * With this option enabled, only maths that is inside unnecessary parenthesis will be processed. Example:
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
     * @since 2.0.0
     * @see #setStrictUnits(boolean)
     */
    public void setStrictMath(final boolean strictMath) {
        this.strictMath = strictMath;
    }

    /**
     * Checks whether a compiler should guess units in Less code when it does maths (default: {@code false}). By default
     * the compiler attempts to guess at the output unit when it does maths. For instance:
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
     * @return {@code true} whether the compiler should guess units in the Less code when it does maths, otherwise
     *         {@code false}.
     * @since 2.0.0
     * @see #isStrictMath()
     */
    public boolean isStrictUnits() {
        return strictUnits;
    }

    /**
     * Sets whether a compiler should guess units in Less code when it does maths (default: {@code false}). By default
     * the compiler attempts to guess at the output unit when it does maths. For instance:
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
     * @since 2.0.0
     * @see #setStrictMath(boolean)
     */
    public void setStrictUnits(final boolean strictUnits) {
        this.strictUnits = strictUnits;
    }

    /**
     * Returns a path that will be prepended to each of the Less file paths inside the Source Map and also to the path
     * to the map file specified in your output CSS (default: {@code null}).
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
     * This option is the opposite of the {@link #getSourceMapBasePath() Source Map base path} option.
     * </p>
     * @return the path which will be added.
     * @since 2.0.0
     * @see #getSourceMapBasePath()
     * @see #getSourceMapUrl()
     */
    public String getSourceMapRootPath() {
        return sourceMapRootPath;
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
     * This option is the opposite of the {@link #setSourceMapBasePath(String) Source Map base path} option.
     * </p>
     * @param sourceMapRootPath the path which will be added.
     * @since 2.0.0
     * @see #setSourceMapBasePath(String)
     * @see #setSourceMapUrl(String)
     */
    public void setSourceMapRootPath(final String sourceMapRootPath) {
        this.sourceMapRootPath = sourceMapRootPath;
    }

    /**
     * Returns a path that will be removed from each of the Less file paths inside the Source Map and also from the path
     * to the map file specified in your output CSS (default: {@code null}).
     * <p>
     * It specifies a path which should be removed from the output paths. For instance if you are compiling a file in
     * the less-files directory but the source files will be available on your web server in the root or current
     * directory, you can specify this to remove the additional less-files part of the path.
     * </p>
     * <p>
     * This option is the opposite of the {@link #getSourceMapRootPath() Source Map root path} option.
     * </p>
     * @return the path which will be removed.
     * @since 2.0.0
     * @see #getSourceMapRootPath()
     * @see #getSourceMapUrl()
     */
    public String getSourceMapBasePath() {
        return sourceMapBasePath;
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
     * This option is the opposite of the {@link #setSourceMapRootPath(String) Source Map root path} option.
     * </p>
     * @param sourceMapBasePath the path which will be removed.
     * @since 2.0.0
     * @see #setSourceMapRootPath(String)
     * @see #setSourceMapUrl(String)
     */
    public void setSourceMapBasePath(final String sourceMapBasePath) {
        this.sourceMapBasePath = sourceMapBasePath;
    }

    /**
     * Checks whether a compiler should include all of the Less files in to the Source Map (default: {@code false}).
     * This means that you only need your map file to get to your original source.
     * @return {@code true} whether the compiler should include all of the Less files in to the Source Map, otherwise
     *         {@code false}.
     * @since 2.0.0
     */
    public boolean isSourceMapLessInline() {
        return sourceMapLessInline;
    }

    /**
     * Sets whether a compiler should include all of the Less files in to the Source Map (default: {@code false}). This
     * means that you only need your map file to get to your original source.
     * @param sourceMapLessInline {@code true} whether the compiler should include all of the Less files in to the
     *            Source Map, otherwise {@code false}.
     * @since 2.0.0
     */
    public void setSourceMapLessInline(final boolean sourceMapLessInline) {
        this.sourceMapLessInline = sourceMapLessInline;
    }

    /**
     * Returns a path which will overwrite the URL in the CSS that points at the Source Map file (default:
     * {@code null}). This is for cases when the {@link #getSourceMapRootPath() root path} and the
     * {@link #getSourceMapBasePath() base path} options are not producing exactly what you need.
     * @return the Source Map URL.
     * @since 2.0.0
     * @see #getSourceMapBasePath()
     * @see #getSourceMapRootPath()
     */
    public String getSourceMapUrl() {
        return sourceMapUrl;
    }

    /**
     * Sets a path which will overwrite the URL in the CSS that points at the Source Map file (default: {@code null}).
     * This is for cases when the {@link #setSourceMapRootPath(String) root path} and the
     * {@link #setSourceMapBasePath(String) base path} options are not producing exactly what you need.
     * @param sourceMapUrl the Source Map URL.
     * @since 2.0.0
     * @see #setSourceMapBasePath(String)
     * @see #setSourceMapRootPath(String)
     */
    public void setSourceMapUrl(final String sourceMapUrl) {
        this.sourceMapUrl = sourceMapUrl;
    }

    /**
     * Returns an encoding used to read source files and save generated code (default: {@code null} - means platform
     * default encoding).
     * @return the encoding.
     * @since 2.0.0
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets an encoding used to read source files and save generated code (default: {@code null} - means platform
     * default encoding).
     * @param encoding the encoding.
     * @since 2.0.0
     */
    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    /**
     * Returns file systems used to fetch content of the source files (default: {@link #DEFAULT_FILE_SYSTEMS}).
     * @return the file systems (never {@code null}).
     * @since 2.0.0
     */
    public List<FileSystemOption> getFileSystems() {
        return new ArrayList<>(fileSystems);
    }

    /**
     * Sets file systems used to fetch content of the source files (default: {@link #DEFAULT_FILE_SYSTEMS}).
     * @param fileSystems the file systems ({@code null} is treated as a collection with default values).
     * @since 2.0.0
     * @see FileSystemOptionsBuilder
     */
    public void setFileSystems(final List<FileSystemOption> fileSystems) {
        if (fileSystems == null) {
            this.fileSystems = DEFAULT_FILE_SYSTEMS;
        } else {
            this.fileSystems = new ArrayList<>(fileSystems);
        }
    }

    /**
     * Returns a banner which will be inserted to a source file before the compilation (default: {@code null}).
     * @return the banner.
     * @since 2.0.0
     */
    public String getBanner() {
        return banner;
    }

    /**
     * Sets a banner which will be inserted to a source file before the compilation (default: {@code null}).
     * @param banner the banner.
     * @since 2.0.0
     */
    public void setBanner(final String banner) {
        this.banner = banner;
    }

    /**
     * Returns global variables that can be referenced by the files (default: {@code []}. Effectively the declarations
     * are put at the top of your base Less file, meaning those variables can be used, but they also can be overridden
     * if variables with the same names are defined in the file.
     * @return the global variables (never {@code null}).
     */
    public List<LessVariableOption> getGlobalVariables() {
        return new ArrayList<>(globalVariables);
    }

    /**
     * Sets global variables that can be referenced by the files (default: {@code []}. Effectively the declarations are
     * put at the top of your base Less file, meaning those variables can be used, but they also can be overridden if
     * variables with the same names are defined in the file.
     * @param globalVariables the global variables ({@code null} is treated as an empty collection).
     * @since 2.0.0
     * @see LessVariableOptionsBuilder
     */
    public void setGlobalVariables(final List<LessVariableOption> globalVariables) {
        if (globalVariables == null) {
            this.globalVariables = Collections.emptyList();
        } else {
            this.globalVariables = new ArrayList<>(globalVariables);
        }
    }

    /**
     * Returns modify variables that can overwrite variables defined in the files (default: {@code []}. Effectively the
     * declarations are put at the bottom of your base Less file, meaning they will override anything defined in your
     * Less file.
     * @return the modify variables (never {@code null}).
     * @since 2.0.0
     */
    public List<LessVariableOption> getModifyVariables() {
        return new ArrayList<>(modifyVariables);
    }

    /**
     * Sets modify variables that can overwrite variables defined in the files (default: {@code []}. Effectively the
     * declarations are put at the bottom of your base Less file, meaning they will override anything defined in your
     * Less file.
     * @param modifyVariables the modify variables ({@code null} is treated as an empty collection).
     * @since 2.0.0
     * @see LessVariableOptionsBuilder
     */
    public void setModifyVariables(final List<LessVariableOption> modifyVariables) {
        if (modifyVariables == null) {
            this.modifyVariables = Collections.emptyList();
        } else {
            this.modifyVariables = new ArrayList<>(modifyVariables);
        }
    }
}
