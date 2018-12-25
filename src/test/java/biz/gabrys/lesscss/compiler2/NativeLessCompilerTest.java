package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import biz.gabrys.lesscss.compiler2.filesystem.FileData;
import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;
import biz.gabrys.lesscss.compiler2.filesystem.LocalFileSystem;

public final class NativeLessCompilerTest {

    private NativeLessOptionsBuilder builder;

    @Before
    public void setup() {
        builder = new NativeLessOptionsBuilder().encoding("UTF-8");
    }

    @Test
    public void execute_basicFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/basic.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic {\n  display: block;\n}");
    }

    @Test
    public void executeWithCompression_basicFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/basic.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).compress(true).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic{display:block}");
    }

    @Test
    public void execute_styleFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/style.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic {\n  display: block;\n}\n.style {\n  width: 100px;\n}");
    }

    @Test
    public void executeWithCompression_styleFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/style.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).compress(true).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic{display:block}.style{width:100px}");
    }

    @Test
    public void execute_referenceFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/reference.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".reference {\n  width: 100px;\n}");
    }

    @Test
    public void executeWithCompression_referenceFile_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/reference.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).compress(true).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".reference{width:100px}");
    }

    @Test
    public void executeMultipleFiles_basicAndStyleFiles_success() {
        String source = new File(NativeLessCompilerTest.class.getResource("/unit/less/basic.less").getPath()).getAbsolutePath();
        Collection<String> options = builder.inputFile(source).compress(true).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        String code = compiler.execute(options);
        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic{display:block}");

        source = new File(NativeLessCompilerTest.class.getResource("/unit/less/style.less").getPath()).getAbsolutePath();
        options = builder.inputFile(source).compress(true).build();

        code = compiler.execute(options);
        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic{display:block}.style{width:100px}");
    }

    @Test(expected = SyntaxException.class)
    public void execute_fileWithSyntaxError_throwsSyntaxException() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/syntax.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).build();
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
            fail("Compiler should throw exception");
        } catch (final ConfigurationException e) {
            assertThat(e.getMessage()).isEqualTo("Source file has not been specified");
        }
    }

    @Test
    public void execute_fileWithNonExistentFileImport_throwsReadFileExceptionWithFilePath() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/import-non-existent-file.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        try {
            compiler.execute(options);
            fail("Compiler should throw exception");
        } catch (final ReadFileException e) {
            assertThat(e.getFilePath()).endsWith("style'not-existent.less");
        }
    }

    @Test
    public void execute_importedFilesDoNotHaveExtensionsSpecified_addsLessExtensionToAllFiles() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/default-less-extension.less").getPath());

        final StringBuilder expectedCode = new StringBuilder();
        expectedCode.append(".none {\n  content: \"none\";\n}\n");
        expectedCode.append(".reference {\n  content: \"reference\";\n}\n");
        expectedCode.append(".less {\n  content: \"less\";\n}\n");
        expectedCode.append(".once {\n  content: \"once\";\n}\n");
        expectedCode.append(".multiple {\n  content: \"multiple\";\n}\n");
        expectedCode.append(".multiple {\n  content: \"multiple\";\n}");

        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(expectedCode.toString());
    }

    @Test
    public void execute_nonExistentFile_throwsReadFileExceptionWithFilePath() {
        final File source = new File("gabrys/non-existent-path-for-tests.less");
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        try {
            compiler.execute(options);
            fail("Compiler should throw exception");
        } catch (final ReadFileException e) {
            assertThat(e.getFilePath()).endsWith("non-existent-path-for-tests.less");
        }
    }

    @Test
    public void execute_datauri_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/datauri.less").getPath());
        final Collection<String> options = builder.inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        final String base64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAwSURBVEhL7c0hAQAACMRAgiDp34wM0GHi1cTpq+25BCPMCDPCjDAjzAgzwoywUDT373m8fKeCA80AAAAASUVORK5CYII=";
        assertThat(code.trim()).isEqualTo(String.format(".style {\n  background: url(\"%s\");\n}", base64));
    }

    @Test
    public void execute_sourceMapInline_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/source-map-inline.less").getPath());
        final Collection<String> options = builder.sourceMapInline(true).inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).startsWith("div {\n  width: 1px;\n}\n/*# sourceMappingURL=data:application/json;base64,");
    }

    @Test
    public void execute_basicFileLoadedFromClasspath_success() {
        final List<FileSystemOption> fileSystems = new FileSystemOptionsBuilder().appendClassPath().build();
        final Collection<String> options = builder.inputFile("classpath://unit/less/basic.less").fileSystems(fileSystems).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic {\n  display: block;\n}");
    }

    @Test
    public void execute_includePathsIsSet_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/include-paths.less").getPath());
        final List<String> includePaths = Arrays.asList(source.getParentFile().getAbsolutePath() + "/subdir/");
        final Collection<String> options = builder.includePaths(includePaths).inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(".basic {\n  display: inline;\n}");
    }

    @Test
    public void execute_useBanner_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/style.less").getPath());
        final Collection<String> options = builder.banner("/** --compress */").inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo("/** --compress */\n.basic {\n  display: block;\n}\n.style {\n  width: 100px;\n}");
    }

    @Test
    public void execute_useUnsupportedEncoding_throwsException() {
        final Collection<String> options = builder.encoding("- invalid -").inputFile("unimportant.less").build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        try {
            compiler.execute(options);
            fail("Compiler should throw exception");
        } catch (final ConfigurationException e) {
            assertThat(e.getMessage()).isEqualTo("Encoding \"- invalid -\" is unsupported");
        }
    }

    @Test
    public void execute_useGlobalVariables_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/variables.less").getPath());
        final List<LessVariableOption> variables = Arrays.asList(new LessVariableOption("width", "100px"),
                new LessVariableOption("height", "80px"));
        final Collection<String> options = builder.globalVariables(variables).inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo("body {\n  width: 100px;\n  height: 20px;\n}");
    }

    @Test
    public void execute_useModifyVariables_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/variables.less").getPath());
        final List<LessVariableOption> variables = Arrays.asList(new LessVariableOption("width", "100px"),
                new LessVariableOption("height", "80px"));
        final Collection<String> options = builder.modifyVariables(variables).inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo("body {\n  width: 100px;\n  height: 80px;\n}");
    }

    @Test
    public void execute_useCustomFileSystems_success() {
        final File source = new File(NativeLessCompilerTest.class.getResource("/unit/less/filesystems.less").getPath());

        final Map<String, String> parameters1 = new HashMap<String, String>();
        parameters1.put("file=name", "system1.less");
        parameters1.put(",class__name,", "class1");
        parameters1.put("param-1", "'_=,'");
        parameters1.put("empty", "");
        final FileSystemOption fileSystem1 = new FileSystemOption(FakeFileSystem.class, parameters1);

        final Map<String, String> parameters2 = new HashMap<String, String>();
        parameters2.put("file=name", "system2.less");
        parameters2.put(",class__name,", "class2");
        parameters2.put("param", "100px");
        parameters2.put("param1_2", "'A__,,==A'");
        final FileSystemOption fileSystem2 = new FileSystemOption(FakeFileSystem.class, parameters2);

        final Map<String, String> parameters3 = new HashMap<String, String>();
        parameters3.put("file=name", "system3.less");
        parameters3.put(",class__name,", "class3");
        final FileSystemOption fileSystem3 = new FileSystemOption(FakeFileSystem.class, parameters3);

        final StringBuilder expectedCode = new StringBuilder();
        expectedCode.append(".class1 {\n");
        expectedCode.append("  key: value;\n");
        expectedCode.append("  param-1: '_=,';\n");
        expectedCode.append("  empty: 'empty';\n");
        expectedCode.append("}\n");
        expectedCode.append(".class2 {\n");
        expectedCode.append("  key: value;\n");
        expectedCode.append("  param: 100px;\n");
        expectedCode.append("  param1_2: 'A__,,==A';\n");
        expectedCode.append("}\n");
        expectedCode.append(".class3 {\n");
        expectedCode.append("  key: value;\n");
        expectedCode.append("}");

        final Collection<String> options = builder
                .fileSystems(Arrays.asList(fileSystem1, fileSystem2, fileSystem3, new FileSystemOption(LocalFileSystem.class)))
                .inputFile(source.getAbsolutePath()).build();
        final NativeLessCompiler compiler = new NativeLessCompiler();

        final String code = compiler.execute(options);

        assertThat(code).isNotEmpty();
        assertThat(code.trim()).isEqualTo(expectedCode.toString());
    }

    public static class FakeFileSystem implements FileSystem {

        private String fileName;
        private String className;
        private Map<String, String> parameters;

        @Override
        public void configure(final Map<String, String> parameters) throws Exception {
            fileName = parameters.remove("file=name");
            className = parameters.remove(",class__name,");
            this.parameters = parameters;
        }

        @Override
        public boolean isSupported(final String path) {
            return path.endsWith(fileName);
        }

        @Override
        public String normalize(final String path) throws Exception {
            return path;
        }

        @Override
        public String expandRedirection(final String path) throws Exception {
            return path;
        }

        @Override
        public boolean exists(final String path) throws Exception {
            return true;
        }

        @Override
        public FileData fetch(final String path) throws Exception {
            final StringBuilder code = new StringBuilder();
            code.append('.');
            code.append(className);
            code.append('{');
            code.append("key:value;");
            for (final Entry<String, String> entry : parameters.entrySet()) {
                code.append(entry.getKey());
                code.append(':');
                code.append(entry.getValue().isEmpty() ? "'empty'" : entry.getValue());
                code.append(';');
            }
            code.append('}');
            return new FileData(code.toString().getBytes("UTF-8"), "UTF-8");
        }
    }
}
