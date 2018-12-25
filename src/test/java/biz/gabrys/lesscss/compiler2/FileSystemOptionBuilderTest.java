package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import biz.gabrys.lesscss.compiler2.filesystem.FileData;
import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;

public class FileSystemOptionBuilderTest {

    private static final String CLASS_NAME = FakeFileSystem.class.getName();

    private FileSystemOptionBuilder builder;

    @Before
    public void setup() {
        builder = new FileSystemOptionBuilder();
    }

    @Test(expected = BuilderCreationException.class)
    public void build_withClassHasNottBeenCalled_throwsException() {
        builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_withClass_classNameIsNull_throwsException() {
        builder.withClass((String) null);
    }

    @Test
    public void build_withClass_classNameIsNotNull_returnsOption() {
        final FileSystemOption option = builder.withClass(CLASS_NAME).build();

        assertThat(option).isNotNull();
        assertThat(option.getClassName()).isEqualTo(CLASS_NAME);
        assertThat(option.getParameters()).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_withClass_classIsNull_throwsException() {
        builder.withClass((Class<FileSystem>) null);
    }

    @Test
    public void build_withClass_classIsNotNull_returnsOption() {
        final FileSystemOption option = builder.withClass(FakeFileSystem.class).build();

        assertThat(option).isNotNull();
        assertThat(option.getClassName()).isEqualTo(CLASS_NAME);
        assertThat(option.getParameters()).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_appendParameters_parametersAreNull_throwsException() {
        builder.appendParameters(null);
    }

    @Test
    public void build_appendParameters_parametersAreNotNull_returnsOption() {
        final Map<CharSequence, CharSequence> parameters = new HashMap<>();
        parameters.put("param1", "value1");
        parameters.put("param2", "value2");
        parameters.put("param3", "value3");

        final FileSystemOptionBuilder builderSpy = spy(builder);
        final FileSystemOption option = builderSpy.withClass(CLASS_NAME).appendParameters(parameters).build();

        assertThat(option).isNotNull();
        assertThat(option.getClassName()).isEqualTo(CLASS_NAME);
        assertThat(option.getParameters()).containsOnly(//
                entry("param1", "value1"), //
                entry("param2", "value2"), //
                entry("param3", "value3"));

        verify(builderSpy).appendParameter("param1", "value1");
        verify(builderSpy).appendParameter("param2", "value2");
        verify(builderSpy).appendParameter("param3", "value3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_appendParameter_nameIsNull_throwsException() {
        builder.appendParameter(null, "value");
    }

    @Test
    public void build_appendParameter_valueIsNull_returnsOption() {
        final FileSystemOption option = builder.withClass(CLASS_NAME).appendParameter("name", null).build();

        assertThat(option).isNotNull();
        assertThat(option.getClassName()).isEqualTo(CLASS_NAME);
        assertThat(option.getParameters()).containsOnly(entry("name", null));
    }

    @Test
    public void build_appendParameter_valueIsNotNull_returnsOption() {
        final FileSystemOption option = builder.withClass(CLASS_NAME).appendParameter("name", "value").build();

        assertThat(option).isNotNull();
        assertThat(option.getClassName()).isEqualTo(CLASS_NAME);
        assertThat(option.getParameters()).containsOnly(entry("name", "value"));
    }

    private static class FakeFileSystem implements FileSystem {

        @Override
        public void configure(final Map<String, String> parameters) throws Exception {
            // do nothing
        }

        @Override
        public boolean isSupported(final String path) {
            return false;
        }

        @Override
        public String normalize(final String path) throws Exception {
            return null;
        }

        @Override
        public String expandRedirection(final String path) throws Exception {
            return null;
        }

        @Override
        public boolean exists(final String path) throws Exception {
            return false;
        }

        @Override
        public FileData fetch(final String path) throws Exception {
            return null;
        }
    }
}
