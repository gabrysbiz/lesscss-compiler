package biz.gabrys.lesscss.compiler2;

import static nl.jqno.equalsverifier.Warning.NONFINAL_FIELDS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import biz.gabrys.lesscss.compiler2.filesystem.HttpFileSystem;
import nl.jqno.equalsverifier.EqualsVerifier;

@RunWith(MockitoJUnitRunner.class)
public class LessOptionsTest {

    @Spy
    private LessOptions options;

    @Test
    public void testEqualsAndHashCodeContracts() {
        EqualsVerifier.forClass(LessOptions.class).usingGetClass().suppress(NONFINAL_FIELDS).verify();
    }

    @Test
    public void includePaths() {
        assertThat(options.getIncludePaths()).isEmpty();

        options.setIncludePaths(Arrays.asList("path"));
        assertThat(options.getIncludePaths()).contains("path");

        options.setIncludePaths(null);
        assertThat(options.getIncludePaths()).isEmpty();

        options.setIncludePaths(Arrays.asList("path3", "path1", "path2"));
        assertThat(options.getIncludePaths()).containsExactly("path3", "path1", "path2");
    }

    @Test
    public void lineNumbers() {
        assertThat(options.getLineNumbers()).isSameAs(LineNumbersValue.OFF);

        options.setLineNumbers(LineNumbersValue.COMMENTS);
        assertThat(options.getLineNumbers()).isSameAs(LineNumbersValue.COMMENTS);

        options.setLineNumbers(null);
        assertThat(options.getLineNumbers()).isSameAs(LineNumbersValue.OFF);
    }

    @Test
    public void fileSystems() {
        assertThat(options.getFileSystems()).containsExactlyElementsOf(LessOptions.DEFAULT_FILE_SYSTEMS);

        final FileSystemOption option = new FileSystemOptionBuilder().withClass(HttpFileSystem.class).build();
        options.setFileSystems(Arrays.asList(option));
        assertThat(options.getFileSystems()).containsExactly(option);

        options.setFileSystems(null);
        assertThat(options.getFileSystems()).containsExactlyElementsOf(LessOptions.DEFAULT_FILE_SYSTEMS);

        options.setFileSystems(Arrays.asList(option));
        assertThat(options.getFileSystems()).containsExactly(option);

        options.setFileSystems(Collections.emptyList());
        assertThat(options.getFileSystems()).containsExactlyElementsOf(LessOptions.DEFAULT_FILE_SYSTEMS);
    }

    @Test
    public void globalVariables() {
        assertThat(options.getGlobalVariables()).isEmpty();

        final LessVariableOption option = new LessVariableOption("name", "value");
        options.setGlobalVariables(Arrays.asList(option));
        assertThat(options.getGlobalVariables()).containsExactly(option);

        options.setGlobalVariables(null);
        assertThat(options.getGlobalVariables()).isEmpty();

        options.setGlobalVariables(Arrays.asList(option));
        assertThat(options.getGlobalVariables()).containsExactly(option);

        options.setGlobalVariables(Collections.emptyList());
        assertThat(options.getGlobalVariables()).isEmpty();
    }

    @Test
    public void modifyVariables() {
        assertThat(options.getModifyVariables()).isEmpty();

        final LessVariableOption option = new LessVariableOption("name", "value");
        options.setModifyVariables(Arrays.asList(option));
        assertThat(options.getModifyVariables()).containsExactly(option);

        options.setModifyVariables(null);
        assertThat(options.getModifyVariables()).isEmpty();

        options.setModifyVariables(Arrays.asList(option));
        assertThat(options.getModifyVariables()).containsExactly(option);

        options.setModifyVariables(Collections.emptyList());
        assertThat(options.getModifyVariables()).isEmpty();
    }
}
