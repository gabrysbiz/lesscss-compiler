package biz.gabrys.lesscss.compiler;

import java.io.Closeable;
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
        Mockito.doThrow(Exception.class).when(closeable).close();

        IOUtils.closeQuietly(closeable);

        Mockito.verify(closeable).close();
        Mockito.verifyNoMoreInteractions(closeable);
    }
}
