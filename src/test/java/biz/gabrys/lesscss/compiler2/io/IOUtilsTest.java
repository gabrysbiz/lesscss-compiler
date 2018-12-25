package biz.gabrys.lesscss.compiler2.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

public final class IOUtilsTest {

    @Test
    public void closeQuietly_objectIsNull_success() {
        IOUtils.closeQuietly(null);
    }

    @Test
    public void closeQuietly_objectIsNotNull_objectIsClosed() throws IOException {
        final Closeable closeable = Mockito.mock(Closeable.class);

        IOUtils.closeQuietly(closeable);

        Mockito.verify(closeable).close();
        Mockito.verifyNoMoreInteractions(closeable);
    }

    @Test
    public void closeQuietly_objectIsNotNullAndThrowException_objectIsClosed() throws IOException {
        final Closeable closeable = Mockito.mock(Closeable.class);
        Mockito.doThrow(IOException.class).when(closeable).close();

        IOUtils.closeQuietly(closeable);

        Mockito.verify(closeable).close();
        Mockito.verifyNoMoreInteractions(closeable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_fileIsNull_throwsException() throws IOException {
        IOUtils.write(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_contentIsNull_throwsException() throws IOException {
        IOUtils.write(Mockito.mock(File.class), null);
    }
}
