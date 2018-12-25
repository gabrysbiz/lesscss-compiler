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

/**
 * Responsible for performing operations on paths pointing to files located on the file system. The files system is
 * responsible for:
 * <ul>
 * <li>{@link #normalize(String) normalizing file paths} - converting relative paths to absolute and canonical
 * paths</li>
 * <li>{@link #expandRedirection(String) expanding file paths redirections} - resolving direct normalized file
 * paths</li>
 * <li>{@link #exists(String) checking if files specified by paths exist}</li>
 * <li>{@link #fetch(String) fetching file data} - reading data of the files stored in the file system</li>
 * </ul>
 * Methods are always called in order:
 * <ol>
 * <li>{@link #isSupported(String)}</li>
 * <li>{@link #normalize(String)}</li>
 * <li>{@link #expandRedirection(String)}</li>
 * <li>{@link #exists(String)}</li>
 * <li>{@link #fetch(String)}</li>
 * </ol>
 * @since 2.0.0
 */
public interface FileSystem {

    /**
     * Tests whether a file path is supported. This method should return {@code true} only when this file system allows
     * to do all operations:
     * <ul>
     * <li>{@link #normalize(String) normalize file paths}</li>
     * <li>{@link #expandRedirection(String) expand file paths redirections}</li>
     * <li>{@link #exists(String) checking if files specified by paths exist}</li>
     * <li>{@link #fetch(String) fetch files data}</li>
     * </ul>
     * @param path the path to verify (never {@code null}).
     * @return {@code true} whether the file path is supported, otherwise {@code false}.
     * @since 2.0.0
     */
    boolean isSupported(String path);

    /**
     * Normalize a path. A normalized path is an absolute path where at least:
     * <ol>
     * <li>all "." segments are removed</li>
     * <li>if ".." segment is preceded by a non-".." segment then both of these segments are removed.</li>
     * </ol>
     * Examples:
     * <ul>
     * <li>C:\Users\less\style.less</li>
     * <li>/home/user/less/style.less</li>
     * <li>http://www.example.org/style.less</li>
     * </ul>
     * @param path the path to normalize (never {@code null}).
     * @return the normalized path (never {@code null}).
     * @throws Exception if any error occurs.
     * @since 2.0.0
     * @see #isSupported(String)
     */
    String normalize(String path) throws Exception;

    /**
     * Returns a direct and {@link #normalize(String) normalized} path to a resource. Example: if a path is equal to
     * "http://example.org/style.less", but this URL forwards to "/static/style.less", then the method will return
     * "http://example.org/static/style.less".
     * @param path the normalized file path (never {@code null}).
     * @return the direct normalized path to the resource (never {@code null}). If the path is already a direct path, it
     *         should return that path.
     * @throws Exception if any error occurs.
     * @since 2.0.0
     * @see #normalize(String)
     */
    String expandRedirection(String path) throws Exception;

    /**
     * Tests whether a direct normalized file path point to an exiting file.
     * @param path the direct normalized file path (never {@code null}).
     * @return {@code true} whether the direct file path point to an exiting file, otherwise {@code false}.
     * @throws Exception if any error occurs.
     * @since 2.0.0
     * @see #expandRedirection(String)
     */
    boolean exists(String path) throws Exception;

    /**
     * Returns a data of the file specified by a path.
     * @param path the direct normalized file path (never {@code null}).
     * @return the file data (never {@code null}).
     * @throws Exception if any error occurs.
     * @since 2.0.0
     * @see #exists(String)
     */
    FileData fetch(String path) throws Exception;
}
