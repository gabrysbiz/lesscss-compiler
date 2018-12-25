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
package biz.gabrys.lesscss.compiler2.filesystem;

/**
 * Represents a file data returned by an instance of the {@link FileSystem}.
 * @since 2.0.0
 */
public class FileData {

    private final byte[] content;
    private final String encoding;

    /**
     * Constructs a new instance and sets the file's content.
     * @param content the file's content (cannot be {@code null}).
     * @throws IllegalArgumentException if the content is {@code null}.
     * @since 2.0.0
     */
    public FileData(final byte[] content) {
        this(content, null);
    }

    /**
     * Constructs a new instance and sets the file's content and encoding.
     * @param content the file's content (cannot be {@code null}).
     * @param encoding the file's encoding.
     * @throws IllegalArgumentException if the content is {@code null}.
     * @since 2.0.0
     */
    public FileData(final byte[] content, final String encoding) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        this.content = content;
        this.encoding = encoding;
    }

    /**
     * Returns the file's content as a byte array.
     * @return the file's content.
     * @since 2.0.0
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Returns the file's data encoding. {@code null} means that a compiler will use encoding defined by a user.
     * @return the file's data encoding or {@code null}.
     * @since 2.0.0
     */
    public String getEncoding() {
        return encoding;
    }
}
