package biz.gabrys.lesscss.compiler2;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class NativeLessOptionsBuilderTest {

    @Spy
    private NativeLessOptionsBuilder builder;

    @Test
    public void getInputFileOption_fileIsNotSet_returnsEmptyText() {
        final String option = builder.inputFile(null).getInputFileOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getInputFileOption_fileIsSet_returnsOption() {
        final File file = Mockito.mock(File.class);
        final String path = "/dir/input.less";
        Mockito.when(file.getAbsolutePath()).thenReturn(path);

        final String option = builder.inputFile(file).getInputFileOption();
        Assertions.assertThat(option).isSameAs(path);
    }

    @Test
    public void getOutputFileOption_fileIsNotSet_returnsEmptyText() {
        final String option = builder.outputFile(null).getOutputFileOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getOutputFileOption_fileIsSet_returnsOption() {
        final File file = Mockito.mock(File.class);
        final String path = "/dir/output.css";
        Mockito.when(file.getAbsolutePath()).thenReturn(path);

        final String option = builder.outputFile(file).getOutputFileOption();
        Assertions.assertThat(option).isSameAs(path);
    }

    @Test
    public void getSilentOption_silentOff_returnsEmptyText() {
        final String option = builder.silent(false).getSilentOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getSilentOption_silentOn_returnsOption() {
        final String option = builder.silent(true).getSilentOption();
        Assertions.assertThat(option).isEqualTo("--silent");
    }

    @Test
    public void getStrictImportsOption_strictOff_returnsEmptyText() {
        final String option = builder.strictImports(false).getStrictImportsOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getStrictImportsOption_strictOn_returnsOption() {
        final String option = builder.strictImports(true).getStrictImportsOption();
        Assertions.assertThat(option).isEqualTo("--strict-imports");
    }

    @Test
    public void getCompressOption_compressOff_returnsEmptyText() {
        final String option = builder.compress(false).getCompressOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getCompressOption_compressOn_returnsOption() {
        final String option = builder.compress(true).getCompressOption();
        Assertions.assertThat(option).isEqualTo("--compress");
    }

    @Test
    public void getIeCompatibilityOption_compatibilityOff_returnsOption() {
        final String option = builder.ieCompatibility(false).getIeCompatibilityOption();
        Assertions.assertThat(option).isEqualTo("--no-ie-compat");
    }

    @Test
    public void getIeCompatibilityOption_compatibilityOn_returnsEmptyText() {
        final String option = builder.ieCompatibility(true).getIeCompatibilityOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getJavaScriptOption_javaScriptOff_returnsOption() {
        final String option = builder.javaScript(false).getJavaScriptOption();
        Assertions.assertThat(option).isEqualTo("--no-js");
    }

    @Test
    public void getJavaScriptOption_javaScriptOn_returnsEmptyText() {
        final String option = builder.javaScript(true).getJavaScriptOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getIncludePathsOption_pathsAreNull_returnsEmptyText() {
        final String option = builder.includePaths(null).getIncludePathsOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getIncludePathsOption_pathsAreEmpty_returnsEmptyText() {
        final String option = builder.includePaths(Collections.<String>emptyList()).getIncludePathsOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getIncludePathsOption_pathsContaintOneElement_returnsOption() {
        final String option = builder.includePaths(Arrays.asList("url1")).getIncludePathsOption();
        Assertions.assertThat(option).isEqualTo("--include-path=url1");
    }

    @Test
    public void getIncludePathsOption_pathsContainTwoElements_returnsOption() {
        final String option = builder.includePaths(Arrays.asList("url1", "url2")).getIncludePathsOption();
        Assertions.assertThat(option).isEqualTo("--include-path=url1gabrys-lesscss-compiler-path-separatorurl2");
    }

    @Test
    public void getIncludePathsOption_pathsContainThreeElements_returnsOption() {
        final String option = builder.includePaths(Arrays.asList("url1", "url2", "url3")).getIncludePathsOption();
        Assertions.assertThat(option)
                .isEqualTo("--include-path=url1gabrys-lesscss-compiler-path-separatorurl2gabrys-lesscss-compiler-path-separatorurl3");
    }

    @Test
    public void getLineNumbersOption_lineNumbersIsNull_returnsEmptyText() {
        final String option = builder.lineNumbers(null).getLineNumbersOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getLineNumbersOption_lineNumbersOff_returnsEmptyText() {
        final String option = builder.lineNumbers(LineNumbersValue.OFF).getLineNumbersOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getLineNumbersOption_lineNumbersOn_returnsOption() {
        for (final LineNumbersValue lineNumbers : LineNumbersValue.values()) {
            if (lineNumbers == LineNumbersValue.OFF) {
                continue;
            }
            final String option = builder.lineNumbers(lineNumbers).getLineNumbersOption();
            Assertions.assertThat(option).isEqualTo("--line-numbers=" + lineNumbers.getValue());
        }
    }

    @Test
    public void withSourceMapDefault_mapOff_returnsEmptyText() {
        final String option = builder.sourceMapDefault(false).getSourceMapDefaultOption();

        Assertions.assertThat(option).isEmpty();
        Assertions.assertThat(builder.getSourceMapFileOption()).isEmpty();
        Assertions.assertThat(builder.getSourceMapInlineOptions()).isEmpty();
    }

    @Test
    public void withSourceMapDefault_mapOn_returnsOption() {
        final String option = builder.sourceMapDefault(true).getSourceMapDefaultOption();

        Assertions.assertThat(option).isEqualTo("--source-map");
        Assertions.assertThat(builder.getSourceMapFileOption()).isEmpty();
        Assertions.assertThat(builder.getSourceMapInlineOptions()).isEmpty();
    }

    @Test
    public void withSourceMapFile_fileIsNull_returnsEmptyText() {
        final String option = builder.sourceMapFile(null).getSourceMapFileOption();

        Assertions.assertThat(option).isEmpty();
        Assertions.assertThat(builder.getSourceMapDefaultOption()).isEmpty();
        Assertions.assertThat(builder.getSourceMapInlineOptions()).isEmpty();
    }

    @Test
    public void withSourceMapFile_mapOn_returnsOption() {
        final File sourceMap = Mockito.mock(File.class);
        Mockito.when(sourceMap.getAbsolutePath()).thenReturn("/file.map");
        final String option = builder.sourceMapFile(sourceMap).getSourceMapFileOption();

        Assertions.assertThat(option).isEqualTo("--source-map=/file.map");
        Assertions.assertThat(builder.getSourceMapDefaultOption()).isEmpty();
        Assertions.assertThat(builder.getSourceMapInlineOptions()).isEmpty();
    }

    @Test
    public void withSourceMapInline_inlineOff_returnsEmptyTexts() {
        final String[] options = builder.sourceMapInline(false).getSourceMapInlineOptions();

        Assertions.assertThat(options).isEmpty();
        Assertions.assertThat(builder.getSourceMapDefaultOption()).isEmpty();
        Assertions.assertThat(builder.getSourceMapFileOption()).isEmpty();
    }

    @Test
    public void withSourceMapInline_inlineOn_returnsOptions() {
        final String[] options = builder.sourceMapInline(true).getSourceMapInlineOptions();

        Assertions.assertThat(options).containsExactly("--source-map", "--source-map-map-inline");
        Assertions.assertThat(builder.getSourceMapDefaultOption()).isEmpty();
        Assertions.assertThat(builder.getSourceMapFileOption()).isEmpty();
    }

    @Test
    public void getSourceMapLessInlineOption_inlineOff_returnsEmptyText() {
        final String option = builder.sourceMapLessInline(false).getSourceMapLessInlineOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getSourceMapLessInlineOption_inlineOn_returnsOption() {
        final String option = builder.sourceMapLessInline(true).getSourceMapLessInlineOption();
        Assertions.assertThat(option).isEqualTo("--source-map-less-inline");
    }

    @Test
    public void getSourceMapRootPathOption_pathIsNull_returnsEmptyText() {
        final String option = builder.sourceMapRootPath(null).getSourceMapRootPathOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getSourceMapRootPathOption_pathIsEmpty_returnsOption() {
        final String option = builder.sourceMapRootPath("").getSourceMapRootPathOption();
        Assertions.assertThat(option).isEqualTo("--source-map-rootpath=");
    }

    @Test
    public void getSourceMapRootPathOption_pathIsBlank_returnsOption() {
        final String option = builder.sourceMapRootPath(" \t\r\n").getSourceMapRootPathOption();
        Assertions.assertThat(option).isEqualTo("--source-map-rootpath= \t\r\n");
    }

    @Test
    public void getSourceMapRootPathOption_pathIsNotEmpty_returnsOption() {
        final String option = builder.sourceMapRootPath("rootpath").getSourceMapRootPathOption();
        Assertions.assertThat(option).isEqualTo("--source-map-rootpath=rootpath");
    }

    @Test
    public void getSourceMapBasePathOption_pathIsNull_returnsEmptyText() {
        final String option = builder.sourceMapBasePath(null).getSourceMapBasePathOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getSourceMapBasePathOption_pathIsEmpty_returnsOption() {
        final String option = builder.sourceMapBasePath("").getSourceMapBasePathOption();
        Assertions.assertThat(option).isEqualTo("--source-map-basepath=");
    }

    @Test
    public void getSourceMapBasePathOption_pathIsBlank_returnsOption() {
        final String option = builder.sourceMapBasePath(" \t\r\n").getSourceMapBasePathOption();
        Assertions.assertThat(option).isEqualTo("--source-map-basepath= \t\r\n");
    }

    @Test
    public void getSourceMapBasePathOption_pathIsNotEmpty_returnsOption() {
        final String option = builder.sourceMapBasePath("basepath").getSourceMapBasePathOption();
        Assertions.assertThat(option).isEqualTo("--source-map-basepath=basepath");
    }

    public void getSourceMapUrlOption_pathIsNull_returnsEmptyText() {
        final String option = builder.sourceMapUrl(null).getSourceMapUrlOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getSourceMapUrlOption_pathIsEmpty_returnsOption() {
        final String option = builder.sourceMapUrl("").getSourceMapUrlOption();
        Assertions.assertThat(option).isEqualTo("--source-map-url=");
    }

    @Test
    public void getSourceMapUrlOption_pathIsBlank_returnsOption() {
        final String option = builder.sourceMapUrl(" \t\r\n").getSourceMapUrlOption();
        Assertions.assertThat(option).isEqualTo("--source-map-url= \t\r\n");
    }

    @Test
    public void getSourceMapUrlOption_pathIsNotEmpty_returnsOption() {
        final String option = builder.sourceMapUrl("../map-url").getSourceMapUrlOption();
        Assertions.assertThat(option).isEqualTo("--source-map-url=../map-url");
    }

    @Test
    public void getRootPathOption_pathIsNull_returnsEmptyText() {
        final String option = builder.rootPath(null).getRootPathOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getRootPathOption_pathIsEmpty_returnsOption() {
        final String option = builder.rootPath("").getRootPathOption();
        Assertions.assertThat(option).isEqualTo("--rootpath=");
    }

    @Test
    public void getRootPathOption_pathIsBlank_returnsOption() {
        final String option = builder.rootPath(" \t\r\n").getRootPathOption();
        Assertions.assertThat(option).isEqualTo("--rootpath= \t\r\n");
    }

    @Test
    public void getRootPathOption_pathIsNotEmpty_returnsOption() {
        final String option = builder.rootPath("rooturl").getRootPathOption();
        Assertions.assertThat(option).isEqualTo("--rootpath=rooturl");
    }

    @Test
    public void getRelativeUrlsOption_relativePathsOff_returnsEmptyText() {
        final String option = builder.relativeUrls(false).getRelativeUrlsOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getRelativeUrlsOption_relativePathsOn_returnsOption() {
        final String option = builder.relativeUrls(true).getRelativeUrlsOption();
        Assertions.assertThat(option).isEqualTo("--relative-urls");
    }

    @Test
    public void getStrictMathOption_strictOff_returnsEmptyText() {
        final String option = builder.strictMath(false).getStrictMathOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getStrictMathOption_strictOn_returnsOption() {
        final String option = builder.strictMath(true).getStrictMathOption();
        Assertions.assertThat(option).isEqualTo("--strict-math=on");
    }

    @Test
    public void getStrictUnitsOption_strictOff_returnsEmptyText() {
        final String option = builder.strictUnits(false).getStrictUnitsOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getStrictUnitsOption_strictOn_returnsOption() {
        final String option = builder.strictUnits(true).getStrictUnitsOption();
        Assertions.assertThat(option).isEqualTo("--strict-units=on");
    }

    @Test
    public void getEncodingOption_encodingIsNull_returnsEmptyText() {
        final String option = builder.encoding(null).getEncodingOption();
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void getEncodingOption_encodingIsEmpty_returnsOption() {
        final String option = builder.encoding("").getEncodingOption();
        Assertions.assertThat(option).isEqualTo("--encoding=");
    }

    @Test
    public void getEncodingOption_encodingIsBlank_returnsOption() {
        final String option = builder.encoding(" \t\r\n").getEncodingOption();
        Assertions.assertThat(option).isEqualTo("--encoding= \t\r\n");
    }

    @Test
    public void getEncodingOption_encodingIsNotEmpty_returnsOption() {
        final String option = builder.encoding("UTF-8").getEncodingOption();
        Assertions.assertThat(option).isEqualTo("--encoding=UTF-8");
    }

    @Test
    public void build_defaultValues_returnsDefaultOptions() {
        final Collection<String> options = builder.build();
        Assertions.assertThat(options).isEmpty();

        Mockito.verify(builder).build();
        Mockito.verify(builder).getSilentOption();
        Mockito.verify(builder).getStrictImportsOption();
        Mockito.verify(builder).getCompressOption();
        Mockito.verify(builder).getIeCompatibilityOption();
        Mockito.verify(builder).getJavaScriptOption();
        Mockito.verify(builder).getIncludePathsOption();
        Mockito.verify(builder).getLineNumbersOption();
        Mockito.verify(builder).getSourceMapDefaultOption();
        Mockito.verify(builder).getSourceMapFileOption();
        Mockito.verify(builder).getSourceMapInlineOptions();
        Mockito.verify(builder).getSourceMapLessInlineOption();
        Mockito.verify(builder).getSourceMapRootPathOption();
        Mockito.verify(builder).getSourceMapBasePathOption();
        Mockito.verify(builder).getSourceMapUrlOption();
        Mockito.verify(builder).getRootPathOption();
        Mockito.verify(builder).getRelativeUrlsOption();
        Mockito.verify(builder).getStrictMathOption();
        Mockito.verify(builder).getStrictUnitsOption();
        Mockito.verify(builder).getEncodingOption();
        Mockito.verify(builder).getInputFileOption();
        Mockito.verify(builder).getOutputFileOption();
        Mockito.verifyNoMoreInteractions(builder);
    }

    @Test
    public void build_inputAndOutputAreSet_returnsOptions() {
        final String inputPath = "/dir/input.less";
        Mockito.doReturn(inputPath).when(builder).getInputFileOption();

        final String outputPath = "/dir/output.css";
        Mockito.doReturn(outputPath).when(builder).getOutputFileOption();

        final Collection<String> options = builder.build();
        Assertions.assertThat(options).hasSize(2);
        Assertions.assertThat(options).containsExactly(inputPath, outputPath);
    }

    @Test
    public void build_inputIsSetAndOutputIsNotSet_returnsOptions() {
        final String inputPath = "/dir/input.less";
        Mockito.doReturn(inputPath).when(builder).getInputFileOption();

        Mockito.doReturn("").when(builder).getOutputFileOption();

        final Collection<String> options = builder.build();
        Assertions.assertThat(options).hasSize(1);
        Assertions.assertThat(options).containsExactly(inputPath);
    }

    @Test(expected = BuilderCreationException.class)
    public void build_inputIsNotSetAndOutputIsSet_throwsException() {
        Mockito.doReturn("").when(builder).getInputFileOption();

        final String outputPath = "/dir/output.css";
        Mockito.doReturn(outputPath).when(builder).getOutputFileOption();

        builder.build();
    }
}
