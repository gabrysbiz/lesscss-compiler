package biz.gabrys.lesscss.compiler2.io;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public final class FileUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void write_fileIsNull_throwsException() throws IOException {
        FileUtils.write(null, "content", "encoding");
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_contentIsNull_throwsException() throws IOException {
        FileUtils.write(mock(File.class), null, "encoding");
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_encodingIsNull_throwsException() throws IOException {
        FileUtils.write(mock(File.class), "content", null);
    }

    @Test(expected = IOException.class)
    public void write_cannotCreateParentDirectories_throwsException() throws IOException {
        final File parent = mock(File.class);
        when(parent.exists()).thenReturn(Boolean.FALSE);
        when(parent.mkdirs()).thenReturn(Boolean.FALSE);

        final File file = mock(File.class);
        when(file.getParentFile()).thenReturn(parent);

        FileUtils.write(file, "content", "encoding");
    }
}
