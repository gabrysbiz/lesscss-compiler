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
import java.util.List;

/**
 * Responsible for creating new instances of the {@link CompilerOptions} which are compatible with version
 * <a href="https://github.com/less/less.js/releases/tag/v1.7.5">1.7.5</a>.
 * @since 1.0
 */
public class CompilerOptionsBuilder {

    private final OperatingSystemChecker systemChecker;

    private boolean ieCompatability = true;
    private List<String> includePaths = new ArrayList<String>();
    private boolean minified;
    private boolean relativeUrls;
    private String rootPath = "";
    private boolean strictImports;
    private boolean strictMath;
    private boolean strictUnits;
    private String urlsArgument = "";

    /**
     * Constructs a new instance.
     * @since 1.0
     */
    public CompilerOptionsBuilder() {
        systemChecker = new OperatingSystemCheckerImpl();
    }

    /**
     * Constructs a new instance.
     * @param systemChecker the operating system software checker.
     * @since 1.0
     */
    public CompilerOptionsBuilder(final OperatingSystemChecker systemChecker) {
        this.systemChecker = systemChecker;
    }

    /**
     * Checks whether the <a href="http://www.w3.org/Style/CSS/">CSS</a> code should be compatible with IE browser
     * (default: {@code true}).
     * @return {@code true} whether the <a href="http://www.w3.org/Style/CSS/">CSS</a> code should be compatible with IE
     *         browser, otherwise {@code false}.
     * @since 1.0
     */
    public boolean isIeCompatability() {
        return ieCompatability;
    }

    /**
     * Sets whether the <a href="http://www.w3.org/Style/CSS/">CSS</a> code should be compatible with IE browser.
     * @param ieCompatability {@code true} whether the <a href="http://www.w3.org/Style/CSS/">CSS</a> code should be
     *            compatible with IE browser, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setIeCompatability(final boolean ieCompatability) {
        this.ieCompatability = ieCompatability;
        return this;
    }

    /**
     * Returns available include paths (default: empty list). Returned list is a copy - changes in the structure do not
     * change internal state.
     * @return available include paths.
     * @since 1.0
     */
    public List<String> getIncludePaths() {
        return new ArrayList<String>(includePaths);
    }

    /**
     * Sets available include paths.
     * @param includePaths the available include paths.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setIncludePaths(final List<String> includePaths) {
        this.includePaths = new ArrayList<String>(includePaths);
        return this;
    }

    /**
     * Checks whether the compiler will minify the <a href="http://www.w3.org/Style/CSS/">CSS</a> code (default:
     * {@code false}).
     * @return {@code true} whether the compiler should minify the <a href="http://www.w3.org/Style/CSS/">CSS</a> code,
     *         otherwise {@code false}.
     * @since 1.0
     */
    public boolean isMinified() {
        return minified;
    }

    /**
     * Sets whether the <a href="http://www.w3.org/Style/CSS/">CSS</a> code should be minified.
     * @param minified {@code true} whether the <a href="http://www.w3.org/Style/CSS/">CSS</a> code should be minified,
     *            otherwise {@code false}.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setMinified(final boolean minified) {
        this.minified = minified;
        return this;
    }

    /**
     * Checks whether the compiler will rewrite relative URLs (default: {@code false}).
     * @return {@code true} whether the compiler will rewrite relative URLs, otherwise {@code false}.
     * @since 1.0
     */
    public boolean isRelativeUrls() {
        return relativeUrls;
    }

    /**
     * Sets whether the compiler should rewrite relative URLs.
     * @param relativeUrls {@code true} whether the compiler should rewrite relative URLs, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setRelativeUrls(final boolean relativeUrls) {
        this.relativeUrls = relativeUrls;
        return this;
    }

    /**
     * Returns a path which will be added to every generated import and URL in the
     * <a href="http://www.w3.org/Style/CSS/">CSS</a> code (default: {@code ""}).
     * @return the path which will be added to every generated import and URL in the
     *         <a href="http://www.w3.org/Style/CSS/">CSS</a> code.
     * @since 1.0
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * Sets a path which will be added to every generated import and URL in the
     * <a href="http://www.w3.org/Style/CSS/">CSS</a> code.
     * @param rootPath the path which will be added.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setRootPath(final String rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    /**
     * Checks whether the compiler will disallow an &#64;import inside of either &#64;media blocks or other selector
     * blocks (default: {@code false}).
     * @return {@code true} whether the compiler will disallow an &#64;import inside of either &#64;media blocks or
     *         other selector blocks, otherwise {@code false}.
     * @since 1.0
     */
    public boolean isStrictImports() {
        return strictImports;
    }

    /**
     * Sets whether the compiler should disallow an &#64;import inside of either &#64;media blocks or other selector
     * blocks.
     * @param strictImports {@code true} whether the compiler should disallow an &#64;import inside of either &#64;media
     *            blocks or other selector blocks, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setStrictImports(final boolean strictImports) {
        this.strictImports = strictImports;
        return this;
    }

    /**
     * Checks whether the compiler will try and process all maths in the <a href="http://www.w3.org/Style/CSS/">CSS</a>
     * code (default: {@code false}).
     * @return {@code true} whether the compiler will try and process all maths in the
     *         <a href="http://www.w3.org/Style/CSS/">CSS</a> code, otherwise {@code false}.
     * @since 1.0
     */
    public boolean isStrictMath() {
        return strictMath;
    }

    /**
     * Sets whether the compiler should try and process all maths in the <a href="http://www.w3.org/Style/CSS/">CSS</a>
     * code.
     * @param strictMath {@code true} whether the compiler should try and process all maths in the
     *            <a href="http://www.w3.org/Style/CSS/">CSS</a> code, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setStrictMath(final boolean strictMath) {
        this.strictMath = strictMath;
        return this;
    }

    /**
     * Checks whether the compiler will guess units in the <a href="http://www.w3.org/Style/CSS/">CSS</a> code when it
     * does maths (default: {@code false}).
     * @return {@code true} whether the compiler will guess units in the <a href="http://www.w3.org/Style/CSS/">CSS</a>
     *         code when it does maths, otherwise {@code false}.
     * @since 1.0
     */
    public boolean isStrictUnits() {
        return strictUnits;
    }

    /**
     * Sets whether the compiler should guess units in the <a href="http://www.w3.org/Style/CSS/">CSS</a> code when it
     * does maths.
     * @param strictUnits {@code true} whether the compiler should guess units in the
     *            <a href="http://www.w3.org/Style/CSS/">CSS</a> code when it does maths, otherwise {@code false}.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setStrictUnits(final boolean strictUnits) {
        this.strictUnits = strictUnits;
        return this;
    }

    /**
     * Returns an argument which will be added to every URL (default: {@code ""}).
     * @return the argument which will be added to every URL.
     * @since 1.0
     */
    public String getUrlsArgument() {
        return urlsArgument;
    }

    /**
     * Sets an argument which will be added to every URL.
     * @param urlsArgument the argument which will be added.
     * @return {@code this} builder.
     * @since 1.0
     */
    public CompilerOptionsBuilder setUrlsArgument(final String urlsArgument) {
        this.urlsArgument = urlsArgument;
        return this;
    }

    /**
     * Creates a new instance of the {@link CompilerOptions}.
     * @return the new instance of the {@link CompilerOptions}.
     * @since 1.0
     */
    public CompilerOptions create() {
        final List<Object> arguments = new ArrayList<Object>();
        if (!ieCompatability) {
            arguments.add("--no-ie-compat");
        }
        final String paths = getIncludePathsArgumentValue();
        if (StringUtils.isNotBlank(paths)) {
            arguments.add("--include-path=" + paths);
        }
        if (minified) {
            arguments.add("-x");
        }
        if (relativeUrls) {
            arguments.add("--relative-urls");
        }
        if (StringUtils.isNotBlank(rootPath)) {
            arguments.add("--rootpath=" + rootPath.trim());
        }
        if (strictImports) {
            arguments.add("--strict-imports");
        }
        arguments.add("--strict-math=" + (strictMath ? "on" : "off"));
        arguments.add("--strict-units=" + (strictUnits ? "on" : "off"));
        if (StringUtils.isNotBlank(urlsArgument)) {
            arguments.add("--url-args=\"" + urlsArgument.trim().replace("\"", "\\\"") + '"');
        }
        return new CompilerOptions(arguments);
    }

    private String getIncludePathsArgumentValue() {
        if (includePaths == null || includePaths.isEmpty()) {
            return null;
        }
        final StringBuilder paths = new StringBuilder();
        for (final String path : includePaths) {
            if (StringUtils.isNotBlank(path)) {
                final boolean separatorRequired = paths.length() == 0;
                if (separatorRequired) {
                    paths.append(systemChecker.isWindows() ? ';' : ':');
                }
                paths.append(path.trim());
            }
        }
        return paths.toString();
    }
}
