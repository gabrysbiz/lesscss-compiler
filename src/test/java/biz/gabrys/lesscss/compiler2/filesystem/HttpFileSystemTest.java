package biz.gabrys.lesscss.compiler2.filesystem;

import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public final class HttpFileSystemTest {

    @Spy
    private HttpFileSystem fileSystem;

    @Test
    public void isSupported_pathDoesNotStartWithFtpProtocol_returnsFalse() {
        assertThat(fileSystem.isSupported("./file.less")).isFalse();
        assertThat(fileSystem.isSupported("C:\\Users\\file.less")).isFalse();
        assertThat(fileSystem.isSupported("ftp://example.org/file.less")).isFalse();
    }

    @Test
    public void isSupported_pathStartsWithFtpProtocol_returnsTrue() {
        assertThat(fileSystem.isSupported("http://example.org/file.less")).isTrue();
        assertThat(fileSystem.isSupported("https://example.org/directory/file.less")).isTrue();
    }

    @Test
    public void normalize_pathIsNormalized_returnsTheSameValue() throws URISyntaxException {
        final String path = "http://example.org/file.less";
        final String result = fileSystem.normalize(path);
        assertThat(result).isEqualTo(path);
    }

    @Test
    public void normalize_pathIsNotNormalized_returnsNormalizedPath() throws URISyntaxException {
        final String path = "http://example.org/dir1/./dir2/../file.less";
        final String result = fileSystem.normalize(path);
        assertThat(result).isEqualTo("http://example.org/dir1/file.less");
    }

    @Test
    public void expandRedirection_directPathAndFileExists_returnsTheSamePath() throws IOException, URISyntaxException {
        final String path = "http://example.org/direct-file.less";
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class), eq(false));
        when(connection.getResponseCode()).thenReturn(HTTP_OK);

        final String result = fileSystem.expandRedirection(path);
        assertThat(result).isSameAs(path);
    }

    @Test
    public void expandRedirection_directPathAndFileDoesNotExist_returnsTheSamePath() throws IOException, URISyntaxException {
        final String path = "http://example.org/direct-file.less";
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class), eq(false));
        when(connection.getResponseCode()).thenReturn(HTTP_NOT_FOUND);

        final String result = fileSystem.expandRedirection(path);
        assertThat(result).isSameAs(path);
    }

    @Test
    public void expandRedirection_nonDirectPath_returnsDirectPath() throws IOException, URISyntaxException {
        final String path1 = "http://example.org/dir/non-direct-file.less";
        final String path2 = "http://example.org/dir2/non-direct-file.less";
        final String path3 = "https://example.org/dir2/dir3/direct-file.less";
        final HttpURLConnection connection1 = mock(HttpURLConnection.class);
        final HttpURLConnection connection2 = mock(HttpURLConnection.class);
        final HttpURLConnection connection3 = mock(HttpURLConnection.class);
        doReturn(connection1, connection2, connection3).when(fileSystem).makeConnection(any(URL.class), eq(false));
        when(connection1.getResponseCode()).thenReturn(HTTP_MOVED_PERM);
        when(connection1.getHeaderField("Location")).thenReturn("../dir2/non-direct-file.less");
        when(connection1.getURL()).thenReturn(new URL(path1));
        when(connection2.getResponseCode()).thenReturn(HTTP_MOVED_TEMP);
        when(connection2.getHeaderField("Location")).thenReturn(path3);
        when(connection2.getURL()).thenReturn(new URL(path2));
        when(connection3.getResponseCode()).thenReturn(HTTP_OK);

        final String result = fileSystem.expandRedirection(path1);
        assertThat(result).isSameAs(path3);
        verify(fileSystem).disconnect(connection1);
        verify(fileSystem).disconnect(connection2);
        verify(fileSystem).disconnect(connection3);
    }

    @Test
    public void exists_fileDoesNotExist_returnsFalse() throws IOException {
        final String path = "http://example.org/missing-file.less";
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class), eq(false));
        when(connection.getResponseCode()).thenReturn(HTTP_NOT_FOUND);

        final boolean result = fileSystem.exists(path);
        assertThat(result).isFalse();
        verify(fileSystem).disconnect(connection);
    }

    @Test
    public void exists_fileExists_returnsTrue() throws IOException {
        final String path = "http://example.org/exiting-file.less";
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class), eq(false));
        when(connection.getResponseCode()).thenReturn(HTTP_OK);

        final boolean result = fileSystem.exists(path);
        assertThat(result).isTrue();
        verify(fileSystem).disconnect(connection);
    }

    @Test(expected = IOException.class)
    public void fetch_fileDoesNotExist_throwsException() throws IOException {
        final String path = "http://example.org/missing-file.less";
        doThrow(IOException.class).when(fileSystem).makeConnection(any(URL.class), eq(true));

        fileSystem.fetch(path);
    }

    @Test
    public void fetch_fileExists_returnsFileData() throws IOException {
        final String path = "http://example.org/existing-file.less";
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class), eq(true));
        when(connection.getResponseCode()).thenReturn(HTTP_OK);
        final String encoding = "encoding";
        doReturn(encoding).when(fileSystem).readEncoding(connection);
        final InputStream stream = mock(InputStream.class);
        when(connection.getInputStream()).thenReturn(stream);
        final byte[] content = new byte[] { 'l', 'e', 's', 's' };
        when(stream.read(any(byte[].class))).thenAnswer(new Answer<Integer>() {
            private boolean firstCall = true;

            @Override
            public Integer answer(final InvocationOnMock invocation) {
                final byte[] bytes = (byte[]) invocation.getArgument(0);
                if (firstCall) {
                    System.arraycopy(content, 0, bytes, 0, content.length);
                    firstCall = false;
                    return content.length;
                } else {
                    return -1;
                }
            }
        });

        final FileData result = fileSystem.fetch(path);
        assertThat(result).isNotNull();
        assertThat(result.getEncoding()).isSameAs(encoding);
        assertThat(result.getContent()).containsExactly(content);
        verify(fileSystem).disconnect(connection);
    }

    @Test(expected = IOException.class)
    public void fetch_fileExistsButTransferThrowsException_throwsException() throws IOException {
        final String path = "http://example.org/existing-file.less";
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class), eq(true));
        when(connection.getResponseCode()).thenReturn(HTTP_OK);
        final String encoding = "encoding";
        doReturn(encoding).when(fileSystem).readEncoding(connection);
        final InputStream stream = mock(InputStream.class);
        when(connection.getInputStream()).thenReturn(stream);
        when(stream.read(any(byte[].class))).thenThrow(IOException.class);

        try {
            fileSystem.fetch(path);
        } finally {
            verify(fileSystem).disconnect(connection);
        }
    }

    @Test
    public void makeConnection_fetchResponseBodyIsTrue_returnsConnection() throws IOException {
        final URL url = new URL("http://example.org/file.less");
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(fileSystem).openConnection(url);

        final HttpURLConnection result = fileSystem.makeConnection(url, true);
        assertThat(result).isSameAs(connection);
        verify(result).setInstanceFollowRedirects(false);
        verify(result).setRequestMethod("GET");
    }

    @Test
    public void makeConnection_fetchResponseBodyIsFalse_returnsConnection() throws IOException {
        final URL url = new URL("http://example.org/file.less");
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doReturn(connection).when(fileSystem).openConnection(url);

        final HttpURLConnection result = fileSystem.makeConnection(url, false);
        assertThat(result).isSameAs(connection);
        verify(result).setInstanceFollowRedirects(false);
        verify(result).setRequestMethod("HEAD");
    }

    @Test(expected = NullPointerException.class)
    public void openConnection_urlIsNull_throwsException() throws IOException {
        fileSystem.openConnection(null);
    }

    @Test(expected = NullPointerException.class)
    public void validateResponseCode_validCodesIsNull_throwsException() throws IOException {
        fileSystem.validateResponseCode(1, null);
    }

    @Test
    public void validateResponseCode_resonseCodeIsValid_returnsNoError() throws IOException {
        fileSystem.validateResponseCode(200, Arrays.asList(200));
    }

    @Test(expected = IOException.class)
    public void validateResponseCode_resonseCodeIsInvalid_throwsException() throws IOException {
        fileSystem.validateResponseCode(205, Arrays.asList(200, 300, 400));
    }

    @Test(expected = NullPointerException.class)
    public void readEncoding_connectionIsNull_throwsException() {
        fileSystem.readEncoding(null);
    }

    @Test
    public void readEncoding_contentTypeIsNull_returnsNull() {
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        final String result = fileSystem.readEncoding(connection);
        assertThat(result).isNull();
    }

    @Test
    public void readEncoding_contentTypeDoesNotContainCharset_returnsNull() {
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getContentType()).thenReturn("text/plain");
        final String result = fileSystem.readEncoding(connection);
        assertThat(result).isNull();
    }

    @Test
    public void readEncoding_contentTypeContainCharset_returnsCharset() {
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getContentType()).thenReturn("text/plain; charset=utf-8");

        final String result = fileSystem.readEncoding(connection);

        assertThat(result).isEqualTo("UTF-8");
    }

    @Test
    public void disconnect_connectionIsNull_doesNothing() {
        fileSystem.disconnect(null);
    }

    @Test
    public void disconnect_connectionIsNotNull_closesConnection() throws IOException {
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        fileSystem.disconnect(connection);
        verify(connection).disconnect();
    }

    @Test
    public void disconnect_connectionThrowsCheckedExceptionOnDisconnect_hidesException() throws IOException {
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doThrow(RuntimeException.class).when(connection).disconnect();

        fileSystem.disconnect(connection);
        verify(connection).disconnect();
    }

    @Test
    public void disconnect_connectionThrowsUncheckedExceptionOnDisconnect_hidesException() throws IOException {
        final HttpURLConnection connection = mock(HttpURLConnection.class);
        doThrow(RuntimeException.class).when(connection).disconnect();

        fileSystem.disconnect(connection);
        verify(connection).disconnect();
    }
}
