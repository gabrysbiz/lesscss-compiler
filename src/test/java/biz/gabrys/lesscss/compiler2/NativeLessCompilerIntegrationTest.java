package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTest.class)
public final class NativeLessCompilerIntegrationTest {

    @Test
    public void execute_localFile_success() {
        final String source = new File(NativeLessCompilerTest.class.getResource("/integration/integration.less").getPath())
                .getAbsolutePath();
        final List<String> fileSystems = new FileSystemsOptionBuilder().withStandard().build();
        final Collection<String> options = new NativeLessOptionsBuilder().encoding("UTF-8").relativeUrls(true).fileSystems(fileSystems)
                .inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);
        assertThat(code).isNotEmpty();

        final String uriData = "\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAwSURBVEhL7c0hAQAACMRAgiDp34wM0GHi1cTpq+25BCPMCDPCjDAjzAgzwoywUDT373m8fKeCA80AAAAASUVORK5CYII=\"";
        final StringBuilder expected = new StringBuilder();
        expected.append(".basic {\n  display: block;\n}\n");
        expected.append(".style {\n  width: 100px;\n}\n");
        expected.append(".basic {\n  display: block;\n}\n");
        expected.append(".style {\n  background: url(");
        expected.append(uriData);
        expected.append(");\n}\n");
        expected.append(".style {\n  background: url(");
        expected.append(uriData);
        expected.append(");\n}");
        assertThat(code.trim()).isEqualTo(expected.toString());
    }

    @Test
    public void execute_fileFromNetwork_success() {
        final String source = "https://raw.githubusercontent.com/gabrysbiz/lesscss-compiler/develop/src/test/resources/integration/less/style.less";
        final List<String> fileSystems = new FileSystemsOptionBuilder().withStandard().build();
        final Collection<String> options = new NativeLessOptionsBuilder().encoding("UTF-8").relativeUrls(true).fileSystems(fileSystems)
                .inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic {\n  display: block;\n}\n.style {\n  width: 100px;\n}");
    }
}
