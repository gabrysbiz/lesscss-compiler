package biz.gabrys.lesscss.compiler;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public final class LessCompilerImplTest {

    @Test
    public void compile_basicFile_success() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/basic.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        final String code = compiler.compile(source);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed code is different than expected", ".basic {\n  display: block;\n}", code.trim());
    }

    @Test
    public void compileWithCompression_basicFile_success() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/basic.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        final CompilerOptions options = new CompilerOptionsBuilder().setMinified(true).create();
        final String code = compiler.compile(source, options);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed code is different than expected", ".basic{display:block}", code.trim());
    }

    @Test
    public void compile_styleFile_success() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/style.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        final String code = compiler.compile(source);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed code is different than expected", ".basic {\n  display: block;\n}\n.style {\n  width: 100px;\n}",
                code.trim());
    }

    @Test
    public void compileWithCompression_styleFile_success() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/style.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        final CompilerOptions options = new CompilerOptionsBuilder().setMinified(true).create();
        final String code = compiler.compile(source, options);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed code is different than expected", ".basic{display:block}.style{width:100px}", code.trim());
    }

    @Test
    public void compile_referenceFile_success() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/reference.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        final String code = compiler.compile(source);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed code is different than expected", ".reference {\n  width: 100px;\n}", code.trim());
    }

    @Test
    public void compileWithCompression_referenceFile_success() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/reference.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        final CompilerOptions options = new CompilerOptionsBuilder().setMinified(true).create();
        final String code = compiler.compile(source, options);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed code is different than expected", ".reference{width:100px}", code.trim());
    }

    @Test
    public void compileMultipleFiles_basicAndStyleFiles_success() throws CompilerException {
        File source = new File(LessCompilerImplTest.class.getResource("/less/basic.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        final CompilerOptions options = new CompilerOptionsBuilder().setMinified(true).create();

        String code = compiler.compile(source, options);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed basic.less code is different than expected", ".basic{display:block}", code.trim());

        source = new File(LessCompilerImplTest.class.getResource("/less/style.less").getPath());
        code = compiler.compile(source, options);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed style.less is different than expected", ".basic{display:block}.style{width:100px}", code.trim());
    }

    @Test(expected = SyntaxException.class)
    public void compile_fileWithSyntaxError_throwsSyntaxException() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/syntax.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        compiler.compile(source);
    }

    @Test(expected = ResolveImportException.class)
    public void compile_fileWithNetworkImport_throwsResolveImportException() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/import-from-network.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        compiler.compile(source);
    }

    @Test(expected = ResolveImportException.class)
    public void compile_fileWithNonExistentFileImport_throwsResolveImportException() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/import-non-existent-file.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        compiler.compile(source);
    }

    @Test
    public void compile_fileWithNonExistentFileImport_throwsResolveImportExceptionWithFileName() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/import-non-existent-file.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        try {
            compiler.compile(source);
            Assert.fail("Compiler should throw exception");
        } catch (final ResolveImportException e) {
            Assert.assertEquals("File name is different than expected", "style'not-existent.less", e.getFileName());
        }
    }

    @Test
    public void compile_datauri_success() throws CompilerException {
        final File source = new File(LessCompilerImplTest.class.getResource("/less/datauri.less").getPath());
        final LessCompilerImpl compiler = new LessCompilerImpl();
        final String code = compiler.compile(source);
        // trim removes empty lines at the end
        Assert.assertEquals("Trimmed code is different than expected",
                ".style {\n  background: url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAwSURBVEhL7c0hAQAACMRAgiDp34wM0GHi1cTpq+25BCPMCDPCjDAjzAgzwoywUDT373m8fKeCA80AAAAASUVORK5CYII=\");\n}",
                code.trim());
    }
}
