package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
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
    public void compileCode_code() {
        final String code = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(code, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);

        final String result = compiler.compileCode(code);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileCode(code);
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
    public void compileCode_code_options_encodingIsNotNull() {
        final String code = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(null).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(code, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(encoding);

        final String result = compiler.compileCode(code, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileCode(code, options);
        verify(compiler).validateSourceCode(code);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(code, encoding);
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
    public void compileCode_code_options_encodingIsNull() {
        final String code = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(code, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(null);

        final String result = compiler.compileCode(code, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileCode(code, options);
        verify(compiler).validateSourceCode(code);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(code, encoding);
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
    public void compile_inputPath() {
        final String input = "path";

        final String result = compiler.compile(input);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(input);
        verify(compiler).validateInputPath(input);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputFile() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);

        final String result = compiler.compile(input);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(input);
        verify(compiler).validateInputFile(input);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputPath_encoding() {
        final String input = "path";
        final Charset encoding = Charset.defaultCharset();

        final String result = compiler.compile(input, encoding);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(input, encoding);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputFile_encoding() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        final Charset encoding = Charset.defaultCharset();
        doNothing().when(compiler).validateInputFile(input);

        final String result = compiler.compile(input, encoding);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(input, encoding);
        verify(compiler).validateInputFile(input);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputPath_outputFile() {
        final String input = "path";
        final File output = mock(File.class);

        compiler.compile(input, output);

        verify(compiler).compile(input, output);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputFile_outputFile() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final File output = mock(File.class);

        compiler.compile(input, output);

        verify(compiler).compile(input, output);
        verify(compiler).validateInputFile(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputPath_outputFile_encoding() {
        final String input = "path";
        final File output = mock(File.class);
        final Charset encoding = Charset.defaultCharset();

        compiler.compile(input, output, encoding);

        verify(compiler).compile(input, output, encoding);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputFile_outputFile_encoding() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final File output = mock(File.class);
        final Charset encoding = Charset.defaultCharset();

        compiler.compile(input, output, encoding);

        verify(compiler).compile(input, output, encoding);
        verify(compiler).validateInputFile(input);
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
    public void compile_inputPath_options() {
        final String input = "path";
        final LessOptions options = new LessOptions();

        final String result = compiler.compile(input, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(input, options);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputFile_options() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final LessOptions options = new LessOptions();

        final String result = compiler.compile(input, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compile(input, options);
        verify(compiler).validateInputFile(input);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputPath_outputFile_options() {
        final String input = "path";
        final File output = mock(File.class);
        final LessOptions options = new LessOptions();

        compiler.compile(input, output, options);

        verify(compiler).compile(input, output, options);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_inputFile_outputFile_options() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final File output = mock(File.class);
        final LessOptions options = new LessOptions();

        compiler.compile(input, output, options);

        verify(compiler).compile(input, output, options);
        verify(compiler).validateInputFile(input);
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
    public void compileCodeAndCompress_code() {
        final String code = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(code, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);

        final String result = compiler.compileCodeAndCompress(code);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileCodeAndCompress(code);
        verify(compiler).validateSourceCode(code);
        verify(compiler).createOptionsBuilder();
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(code, encoding);
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
    public void compileAndCompress_inputPath() {
        final String input = "path";

        final String result = compiler.compileAndCompress(input);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileAndCompress(input);
        verify(compiler).validateInputPath(input);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_inputFile() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);

        final String result = compiler.compileAndCompress(input);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileAndCompress(input);
        verify(compiler).validateInputFile(input);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(absolutePath);
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_inputPath_encoding() {
        final String input = "path";
        final Charset encoding = Charset.defaultCharset();

        final String result = compiler.compileAndCompress(input, encoding);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileAndCompress(input, encoding);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_inputFile_encoding() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final Charset encoding = Charset.defaultCharset();

        final String result = compiler.compileAndCompress(input, encoding);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileAndCompress(input, encoding);
        verify(compiler).validateInputFile(input);
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
    public void compileAndCompress_inputPath_outputFile() {
        final String input = "path";
        final File output = mock(File.class);

        compiler.compileAndCompress(input, output);

        verify(compiler).compileAndCompress(input, output);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_inputFile_outputFile() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final File output = mock(File.class);

        compiler.compileAndCompress(input, output);

        verify(compiler).compileAndCompress(input, output);
        verify(compiler).validateInputFile(input);
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
    public void compileAndCompress_inputPath_outputFile_encoding() {
        final String input = "path";
        final File output = mock(File.class);
        final Charset encoding = Charset.defaultCharset();

        compiler.compileAndCompress(input, output, encoding);

        verify(compiler).compileAndCompress(input, output, encoding);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateEncoding(encoding);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).encoding(encoding.name());
        verify(optionsBuilder).compress(true);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_inputFile_outputFile_encoding() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final File output = mock(File.class);
        final Charset encoding = Charset.defaultCharset();

        compiler.compileAndCompress(input, output, encoding);

        verify(compiler).compileAndCompress(input, output, encoding);
        verify(compiler).validateInputFile(input);
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
    public void compileCodeWithInlineSourceMap_code_options_encodingIsNotNull() {
        final String code = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(null).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(code, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(encoding);

        final String result = compiler.compileCodeWithInlineSourceMap(code, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileCodeWithInlineSourceMap(code, options);
        verify(compiler).validateSourceCode(code);
        verify(compiler).validateOptions(options);
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(code, encoding);
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
    public void compileCodeWithInlineSourceMap_code_options_encodingIsNull() {
        final String code = "code";
        final File sourceFile = mock(File.class);
        final String absolutePath = "absolutePath";
        when(sourceFile.getAbsolutePath()).thenReturn(absolutePath);
        final String encoding = "encoding";
        doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(code, encoding);
        doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(null);

        final String result = compiler.compileCodeWithInlineSourceMap(code, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileCodeWithInlineSourceMap(code, options);
        verify(compiler).validateSourceCode(code);
        verify(compiler).validateOptions(options);
        verify(compiler).getDefaultPlatformEncoding();
        verify(compiler).createTemporaryFileWithCode(code, encoding);
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
    public void compileWithInlineSourceMap_inputPath_options() {
        final String input = "path";
        final LessOptions options = new LessOptions();

        final String result = compiler.compileWithInlineSourceMap(input, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileWithInlineSourceMap(input, options);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).sourceMapInline(true);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithInlineSourceMap_inputFile_options() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final LessOptions options = new LessOptions();

        final String result = compiler.compileWithInlineSourceMap(input, options);
        assertThat(result).isSameAs(RESULT);

        verify(compiler).compileWithInlineSourceMap(input, options);
        verify(compiler).validateInputFile(input);
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
    public void compileWithInlineSourceMap_inputPath_outputFile_options() {
        final String input = "path";
        final File output = mock(File.class);
        final LessOptions options = new LessOptions();

        compiler.compileWithInlineSourceMap(input, output, options);

        verify(compiler).compileWithInlineSourceMap(input, output, options);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).sourceMapInline(true);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithInlineSourceMap_inputFile_outputFile_options() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final File output = mock(File.class);
        final LessOptions options = new LessOptions();

        compiler.compileWithInlineSourceMap(input, output, options);

        verify(compiler).compileWithInlineSourceMap(input, output, options);
        verify(compiler).validateInputFile(input);
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
    public void compileWithSourceMap_inputPath_outputFile_options() {
        final String input = "path";
        final File output = mock(File.class);
        final LessOptions options = new LessOptions();

        compiler.compileWithSourceMap(input, output, options);

        verify(compiler).compileWithSourceMap(input, output, options);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).sourceMapDefault(true);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithSourceMap_inputFile_outputFile_options() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final File output = mock(File.class);
        final LessOptions options = new LessOptions();

        compiler.compileWithSourceMap(input, output, options);

        verify(compiler).compileWithSourceMap(input, output, options);
        verify(compiler).validateInputFile(input);
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
    public void compileWithSourceMap_inputPath_outputFile_outputSourceMapFile_options() {
        final String input = "path";
        final File output = mock(File.class);
        final File outputSourceMap = mock(File.class);
        final LessOptions options = new LessOptions();

        compiler.compileWithSourceMap(input, output, outputSourceMap, options);

        verify(compiler).compileWithSourceMap(input, output, outputSourceMap, options);
        verify(compiler).validateInputPath(input);
        verify(compiler).validateOutputFile(output);
        verify(compiler).validateOutputSourceMapFile(outputSourceMap);
        verify(compiler).validateOptions(options);
        verify(compiler).createOptionsBuilder();
        verify(optionsBuilder).inputFile(input);
        verify(optionsBuilder).outputFile(output);
        verify(optionsBuilder).sourceMapFile(outputSourceMap);
        verify(optionsBuilder).options(options);
        verify(optionsBuilder).build();
        verify(nativeCompiler).execute(commandLineOptions);
        verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithSourceMap_inputFile_outputFile_outputSourceMapFile_options() {
        final File input = mock(File.class);
        final String absolutePath = "absolutePath";
        when(input.getAbsolutePath()).thenReturn(absolutePath);
        doNothing().when(compiler).validateInputFile(input);
        final File output = mock(File.class);
        final File outputSourceMap = mock(File.class);
        final LessOptions options = new LessOptions();

        compiler.compileWithSourceMap(input, output, outputSourceMap, options);

        verify(compiler).compileWithSourceMap(input, output, outputSourceMap, options);
        verify(compiler).validateInputFile(input);
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
    public void validateSourceCode_codeIsNull_throwsException() {
        compiler.validateSourceCode(null);
    }

    @Test
    public void validateSourceCode_codeIsNotNull_doesNothing() {
        final String source = "";
        compiler.validateSourceCode(source);

        verify(compiler).validateSourceCode(source);
        verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateInputPath_pathIsNull_throwsException() {
        compiler.validateInputPath(null);
    }

    @Test
    public void validateInputPath_pathIsNotNull_doesNothing() {
        final String input = "";
        compiler.validateInputPath(input);

        verify(compiler).validateInputPath(input);
        verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateInputFile_fileIsNull_throwsException() {
        compiler.validateInputFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateInputFile_fileDoesNotExist_throwsException() {
        final File input = mock(File.class);

        compiler.validateInputFile(input);
    }

    @Test
    public void validateInputFile_fileExists_doesNothing() {
        final File input = mock(File.class);
        when(input.exists()).thenReturn(Boolean.TRUE);

        compiler.validateInputFile(input);
        verify(compiler).validateInputFile(input);
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

    @Test(expected = IllegalArgumentException.class)
    public void validateOutputSourceMapFile_fileIsNull_throwsException() {
        compiler.validateOutputSourceMapFile(null);
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
    public void deleteFile_fileIsNotNull_deletesFile() throws IOException {
        final File file = mock(File.class);
        final Path path = mock(Path.class);
        when(file.toPath()).thenReturn(path);
        final FileSystem fileSystem = mock(FileSystem.class);
        when(path.getFileSystem()).thenReturn(fileSystem);
        final FileSystemProvider fileSystemProvider = mock(FileSystemProvider.class);
        when(fileSystem.provider()).thenReturn(fileSystemProvider);

        compiler.deleteFile(file);

        verify(compiler).deleteFile(file);
        verify(file).toPath();
        verify(path).getFileSystem();
        verify(fileSystem).provider();
        verify(fileSystemProvider).delete(path);
        verifyNoMoreInteractions(compiler, file, path, fileSystem, fileSystemProvider);
    }

    @Test(expected = CompilerException.class)
    public void deleteFile_fileCannotBeDeleted_throwsException() throws IOException {
        final File file = mock(File.class);
        final Path path = mock(Path.class);
        when(file.toPath()).thenReturn(path);
        final FileSystem fileSystem = mock(FileSystem.class);
        when(path.getFileSystem()).thenReturn(fileSystem);
        final FileSystemProvider fileSystemProvider = mock(FileSystemProvider.class);
        when(fileSystem.provider()).thenReturn(fileSystemProvider);
        doThrow(IOException.class).when(fileSystemProvider).delete(path);

        compiler.deleteFile(file);
    }
}
