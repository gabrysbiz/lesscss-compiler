package biz.gabrys.lesscss.compiler2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import biz.gabrys.lesscss.compiler2.io.TemporaryFileFactory;

public final class LessCompilerTest {

    private static final String RESULT = "result";

    private NativeLessCompiler nativeCompiler;
    private LessCompiler compiler;
    private NativeLessOptionsBuilder optionsBuilder;
    private Collection<String> commandLineOptions;

    @Before
    public void setup() throws IOException {
        nativeCompiler = Mockito.mock(NativeLessCompiler.class);
        Mockito.when(nativeCompiler.execute(ArgumentMatchers.anyCollection())).thenReturn(RESULT);

        compiler = Mockito.spy(new LessCompiler(nativeCompiler));

        optionsBuilder = Mockito.spy(new NativeLessOptionsBuilder());
        Mockito.doReturn(optionsBuilder).when(compiler).createOptionsBuilder();

        commandLineOptions = Collections.emptyList();
        Mockito.doReturn(commandLineOptions).when(optionsBuilder).build();
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
    public void compile_sourceText() {
        final String source = "code";
        final File sourceFile = Mockito.mock(File.class);
        final String encoding = "encoding";
        Mockito.doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        Mockito.doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.doNothing().when(compiler).deleteFile(sourceFile);

        final String result = compiler.compile(source);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compile(source);
        Mockito.verify(compiler).validateSourceCode(source);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(compiler).getDefaultPlatformEncoding();
        Mockito.verify(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.verify(optionsBuilder).inputFile(sourceFile);
        Mockito.verify(optionsBuilder).encoding(encoding);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verify(compiler).deleteFile(sourceFile);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
        Mockito.verifyZeroInteractions(sourceFile);
    }

    @Test
    public void compile_sourceFile() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);

        final String result = compiler.compile(source);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compile(source);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_encoding() {
        final File source = Mockito.mock(File.class);
        final Charset encoding = Charset.defaultCharset();
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        Mockito.doNothing().when(compiler).validateEncoding(encoding);

        final String result = compiler.compile(source, encoding);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compile(source, encoding);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateEncoding(encoding);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).encoding(encoding.name());
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_outputFile() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final File output = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputFile(output);

        compiler.compile(source, output);

        Mockito.verify(compiler).compile(source, output);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).outputFile(output);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_outputFile_encoding() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final File output = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputFile(output);
        final Charset encoding = Charset.defaultCharset();
        Mockito.doNothing().when(compiler).validateEncoding(encoding);

        compiler.compile(source, output, encoding);

        Mockito.verify(compiler).compile(source, output, encoding);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verify(compiler).validateEncoding(encoding);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).outputFile(output);
        Mockito.verify(optionsBuilder).encoding(encoding.name());
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceText_options_encodingIsNotNull() {
        final String source = "code";
        final File sourceFile = Mockito.mock(File.class);
        final String encoding = "encoding";
        Mockito.doReturn(null).when(compiler).getDefaultPlatformEncoding();
        Mockito.doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(encoding);

        final String result = compiler.compile(source, options);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compile(source, options);
        Mockito.verify(compiler).validateSourceCode(source);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(compiler).getDefaultPlatformEncoding();
        Mockito.verify(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.verify(optionsBuilder).inputFile(sourceFile);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).encoding(encoding);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verify(compiler).deleteFile(sourceFile);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
        Mockito.verifyZeroInteractions(sourceFile);
    }

    @Test
    public void compile_sourceText_options_encodingIsNull() {
        final String source = "code";
        final File sourceFile = Mockito.mock(File.class);
        final String encoding = "encoding";
        Mockito.doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        Mockito.doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(null);

        final String result = compiler.compile(source, options);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compile(source, options);
        Mockito.verify(compiler).validateSourceCode(source);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(compiler).getDefaultPlatformEncoding();
        Mockito.verify(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.verify(optionsBuilder).inputFile(sourceFile);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).encoding(encoding);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verify(compiler).deleteFile(sourceFile);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
        Mockito.verifyZeroInteractions(sourceFile);
    }

    @Test
    public void compile_sourceFile_options() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final LessOptions options = new LessOptions();

        final String result = compiler.compile(source, options);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compile(source, options);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compile_sourceFile_outputFile_options() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final File output = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputFile(output);
        final LessOptions options = new LessOptions();

        compiler.compile(source, output, options);

        Mockito.verify(compiler).compile(source, output, options);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).outputFile(output);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceText() {
        final String source = "code";
        final File sourceFile = Mockito.mock(File.class);
        final String encoding = "encoding";
        Mockito.doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        Mockito.doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.doNothing().when(compiler).deleteFile(sourceFile);

        final String result = compiler.compileAndCompress(source);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compileAndCompress(source);
        Mockito.verify(compiler).validateSourceCode(source);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(compiler).getDefaultPlatformEncoding();
        Mockito.verify(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.verify(optionsBuilder).inputFile(sourceFile);
        Mockito.verify(optionsBuilder).compress(true);
        Mockito.verify(optionsBuilder).encoding(encoding);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verify(compiler).deleteFile(sourceFile);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
        Mockito.verifyZeroInteractions(sourceFile);
    }

    @Test
    public void compileAndCompress_sourceFile() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);

        final String result = compiler.compileAndCompress(source);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compileAndCompress(source);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).compress(true);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceFile_encoding() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final Charset encoding = Charset.defaultCharset();
        Mockito.doNothing().when(compiler).validateEncoding(encoding);

        final String result = compiler.compileAndCompress(source, encoding);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compileAndCompress(source, encoding);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateEncoding(encoding);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).encoding(encoding.name());
        Mockito.verify(optionsBuilder).compress(true);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceFile_outputFile() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final File output = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputFile(output);

        compiler.compileAndCompress(source, output);

        Mockito.verify(compiler).compileAndCompress(source, output);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).outputFile(output);
        Mockito.verify(optionsBuilder).compress(true);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileAndCompress_sourceFile_outputFile_encoding() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final File output = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputFile(output);
        final Charset encoding = Charset.defaultCharset();
        Mockito.doNothing().when(compiler).validateEncoding(encoding);

        compiler.compileAndCompress(source, output, encoding);

        Mockito.verify(compiler).compileAndCompress(source, output, encoding);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verify(compiler).validateEncoding(encoding);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).outputFile(output);
        Mockito.verify(optionsBuilder).encoding(encoding.name());
        Mockito.verify(optionsBuilder).compress(true);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithInlineSourceMap_sourceCode_options_encodingIsNotNull() {
        final String source = "code";
        final File sourceFile = Mockito.mock(File.class);
        final String encoding = "encoding";
        Mockito.doReturn(null).when(compiler).getDefaultPlatformEncoding();
        Mockito.doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(encoding);

        final String result = compiler.compileWithInlineSourceMap(source, options);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compileWithInlineSourceMap(source, options);
        Mockito.verify(compiler).validateSourceCode(source);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).getDefaultPlatformEncoding();
        Mockito.verify(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(sourceFile);
        Mockito.verify(optionsBuilder).sourceMapInline(true);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).encoding(encoding);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verify(compiler).deleteFile(sourceFile);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
        Mockito.verifyZeroInteractions(sourceFile);
    }

    @Test
    public void compileWithInlineSourceMap_sourceCode_options_encodingIsNull() {
        final String source = "code";
        final File sourceFile = Mockito.mock(File.class);
        final String encoding = "encoding";
        Mockito.doReturn(encoding).when(compiler).getDefaultPlatformEncoding();
        Mockito.doReturn(sourceFile).when(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.doNothing().when(compiler).deleteFile(sourceFile);
        final LessOptions options = new LessOptions();
        options.setEncoding(null);

        final String result = compiler.compileWithInlineSourceMap(source, options);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compileWithInlineSourceMap(source, options);
        Mockito.verify(compiler).validateSourceCode(source);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).getDefaultPlatformEncoding();
        Mockito.verify(compiler).createTemporaryFileWithCode(source, encoding);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(sourceFile);
        Mockito.verify(optionsBuilder).sourceMapInline(true);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).encoding(encoding);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verify(compiler).deleteFile(sourceFile);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
        Mockito.verifyZeroInteractions(sourceFile);
    }

    @Test
    public void compileWithInlineSourceMap_sourceFile_options() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final LessOptions options = new LessOptions();

        final String result = compiler.compileWithInlineSourceMap(source, options);
        Assertions.assertThat(result).isSameAs(RESULT);

        Mockito.verify(compiler).compileWithInlineSourceMap(source, options);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).sourceMapInline(true);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithInlineSourceMap_sourceFile_outputFile_options() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final File output = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputFile(output);
        final LessOptions options = new LessOptions();

        compiler.compileWithInlineSourceMap(source, output, options);

        Mockito.verify(compiler).compileWithInlineSourceMap(source, output, options);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).outputFile(output);
        Mockito.verify(optionsBuilder).sourceMapInline(true);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithSourceMap_sourceFile_outputFile_options() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final File output = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputFile(output);
        final LessOptions options = new LessOptions();

        compiler.compileWithSourceMap(source, output, options);

        Mockito.verify(compiler).compileWithSourceMap(source, output, options);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).outputFile(output);
        Mockito.verify(optionsBuilder).sourceMapDefault(true);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test
    public void compileWithSourceMap_sourceFile_outputFile_outputSourceMapFile_options() {
        final File source = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateSourceFile(source);
        final File output = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputFile(output);
        final File outputSourceMap = Mockito.mock(File.class);
        Mockito.doNothing().when(compiler).validateOutputSourceMapFile(outputSourceMap);
        final LessOptions options = new LessOptions();

        compiler.compileWithSourceMap(source, output, outputSourceMap, options);

        Mockito.verify(compiler).compileWithSourceMap(source, output, outputSourceMap, options);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verify(compiler).validateOutputSourceMapFile(outputSourceMap);
        Mockito.verify(compiler).validateOptions(options);
        Mockito.verify(compiler).createOptionsBuilder();
        Mockito.verify(optionsBuilder).inputFile(source);
        Mockito.verify(optionsBuilder).outputFile(output);
        Mockito.verify(optionsBuilder).sourceMapFile(outputSourceMap);
        Mockito.verify(optionsBuilder).options(options);
        Mockito.verify(optionsBuilder).build();
        Mockito.verify(nativeCompiler).execute(commandLineOptions);
        Mockito.verifyNoMoreInteractions(compiler, optionsBuilder, nativeCompiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateSourceCode_sourceIsNull_throwsException() {
        compiler.validateSourceCode(null);
    }

    @Test
    public void validateSourceCode_sourceIsNotNull_doesNothing() {
        final String source = "";
        compiler.validateSourceCode(source);

        Mockito.verify(compiler).validateSourceCode(source);
        Mockito.verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateSourceFile_fileIsNull_throwsException() {
        compiler.validateSourceFile(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateSourceFile_fileDoesNotExist_throwsException() {
        final File source = Mockito.mock(File.class);

        compiler.validateSourceFile(source);
    }

    @Test
    public void validateSourceFile_fileExists_doesNothing() {
        final File source = Mockito.mock(File.class);
        Mockito.when(source.exists()).thenReturn(Boolean.TRUE);

        compiler.validateSourceFile(source);
        Mockito.verify(compiler).validateSourceFile(source);
        Mockito.verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateOutputFile_fileIsNull_throwsException() {
        compiler.validateOutputFile(null);
    }

    @Test
    public void validateOutputFile_fileDoesNotExist_doesNothing() {
        final File output = Mockito.mock(File.class);

        compiler.validateOutputFile(output);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verifyNoMoreInteractions(compiler);
    }

    @Test
    public void validateOutputFile_fileExists_doesNothing() {
        final File output = Mockito.mock(File.class);
        Mockito.when(output.exists()).thenReturn(Boolean.TRUE);

        compiler.validateOutputFile(output);
        Mockito.verify(compiler).validateOutputFile(output);
        Mockito.verifyNoMoreInteractions(compiler);
    }

    @Test
    public void validateOutputSourceMapFile_fileDoesNotExist_doesNothing() {
        final File outputSourceMap = Mockito.mock(File.class);

        compiler.validateOutputSourceMapFile(outputSourceMap);
        Mockito.verify(compiler).validateOutputSourceMapFile(outputSourceMap);
        Mockito.verifyNoMoreInteractions(compiler);
    }

    @Test
    public void validateOutputSourceMapFile_fileExists_doesNothing() {
        final File outputSourceMap = Mockito.mock(File.class);
        Mockito.when(outputSourceMap.exists()).thenReturn(Boolean.TRUE);

        compiler.validateOutputSourceMapFile(outputSourceMap);
        Mockito.verify(compiler).validateOutputSourceMapFile(outputSourceMap);
        Mockito.verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateOptions_optionsAreNull_throwsException() {
        compiler.validateOptions(null);
    }

    @Test
    public void validateOptions_optionsAreNotNull_doesNothing() {
        final LessOptions options = new LessOptions();
        compiler.validateOptions(options);

        Mockito.verify(compiler).validateOptions(options);
        Mockito.verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateEncoding_encodingIsNull_throwsException() {
        compiler.validateEncoding(null);
    }

    @Test
    public void validateEncoding_encodingIsNotNull_doesNothing() {
        final Charset encoding = Mockito.mock(Charset.class);
        compiler.validateEncoding(encoding);

        Mockito.verify(compiler).validateEncoding(encoding);
        Mockito.verifyNoMoreInteractions(compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteFile_fileIsNull_throwsExceptions() {
        compiler.deleteFile(null);
    }

    @Test
    public void deleteFile_fileIsNotNull_deletesFile() {
        final File file = Mockito.mock(File.class);
        Mockito.when(file.delete()).thenReturn(Boolean.TRUE);

        compiler.deleteFile(file);

        Mockito.verify(compiler).deleteFile(file);
        Mockito.verify(file).delete();
        Mockito.verifyNoMoreInteractions(compiler, file);
    }

    @Test(expected = CompilerException.class)
    public void deleteFile_fileCannotBeDeleted_throwsException() {
        final File file = Mockito.mock(File.class);
        Mockito.when(file.delete()).thenReturn(Boolean.FALSE);

        compiler.deleteFile(file);
    }
}
