package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;

import biz.gabrys.lesscss.compiler2.io.TemporaryFileFactory;

public final class LessCompilerTest {

    private static final String RESULT = "result";

    private NativeLessCompiler nativeCompiler;
    private LessCompiler compiler;
    private NativeLessOptionsBuilder optionsBuilder;
    private Collection<String> commandLineOptions;

    @Before
    public void setup() throws IOException {
        nativeCompiler = mock(NativeLessCompiler.class);
        when(nativeCompiler.execute(anyCollection())).thenReturn(RESULT);

        compiler = spy(new LessCompiler(nativeCompiler));

        optionsBuilder = mock(NativeLessOptionsBuilder.class, Answers.RETURNS_SELF);
        doReturn(optionsBuilder).when(compiler).createOptionsBuilder();

        commandLineOptions = Collections.emptyList();
        doReturn(commandLineOptions).when(optionsBuilder).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_nativeCompilerIsNull_throwsException() {
        new LessCompiler((NativeLessCompiler) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_fileFactoryIsNull_throwsException() {
        new LessCompiler((TemporaryFileFactory) null);
    }

    @Test
    public void compile_sourceCode() {
        final String code = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(code, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);

        final String result = compiler.compile(code);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(code);
        verify(compiler).validateSourceCode(code);
        verify(compiler).createOptionsBuilder();
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(code, encoding);
        verify(sourceFile).getAbsolutePath();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).encoding(encoding);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verify(compiler).deleteFile(sourceFile);
        verifyNoMoreInteractions(compiler, sourceFile, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceCode_options_encodingIsNotNull() {
        final String source = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(null).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(encoding);

        final String result = compiler.compile(source, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(source, options);
        verify(compiler).validateSourceCode(source);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(source, encoding);
        verify(sourceFile).getAbsolutePath();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).encoding(encoding);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verify(compiler).deleteFile(sourceFile);
        verifyNoMoreInteractions(compiler, sourceFile, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceCode_options_encodingIsNull() {
        final String source = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(null);

        final String result = compiler.compile(source, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(source, options);
        verify(compiler).validateSourceCode(source);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(source, encoding);
        verify(sourceFile).getAbsolutePath();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).encoding(encoding);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verify(compiler).deleteFile(sourceFile);
        verifyNoMoreInteractions(compiler, sourceFile, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile() {
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(sourceFile);

        final String result = compiler.compile(sourceFile);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(sourceFile);
        verify(compiler).validateSourceFile(sourceFile);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_encoding() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        final Charset encoding = Charset.defaultCharset();
        doNothing().when(compiler).validateSourceFile(source);
        doNothing().when(compiler).validateEncoding(encoding);

        final String result = compiler.compile(source, encoding);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(source, encoding);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_outputFile() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final File output = mock(File.class);
        doNothing().when(compiler).validateOutputFile(output);

        compiler.compile(source, output);

        verify(compiler).compile(source, output);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOutputFile(output);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_outputFile_encoding() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final File output = mock(File.class);
        doNothing().when(compiler).validateOutputFile(output);
        final Charset encoding = Charset.defaultCharset();
        doNothing().when(compiler).validateEncoding(encoding);

        compiler.compile(source, output, encoding);

        verify(compiler).compile(source, output, encoding);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_options() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final LessOptions options = new LessOptions();

        final String result = compiler.compile(source, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(source, options);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_outputFile_options() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final File output = mock(File.class);
        doNothing().when(compiler).validateOutputFile(output);
        final LessOptions options = new LessOptions();

        compiler.compile(source, output, options);

        verify(compiler).compile(source, output, options);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceCode() {
        final String source = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);

        final String result = compiler.compileAndCompress(source);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileAndCompress(source);
        verify(compiler).validateSourceCode(source);
        verify(compiler).createOptionsBuilder();
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(source, encoding);
        verify(sourceFile).getAbsolutePath();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).encoding(encoding);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verify(compiler).deleteFile(sourceFile);
        verifyNoMoreInteractions(compiler, sourceFile, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceFile() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);

        final String result = compiler.compileAndCompress(source);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileAndCompress(source);
        verify(compiler).validateSourceFile(source);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceFile_encoding() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final Charset encoding = Charset.defaultCharset();
        doNothing().when(compiler).validateEncoding(encoding);

        final String result = compiler.compileAndCompress(source, encoding);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileAndCompress(source, encoding);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceFile_outputFile() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final File output = mock(File.class);
        doNothing().when(compiler).validateOutputFile(output);

        compiler.compileAndCompress(source, output);

        verify(compiler).compileAndCompress(source, output);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOutputFile(output);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceFile_outputFile_encoding() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final File output = mock(File.class);
        doNothing().when(compiler).validateOutputFile(output);
        final Charset encoding = Charset.defaultCharset();
        doNothing().when(compiler).validateEncoding(encoding);

        compiler.compileAndCompress(source, output, encoding);

        verify(compiler).compileAndCompress(source, output, encoding);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithInlineSourceMap_sourceCode_options_encodingIsNotNull() {
        final String source = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(null).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(encoding);

        final String result = compiler.compileWithInlineSourceMap(source, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileWithInlineSourceMap(source, options);
        verify(compiler).validateSourceCode(source);
        verify(compiler).validateOptions(options);
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(source, encoding);
        verify(compiler).createOptionsBuilder();
        verify(sourceFile).getAbsolutePath();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).sourceMapInline(true);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).encoding(encoding);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verify(compiler).deleteFile(sourceFile);
        verifyNoMoreInteractions(compiler, sourceFile, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithInlineSourceMap_sourceCode_options_encodingIsNull() {
        final String source = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(null);

        final String result = compiler.compileWithInlineSourceMap(source, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileWithInlineSourceMap(source, options);
        verify(compiler).validateSourceCode(source);
        verify(compiler).validateOptions(options);
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(source, encoding);
        verify(compiler).createOptionsBuilder();
        verify(sourceFile).getAbsolutePath();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).sourceMapInline(true);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).encoding(encoding);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verify(compiler).deleteFile(sourceFile);
        verifyNoMoreInteractions(compiler, sourceFile, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithInlineSourceMap_sourceFile_options() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final LessOptions options = new LessOptions();

        final String result = compiler.compileWithInlineSourceMap(source, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileWithInlineSourceMap(source, options);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).sourceMapInline(true);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithInlineSourceMap_sourceFile_outputFile_options() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final File output = mock(File.class);
        doNothing().when(compiler).validateOutputFile(output);
        final LessOptions options = new LessOptions();

        compiler.compileWithInlineSourceMap(source, output, options);

        verify(compiler).compileWithInlineSourceMap(source, output, options);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).sourceMapInline(true);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithSourceMap_sourceFile_outputFile_options() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final File output = mock(File.class);
        doNothing().when(compiler).validateOutputFile(output);
        final LessOptions options = new LessOptions();

        compiler.compileWithSourceMap(source, output, options);

        verify(compiler).compileWithSourceMap(source, output, options);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).sourceMapDefault(true);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithSourceMap_sourceFile_outputFile_outputSourceMapFile_options() {
        final File source = mock(File.class);
        final String absolutePath = "absolutePath";
        when(source.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateSourceFile(source);
        final File output = mock(File.class);
        doNothing().when(compiler).validateOutputFile(output);
        final File outputSourceMap = mock(File.class);
        doNothing().when(compiler).validateOutputSourceMapFile(outputSourceMap);
        final LessOptions options = new LessOptions();

        compiler.compileWithSourceMap(source, output, outputSourceMap, options);

        verify(compiler).compileWithSourceMap(source, output, outputSourceMap, options);
        verify(compiler).validateSourceFile(source);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateOutputSourceMapFile(outputSourceMap);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).sourceMapFile(outputSourceMap);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateSourceCode_sourceIsNull_throwsException() {
        compiler.validateSourceCode(null);
    }

    @Test
    public void validateSourceCode_sourceIsNotNull_doesNothing() {
        final String source = "";
        compiler.validateSourceCode(source);

        verify(compiler).validateSourceCode(source);
        verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateSourceFile_fileIsNull_throwsException() {
        compiler.validateSourceFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateSourceFile_fileDoesNotExist_throwsException() {
        final File source = mock(File.class);

        compiler.validateSourceFile(source);
    }

    @Test
    public void validateSourceFile_fileExists_doesNothing() {
        final File source = mock(File.class);
        when(source.exists()).thenReturn(Boolean.TRUE);

        compiler.validateSourceFile(source);
        verify(compiler).validateSourceFile(source);
        verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateOutputFile_fileIsNull_throwsException() {
        compiler.validateOutputFile(null);
    }

    @Test
    public void validateOutputFile_fileDoesNotExist_doesNothing() {
        final File output = mock(File.class);

        compiler.validateOutputFile(output);
        verify(compiler).validateOutputFile(output);
        verifyNoMoreInteractions(compiler);
    }

    @Test
    public void validateOutputFile_fileExists_doesNothing() {
        final File output = mock(File.class);
        when(output.exists()).thenReturn(Boolean.TRUE);

        compiler.validateOutputFile(output);
        verify(compiler).validateOutputFile(output);
        verifyNoMoreInteractions(compiler);
    }

    @Test
    public void validateOutputSourceMapFile_fileDoesNotExist_doesNothing() {
        final File outputSourceMap = mock(File.class);

        compiler.validateOutputSourceMapFile(outputSourceMap);
        verify(compiler).validateOutputSourceMapFile(outputSourceMap);
        verifyNoMoreInteractions(compiler);
    }

    @Test
    public void validateOutputSourceMapFile_fileExists_doesNothing() {
        final File outputSourceMap = mock(File.class);
        when(outputSourceMap.exists()).thenReturn(Boolean.TRUE);

        compiler.validateOutputSourceMapFile(outputSourceMap);
        verify(compiler).validateOutputSourceMapFile(outputSourceMap);
        verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateOptions_optionsAreNull_throwsException() {
        compiler.validateOptions(null);
    }

    @Test
    public void validateOptions_optionsAreNotNull_doesNothing() {
        final LessOptions options = new LessOptions();
        compiler.validateOptions(options);

        verify(compiler).validateOptions(options);
        verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateEncoding_encodingIsNull_throwsException() {
        compiler.validateEncoding(null);
    }

    @Test
    public void validateEncoding_encodingIsNotNull_doesNothing() {
        final Charset encoding = mock(Charset.class);
        compiler.validateEncoding(encoding);

        verify(compiler).validateEncoding(encoding);
        verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteFile_fileIsNull_throwsExceptions() {
        compiler.deleteFile(null);
    }

    @Test
    public void deleteFile_fileIsNotNull_deletesFile() {
        final File file = mock(File.class);
        when(file.delete()).thenReturn(Boolean.TRUE);

        compiler.deleteFile(file);

        verify(compiler).deleteFile(file);
        verify(file).delete();
        verifyNoMoreInteractions(compiler, file);
    }

    @Test(expected = CompilerException.class)
    public void deleteFile_fileCannotBeDeleted_throwsException() {
        final File file = mock(File.class);
        when(file.delete()).thenReturn(Boolean.FALSE);

        compiler.deleteFile(file);
    }
}
