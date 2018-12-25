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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import biz.gabrys.lesscss.compiler2.util.StringUtils;

/**
 * <p>
 * Responsible for creating new instances of the {@link LessOptions}.
 * </p>
 * <p>
 * Example code:
 * </p>
 * 
 * <pre>
 * {@link LessOptions} options = null;
 * 
 * // create options with enabled code compression
 * options = new {@link #LessOptionsBuilder() LessOptionsBuilder}().{@link #compressOn() compressOn}().{@link #build() build}();
 * 
 * // create options with enabled strict units and disabled IE compatibility
 * options = new {@link #LessOptionsBuilder() LessOptionsBuilder}().{@link #strictUnitsOn() strictUnitsOn}().{@link #ieCompatibilityOff() ieCompatibilityOff}().{@link #build() build}();
 * 
 * // use existing options object to create a new one
 * boolean compression = false;
 * options = new {@link #LessOptionsBuilder(LessOptions) LessOptionsBuilder}(options).{@link #compress(boolean) compress}(compression).{@link #build() build}();
 * </pre>
 * 
 * @since 2.0.0
 * @see FileSystemOptionBuilder
 * @see FileSystemOptionsBuilder
 * @see LessVariableOptionsBuilder
 */
public class LessOptionsBuilder {

    private final LessOptions options;

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public LessOptionsBuilder() {
        options = new LessOptions();
    }

    /**
     * Constructs a new instance and copies options into {@code this} builder.
     * @param options the copied options.
     * @throws IllegalArgumentException if the options object is {@code null}.
     * @since 2.0.0
     */
    public LessOptionsBuilder(final LessOptions options) {
        if (options == null) {
            throw new IllegalArgumentException("Options cannot be null");
        }
        this.options = new LessOptions(options);
    }

    /**
     * Sets whether a compiler shouldn't log compilation warnings (default: {@code false}).
     * @param silent {@code true} whether the compiler shouldn't log compilation warnings, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #silentOn()
     * @see #silentOff()
     */
    public LessOptionsBuilder silent(final boolean silent) {
        getOptions().setSilent(silent);
        return this;
    }

    /**
     * Disables logging compilation warnings (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #silent(boolean)
     * @see #silentOff()
     */
    public LessOptionsBuilder silentOn() {
        return silent(true);
    }

    /**
     * Enables logging compilation warnings (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #silent(boolean)
     * @see #silentOn()
     */
    public LessOptionsBuilder silentOff() {
        return silent(false);
    }

    /**
     * Sets whether a compiler should disallow an &#64;import operation inside of either &#64;media blocks or other
     * selector blocks (default: {@code false}).
     * @param strictImports {@code true} whether the compiler should disallow the &#64;import operation inside of either
     *            &#64;media blocks or other selector blocks, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictImportsOn()
     * @see #strictImportsOff()
     */
    public LessOptionsBuilder strictImports(final boolean strictImports) {
        getOptions().setStrictImports(strictImports);
        return this;
    }

    /**
     * Disallows usage of &#64;import operations inside of either &#64;media blocks or other selector blocks (default:
     * {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictImports(boolean)
     * @see #strictImportsOff()
     */
    public LessOptionsBuilder strictImportsOn() {
        return strictImports(true);
    }

    /**
     * Allows usage of &#64;import operations inside of either &#64;media blocks or other selector blocks (default:
     * {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictImports(boolean)
     * @see #strictImportsOn()
     */
    public LessOptionsBuilder strictImportsOff() {
        return strictImports(false);
    }

    /**
     * Sets whether a CSS code should be compressed (default: {@code false}).
     * @param compress {@code true} whether the CSS code should be compressed, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #compressOn()
     * @see #compressOff()
     */
    public LessOptionsBuilder compress(final boolean compress) {
        getOptions().setCompress(compress);
        return this;
    }

    /**
     * Enables a CSS code compression (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #compress(boolean)
     * @see #compressOff()
     */
    public LessOptionsBuilder compressOn() {
        return compress(true);
    }

    /**
     * Disables a CSS code compression (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #compress(boolean)
     * @see #compressOn()
     */
    public LessOptionsBuilder compressOff() {
        return compress(false);
    }

    /**
     * Sets whether a CSS code should be compatible with IE browser (default: {@code true}).
     * @param ieCompatibility {@code true} whether the CSS code should be compatible with IE browser, otherwise
     *            {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #ieCompatibilityOn()
     * @see #ieCompatibilityOff()
     */
    public LessOptionsBuilder ieCompatibility(final boolean ieCompatibility) {
        getOptions().setIeCompatibility(ieCompatibility);
        return this;
    }

    /**
     * Sets that a CSS code should be compatible with IE browser (default: {@code on}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #ieCompatibility(boolean)
     * @see #ieCompatibilityOff()
     */
    public LessOptionsBuilder ieCompatibilityOn() {
        return ieCompatibility(true);
    }

    /**
     * Sets that a CSS code does not have to be compatible with IE browser (default: {@code on}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #ieCompatibility(boolean)
     * @see #ieCompatibilityOn()
     */
    public LessOptionsBuilder ieCompatibilityOff() {
        return ieCompatibility(false);
    }

    /**
     * Sets whether a compiler should allow usage of JavaScript language (default: {@code true}).
     * @param javaScript {@code true} whether the compiler should allow usage of JavaScript language, otherwise
     *            {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #javaScriptOn()
     * @see #javaScriptOff()
     */
    public LessOptionsBuilder javaScript(final boolean javaScript) {
        getOptions().setJavaScript(javaScript);
        return this;
    }

    /**
     * Allows usage of JavaScript language (default: {@code on}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #javaScript(boolean)
     * @see #javaScriptOff()
     */
    public LessOptionsBuilder javaScriptOn() {
        return javaScript(true);
    }

    /**
     * Disallows usage of JavaScript language (default: {@code on}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #javaScript(boolean)
     * @see #javaScriptOn()
     */
    public LessOptionsBuilder javaScriptOff() {
        return javaScript(false);
    }

    /**
     * Sets available include paths (default: {@code []}).
     * @param includePaths the available include paths ({@code null} is treated as an empty collection).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #includePaths(CharSequence...)
     * @see #includePathsOff()
     */
    public LessOptionsBuilder includePaths(final Collection<? extends CharSequence> includePaths) {
        if (includePaths != null) {
            final List<String> paths = new ArrayList<>(includePaths.size());
            for (final CharSequence path : includePaths) {
                paths.add(StringUtils.toStringIfNotNull(path));
            }
            getOptions().setIncludePaths(paths);
        } else {
            getOptions().setIncludePaths(null);
        }
        return this;
    }

    /**
     * Sets available include paths (default: {@code []}).
     * @param includePaths the available include paths.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #includePaths(Collection)
     * @see #includePathsOff()
     */
    public LessOptionsBuilder includePaths(final CharSequence... includePaths) {
        if (includePaths != null) {
            return includePaths(Arrays.asList(includePaths));
        } else {
            return includePaths((Collection<CharSequence>) null);
        }
    }

    /**
     * Clears available include paths (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #includePaths(Collection)
     * @see #includePaths(CharSequence...)
     */
    public LessOptionsBuilder includePathsOff() {
        return includePaths((Collection<CharSequence>) null);
    }

    /**
     * Sets whether a compiler should generate inline source-mapping (default: {@link LineNumbersValue#OFF}).
     * @param lineNumbers the line numbers value ({@code null} is treated as {@link LineNumbersValue#OFF}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #lineNumbersOff()
     * @see #lineNumbersComments()
     * @see #lineNumbersMediaQuery()
     * @see #lineNumbersAll()
     */
    public LessOptionsBuilder lineNumbers(final LineNumbersValue lineNumbers) {
        getOptions().setLineNumbers(lineNumbers);
        return this;
    }

    /**
     * Disables generating inline source-mapping (default: {@link LineNumbersValue#OFF}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #lineNumbers(LineNumbersValue)
     * @see #lineNumbersComments()
     * @see #lineNumbersMediaQuery()
     * @see #lineNumbersAll()
     */
    public LessOptionsBuilder lineNumbersOff() {
        return lineNumbers(LineNumbersValue.OFF);
    }

    /**
     * Enables generating inline source-mapping in comments blocks (default: {@link LineNumbersValue#OFF}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #lineNumbers(LineNumbersValue)
     * @see #lineNumbersOff()
     * @see #lineNumbersMediaQuery()
     * @see #lineNumbersAll()
     */
    public LessOptionsBuilder lineNumbersComments() {
        return lineNumbers(LineNumbersValue.COMMENTS);
    }

    /**
     * Enables generating inline source-mapping in &#64;media queries (default: {@link LineNumbersValue#OFF}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #lineNumbers(LineNumbersValue)
     * @see #lineNumbersOff()
     * @see #lineNumbersComments()
     * @see #lineNumbersAll()
     */
    public LessOptionsBuilder lineNumbersMediaQuery() {
        return lineNumbers(LineNumbersValue.MEDIA_QUERY);
    }

    /**
     * Enables generating inline source-mapping in comments blocks and &#64;media queries (default:
     * {@link LineNumbersValue#OFF}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #lineNumbers(LineNumbersValue)
     * @see #lineNumbersOff()
     * @see #lineNumbersComments()
     * @see #lineNumbersMediaQuery()
     */
    public LessOptionsBuilder lineNumbersAll() {
        return lineNumbers(LineNumbersValue.ALL);
    }

    /**
     * Sets a path which will be added to every generated import and URL in CSS code (default: {@code null}).
     * @param rootPath the root path.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #rootPathOff()
     */
    public LessOptionsBuilder rootPath(final CharSequence rootPath) {
        getOptions().setRootPath(StringUtils.toStringIfNotNull(rootPath));
        return this;
    }

    /**
     * Clears a value of the root path option (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #rootPath(CharSequence)
     */
    public LessOptionsBuilder rootPathOff() {
        return rootPath(null);
    }

    /**
     * Sets whether a compiler should rewrite relative URLs (default: {@code false}).
     * @param relativeUrls {@code true} whether the compiler should rewrite relative URLs, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #relativeUrlsOn()
     * @see #relativeUrlsOff()
     */
    public LessOptionsBuilder relativeUrls(final boolean relativeUrls) {
        getOptions().setRelativeUrls(relativeUrls);
        return this;
    }

    /**
     * Enables rewriting relative URLs (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #relativeUrls(boolean)
     * @see #relativeUrlsOff()
     */
    public LessOptionsBuilder relativeUrlsOn() {
        return relativeUrls(true);
    }

    /**
     * Disables rewriting relative URLs (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #relativeUrls(boolean)
     * @see #relativeUrlsOn()
     */
    public LessOptionsBuilder relativeUrlsOff() {
        return relativeUrls(false);
    }

    /**
     * Sets whether a compiler should try and process all maths in Less code (default: {@code false}).
     * @param strictMath {@code true} whether the compiler should try and process all maths in the Less code, otherwise
     *            {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictMathOn()
     * @see #strictMathOff()
     */
    public LessOptionsBuilder strictMath(final boolean strictMath) {
        getOptions().setStrictMath(strictMath);
        return this;
    }

    /**
     * Sets that a compiler should try and process all maths in Less code (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictMath(boolean)
     * @see #strictMathOff()
     */
    public LessOptionsBuilder strictMathOn() {
        return strictMath(true);
    }

    /**
     * Sets that a compiler shouldn't try and process all maths in Less code (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictMath(boolean)
     * @see #strictMathOn()
     */
    public LessOptionsBuilder strictMathOff() {
        return strictMath(false);
    }

    /**
     * Sets whether a compiler should guess units in Less code when it does maths (default: {@code false}).
     * @param strictUnits {@code true} whether the compiler should guess units in the Less code when it does maths,
     *            otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictUnitsOn()
     * @see #strictUnitsOff()
     */
    public LessOptionsBuilder strictUnits(final boolean strictUnits) {
        getOptions().setStrictUnits(strictUnits);
        return this;
    }

    /**
     * Sets that a compiler should guess units in Less code when it does maths (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictUnits(boolean)
     * @see #strictUnitsOff()
     */
    public LessOptionsBuilder strictUnitsOn() {
        return strictUnits(true);
    }

    /**
     * Sets that a compiler shouldn't guess units in Less code when it does maths (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #strictUnits(boolean)
     * @see #strictUnitsOn()
     */
    public LessOptionsBuilder strictUnitsOff() {
        return strictUnits(false);
    }

    /**
     * Sets a path that will be prepended to each of the Less file paths inside the Source Map and also to the path to
     * the map file specified in your output CSS (default: {@code null}).
     * @param rootPath the path which will be added.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapRootPathOff()
     */
    public LessOptionsBuilder sourceMapRootPath(final CharSequence rootPath) {
        getOptions().setSourceMapRootPath(StringUtils.toStringIfNotNull(rootPath));
        return this;
    }

    /**
     * Clears a value of the Source Map root path option (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapRootPath(CharSequence)
     */
    public LessOptionsBuilder sourceMapRootPathOff() {
        return sourceMapRootPath(null);
    }

    /**
     * Sets a path that will be removed from each of the Less file paths inside the Source Map and also from the path to
     * the map file specified in your output CSS (default: {@code null}).
     * @param basePath the path which will be removed.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapBasePathOff()
     */
    public LessOptionsBuilder sourceMapBasePath(final CharSequence basePath) {
        getOptions().setSourceMapBasePath(StringUtils.toStringIfNotNull(basePath));
        return this;
    }

    /**
     * Clears a value of the Source Map base path option (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapBasePath(CharSequence)
     */
    public LessOptionsBuilder sourceMapBasePathOff() {
        return sourceMapBasePath(null);
    }

    /**
     * Sets whether a compiler should include all of the Less files in to the Source Map (default: {@code false}).
     * @param inline {@code true} whether the compiler should include all of the Less files in to the Source Map,
     *            otherwise {@code false}.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapLessInlineOn()
     * @see #sourceMapLessInlineOff()
     */
    public LessOptionsBuilder sourceMapLessInline(final boolean inline) {
        getOptions().setSourceMapLessInline(inline);
        return this;
    }

    /**
     * Sets that a compiler should include all of the Less files in to the Source Map (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapLessInline(boolean)
     * @see #sourceMapLessInlineOff()
     */
    public LessOptionsBuilder sourceMapLessInlineOn() {
        return sourceMapLessInline(true);
    }

    /**
     * Sets that a compiler will not include all of the Less files in to the Source Map (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapLessInline(boolean)
     * @see #sourceMapLessInlineOn()
     */
    public LessOptionsBuilder sourceMapLessInlineOff() {
        return sourceMapLessInline(false);
    }

    /**
     * Sets a path which will overwrite the URL in the CSS that points at the Source Map map file (default:
     * {@code null}).
     * @param url the Source Map URL.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapUrlOff()
     */
    public LessOptionsBuilder sourceMapUrl(final CharSequence url) {
        getOptions().setSourceMapUrl(StringUtils.toStringIfNotNull(url));
        return this;
    }

    /**
     * Clears a value of the Source Map URL option (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #sourceMapUrl(CharSequence)
     */
    public LessOptionsBuilder sourceMapUrlOff() {
        return sourceMapUrl(null);
    }

    /**
     * Sets an encoding used to read source files and save generated code (default: {@code null} - means platform
     * default encoding).
     * @param encoding the encoding.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #encoding(Charset)
     * @see #encodingPlatformDefault()
     */
    public LessOptionsBuilder encoding(final CharSequence encoding) {
        getOptions().setEncoding(StringUtils.toStringIfNotNull(encoding));
        return this;
    }

    /**
     * Sets an encoding used to read source files and save generated code (default: {@code null} - means platform
     * default encoding).
     * @param encoding the encoding.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #encoding(CharSequence)
     * @see #encodingPlatformDefault()
     */
    public LessOptionsBuilder encoding(final Charset encoding) {
        getOptions().setEncoding(StringUtils.toStringIfNotNull(encoding));
        return this;
    }

    /**
     * Sets an encoding used to read source files and save generated code to platform default (default:
     * {@code platform default encoding}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #encoding(CharSequence)
     * @see #encoding(Charset)
     */
    public LessOptionsBuilder encodingPlatformDefault() {
        return encoding((CharSequence) null);
    }

    /**
     * Sets available file systems (default: {@link LessOptions#DEFAULT_FILE_SYSTEMS}).
     * @param fileSystems the available {@link biz.gabrys.lesscss.compiler2.filesystem.FileSystem file systems}
     *            ({@code null} is treated as a collection with default values).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #fileSystems(FileSystemOption...)
     * @see FileSystemOptionsBuilder
     */
    public LessOptionsBuilder fileSystems(final Collection<? extends FileSystemOption> fileSystems) {
        if (fileSystems != null) {
            getOptions().setFileSystems(new ArrayList<FileSystemOption>(fileSystems));
        } else {
            getOptions().setFileSystems(null);
        }
        return this;
    }

    /**
     * Sets available file systems (default: {@link LessOptions#DEFAULT_FILE_SYSTEMS}).
     * @param fileSystems the available {@link biz.gabrys.lesscss.compiler2.filesystem.FileSystem file systems}
     *            ({@code null} is treated as a collection with default values).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #fileSystems(Collection)
     * @see FileSystemOptionBuilder
     */
    public LessOptionsBuilder fileSystems(final FileSystemOption... fileSystems) {
        if (fileSystems != null) {
            return fileSystems(Arrays.asList(fileSystems));
        } else {
            return fileSystems((Collection<FileSystemOption>) null);
        }
    }

    /**
     * Sets a banner which will be inserted to a source file before the compilation (default: {@code null}).
     * @param banner the banner.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #bannerOff()
     */
    public LessOptionsBuilder banner(final CharSequence banner) {
        getOptions().setBanner(StringUtils.toStringIfNotNull(banner));
        return this;
    }

    /**
     * Clears a value of the banner option (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #banner(CharSequence)
     */
    public LessOptionsBuilder bannerOff() {
        return banner(null);
    }

    /**
     * Sets global variables (default: {@code []}).
     * @param globalVariables the global variables ({@code null} is treated as an empty collection).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #globalVariables(LessVariableOption...)
     * @see #globalVariablesOff()
     * @see LessVariableOptionsBuilder
     */
    public LessOptionsBuilder globalVariables(final Collection<? extends LessVariableOption> globalVariables) {
        if (globalVariables != null) {
            getOptions().setGlobalVariables(new ArrayList<LessVariableOption>(globalVariables));
        } else {
            getOptions().setGlobalVariables(null);
        }
        return this;
    }

    /**
     * Sets global variables (default: {@code []}).
     * @param globalVariables the global variables.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #globalVariables(Collection)
     * @see #globalVariablesOff()
     */
    public LessOptionsBuilder globalVariables(final LessVariableOption... globalVariables) {
        if (globalVariables != null) {
            return globalVariables(Arrays.asList(globalVariables));
        } else {
            return globalVariables((Collection<LessVariableOption>) null);
        }
    }

    /**
     * Clears global variables (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #globalVariables(Collection)
     * @see #globalVariables(LessVariableOption...)
     */
    public LessOptionsBuilder globalVariablesOff() {
        return globalVariables((Collection<LessVariableOption>) null);
    }

    /**
     * Sets modify variables (default: {@code []}).
     * @param modifyVariables the modify variables ({@code null} is treated as an empty collection).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #modifyVariables(LessVariableOption...)
     * @see #modifyVariablesOff()
     * @see LessVariableOptionsBuilder
     */
    public LessOptionsBuilder modifyVariables(final Collection<? extends LessVariableOption> modifyVariables) {
        if (modifyVariables != null) {
            getOptions().setModifyVariables(new ArrayList<LessVariableOption>(modifyVariables));
        } else {
            getOptions().setModifyVariables(null);
        }
        return this;
    }

    /**
     * Sets modify variables (default: {@code []}).
     * @param modifyVariables the modify variables.
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #modifyVariables(Collection)
     * @see #modifyVariablesOff()
     */
    public LessOptionsBuilder modifyVariables(final LessVariableOption... modifyVariables) {
        if (modifyVariables != null) {
            return modifyVariables(Arrays.asList(modifyVariables));
        } else {
            return modifyVariables((Collection<LessVariableOption>) null);
        }
    }

    /**
     * Clears modify variables (default: {@code off}).
     * @return {@code this} builder.
     * @since 2.0.0
     * @see #modifyVariables(Collection)
     * @see #modifyVariables(LessVariableOption...)
     */
    public LessOptionsBuilder modifyVariablesOff() {
        return modifyVariables((Collection<LessVariableOption>) null);
    }

    /**
     * Builds a new instance of the {@link LessOptions}.
     * @return the new instance of the {@link LessOptions}.
     * @since 2.0.0
     */
    public LessOptions build() {
        return new LessOptions(getOptions());
    }

    /**
     * Returns an internal state of the builder.
     * @return the internal state.
     * @since 2.0.0
     */
    protected LessOptions getOptions() {
        return options;
    }
}
