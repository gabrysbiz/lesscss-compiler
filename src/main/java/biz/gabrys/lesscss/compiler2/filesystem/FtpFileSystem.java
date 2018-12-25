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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * <p>
 * Represents a file system accessible via <a href="https://www.w3.org/Protocols/rfc959/">FTP</a> protocol.
 * </p>
 * <p>
 * Example paths:
 * </p>
 * <ul>
 * <li>ftp://example.org/style.less</li>
 * <li>ftp://example.org/styles/main.less</li>
 * </ul>
 * <p>
 * <strong>Warning</strong>: requires <a href="https://commons.apache.org/proper/commons-net/">Apache Commons Net</a>
 * library in the class path.
 * </p>
 * @since 2.0.0
 */
public class FtpFileSystem implements FileSystem {

    /**
     * Constructs a new instance.
     * @throws IllegalStateException if <a href="https://commons.apache.org/proper/commons-net/">Apache Commons Net</a>
     *             library is not available in the classpath.
     * @since 2.0.0
     */
    public FtpFileSystem() {
        if (!isAvailable()) {
            throw new IllegalStateException("Apache Commons Net library is required to work with FTP file system");
        }
    }

    /**
     * Tests whether the file system is available (all requirements are met).
     * @return {@code true} whether the FTP file system is available, otherwise {@code false}.
     * @since 2.0.0
     */
    public static boolean isAvailable() {
        try {
            Class.forName("org.apache.commons.net.ftp.FTP");
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void configure(final Map<String, String> parameters) {
        // do nothing
    }

    @Override
    public boolean isSupported(final String path) {
        return path.startsWith("ftp://");
    }

    @Override
    public String normalize(final String path) throws URISyntaxException {
        return new URI(path).normalize().toString();
    }

    @Override
    public String expandRedirection(final String path) {
        return path;
    }

    @Override
    public boolean exists(final String path) throws IOException {
        final URL url = new URL(path);
        FTPClient connection = null;
        try {
            connection = makeConnection(url);
            return isFileExist(connection, url);
        } catch (final IOException e) {
            throw new IOException(String.format("cannot access source \"%s\" file", url), e);
        } finally {
            disconnect(connection);
        }
    }

    boolean isFileExist(final FTPClient connection, final URL url) throws IOException {
        try (final InputStream inputStream = connection.retrieveFileStream(url.getPath())) {
            return inputStream != null && connection.getReplyCode() != FTPReply.FILE_UNAVAILABLE;
        }
    }

    @Override
    public FileData fetch(final String path) throws IOException {
        final URL url = new URL(path);
        FTPClient connection = null;
        try {
            connection = makeConnection(url);
            return new FileData(fetchContent(connection, url));
        } catch (final IOException e) {
            throw new IOException(String.format("cannot download source \"%s\" file", url), e);
        } finally {
            disconnect(connection);
        }
    }

    FTPClient makeConnection(final URL url) throws IOException {
        final FTPClient connection = createFtpClient();
        connection.setAutodetectUTF8(true);
        final int port = url.getPort() != -1 ? url.getPort() : FTP.DEFAULT_PORT;
        connection.connect(url.getHost(), port);
        connection.enterLocalActiveMode();
        if (!connection.login("anonymous", "anonymous")) {
            throw new IOException(String.format("cannot login as anonymous user, reason: %s", connection.getReplyString()));
        }
        connection.enterLocalPassiveMode();
        connection.setFileType(FTP.BINARY_FILE_TYPE);
        return connection;
    }

    FTPClient createFtpClient() {
        return new FTPClient();
    }

    byte[] fetchContent(final FTPClient connection, final URL url) throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            if (!connection.retrieveFile(url.getPath(), outputStream)) {
                throw new IOException(String.format("cannot retrieve file, reason: %s", connection.getReplyString()));
            }
            return outputStream.toByteArray();
        }
    }

    void disconnect(final FTPClient connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.disconnect();
        } catch (final Exception e) {
            // do nothing
        }
    }
}
