package biz.gabrys.lesscss.compiler2.filesystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public final class ClassPathFileSystemTest {

    @Spy
    private ClassPathFileSystem fileSystem;

    @Test
    public void isSupported_pathDoesNotStartWithClasspathProtocol_returnsFalse() {
        assertThat(fileSystem.isSupported("./file.less")).isFalse();
        assertThat(fileSystem.isSupported("C:\\Users\\file.less")).isFalse();
        assertThat(fileSystem.isSupported("http://example.org/file.less")).isFalse();
    }

    @Test
    public void isSupported_pathStartsWithClasspathProtocol_returnsTrue() {
        assertThat(fileSystem.isSupported("classpath://file.less")).isTrue();
        assertThat(fileSystem.isSupported("classpath://directory/file.less")).isTrue();
    }

    @Test
    public void normalize_pathIsNormalized_returnsTheSameValue() throws URISyntaxException {
        final String path = "classpath://file.less";
        final String result = fileSystem.normalize(path);
        assertThat(result).isEqualTo(path);
    }

    @Test
    public void normalize_pathIsNotNormalized_returnsNormalizedPath() throws URISyntaxException {
        final String path = "classpath://dir1/./dir2/../file.less";
        final String result = fileSystem.normalize(path);
        assertThat(result).isEqualTo("classpath://dir1/file.less");
    }

    @Test
    public void expandRedirection_anyPath_returnsTheSamePath() {
        final String path = "any-path";
        final String result = fileSystem.expandRedirection(path);
        assertThat(result).isSameAs(path);
    }

    @Test
    public void exists_resourceExists_returnsTrue() throws MalformedURLException {
        final ClassLoader classLoader = mock(ClassLoader.class);
        doReturn(classLoader).when(fileSystem).getClassLoader();
        final String name = "existing-file.less";
        final String path = "classpath://" + name;
        final URL url = new URL("file://" + name);
        when(classLoader.getResource(name)).thenReturn(url);

        final boolean result = fileSystem.exists(path);

        assertThat(result).isTrue();
        verify(classLoader).getResource(name);
        verifyNoMoreInteractions(classLoader);
    }

    @Test
    public void exists_resourceDoesNotExist_returnsFalse() throws MalformedURLException {
        final ClassLoader classLoader = mock(ClassLoader.class);
        doReturn(classLoader).when(fileSystem).getClassLoader();
        final String name = "missing-file.less";
        final String path = "classpath://" + name;
        when(classLoader.getResource(name)).thenReturn(null);

        final boolean result = fileSystem.exists(path);

        assertThat(result).isFalse();
        verify(classLoader).getResource(name);
        verifyNoMoreInteractions(classLoader);
    }

    @Test(expected = IOException.class)
    public void fetch_resourceDoesNotExist_throwsException() throws IOException {
        final ClassLoader classLoader = mock(ClassLoader.class);
        doReturn(classLoader).when(fileSystem).getClassLoader();
        final String name = "missing-file.less";
        final String path = "classpath://" + name;
        when(classLoader.getResourceAsStream(name)).thenReturn(null);

        fileSystem.fetch(path);
    }

    @Test
    public void fetch_resourceExists_returnsData() throws IOException {
        final ClassLoader classLoader = mock(ClassLoader.class);
        doReturn(classLoader).when(fileSystem).getClassLoader();
        final String name = "existing-file.less";
        final String path = "classpath://" + name;
        final InputStream stream = mock(InputStream.class);
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
        when(classLoader.getResourceAsStream(name)).thenReturn(stream);

        final FileData result = fileSystem.fetch(path);

        assertThat(result).isNotNull();
        assertThat(result.getEncoding()).isNull();
        assertThat(result.getContent()).containsExactly(content);
        verify(classLoader).getResourceAsStream(name);
        verifyNoMoreInteractions(classLoader);
    }
}
