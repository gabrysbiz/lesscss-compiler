package biz.gabrys.lesscss.compiler2.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public final class IOUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void toByteArray_streamIsNull_throwsException() throws IOException {
        IOUtils.toByteArray(null);
    }

    @Test
    public void toByteArray_streamWithData_returnsArray() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(any(byte[].class))).thenAnswer(new Answer<Integer>() {
            private int call;

            @Override
            public Integer answer(final InvocationOnMock invocation) {
                final byte[] bytes = (byte[]) invocation.getArgument(0);
                switch (call) {
                    case 0:
                        bytes[0] = 'b';
                        bytes[1] = 'y';
                        bytes[2] = 't';
                        ++call;
                        return 3;
                    case 1:
                        bytes[0] = 'e';
                        bytes[1] = 's';
                        ++call;
                        return 2;
                    default:
                        return -1;
                }
            }
        });

        final byte[] bytes = IOUtils.toByteArray(inputStream);

        assertThat(bytes).hasSize(5);
        assertThat(bytes[0]).isEqualTo((byte) 'b');
        assertThat(bytes[1]).isEqualTo((byte) 'y');
        assertThat(bytes[2]).isEqualTo((byte) 't');
        assertThat(bytes[3]).isEqualTo((byte) 'e');
        assertThat(bytes[4]).isEqualTo((byte) 's');
        verify(inputStream).close();
    }

    @Test(expected = IOException.class)
    public void toString_streamThrowsException_throwsException() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(any(byte[].class))).thenThrow(IOException.class);

        try {
            IOUtils.toByteArray(inputStream);
        } finally {
            verify(inputStream).close();
        }
    }

    @Test
    public void closeQuietly_objectIsNull_doesNothing() {
        IOUtils.closeQuietly(null);
    }

    @Test
    public void closeQuietly_objectIsNotNull_objectIsClosed() throws IOException {
        final Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);

        verify(closeable).close();
        verifyNoMoreInteractions(closeable);
    }

    @Test
    public void closeQuietly_objectIsNotNullAndThrowCheckedException_objectIsClosed() throws IOException {
        final Closeable closeable = mock(Closeable.class);
        doThrow(IOException.class).when(closeable).close();

        IOUtils.closeQuietly(closeable);

        verify(closeable).close();
        verifyNoMoreInteractions(closeable);
    }

    @Test
    public void closeQuietly_objectIsNotNullAndThrowUncheckedException_objectIsClosed() throws IOException {
        final Closeable closeable = mock(Closeable.class);
        doThrow(RuntimeException.class).when(closeable).close();

        IOUtils.closeQuietly(closeable);

        verify(closeable).close();
        verifyNoMoreInteractions(closeable);
    }
}
