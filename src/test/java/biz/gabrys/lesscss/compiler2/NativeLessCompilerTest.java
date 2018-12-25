package biz.gabrys.lesscss.compiler2;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public final class NativeLessCompilerTest {

    private NativeLessOptionsBuilder builder;

    @Before
    public void setup() {
        builder = new NativeLessOptionsBuilder().encoding("UTF-8");
    }

    @Test
    public void execute_basicFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/basic.less").getPath());
        final Collection<String> options = builder.inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(".basic {\n  display: block;\n}");
    }

    @Test
    public void executeWithCompression_basicFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/basic.less").getPath());
        final Collection<String> options = builder.inputFile(source).compress(true).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(".basic{display:block}");
    }

    @Test
    public void execute_styleFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/style.less").getPath());
        final Collection<String> options = builder.inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(".basic {\n  display: block;\n}\n.style {\n  width: 100px;\n}");
    }

    @Test
    public void executeWithCompression_styleFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/style.less").getPath());
        final Collection<String> options = builder.inputFile(source).compress(true).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(".basic{display:block}.style{width:100px}");
    }

    @Test
    public void execute_referenceFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/reference.less").getPath());
        final Collection<String> options = builder.inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(".reference {\n  width: 100px;\n}");
    }

    @Test
    public void executeWithCompression_referenceFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/reference.less").getPath());
        final Collection<String> options = builder.inputFile(source).compress(true).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(".reference{width:100px}");
    }

    @Test
    public void executeMultipleFiles_basicAndStyleFiles_success() {
        File source = new File(NativeLessCompilerTest.class.getResource("/less/basic.less").getPath());
        Collection<String> options = builder.inputFile(source).compress(true).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        String code = compiler.execute(options);
        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(".basic{display:block}");

        source = new File(NativeLessCompilerTest.class.getResource("/less/style.less").getPath());
        options = builder.inputFile(source).compress(true).build();

        code = compiler.execute(options);
        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(".basic{display:block}.style{width:100px}");
    }

    @Test(expected = SyntaxException.class)
    public void execute_fileWithSyntaxError_throwsSyntaxException() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/syntax.less").getPath());
        final Collection<String> options = builder.inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        compiler.execute(options);
    }

    @Test(expected = ConfigurationException.class)
    public void execute_missingSourceFile_throwsConfigurationException() {
        final NativeLessCompiler compiler = new NativeLessCompiler();

        compiler.execute(Collections.emptyList());
    }

    @Test
    public void execute_missingSourceFile_throwsConfigurationExceptionWithCorrectMessage() {
        final NativeLessCompiler compiler = new NativeLessCompiler();

        try {
            compiler.execute(Collections.emptyList());
            Assertions.fail("Compiler should throw exception");
        } catch (final ConfigurationException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("Source file has not been specified");
        }
    }

    @Test(expected = ReadFileException.class)
    public void execute_fileWithNetworkImport_throwsReadFileException() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/import-from-network.less").getPath());
        final Collection<String> options = builder.inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        compiler.execute(options);
    }

    @Test
    public void execute_fileWithNonExistentFileImport_throwsReadFileExceptionWithFilePath() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/import-non-existent-file.less").getPath());
        final Collection<String> options = builder.inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        try {
            compiler.execute(options);
            Assertions.fail("Compiler should throw exception");
        } catch (final ReadFileException e) {
            Assertions.assertThat(e.getFilePath()).endsWith("style'not-existent.less");
        }
    }

    @Test
    public void execute_nonExistentFile_throwsReadFileExceptionWithFilePath() {
        final File source = new File("gabrys/non-existent-path-for-tests.less");
        final Collection<String> options = builder.inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        try {
            compiler.execute(options);
            Assertions.fail("Compiler should throw exception");
        } catch (final ReadFileException e) {
            Assertions.assertThat(e.getFilePath()).endsWith("non-existent-path-for-tests.less");
        }
    }

    @Test
    public void execute_datauri_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/datauri.less").getPath());
        final Collection<String> options = builder.inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).isEqualTo(
                ".style {\n  background: url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAwSURBVEhL7c0hAQAACMRAgiDp34wM0GHi1cTpq+25BCPMCDPCjDAjzAgzwoywUDT373m8fKeCA80AAAAASUVORK5CYII=\");\n}");
    }

    @Test
    public void execute_sourceMapInline_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/less/source-map-inline.less").getPath());
        final Collection<String> options = builder.sourceMapInline(true).inputFile(source).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        // trim removes empty lines at the end
        Assertions.assertThat(code.trim()).startsWith("div {\n  width: 1px;\n}\n/*# sourceMappingURL=data:application/json;base64,");
    }
}
