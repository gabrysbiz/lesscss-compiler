package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import biz.gabrys.lesscss.compiler2.filesystem.ClassPathFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.FtpFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.HttpFileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.LocalFileSystem;

public final class FileSystemOptionsBuilderTest {

    private FileSystemOptionsBuilder builder;

    @Before
    public void setup() {
        builder = new FileSystemOptionsBuilder();
    }

    @Test
    public void build_standardEnabled() {
        final List<FileSystemOption> fileSystems = builder.appendStandard().build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(HttpFileSystem.class), new FileSystemOption(FtpFileSystem.class),
                new FileSystemOption(LocalFileSystem.class));
    }

    @Test
    public void build_localEnabled() {
        final List<FileSystemOption> fileSystems = builder.appendLocal().build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(LocalFileSystem.class));
    }

    @Test
    public void build_httpEnabled() {
        final List<FileSystemOption> fileSystems = builder.appendHttp().build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(HttpFileSystem.class));
    }

    @Test
    public void build_ftpEnabled() {
        final List<FileSystemOption> fileSystems = builder.appendFtp().build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(FtpFileSystem.class));
    }

    @Test
    public void build_classpathEnabled() {
        final List<FileSystemOption> fileSystems = builder.appendClassPath().build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(ClassPathFileSystem.class));
    }

    @Test
    public void build_customEnabledByClassesWhereOneIsNull_createdTwoOptions() {
        @SuppressWarnings("unchecked")
        final List<FileSystemOption> fileSystems = builder.appendCustom(CustomFileSystem1.class, null, CustomFileSystem2.class).build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(CustomFileSystem1.class),
                new FileSystemOption(CustomFileSystem2.class));
    }

    @Test
    public void build_customEnabledByClass() {
        final List<FileSystemOption> fileSystems = builder.appendCustom(CustomFileSystem1.class).build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(CustomFileSystem1.class));
    }

    @Test
    public void build_customEnabledByClassWithParameters() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        final List<FileSystemOption> fileSystems = builder.appendCustom(CustomFileSystem1.class, parameters).build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(CustomFileSystem1.class, parameters));
    }

    @Test
    public void build_customEnabledByOptionsArrayWhereOneIsNull_createdTwoOptions() {
        final List<FileSystemOption> fileSystems = builder
                .appendCustom(new FileSystemOption(CustomFileSystem1.class), null, new FileSystemOption(CustomFileSystem2.class)).build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(CustomFileSystem1.class),
                new FileSystemOption(CustomFileSystem2.class));
    }

    @Test
    public void build_customEnabledByOptionsCollectionWhereOneIsNull_createdTwoOptions() {
        final List<FileSystemOption> fileSystems = builder
                .appendCustom(
                        Arrays.asList(new FileSystemOption(CustomFileSystem1.class), null, new FileSystemOption(CustomFileSystem2.class)))
                .build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(CustomFileSystem1.class),
                new FileSystemOption(CustomFileSystem2.class));
    }

    @Test
    public void build_customEnabledByOption() {
        final List<FileSystemOption> fileSystems = builder.appendCustom(new FileSystemOption(CustomFileSystem1.class)).build();

        assertThat(fileSystems).containsExactly(new FileSystemOption(CustomFileSystem1.class));
    }

    @Test
    public void build_multiple() {
        final Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("name1", "value1");
        final Map<String, String> parameters2 = new HashMap<>();
        parameters2.put("name2", "value2");

        builder.appendCustom(CustomFileSystem1.class);
        builder.appendHttp();
        builder.appendCustom(CustomFileSystem2.class);
        builder.appendCustom(CustomFileSystem2.class, parameters1);
        builder.appendCustom(CustomFileSystem2.class);
        builder.appendCustom(CustomFileSystem1.class);
        builder.appendCustom(new FileSystemOption(CustomFileSystem2.class, parameters1));
        builder.appendCustom(CustomFileSystem2.class, parameters2);
        builder.appendLocal();

        final List<FileSystemOption> fileSystems = builder.build();

        assertThat(fileSystems).containsExactly(//
                new FileSystemOption(HttpFileSystem.class), //
                new FileSystemOption(CustomFileSystem2.class), //
                new FileSystemOption(CustomFileSystem1.class), //
                new FileSystemOption(CustomFileSystem2.class, parameters1), //
                new FileSystemOption(CustomFileSystem2.class, parameters2), //
                new FileSystemOption(LocalFileSystem.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendCustom_classArrayIsNull_throwsException() {
        builder.appendCustom((Class<FileSystem>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendCustom_classIsNull_throwsException() {
        builder.appendCustom((Class<FileSystem>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendCustom_classIsNullAndParemetersAreNotNull_throwsException() {
        builder.appendCustom(null, Collections.<String, String>emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendCustom_classIsNotNullAndParemetersAreNull_throwsException() {
        builder.appendCustom(CustomFileSystem1.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendCustom_optionsArrayIsNull_throwsException() {
        builder.appendCustom((FileSystemOption[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendCustom_optionsCollectionIsNull_throwsException() {
        builder.appendCustom((Iterable<FileSystemOption>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void appendCustom_optionIsNull_throwsException() {
        builder.appendCustom((FileSystemOption) null);
    }

    private abstract static class CustomFileSystem1 implements FileSystem {

    }

    private abstract static class CustomFileSystem2 implements FileSystem {

    }
}
