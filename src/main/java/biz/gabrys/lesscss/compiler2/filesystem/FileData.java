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
package biz.gabrys.lesscss.compiler2.filesystem;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a file data returned by an instance of the {@link FileSystem}.
 * @since 2.0.0
 */
public class FileData implements Serializable {

    private static final long serialVersionUID = -9078800439439146580L;

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

    /**
     * {@inheritDoc}
     * @since 2.0.0
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = prime + Arrays.hashCode(content);
        return prime * result + (encoding == null ? 0 : encoding.hashCode());
    }

    /**
     * {@inheritDoc}
     * @since 2.0.0
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FileData other = (FileData) obj;
        if (!Arrays.equals(content, other.content)) {
            return false;
        }
        return Objects.equals(encoding, other.encoding);
    }

    /**
     * Returns a string representation of this object in JSON format.
     * @since 2.0.0
     */
    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder();
        text.append("{\n");
        text.append("  \"encoding\": ");
        if (encoding == null) {
            text.append("null");
        } else {
            text.append('"');
            text.append(encoding);
            text.append('"');
        }
        text.append(",\n  \"content\": ");
        text.append(Arrays.toString(content));
        text.append("\n}");
        return text.toString();
    }
}
