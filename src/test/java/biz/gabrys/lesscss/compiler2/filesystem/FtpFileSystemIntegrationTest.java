package biz.gabrys.lesscss.compiler2.filesystem;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import biz.gabrys.lesscss.compiler2.IntegrationTest;

@Category(IntegrationTest.class)
public final class FtpFileSystemIntegrationTest {

    private static final String HOST = "ftp://ftp.freebsd.org/pub/FreeBSD/";
    private static final String MISSING_FILE = HOST + "gabrys-non-existent-file";
    private static final String EXISTING_FILE = HOST + "README.TXT";
    private static final String EXISTING_FILE_STARTS_WITH = "Welcome to the FreeBSD archive!";

    private FtpFileSystem fileSystem;

    @Before
    public void setup() {
        fileSystem = new FtpFileSystem();
    }

    @Test
    public void exists_fileDoesNotExist_returnsFalse() throws IOException {
        final boolean result = fileSystem.exists(MISSING_FILE);
        assertThat(result).isFalse();
    }

    @Test
    public void exists_fileExists_returnsTrue() throws IOException {
        final boolean result = fileSystem.exists(EXISTING_FILE);
        assertThat(result).isTrue();
    }

    @Test(expected = IOException.class)
    public void fetch_fileDoesNotExist_throwsException() throws IOException {
        fileSystem.fetch(MISSING_FILE);
    }

    @Test
    public void fetch_fileExists_returnsFileData() throws IOException {
        final FileData data = fileSystem.fetch(EXISTING_FILE);
        assertThat(data).isNotNull();
        assertThat(data.getContent()).isNotNull();
        final String text = new String(data.getContent());
        assertThat(text).startsWith(EXISTING_FILE_STARTS_WITH);
    }
}
