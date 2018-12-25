package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import biz.gabrys.lesscss.compiler2.filesystem.ClassPathFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.FtpFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.HttpFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.LocalFileSystem;

public final class FileSystemsOptionBuilderTest {

    private FileSystemsOptionBuilder builder;

    @Before
    public void setup() {
        builder = new FileSystemsOptionBuilder();
    }

    @Test
    public void build_standardEnabled() {
        final List<String> fileSystems = builder.withStandard().build();

        assertThat(fileSystems).containsExactly(HttpFileSystem.class.getName(), FtpFileSystem.class.getName(),
                LocalFileSystem.class.getName());
    }

    @Test
    public void build_localEnabled() {
        final List<String> fileSystems = builder.withLocal().build();

        assertThat(fileSystems).containsExactly(LocalFileSystem.class.getName());
    }

    @Test
    public void build_httpEnabled() {
        final List<String> fileSystems = builder.withHttp().build();

        assertThat(fileSystems).containsExactly(HttpFileSystem.class.getName());
    }

    @Test
    public void build_ftpEnabled() {
        final List<String> fileSystems = builder.withFtp().build();

        assertThat(fileSystems).containsExactly(FtpFileSystem.class.getName());
    }

    @Test
    public void build_classpathEnabled() {
        final List<String> fileSystems = builder.withClassPath().build();

        assertThat(fileSystems).containsExactly(ClassPathFileSystem.class.getName());
    }

    @Test
    public void build_customEnabled() {
        final List<String> fileSystems = builder.withCustom(CustomFileSystem1.class).build();

        assertThat(fileSystems).containsExactly(CustomFileSystem1.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_customWhenClassIsNull_throwsException() {
        new FileSystemsOptionBuilder().withCustom(null);
    }

    @Test
    public void build_multiple() {
        builder.withCustom(CustomFileSystem1.class);
        builder.withHttp();
        builder.withCustom(CustomFileSystem2.class);
        builder.withCustom(CustomFileSystem1.class);
        builder.withLocal();

        final List<String> fileSystems = builder.build();

        assertThat(fileSystems).containsExactly(HttpFileSystem.class.getName(), CustomFileSystem2.class.getName(),
                CustomFileSystem1.class.getName(), LocalFileSystem.class.getName());
    }

    private abstract static class CustomFileSystem1 implements FileSystem {

    }

    private abstract static class CustomFileSystem2 implements FileSystem {

    }
}
