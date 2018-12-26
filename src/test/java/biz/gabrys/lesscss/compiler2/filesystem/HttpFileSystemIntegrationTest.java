package biz.gabrys.lesscss.compiler2.filesystem;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import biz.gabrys.lesscss.compiler2.IntegrationTest;

@Category(IntegrationTest.class)
public final class HttpFileSystemIntegrationTest {

    private static final String HTTPS = "https";
    private static final String HTTP = "http";

    private static final String HOST = "://raw.githubusercontent.com/gabrysbiz/lesscss-compiler/master/";
    private static final String MISSING_FILE = HOST + "non-existent-file.gabrys";
    private static final String EXISTING_FILE = HOST + "README.md";
    private static final String EXISTING_FILE_STARTS_WITH = "# About";

    private HttpFileSystem fileSystem;

    @Before
    public void setup() {
        fileSystem = new HttpFileSystem();
    }

    @Test
    public void expandRedirection_fileExists_returnsDirectPath() throws IOException, URISyntaxException {
        final String result = fileSystem.expandRedirection(HTTP + EXISTING_FILE);
        assertThat(result).isEqualTo(HTTPS + EXISTING_FILE);
    }

    @Test
    public void exists_fileDoesNotExist_returnsFalse() throws IOException {
        final boolean result = fileSystem.exists(HTTPS + MISSING_FILE);
        assertThat(result).isFalse();
    }

    @Test
    public void exists_fileExists_returnsTrue() throws IOException {
        final boolean result = fileSystem.exists(HTTPS + EXISTING_FILE);
        assertThat(result).isTrue();
    }

    @Test(expected = IOException.class)
    public void fetch_fileDoesNotExist_throwsException() throws IOException {
        fileSystem.fetch(HTTPS + MISSING_FILE);
    }

    @Test
    public void fetch_fileExists_returnsFileData() throws IOException {
        final FileData data = fileSystem.fetch(HTTPS + EXISTING_FILE);
        assertThat(data).isNotNull();
        assertThat(data.getContent()).isNotNull();
        final String text = new String(data.getContent());
        assertThat(text).startsWith(EXISTING_FILE_STARTS_WITH);
    }
}
