package biz.gabrys.lesscss.compiler2;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class LessOptionsBuilderTest {

    @Spy
    private LessOptionsBuilder builder;
    @Mock
    private LessOptions options;

    @Before
    public void setup() {
        Mockito.doReturn(options).when(builder).getOptions();
    }

    @Test
    public void silent_true() {
        final LessOptionsBuilder builder2 = builder.silent(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).silent(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setSilent(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void silent_false() {
        final LessOptionsBuilder builder2 = builder.silent(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).silent(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setSilent(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void silentOn() {
        Mockito.doReturn(builder).when(builder).silent(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.silentOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).silentOn();
        Mockito.verify(builder).silent(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void silentOff() {
        Mockito.doReturn(builder).when(builder).silent(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.silentOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).silentOff();
        Mockito.verify(builder).silent(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void strictImports_true() {
        final LessOptionsBuilder builder2 = builder.strictImports(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictImports(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setStrictImports(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictImports_false() {
        final LessOptionsBuilder builder2 = builder.strictImports(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictImports(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setStrictImports(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictImportsOn() {
        Mockito.doReturn(builder).when(builder).strictImports(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictImportsOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictImportsOn();
        Mockito.verify(builder).strictImports(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void strictImportsOff() {
        Mockito.doReturn(builder).when(builder).strictImports(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictImportsOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictImportsOff();
        Mockito.verify(builder).strictImports(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void compress_true() {
        final LessOptionsBuilder builder2 = builder.compress(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).compress(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setCompress(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void compress_false() {
        final LessOptionsBuilder builder2 = builder.compress(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).compress(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setCompress(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void compressOn() {
        Mockito.doReturn(builder).when(builder).compress(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.compressOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).compressOn();
        Mockito.verify(builder).compress(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void compressOff() {
        Mockito.doReturn(builder).when(builder).compress(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.compressOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).compressOff();
        Mockito.verify(builder).compress(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void ieCompatibility_true() {
        final LessOptionsBuilder builder2 = builder.ieCompatibility(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).ieCompatibility(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setIeCompatibility(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void ieCompatibility_false() {
        final LessOptionsBuilder builder2 = builder.ieCompatibility(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).ieCompatibility(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setIeCompatibility(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void ieCompatibilityOn() {
        Mockito.doReturn(builder).when(builder).ieCompatibility(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.ieCompatibilityOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).ieCompatibilityOn();
        Mockito.verify(builder).ieCompatibility(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void ieCompatibilityOff() {
        Mockito.doReturn(builder).when(builder).ieCompatibility(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.ieCompatibilityOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).ieCompatibilityOff();
        Mockito.verify(builder).ieCompatibility(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void javaScript_true() {
        final LessOptionsBuilder builder2 = builder.javaScript(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).javaScript(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setJavaScript(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void javaScript_false() {
        final LessOptionsBuilder builder2 = builder.javaScript(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).javaScript(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setJavaScript(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void javaScriptOn() {
        Mockito.doReturn(builder).when(builder).javaScript(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.javaScriptOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).javaScriptOn();
        Mockito.verify(builder).javaScript(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void javaScriptOff() {
        Mockito.doReturn(builder).when(builder).javaScript(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.javaScriptOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).javaScriptOff();
        Mockito.verify(builder).javaScript(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void includePaths_nullCollection() {
        final Collection<CharSequence> includePaths = null;

        final LessOptionsBuilder builder2 = builder.includePaths(includePaths);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).includePaths(includePaths);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setIncludePaths(null);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void includePaths_notNullCollection() {
        final Collection<CharSequence> includePaths = Arrays.<CharSequence>asList("value1", "value2");

        final LessOptionsBuilder builder2 = builder.includePaths(includePaths);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).includePaths(includePaths);
        Mockito.verify(builder).getOptions();
        @SuppressWarnings("unchecked")
        final ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(options).setIncludePaths(captor.capture());
        Assertions.assertThat(captor.getValue()).containsExactly("value1", "value2");
        Mockito.verifyNoMoreInteractions(builder);
    }

    @Test
    public void includePaths_nullArray() {
        Mockito.doReturn(builder).when(builder).includePaths(ArgumentMatchers.<Collection<CharSequence>>any());
        final String[] includePaths = null;

        final LessOptionsBuilder builder2 = builder.includePaths(includePaths);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).includePaths(includePaths);
        Mockito.verify(builder).includePaths((Collection<CharSequence>) null);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void includePaths_notNullArray() {
        Mockito.doReturn(builder).when(builder).includePaths(ArgumentMatchers.<Collection<CharSequence>>any());
        final String[] includePaths = new String[] { "val1", "val2" };

        final LessOptionsBuilder builder2 = builder.includePaths(includePaths);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).includePaths(includePaths);
        @SuppressWarnings("unchecked")
        final ArgumentCaptor<Collection<CharSequence>> captor = ArgumentCaptor.forClass(Collection.class);
        Mockito.verify(builder).includePaths(captor.capture());
        Assertions.assertThat(captor.getValue()).containsExactly("val1", "val2");
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void includePathsOff() {
        Mockito.doReturn(builder).when(builder).includePaths(ArgumentMatchers.<Collection<CharSequence>>any());

        final LessOptionsBuilder builder2 = builder.includePathsOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).includePathsOff();
        Mockito.verify(builder).includePaths((Collection<CharSequence>) null);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void lineNumbers() {
        final LineNumbersValue value = LineNumbersValue.COMMENTS;

        final LessOptionsBuilder builder2 = builder.lineNumbers(value);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).lineNumbers(value);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setLineNumbers(LineNumbersValue.COMMENTS);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void lineNumbersOff() {
        Mockito.doReturn(builder).when(builder).lineNumbers(ArgumentMatchers.<LineNumbersValue>any());

        final LessOptionsBuilder builder2 = builder.lineNumbersOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).lineNumbersOff();
        Mockito.verify(builder).lineNumbers(LineNumbersValue.OFF);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void lineNumbersComments() {
        Mockito.doReturn(builder).when(builder).lineNumbers(ArgumentMatchers.<LineNumbersValue>any());

        final LessOptionsBuilder builder2 = builder.lineNumbersComments();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).lineNumbersComments();
        Mockito.verify(builder).lineNumbers(LineNumbersValue.COMMENTS);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void lineNumbersMediaQuery() {
        Mockito.doReturn(builder).when(builder).lineNumbers(ArgumentMatchers.<LineNumbersValue>any());

        final LessOptionsBuilder builder2 = builder.lineNumbersMediaQuery();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).lineNumbersMediaQuery();
        Mockito.verify(builder).lineNumbers(LineNumbersValue.MEDIA_QUERY);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void lineNumbersAll() {
        Mockito.doReturn(builder).when(builder).lineNumbers(ArgumentMatchers.<LineNumbersValue>any());

        final LessOptionsBuilder builder2 = builder.lineNumbersAll();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).lineNumbersAll();
        Mockito.verify(builder).lineNumbers(LineNumbersValue.ALL);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void rootPath() {
        final String rootPath = "rootPath";

        final LessOptionsBuilder builder2 = builder.rootPath(rootPath);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).rootPath(rootPath);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setRootPath(rootPath);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void rootPathOff() {
        Mockito.doReturn(builder).when(builder).rootPath(ArgumentMatchers.<CharSequence>any());

        final LessOptionsBuilder builder2 = builder.rootPathOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).rootPathOff();
        Mockito.verify(builder).rootPath(null);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void relativeUrls_true() {
        final LessOptionsBuilder builder2 = builder.relativeUrls(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).relativeUrls(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setRelativeUrls(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void relativeUrls_false() {
        final LessOptionsBuilder builder2 = builder.relativeUrls(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).relativeUrls(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setRelativeUrls(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void relativeUrlsOn() {
        Mockito.doReturn(builder).when(builder).relativeUrls(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.relativeUrlsOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).relativeUrlsOn();
        Mockito.verify(builder).relativeUrls(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void relativeUrlsOff() {
        Mockito.doReturn(builder).when(builder).relativeUrls(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.relativeUrlsOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).relativeUrlsOff();
        Mockito.verify(builder).relativeUrls(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void strictMath_true() {
        final LessOptionsBuilder builder2 = builder.strictMath(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictMath(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setStrictMath(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictMath_false() {
        final LessOptionsBuilder builder2 = builder.strictMath(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictMath(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setStrictMath(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictMathOn() {
        Mockito.doReturn(builder).when(builder).strictMath(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictMathOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictMathOn();
        Mockito.verify(builder).strictMath(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void strictMathOff() {
        Mockito.doReturn(builder).when(builder).strictMath(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictMathOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictMathOff();
        Mockito.verify(builder).strictMath(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void strictUnits_true() {
        final LessOptionsBuilder builder2 = builder.strictUnits(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictUnits(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setStrictUnits(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictUnits_false() {
        final LessOptionsBuilder builder2 = builder.strictUnits(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictUnits(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setStrictUnits(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictUnitsOn() {
        Mockito.doReturn(builder).when(builder).strictUnits(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictUnitsOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictUnitsOn();
        Mockito.verify(builder).strictUnits(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void strictUnitsOff() {
        Mockito.doReturn(builder).when(builder).strictUnits(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictUnitsOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).strictUnitsOff();
        Mockito.verify(builder).strictUnits(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapRootPath() {
        final String sourceMapRootPath = "sourceMapRootPath";

        final LessOptionsBuilder builder2 = builder.sourceMapRootPath(sourceMapRootPath);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapRootPath(sourceMapRootPath);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setSourceMapRootPath(sourceMapRootPath);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapRootPathOff() {
        Mockito.doReturn(builder).when(builder).sourceMapRootPath(ArgumentMatchers.<CharSequence>any());

        final LessOptionsBuilder builder2 = builder.sourceMapRootPathOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapRootPathOff();
        Mockito.verify(builder).sourceMapRootPath(null);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapBasePath() {
        final String sourceMapBasePath = "sourceMapBasePath";

        final LessOptionsBuilder builder2 = builder.sourceMapBasePath(sourceMapBasePath);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapBasePath(sourceMapBasePath);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setSourceMapBasePath(sourceMapBasePath);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapBasePathOff() {
        Mockito.doReturn(builder).when(builder).sourceMapBasePath(ArgumentMatchers.<CharSequence>any());

        final LessOptionsBuilder builder2 = builder.sourceMapBasePathOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapBasePathOff();
        Mockito.verify(builder).sourceMapBasePath(null);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapLessInline_true() {
        final LessOptionsBuilder builder2 = builder.sourceMapLessInline(true);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapLessInline(true);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setSourceMapLessInline(true);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapLessInline_false() {
        final LessOptionsBuilder builder2 = builder.sourceMapLessInline(false);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapLessInline(false);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setSourceMapLessInline(false);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapLessInlineOn() {
        Mockito.doReturn(builder).when(builder).sourceMapLessInline(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.sourceMapLessInlineOn();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapLessInlineOn();
        Mockito.verify(builder).sourceMapLessInline(true);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapLessInlineOff() {
        Mockito.doReturn(builder).when(builder).sourceMapLessInline(ArgumentMatchers.anyBoolean());

        final LessOptionsBuilder builder2 = builder.sourceMapLessInlineOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapLessInlineOff();
        Mockito.verify(builder).sourceMapLessInline(false);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapUrl() {
        final String sourceMapUrl = "sourceMapUrl";

        final LessOptionsBuilder builder2 = builder.sourceMapUrl(sourceMapUrl);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapUrl(sourceMapUrl);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setSourceMapUrl(sourceMapUrl);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapUrlOff() {
        Mockito.doReturn(builder).when(builder).sourceMapUrl(ArgumentMatchers.<CharSequence>any());

        final LessOptionsBuilder builder2 = builder.sourceMapUrlOff();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).sourceMapUrlOff();
        Mockito.verify(builder).sourceMapUrl(null);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }

    @Test
    public void encoding_charsequence() {
        final String encoding = "encoding";

        final LessOptionsBuilder builder2 = builder.encoding(encoding);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).encoding(encoding);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setEncoding(encoding);
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void encoding_charset() {
        final Charset encoding = Charset.forName("UTF-8");

        final LessOptionsBuilder builder2 = builder.encoding(encoding);

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).encoding(encoding);
        Mockito.verify(builder).getOptions();
        Mockito.verify(options).setEncoding("UTF-8");
        Mockito.verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void encodingPlatformDefault() {
        Mockito.doReturn(builder).when(builder).encoding(ArgumentMatchers.<CharSequence>any());

        final LessOptionsBuilder builder2 = builder.encodingPlatformDefault();

        Assertions.assertThat(builder2).isSameAs(builder);
        Mockito.verify(builder).encodingPlatformDefault();
        Mockito.verify(builder).encoding((CharSequence) null);
        Mockito.verifyNoMoreInteractions(builder);
        Mockito.verifyZeroInteractions(options);
    }
}
