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
import java.util.Map;
import java.util.Set;

import biz.gabrys.lesscss.compiler2.filesystem.ClassPathFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.FtpFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.HttpFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.LocalFileSystem;

/**
 * <p>
 * Responsible for creating value of the {@link LessOptions#getFileSystems() file system} options.
 * </p>
 * <p>
 * Example code:
 * </p>
 * 
 * <pre>
 * List&lt;{@link FileSystemOption}&gt; fileSystems;
 * // create a list with standard file systems
 * fileSystems = new {@link #FileSystemOptionsBuilder() FileSystemOptionsBuilder}().{@link #appendHttp() appendHttp}().{@link #appendLocal() appendLocal}().{@link #build() build}();
 * 
 * // create a list with standard file systems (short version)
 * fileSystems = new {@link #FileSystemOptionsBuilder() FileSystemOptionsBuilder}().{@link #appendStandard() appendStandard}().{@link #build() build}();
 * 
 * // create a list with  file system
 * class CustomFileSystem implements {@link FileSystem} {
 *      // omitted implementation
 * }
 * 
 * fileSystems = new {@link #FileSystemOptionsBuilder() FileSystemOptionsBuilder}().{@link #appendCustom(Class) appendCustom}(CustomFileSystem.class).{@link #build() build}();
 * </pre>
 * <p>
 * <strong>Warning</strong>: the {@link #appendLocal() local file system} should be appended as last or not at all.
 * </p>
 * @since 2.0.0
 * @see FileSystemOptionBuilder
 * @see LessOptions
 */
public class FileSystemOptionsBuilder {

    private final Set<FileSystemOption> options = new LinkedHashSet<>();

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder() {
        // do nothing
    }

    /**
     * Appends standard file systems with no parameters at the end. If any file system (with no parameters) was appended
     * before, then it will be removed and appended again. Appended file systems:
     * <ol>
     * <li>{@link HttpFileSystem}</li>
     * <li>{@link FtpFileSystem} (only if <a href="https://commons.apache.org/proper/commons-net/">Apache Commons
     * Net</a> library in the class path)</li>
     * <li>{@link LocalFileSystem}</li>
     * </ol>
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendStandard() {
        if (FtpFileSystem.isAvailable()) {
            return appendHttp().appendFtp().appendLocal();
        }
        return appendHttp().appendLocal();
    }

    /**
     * Appends the {@link LocalFileSystem} with no parameters at the end. If the file system (with no parameters) was
     * appended before, then it will be removed and appended again.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendLocal() {
        appendFileSystem(new FileSystemOption(LocalFileSystem.class));
        return this;
    }

    /**
     * Appends the {@link HttpFileSystem} with no parameters at the end. If the file system (with no parameters) was
     * appended before, then it will be removed and appended again.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendHttp() {
        appendFileSystem(new FileSystemOption(HttpFileSystem.class));
        return this;
    }

    /**
     * <p>
     * Appends the {@link FtpFileSystem} with no parameters at the end. If the file system (with no parameters) was
     * appended before, then it will be removed and appended again.
     * </p>
     * <p>
     * <strong>Warning</strong>: requires <a href="https://commons.apache.org/proper/commons-net/">Apache Commons
     * Net</a> library in the class path.
     * </p>
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendFtp() {
        appendFileSystem(new FileSystemOption(FtpFileSystem.class));
        return this;
    }

    /**
     * Appends the {@link ClassPathFileSystem} with no parameters at the end. If the file system (with no parameters)
     * was appended before, then it will be removed and appended again.
     * @return {@code this} builder.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendClassPath() {
        appendFileSystem(new FileSystemOption(ClassPathFileSystem.class));
        return this;
    }

    /**
     * Appends file systems specified by classes with no parameters at the end. If any file system (with no parameters)
     * was appended before, then it will be removed and appended again.
     * @param classes the file system classes.
     * @return {@code this} builder.
     * @throws IllegalArgumentException is the file system classes are equal to {@code null}.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendCustom(@SuppressWarnings("unchecked") final Class<? extends FileSystem>... classes) {
        if (classes == null) {
            throw new IllegalArgumentException("File system classes cannot be null");
        }
        for (final Class<? extends FileSystem> clazz : classes) {
            if (clazz != null) {
                appendFileSystem(new FileSystemOption(clazz));
            }
        }
        return this;
    }

    /**
     * Appends a file system specified by class with no parameters at the end. If the file system (with no parameters)
     * was appended before, then it will be removed and appended again.
     * @param clazz the file system class.
     * @return {@code this} builder.
     * @throws IllegalArgumentException is the file system class is equal to {@code null}.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendCustom(final Class<? extends FileSystem> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("File system class cannot be null");
        }
        appendFileSystem(new FileSystemOption(clazz));
        return this;
    }

    /**
     * Appends a file system specified by class with parameters at the end. If the file system with the same parameters
     * was appended before, then it will be removed and appended again.
     * @param clazz the file system class.
     * @param parameters the configuration parameters.
     * @return {@code this} builder.
     * @throws IllegalArgumentException is the file system class or parameters are equal to {@code null}.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendCustom(final Class<? extends FileSystem> clazz, final Map<String, String> parameters) {
        if (clazz == null) {
            throw new IllegalArgumentException("File system class cannot be null");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        appendFileSystem(new FileSystemOption(clazz, parameters));
        return this;
    }

    /**
     * Appends file system options at the end. If any file system option was appended before, then it will be removed
     * and appended again.
     * @param options the file system options.
     * @return {@code this} builder.
     * @throws IllegalArgumentException is the options is equal to {@code null}.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendCustom(final FileSystemOption... options) {
        if (options == null) {
            throw new IllegalArgumentException("File system options cannot be null");
        }
        for (final FileSystemOption option : options) {
            if (option != null) {
                appendFileSystem(option);
            }
        }
        return this;
    }

    /**
     * Appends file system options at the end. If any file system option was appended before, then it will be removed
     * and appended again.
     * @param options the file system options.
     * @return {@code this} builder.
     * @throws IllegalArgumentException is the options is equal to {@code null}.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendCustom(final Iterable<? extends FileSystemOption> options) {
        if (options == null) {
            throw new IllegalArgumentException("File system options cannot be null");
        }
        for (final FileSystemOption option : options) {
            if (option != null) {
                appendFileSystem(option);
            }
        }
        return this;
    }

    /**
     * Appends a file system option at the end. If the file system option was appended before, then it will be removed
     * and appended again.
     * @param option the file system option.
     * @return {@code this} builder.
     * @throws IllegalArgumentException is the fileSystemOption is equal to {@code null}.
     * @since 2.0.0
     */
    public FileSystemOptionsBuilder appendCustom(final FileSystemOption option) {
        if (option == null) {
            throw new IllegalArgumentException("File system option cannot be null");
        }
        appendFileSystem(option);
        return this;
    }

    private void appendFileSystem(final FileSystemOption option) {
        options.remove(option);
        options.add(option);
    }

    /**
     * <p>
     * Builds a list with file system options.
     * </p>
     * <p>
     * <strong>Warning</strong>: the {@link #appendLocal() local file system} should be appended as last or not at all.
     * </p>
     * @return the list with class names.
     * @since 2.0.0
     */
    public List<FileSystemOption> build() {
        return new ArrayList<>(options);
    }
}
