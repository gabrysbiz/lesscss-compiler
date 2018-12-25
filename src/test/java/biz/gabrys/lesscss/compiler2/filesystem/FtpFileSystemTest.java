package biz.gabrys.lesscss.compiler2.filesystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public final class FtpFileSystemTest {

    @Spy
    private FtpFileSystem fileSystem;

    @Test
    public void isSupported_pathDoesNotStartWithFtpProtocol_returnsFalse() {
        assertThat(fileSystem.isSupported("./file.less")).isFalse();
        assertThat(fileSystem.isSupported("C:\\Users\\file.less")).isFalse();
        assertThat(fileSystem.isSupported("http://example.org/file.less")).isFalse();
    }

    @Test
    public void isSupported_pathStartsWithFtpProtocol_returnsTrue() {
        assertThat(fileSystem.isSupported("ftp://example.org/file.less")).isTrue();
        assertThat(fileSystem.isSupported("ftp://example.org/directory/file.less")).isTrue();
    }

    @Test
    public void normalize_pathIsNormalized_returnsTheSameValue() throws URISyntaxException {
        final String path = "ftp://example.org/file.less";
        final String result = fileSystem.normalize(path);
        assertThat(result).isEqualTo(path);
    }

    @Test
    public void normalize_pathIsNotNormalized_returnsNormalizedPath() throws URISyntaxException {
        final String path = "ftp://example.org/dir1/./dir2/../file.less";
        final String result = fileSystem.normalize(path);
        assertThat(result).isEqualTo("ftp://example.org/dir1/file.less");
    }

    @Test
    public void expandRedirection_anyPath_returnsTheSamePath() {
        final String path = "any-path";
        final String result = fileSystem.expandRedirection(path);
        assertThat(result).isSameAs(path);
    }

    @Test(expected = IOException.class)
    public void exists_cannotConnectToTheServer_throwsException() throws IOException {
        final String path = "ftp://example.org/file.less";
        doThrow(IOException.class).when(fileSystem).makeConnection(any(URL.class));

        fileSystem.exists(path);
    }

    @Test(expected = IOException.class)
    public void exists_cannotRetrieveFile_throwsException() throws IOException {
        final String path = "ftp://example.org/file.less";
        final FTPClient connection = mock(FTPClient.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class));
        doThrow(IOException.class).when(fileSystem).isFileExist(eq(connection), any(URL.class));

        try {
            fileSystem.exists(path);
        } finally {
            verify(connection).disconnect();
        }
    }

    @Test
    public void exists_fileDoesNotExist_returnsFalse() throws IOException {
        final String path = "ftp://example.org/missing-file.less";
        final FTPClient connection = mock(FTPClient.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class));
        doReturn(Boolean.FALSE).when(fileSystem).isFileExist(eq(connection), any(URL.class));

        final boolean result = fileSystem.exists(path);

        assertThat(result).isFalse();
        verify(fileSystem).disconnect(connection);
    }

    @Test
    public void exists_fileExists_returnsTrue() throws IOException {
        final String path = "ftp://example.org/existing-file.less";
        final FTPClient connection = mock(FTPClient.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class));
        doReturn(Boolean.TRUE).when(fileSystem).isFileExist(eq(connection), any(URL.class));

        final boolean result = fileSystem.exists(path);

        assertThat(result).isTrue();
        verify(fileSystem).disconnect(connection);
    }

    @Test(expected = IOException.class)
    public void isFileExist_cannotRetrieveFile_throwsException() throws IOException {
        final String path = "ftp://example.org/file.less";
        final FTPClient connection = mock(FTPClient.class);
        when(connection.retrieveFileStream("/file.less")).thenThrow(IOException.class);

        fileSystem.isFileExist(connection, new URL(path));
    }

    @Test
    public void isFileExist_connectionReturnsNullStream_returnsFalse() throws IOException {
        final String path = "ftp://example.org/file.less";
        final FTPClient connection = mock(FTPClient.class);
        when(connection.retrieveFileStream("/file.less")).thenReturn(null);

        final boolean result = fileSystem.isFileExist(connection, new URL(path));

        assertThat(result).isFalse();
    }

    @Test
    public void isFileExist_fileIsUnavailable_returnsFalse() throws IOException {
        final String path = "ftp://example.org/missing-file.less";
        final FTPClient connection = mock(FTPClient.class);
        final InputStream stream = mock(InputStream.class);
        when(connection.retrieveFileStream("/missing-file.less")).thenReturn(stream);
        when(connection.getReplyCode()).thenReturn(FTPReply.FILE_UNAVAILABLE);

        final boolean result = fileSystem.isFileExist(connection, new URL(path));

        assertThat(result).isFalse();
        verify(stream).close();
    }

    @Test
    public void isFileExist_fileExists_returnsTrue() throws IOException {
        final String path = "ftp://example.org/existing-file.less";
        final FTPClient connection = mock(FTPClient.class);
        final InputStream stream = mock(InputStream.class);
        when(connection.retrieveFileStream("/existing-file.less")).thenReturn(stream);
        when(connection.getReplyCode()).thenReturn(FTPReply.FILE_STATUS_OK);

        final boolean result = fileSystem.isFileExist(connection, new URL(path));

        assertThat(result).isTrue();
        verify(stream).close();
    }

    @Test(expected = IOException.class)
    public void fetch_cannotConnectToTheServer_throwsException() throws IOException {
        final String path = "ftp://example.org/file.less";
        doThrow(IOException.class).when(fileSystem).makeConnection(any(URL.class));

        fileSystem.fetch(path);
    }

    @Test(expected = IOException.class)
    public void fetch_cannotFetchContent_throwsException() throws IOException {
        final String path = "ftp://example.org/missing-file.less";
        final FTPClient connection = mock(FTPClient.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class));
        doThrow(IOException.class).when(fileSystem).fetchContent(eq(connection), any(URL.class));

        try {
            fileSystem.fetch(path);
        } finally {
            verify(connection).disconnect();
        }
    }

    @Test
    public void fetch_fileExists_returnsFileData() throws IOException {
        final String path = "ftp://example.org/existing-file.less";
        final FTPClient connection = mock(FTPClient.class);
        doReturn(connection).when(fileSystem).makeConnection(any(URL.class));
        final byte[] content = new byte[] { 'l', 'e', 's', 's' };
        doReturn(content).when(fileSystem).fetchContent(eq(connection), any(URL.class));

        final FileData result = fileSystem.fetch(path);

        assertThat(result).isNotNull();
        assertThat(result.getEncoding()).isNull();
        assertThat(result.getContent()).containsExactly(content);
        verify(connection).disconnect();
    }

    @Test(expected = IOException.class)
    public void makeConnection_cannotLogin_throwsException() throws IOException {
        final String path = "ftp://example.org/file.less";
        final FTPClient connection = mock(FTPClient.class);
        doReturn(connection).when(fileSystem).createFtpClient();
        when(connection.login(anyString(), anyString())).thenReturn(Boolean.FALSE);

        fileSystem.makeConnection(new URL(path));
    }

    @Test
    public void makeConnection_allWorksGood_success() throws IOException {
        final String path = "ftp://example.org:27/file.less";
        final FTPClient connection = mock(FTPClient.class);
        doReturn(connection).when(fileSystem).createFtpClient();
        when(connection.login(anyString(), anyString())).thenReturn(Boolean.TRUE);

        final FTPClient result = fileSystem.makeConnection(new URL(path));

        assertThat(result).isSameAs(connection);
        verify(result).setAutodetectUTF8(true);
        verify(result).connect("example.org", 27);
        verify(result).enterLocalActiveMode();
        verify(result).login("anonymous", "anonymous");
        verify(result).enterLocalPassiveMode();
        verify(result).setFileType(FTP.BINARY_FILE_TYPE);
        verifyNoMoreInteractions(result);
    }

    @Test(expected = IOException.class)
    public void fetchContent_retrieveFileIsUnsuccessful_throwsException() throws IOException {
        final FTPClient connection = mock(FTPClient.class);
        final URL url = new URL("ftp://example.org/file.less");
        when(connection.retrieveFile(anyString(), any(OutputStream.class))).thenReturn(Boolean.FALSE);

        fileSystem.fetchContent(connection, url);
    }

    @Test(expected = IOException.class)
    public void fetchContent_retrieveThrowsException_throwsException() throws IOException {
        final FTPClient connection = mock(FTPClient.class);
        final URL url = new URL("ftp://example.org/file.less");
        when(connection.retrieveFile(anyString(), any(OutputStream.class))).thenThrow(IOException.class);

        fileSystem.fetchContent(connection, url);
    }

    @Test
    public void fetchContent_retrieveFileSuccessfully_returnsContent() throws IOException {
        final FTPClient connection = mock(FTPClient.class);
        final URL url = new URL("ftp://example.org/file.less");
        final byte[] content = new byte[] { 'l', 'e', 's', 's' };
        when(connection.retrieveFile(eq("/file.less"), any(OutputStream.class))).thenAnswer(new Answer<Boolean>() {

            @Override
            public Boolean answer(final InvocationOnMock invocation) throws IOException {
                final OutputStream stream = (OutputStream) invocation.getArgument(1);
                stream.write(content);
                return Boolean.TRUE;
            }
        });

        final byte[] result = fileSystem.fetchContent(connection, url);

        assertThat(result).containsExactly(content);
    }

    @Test
    public void disconnect_connectionIsNull_doesNothing() {
        fileSystem.disconnect(null);
    }

    @Test
    public void disconnect_connectionIsNotNull_closesConnection() throws IOException {
        final FTPClient connection = mock(FTPClient.class);
        fileSystem.disconnect(connection);
        verify(connection).disconnect();
    }

    @Test
    public void disconnect_connectionThrowsCheckedExceptionOnDisconnect_hidesException() throws IOException {
        final FTPClient connection = mock(FTPClient.class);
        doThrow(IOException.class).when(connection).disconnect();

        fileSystem.disconnect(connection);
        verify(connection).disconnect();
    }

    @Test
    public void disconnect_connectionThrowsUncheckedExceptionOnDisconnect_hidesException() throws IOException {
        final FTPClient connection = mock(FTPClient.class);
        doThrow(RuntimeException.class).when(connection).disconnect();

        fileSystem.disconnect(connection);
        verify(connection).disconnect();
    }
}
