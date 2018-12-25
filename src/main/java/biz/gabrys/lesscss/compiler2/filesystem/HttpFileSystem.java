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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import biz.gabrys.lesscss.compiler2.io.IOUtils;

/**
 * <p>
 * Represents a file system accessible via <a href="https://www.w3.org/Protocols/">HTTP</a> and HTTPS protocols. The
 * file system handles HTTP codes:
 * </p>
 * <ul>
 * <li>200 OK</li>
 * <li>301 Moved Permanently</li>
 * <li>302 Temporary Redirect</li>
 * <li>303 See Other</li>
 * <li>404 Not Found</li>
 * </ul>
 * <p>
 * Example paths:
 * </p>
 * <ul>
 * <li>http://example.org/style.less</li>
 * <li>https://example.org/styles/main.less</li>
 * </ul>
 * @since 2.0.0
 */
public class HttpFileSystem implements FileSystem {

    /**
     * Stores HTTP {@code OK} (200) and {@code NOT FOUND} (404) codes.
     * @since 2.0.0
     * @see HttpURLConnection#HTTP_OK
     * @see HttpURLConnection#HTTP_NOT_FOUND
     */
    protected static final Collection<Integer> OK_NOTFOUND_CODES = Collections
            .unmodifiableCollection(Arrays.asList(HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_NOT_FOUND));

    /**
     * Stores HTTP {@code MOVED PERMANENTLY} (301), {@code MOVED TEMPORARILY} (302), and {@code SEE OTHER} (303) codes.
     * @since 2.0.0
     * @see HttpURLConnection#HTTP_MOVED_PERM
     * @see HttpURLConnection#HTTP_MOVED_TEMP
     * @see HttpURLConnection#HTTP_SEE_OTHER
     */
    protected static final Collection<Integer> REDIRECT_CODES = Collections.unmodifiableCollection(
            Arrays.asList(HttpURLConnection.HTTP_MOVED_PERM, HttpURLConnection.HTTP_MOVED_TEMP, HttpURLConnection.HTTP_SEE_OTHER));

    /**
     * Stores HTTP {@code OK} (200), {@code NOT FOUND} (404), {@code MOVED PERMANENTLY} (301), {@code MOVED TEMPORARILY}
     * (302), and {@code SEE OTHER} (303) codes.
     * @since 2.0.0
     * @see HttpURLConnection#HTTP_OK
     * @see HttpURLConnection#HTTP_NOT_FOUND
     * @see HttpURLConnection#HTTP_MOVED_PERM
     * @see HttpURLConnection#HTTP_MOVED_TEMP
     * @see HttpURLConnection#HTTP_SEE_OTHER
     */
    protected static final Collection<Integer> OK_NOTFOUND_REDIRECT_CODES;

    static {
        final Collection<Integer> codes = new ArrayList<>(5);
        codes.addAll(OK_NOTFOUND_CODES);
        codes.addAll(REDIRECT_CODES);
        OK_NOTFOUND_REDIRECT_CODES = Collections.unmodifiableCollection(codes);
    }

    private static final Pattern CHARSET_PATTERN = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public HttpFileSystem() {
        // do nothing
    }

    @Override
    public void configure(final Map<String, String> parameters) {
        // do nothing
    }

    @Override
    public boolean isSupported(final String path) {
        return path.startsWith("http://") || path.startsWith("https://");
    }

    @Override
    public String normalize(final String path) throws URISyntaxException {
        return new URI(path).normalize().toString();
    }

    @Override
    public String expandRedirection(final String path) throws IOException, URISyntaxException {
        HttpURLConnection connection = null;
        String redirectedPath;
        try {
            connection = makeConnection(new URL(path), false);
            final int responseCode = connection.getResponseCode();
            validateResponseCode(responseCode, OK_NOTFOUND_REDIRECT_CODES);
            if (OK_NOTFOUND_CODES.contains(responseCode)) {
                return path;
            }
            final String location = connection.getHeaderField("Location");
            redirectedPath = connection.getURL().toURI().resolve(location).toString();
        } finally {
            disconnect(connection);
        }
        return expandRedirection(redirectedPath);
    }

    @Override
    public boolean exists(final String path) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = makeConnection(new URL(path), false);
            final int responseCode = connection.getResponseCode();
            validateResponseCode(responseCode, OK_NOTFOUND_CODES);
            return responseCode == HttpURLConnection.HTTP_OK;
        } finally {
            disconnect(connection);
        }
    }

    @Override
    public FileData fetch(final String path) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = makeConnection(new URL(path), true);
            validateResponseCode(connection.getResponseCode(), Arrays.asList(HttpURLConnection.HTTP_OK));
            final String responseEncoding = readEncoding(connection);
            try {
                return new FileData(IOUtils.toByteArray(connection.getInputStream()), responseEncoding);
            } catch (final IOException e) {
                throw new IOException("cannot download file", e);
            }
        } finally {
            disconnect(connection);
        }
    }

    /**
     * Opens and configures a new {@link HttpURLConnection} instance that represents a connection to the remote object
     * referred to by the URL.
     * @param url the URL (cannot be {@code null}).
     * @param fetchResponseBody whether the connection should fetch response body ({@code GET} request) or not
     *            ({@code HEAD} request).
     * @return the new configured {@link HttpURLConnection} instance.
     * @throws IOException if an I/O exception occurs.
     * @since 2.0.0
     */
    protected HttpURLConnection makeConnection(final URL url, final boolean fetchResponseBody) throws IOException {
        final HttpURLConnection connection = openConnection(url);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(fetchResponseBody ? "GET" : "HEAD");
        return connection;
    }

    /**
     * Creates a new {@link HttpURLConnection} instance that represents a connection to the remote object referred to by
     * the URL. The reason why this method has been extracted is allow stubbing and mocking connection objects in tests.
     * @param url the URL (cannot be {@code null}).
     * @return the new {@link HttpURLConnection} instance.
     * @throws IOException if an I/O exception occurs.
     * @since 2.0.0
     */
    protected HttpURLConnection openConnection(final URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    /**
     * Validates if a response code is in an allowed set.
     * @param responseCode the tested response code.
     * @param validCodes the collection with allowed response codes (cannot be {@code null}).
     * @throws IOException if the response code is not in the allowed set.
     * @since 2.0.0
     */
    protected void validateResponseCode(final int responseCode, final Collection<Integer> validCodes) throws IOException {
        if (!validCodes.contains(responseCode)) {
            final List<Integer> supportedCodes = new ArrayList<>(validCodes);
            Collections.sort(supportedCodes);
            throw new IOException(String.format("response HTTP status code %s is not allowed (supports only: %s)", responseCode,
                    supportedCodes.toString().replaceAll("\\[|\\]", "")));
        }
    }

    /**
     * Returns a connection encoding or {@code null} if the {@code content-type} header has incorrect format.
     * @param connection the connection (cannot be {@code null}).
     * @return the connection encoding or {@code null} if the {@code content-type} header has incorrect format.
     * @since 2.0.0
     */
    protected String readEncoding(final HttpURLConnection connection) {
        final String contentType = connection.getContentType();
        if (contentType != null) {
            final Matcher matcher = CHARSET_PATTERN.matcher(contentType);
            if (matcher.find()) {
                return matcher.group(1).trim().toUpperCase(Locale.ROOT);
            }
        }
        return null;
    }

    /**
     * Disconnects a connection unconditionally.
     * @param connection the connection to close (can be {@code null} or already closed).
     * @since 2.0.0
     */
    protected void disconnect(final HttpURLConnection connection) {
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
