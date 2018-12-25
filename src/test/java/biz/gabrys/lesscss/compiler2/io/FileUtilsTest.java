package biz.gabrys.lesscss.compiler2.io;

import static org.mockito.Mockito.mock;

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
}
