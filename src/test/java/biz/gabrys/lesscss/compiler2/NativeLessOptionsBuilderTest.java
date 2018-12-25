package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class NativeLessOptionsBuilderTest {

    @Spy
    private NativeLessOptionsBuilder builder;

    @Test
    public void getInputFileOption_fileIsNotSet_returnsEmptyText() {
        final String option = builder.inputFile(null).getInputFileOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getInputFileOption_fileIsSet_returnsOption() {
        final String path = "/dir/input.less";

        final String option = builder.inputFile(path).getInputFileOption();
        assertThat(option).isSameAs(path);
    }

    @Test
    public void getOutputFileOption_fileIsNotSet_returnsEmptyText() {
        final String option = builder.outputFile(null).getOutputFileOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getOutputFileOption_fileIsSet_returnsOption() {
        final File file = mock(File.class);
        final String path = "/dir/output.css";
        when(file.getAbsolutePath()).thenReturn(path);

        final String option = builder.outputFile(file).getOutputFileOption();
        assertThat(option).isSameAs(path);
    }

    @Test
    public void getSilentOption_silentOff_returnsEmptyText() {
        final String option = builder.silent(false).getSilentOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getSilentOption_silentOn_returnsOption() {
        final String option = builder.silent(true).getSilentOption();
        assertThat(option).isEqualTo("--silent");
    }

    @Test
    public void getStrictImportsOption_strictOff_returnsEmptyText() {
        final String option = builder.strictImports(false).getStrictImportsOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getStrictImportsOption_strictOn_returnsOption() {
        final String option = builder.strictImports(true).getStrictImportsOption();
        assertThat(option).isEqualTo("--strict-imports");
    }

    @Test
    public void getCompressOption_compressOff_returnsEmptyText() {
        final String option = builder.compress(false).getCompressOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getCompressOption_compressOn_returnsOption() {
        final String option = builder.compress(true).getCompressOption();
        assertThat(option).isEqualTo("--compress");
    }

    @Test
    public void getIeCompatibilityOption_compatibilityOff_returnsOption() {
        final String option = builder.ieCompatibility(false).getIeCompatibilityOption();
        assertThat(option).isEqualTo("--no-ie-compat");
    }

    @Test
    public void getIeCompatibilityOption_compatibilityOn_returnsEmptyText() {
        final String option = builder.ieCompatibility(true).getIeCompatibilityOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getJavaScriptOption_javaScriptOff_returnsOption() {
        final String option = builder.javaScript(false).getJavaScriptOption();
        assertThat(option).isEqualTo("--no-js");
    }

    @Test
    public void getJavaScriptOption_javaScriptOn_returnsEmptyText() {
        final String option = builder.javaScript(true).getJavaScriptOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getIncludePathsOption_pathsAreNull_returnsEmptyText() {
        final String option = builder.includePaths(null).getIncludePathsOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getIncludePathsOption_pathsAreEmpty_returnsEmptyText() {
        final String option = builder.includePaths(Collections.<String>emptyList()).getIncludePathsOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getIncludePathsOption_pathsContaintOneElement_returnsOption() {
        final String option = builder.includePaths(Arrays.asList("url1")).getIncludePathsOption();
        assertThat(option).isEqualTo("--include-path=url1");
    }

    @Test
    public void getIncludePathsOption_pathsContainTwoElements_returnsOption() {
        final String option = builder.includePaths(Arrays.asList("url1", "url2")).getIncludePathsOption();
        assertThat(option).isEqualTo("--include-path=url1gabrys-lesscss-compiler-path-separatorurl2");
    }

    @Test
    public void getIncludePathsOption_pathsContainThreeElements_returnsOption() {
        final String option = builder.includePaths(Arrays.asList("url1", "url2", "url3")).getIncludePathsOption();
        assertThat(option)
                .isEqualTo("--include-path=url1gabrys-lesscss-compiler-path-separatorurl2gabrys-lesscss-compiler-path-separatorurl3");
    }

    @Test
    public void getLineNumbersOption_lineNumbersIsNull_returnsEmptyText() {
        final String option = builder.lineNumbers(null).getLineNumbersOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getLineNumbersOption_lineNumbersOff_returnsEmptyText() {
        final String option = builder.lineNumbers(LineNumbersValue.OFF).getLineNumbersOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getLineNumbersOption_lineNumbersOn_returnsOption() {
        for (final LineNumbersValue lineNumbers : LineNumbersValue.values()) {
            if (lineNumbers == LineNumbersValue.OFF) {
                continue;
            }
            final String option = builder.lineNumbers(lineNumbers).getLineNumbersOption();
            assertThat(option).isEqualTo("--line-numbers=" + lineNumbers.getValue());
        }
    }

    @Test
    public void withSourceMapDefault_mapOff_returnsEmptyText() {
        final String option = builder.sourceMapDefault(false).getSourceMapDefaultOption();

        assertThat(option).isEmpty();
        assertThat(builder.getSourceMapFileOption()).isEmpty();
        assertThat(builder.getSourceMapInlineOptions()).isEmpty();
    }

    @Test
    public void withSourceMapDefault_mapOn_returnsOption() {
        final String option = builder.sourceMapDefault(true).getSourceMapDefaultOption();

        assertThat(option).isEqualTo("--source-map");
        assertThat(builder.getSourceMapFileOption()).isEmpty();
        assertThat(builder.getSourceMapInlineOptions()).isEmpty();
    }

    @Test
    public void withSourceMapFile_fileIsNull_returnsEmptyText() {
        final String option = builder.sourceMapFile(null).getSourceMapFileOption();

        assertThat(option).isEmpty();
        assertThat(builder.getSourceMapDefaultOption()).isEmpty();
        assertThat(builder.getSourceMapInlineOptions()).isEmpty();
    }

    @Test
    public void withSourceMapFile_mapOn_returnsOption() {
        final File sourceMap = mock(File.class);
        when(sourceMap.getAbsolutePath()).thenReturn("/file.map");
        final String option = builder.sourceMapFile(sourceMap).getSourceMapFileOption();

        assertThat(option).isEqualTo("--source-map=/file.map");
        assertThat(builder.getSourceMapDefaultOption()).isEmpty();
        assertThat(builder.getSourceMapInlineOptions()).isEmpty();
    }

    @Test
    public void withSourceMapInline_inlineOff_returnsEmptyTexts() {
        final String[] options = builder.sourceMapInline(false).getSourceMapInlineOptions();

        assertThat(options).isEmpty();
        assertThat(builder.getSourceMapDefaultOption()).isEmpty();
        assertThat(builder.getSourceMapFileOption()).isEmpty();
    }

    @Test
    public void withSourceMapInline_inlineOn_returnsOptions() {
        final String[] options = builder.sourceMapInline(true).getSourceMapInlineOptions();

        assertThat(options).containsExactly("--source-map", "--source-map-map-inline");
        assertThat(builder.getSourceMapDefaultOption()).isEmpty();
        assertThat(builder.getSourceMapFileOption()).isEmpty();
    }

    @Test
    public void getSourceMapLessInlineOption_inlineOff_returnsEmptyText() {
        final String option = builder.sourceMapLessInline(false).getSourceMapLessInlineOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getSourceMapLessInlineOption_inlineOn_returnsOption() {
        final String option = builder.sourceMapLessInline(true).getSourceMapLessInlineOption();
        assertThat(option).isEqualTo("--source-map-less-inline");
    }

    @Test
    public void getSourceMapRootPathOption_pathIsNull_returnsEmptyText() {
        final String option = builder.sourceMapRootPath(null).getSourceMapRootPathOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getSourceMapRootPathOption_pathIsEmpty_returnsOption() {
        final String option = builder.sourceMapRootPath("").getSourceMapRootPathOption();
        assertThat(option).isEqualTo("--source-map-rootpath=");
    }

    @Test
    public void getSourceMapRootPathOption_pathIsBlank_returnsOption() {
        final String option = builder.sourceMapRootPath(" \t\r\n").getSourceMapRootPathOption();
        assertThat(option).isEqualTo("--source-map-rootpath= \t\r\n");
    }

    @Test
    public void getSourceMapRootPathOption_pathIsNotBlank_returnsOption() {
        final String option = builder.sourceMapRootPath("rootpath").getSourceMapRootPathOption();
        assertThat(option).isEqualTo("--source-map-rootpath=rootpath");
    }

    @Test
    public void getSourceMapBasePathOption_pathIsNull_returnsEmptyText() {
        final String option = builder.sourceMapBasePath(null).getSourceMapBasePathOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getSourceMapBasePathOption_pathIsEmpty_returnsOption() {
        final String option = builder.sourceMapBasePath("").getSourceMapBasePathOption();
        assertThat(option).isEqualTo("--source-map-basepath=");
    }

    @Test
    public void getSourceMapBasePathOption_pathIsBlank_returnsOption() {
        final String option = builder.sourceMapBasePath(" \t\r\n").getSourceMapBasePathOption();
        assertThat(option).isEqualTo("--source-map-basepath= \t\r\n");
    }

    @Test
    public void getSourceMapBasePathOption_pathIsNotBlank_returnsOption() {
        final String option = builder.sourceMapBasePath("basepath").getSourceMapBasePathOption();
        assertThat(option).isEqualTo("--source-map-basepath=basepath");
    }

    public void getSourceMapUrlOption_pathIsNull_returnsEmptyText() {
        final String option = builder.sourceMapUrl(null).getSourceMapUrlOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getSourceMapUrlOption_pathIsEmpty_returnsOption() {
        final String option = builder.sourceMapUrl("").getSourceMapUrlOption();
        assertThat(option).isEqualTo("--source-map-url=");
    }

    @Test
    public void getSourceMapUrlOption_pathIsBlank_returnsOption() {
        final String option = builder.sourceMapUrl(" \t\r\n").getSourceMapUrlOption();
        assertThat(option).isEqualTo("--source-map-url= \t\r\n");
    }

    @Test
    public void getSourceMapUrlOption_pathIsNotBlank_returnsOption() {
        final String option = builder.sourceMapUrl("../map-url").getSourceMapUrlOption();
        assertThat(option).isEqualTo("--source-map-url=../map-url");
    }

    @Test
    public void getRootPathOption_pathIsNull_returnsEmptyText() {
        final String option = builder.rootPath(null).getRootPathOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getRootPathOption_pathIsEmpty_returnsOption() {
        final String option = builder.rootPath("").getRootPathOption();
        assertThat(option).isEqualTo("--rootpath=");
    }

    @Test
    public void getRootPathOption_pathIsBlank_returnsOption() {
        final String option = builder.rootPath(" \t\r\n").getRootPathOption();
        assertThat(option).isEqualTo("--rootpath= \t\r\n");
    }

    @Test
    public void getRootPathOption_pathIsNotBlank_returnsOption() {
        final String option = builder.rootPath("rooturl").getRootPathOption();
        assertThat(option).isEqualTo("--rootpath=rooturl");
    }

    @Test
    public void getRelativeUrlsOption_relativePathsOff_returnsEmptyText() {
        final String option = builder.relativeUrls(false).getRelativeUrlsOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getRelativeUrlsOption_relativePathsOn_returnsOption() {
        final String option = builder.relativeUrls(true).getRelativeUrlsOption();
        assertThat(option).isEqualTo("--relative-urls");
    }

    @Test
    public void getStrictMathOption_strictOff_returnsEmptyText() {
        final String option = builder.strictMath(false).getStrictMathOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getStrictMathOption_strictOn_returnsOption() {
        final String option = builder.strictMath(true).getStrictMathOption();
        assertThat(option).isEqualTo("--strict-math=on");
    }

    @Test
    public void getStrictUnitsOption_strictOff_returnsEmptyText() {
        final String option = builder.strictUnits(false).getStrictUnitsOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getStrictUnitsOption_strictOn_returnsOption() {
        final String option = builder.strictUnits(true).getStrictUnitsOption();
        assertThat(option).isEqualTo("--strict-units=on");
    }

    @Test
    public void getEncodingOption_encodingIsNull_returnsEmptyText() {
        final String option = builder.encoding(null).getEncodingOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getEncodingOption_encodingIsEmpty_returnsOption() {
        final String option = builder.encoding("").getEncodingOption();
        assertThat(option).isEqualTo("--encoding=");
    }

    @Test
    public void getEncodingOption_encodingIsBlank_returnsOption() {
        final String option = builder.encoding(" \t\r\n").getEncodingOption();
        assertThat(option).isEqualTo("--encoding= \t\r\n");
    }

    @Test
    public void getEncodingOption_encodingIsNotBlank_returnsOption() {
        final String option = builder.encoding("UTF-8").getEncodingOption();
        assertThat(option).isEqualTo("--encoding=UTF-8");
    }

    @Test
    public void getFileSystemsOption_fileSystemsAreNull_returnsOption() {
        final String option = builder.fileSystems(null).getFileSystemsOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getFileSystemsOption_fileSystemsAreEmpty_returnsEmptyText() {
        final String option = builder.fileSystems(Collections.<String>emptyList()).getFileSystemsOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getFileSystemsOption_fileSystemsContaintOneElement_returnsOption() {
        final String option = builder.fileSystems(Arrays.asList("system")).getFileSystemsOption();
        assertThat(option).isEqualTo("--file-systems=system");
    }

    @Test
    public void getFileSystemsOption_fileSystemsContainTwoElements_returnsOption() {
        final String option = builder.fileSystems(Arrays.asList("system1", "system2")).getFileSystemsOption();
        assertThat(option).isEqualTo("--file-systems=system1,system2");
    }

    @Test
    public void getFileSystemsOption_fileSystemsContainThreeElements_returnsOption() {
        final String option = builder.fileSystems(Arrays.asList("system1", "system2", "system3")).getFileSystemsOption();
        assertThat(option).isEqualTo("--file-systems=system1,system2,system3");
    }

    @Test
    public void getBannerOption_bannerIsNull_returnsEmptyText() {
        final String option = builder.banner(null).getBannerOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getBannerOption_bannerIsEmpty_returnsEmptyText() {
        final String option = builder.banner("").getBannerOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getBannerOption_bannerIsBlank_returnsEmptyText() {
        final String option = builder.banner("  ").getBannerOption();
        assertThat(option).isEmpty();
    }

    @Test
    public void getBannerOption_bannerIsNotBlank_returnsOption() {
        final String option = builder.banner("text").getBannerOption();
        assertThat(option).isEqualTo("--banner=text");
    }

    @Test
    public void build_defaultValues_returnsDefaultOptions() {
        final Collection<String> options = builder.build();
        assertThat(options).isEmpty();

        verify(builder).build();
        verify(builder).getSilentOption();
        verify(builder).getStrictImportsOption();
        verify(builder).getCompressOption();
        verify(builder).getIeCompatibilityOption();
        verify(builder).getJavaScriptOption();
        verify(builder).getIncludePathsOption();
        verify(builder).getLineNumbersOption();
        verify(builder).getSourceMapDefaultOption();
        verify(builder).getSourceMapFileOption();
        verify(builder).getSourceMapInlineOptions();
        verify(builder).getSourceMapLessInlineOption();
        verify(builder).getSourceMapRootPathOption();
        verify(builder).getSourceMapBasePathOption();
        verify(builder).getSourceMapUrlOption();
        verify(builder).getRootPathOption();
        verify(builder).getRelativeUrlsOption();
        verify(builder).getStrictMathOption();
        verify(builder).getStrictUnitsOption();
        verify(builder).getEncodingOption();
        verify(builder).getFileSystemsOption();
        verify(builder).getBannerOption();
        verify(builder).getInputFileOption();
        verify(builder).getOutputFileOption();
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void build_inputAndOutputAreSet_returnsOptions() {
        final String inputPath = "/dir/input.less";
        doReturn(inputPath).when(builder).getInputFileOption();

        final String outputPath = "/dir/output.css";
        doReturn(outputPath).when(builder).getOutputFileOption();

        final Collection<String> options = builder.build();
        assertThat(options).hasSize(2);
        assertThat(options).containsExactly(inputPath, outputPath);
    }

    @Test
    public void build_inputIsSetAndOutputIsNotSet_returnsOptions() {
        final String inputPath = "/dir/input.less";
        doReturn(inputPath).when(builder).getInputFileOption();

        doReturn("").when(builder).getOutputFileOption();

        final Collection<String> options = builder.build();
        assertThat(options).hasSize(1);
        assertThat(options).containsExactly(inputPath);
    }

    @Test(expected = BuilderCreationException.class)
    public void build_inputIsNotSetAndOutputIsSet_throwsException() {
        doReturn("").when(builder).getInputFileOption();

        final String outputPath = "/dir/output.css";
        doReturn(outputPath).when(builder).getOutputFileOption();

        builder.build();
    }
}
