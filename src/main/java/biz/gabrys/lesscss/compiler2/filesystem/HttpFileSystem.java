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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

    private static final Collection<Integer> REDIRECT_CODES = new HashSet<Integer>(
            Arrays.asList(HttpURLConnection.HTTP_MOVED_PERM, HttpURLConnection.HTTP_MOVED_TEMP, HttpURLConnection.HTTP_SEE_OTHER));

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public HttpFileSystem() {
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
        final HttpURLConnection connection = makeConnection(new URL(path), false);
        final String expandedUrl = connection.getURL().toURI().normalize().toString();
        connection.disconnect();
        return expandedUrl;
    }

    @Override
    public FileData fetch(final String path) throws IOException {
        final HttpURLConnection connection = makeConnection(new URL(path), true);
        final String responseEncoding = getEncodingFromContentType(connection.getContentType());
        FileData fileData;
        try {
            fileData = new FileData(IOUtils.toByteArray(connection.getInputStream()), responseEncoding);
        } catch (final IOException e) {
            throw new IOException("cannot read file's content", e);
        } finally {
            connection.disconnect();
        }
        return fileData;
    }

    private static HttpURLConnection makeConnection(final URL url, final boolean fetchResponseBody) throws IOException {
        HttpURLConnection connection;
        int responseCode;
        URL resourceUrl = url;
        while (true) {
            connection = (HttpURLConnection) resourceUrl.openConnection();
            connection.setRequestMethod(fetchResponseBody ? "GET" : "HEAD");
            responseCode = connection.getResponseCode();
            if (!REDIRECT_CODES.contains(responseCode)) {
                break;
            }
            final String location = connection.getHeaderField("Location");
            try {
                resourceUrl = new URL(location);
            } catch (final MalformedURLException e) {
                throw new IOException(String.format("invalid \"Location\" header: %s", location), e);
            }
        }
        if (responseCode != HttpURLConnection.HTTP_OK) {
            final List<Integer> supportedCodes = new ArrayList<Integer>(REDIRECT_CODES);
            supportedCodes.add(0, HttpURLConnection.HTTP_OK);
            throw new IllegalArgumentException(String.format("response HTTP status code %s is not allowed (supports only: %s)",
                    responseCode, supportedCodes.toString().replaceAll("\\[|\\]", "")));
        }
        return connection;
    }

    private static String getEncodingFromContentType(final String contentType) {
        if (contentType == null || !contentType.contains("charset=")) {
            return Charset.defaultCharset().toString();
        }
        return contentType.substring(contentType.lastIndexOf('=') + 1);
    }
}
