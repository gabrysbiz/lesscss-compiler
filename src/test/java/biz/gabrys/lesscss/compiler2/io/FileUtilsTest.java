package biz.gabrys.lesscss.compiler2.io;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

public final class FileUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void write_fileIsNull_throwsException() throws IOException {
        FileUtils.write(null, "content", "encoding");
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_contentIsNull_throwsException() throws IOException {
        FileUtils.write(Mockito.mock(File.class), null, "encoding");
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_encodingIsNull_throwsException() throws IOException {
        FileUtils.write(Mockito.mock(File.class), "content", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_fileIsNull_throwsException() throws IOException {
        FileUtils.read(null, "encoding");
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_encodingIsNull_throwsException() throws IOException {
        FileUtils.read(Mockito.mock(File.class), null);
    }
}
