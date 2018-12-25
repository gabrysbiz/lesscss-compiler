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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import biz.gabrys.lesscss.compiler2.filesystem.ClassPathFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.FtpFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.HttpFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.LocalFileSystem;

/**
 * <p>
 * Allows to create value of the {@link LessOptions#getFileSystems() file systems} option.
 * </p>
 * <p>
 * Example code:
 * </p>
 * 
 * <pre>
 * List&lt;String&gt; fileSystems;
 * // create a list with standard file systems
 * fileSystems = new {@link #FileSystemsOptionBuilder() FileSystemsOptionBuilder}().{@link #withHttp() withHttp}().{@link #withLocal() withLocal}().{@link #build() build}();
 * 
 * // create a list with standard file systems (short version)
 * fileSystems = new {@link #FileSystemsOptionBuilder() FileSystemsOptionBuilder}().{@link #withStandard() withStandard}().{@link #build() build}();
 * 
 * // create a list with custom file system
 * class CustomFileSystem implements {@link FileSystem} {
 *      // omitted implementation
 * }
 * 
 * fileSystems = new {@link #FileSystemsOptionBuilder() FileSystemsOptionBuilder}().{@link #withCustom(Class) withCustom}(CustomFileSystem.class).{@link #build() build}();
 * </pre>
 * <p>
 * <strong>Warning</strong>: the {@link #withLocal() local file system} should be appended as last or not at all.
 * </p>
 * @since 2.0.0
 * @see FileSystem
 * @see LessOptions
 */
public class FileSystemsOptionBuilder {

    private final Set<String> fileSystems = new LinkedHashSet<String>();

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public FileSystemsOptionBuilder() {
        // do nothing
    }

    /**
     * Appends standard file systems at the end. If a file system was added before, then it will be removed and added
     * again. Appended file systems:
     * <ol>
     * <li>{@link HttpFileSystem}</li>
     * <li>{@link FtpFileSystem} (only if <a href="https://commons.apache.org/proper/commons-net/">Apache Commons
     * Net</a> library in the class path)</li>
     * <li>{@link LocalFileSystem}</li>
     * </ol>
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemsOptionBuilder withStandard() {
        if (FtpFileSystem.isAvailable()) {
            return withHttp().withFtp().withLocal();
        }
        return withHttp().withLocal();
    }

    /**
     * Appends the {@link LocalFileSystem} at the end. If the file system was added before, then it will be removed and
     * added again.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemsOptionBuilder withLocal() {
        appendFileSystem(LocalFileSystem.class);
        return this;
    }

    /**
     * Appends the {@link HttpFileSystem} at the end. If the file system was added before, then it will be removed and
     * added again.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemsOptionBuilder withHttp() {
        appendFileSystem(HttpFileSystem.class);
        return this;
    }

    /**
     * <p>
     * Appends the {@link FtpFileSystem} at the end. If file system was added before, then it will be removed and added
     * again.
     * </p>
     * <p>
     * <strong>Warning</strong>: requires <a href="https://commons.apache.org/proper/commons-net/">Apache Commons
     * Net</a> library in the classpath.
     * </p>
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemsOptionBuilder withFtp() {
        appendFileSystem(FtpFileSystem.class);
        return this;
    }

    /**
     * Appends the {@link ClassPathFileSystem} at the end. If file system was added before, then it will be removed and
     * added again.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemsOptionBuilder withClassPath() {
        appendFileSystem(ClassPathFileSystem.class);
        return this;
    }

    /**
     * Appends a {@link FileSystem custom file system implementation} at the end. If file system was added before, then
     * it will be removed and added again.
     * @param clazz the custom file system implementation class.
     * @return {@code this} builder.
     * @throws IllegalArgumentException is clazz is equal to {@code null}.
     * @since 2.0.0
     */
    public FileSystemsOptionBuilder withCustom(final Class<? extends FileSystem> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Clazz cannot be null");
        }
        appendFileSystem(clazz);
        return this;
    }

    private void appendFileSystem(final Class<? extends FileSystem> clazz) {
        fileSystems.remove(clazz.getName());
        fileSystems.add(clazz.getName());
    }

    /**
     * <p>
     * Builds a list with {@link FileSystem file systems} class names.
     * </p>
     * <p>
     * <strong>Warning</strong>: the {@link #withLocal() local file system} should be appended as last or not at all.
     * </p>
     * @return the list with class names.
     * @since 2.0.0
     */
    public List<String> build() {
        return new ArrayList<String>(fileSystems);
    }
}
