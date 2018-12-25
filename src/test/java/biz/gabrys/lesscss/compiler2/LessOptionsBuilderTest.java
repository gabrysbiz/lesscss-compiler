package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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
        doReturn(options).when(builder).getOptions();
    }

    @Test
    public void silent_true() {
        final LessOptionsBuilder builder2 = builder.silent(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).silent(true);
        verify(builder).getOptions();
        verify(options).setSilent(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void silent_false() {
        final LessOptionsBuilder builder2 = builder.silent(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).silent(false);
        verify(builder).getOptions();
        verify(options).setSilent(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void silentOn() {
        doReturn(builder).when(builder).silent(anyBoolean());

        final LessOptionsBuilder builder2 = builder.silentOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).silentOn();
        verify(builder).silent(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void silentOff() {
        doReturn(builder).when(builder).silent(anyBoolean());

        final LessOptionsBuilder builder2 = builder.silentOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).silentOff();
        verify(builder).silent(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void strictImports_true() {
        final LessOptionsBuilder builder2 = builder.strictImports(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictImports(true);
        verify(builder).getOptions();
        verify(options).setStrictImports(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictImports_false() {
        final LessOptionsBuilder builder2 = builder.strictImports(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictImports(false);
        verify(builder).getOptions();
        verify(options).setStrictImports(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictImportsOn() {
        doReturn(builder).when(builder).strictImports(anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictImportsOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictImportsOn();
        verify(builder).strictImports(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void strictImportsOff() {
        doReturn(builder).when(builder).strictImports(anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictImportsOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictImportsOff();
        verify(builder).strictImports(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void compress_true() {
        final LessOptionsBuilder builder2 = builder.compress(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).compress(true);
        verify(builder).getOptions();
        verify(options).setCompress(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void compress_false() {
        final LessOptionsBuilder builder2 = builder.compress(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).compress(false);
        verify(builder).getOptions();
        verify(options).setCompress(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void compressOn() {
        doReturn(builder).when(builder).compress(anyBoolean());

        final LessOptionsBuilder builder2 = builder.compressOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).compressOn();
        verify(builder).compress(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void compressOff() {
        doReturn(builder).when(builder).compress(anyBoolean());

        final LessOptionsBuilder builder2 = builder.compressOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).compressOff();
        verify(builder).compress(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void ieCompatibility_true() {
        final LessOptionsBuilder builder2 = builder.ieCompatibility(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).ieCompatibility(true);
        verify(builder).getOptions();
        verify(options).setIeCompatibility(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void ieCompatibility_false() {
        final LessOptionsBuilder builder2 = builder.ieCompatibility(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).ieCompatibility(false);
        verify(builder).getOptions();
        verify(options).setIeCompatibility(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void ieCompatibilityOn() {
        doReturn(builder).when(builder).ieCompatibility(anyBoolean());

        final LessOptionsBuilder builder2 = builder.ieCompatibilityOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).ieCompatibilityOn();
        verify(builder).ieCompatibility(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void ieCompatibilityOff() {
        doReturn(builder).when(builder).ieCompatibility(anyBoolean());

        final LessOptionsBuilder builder2 = builder.ieCompatibilityOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).ieCompatibilityOff();
        verify(builder).ieCompatibility(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void javaScript_true() {
        final LessOptionsBuilder builder2 = builder.javaScript(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).javaScript(true);
        verify(builder).getOptions();
        verify(options).setJavaScript(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void javaScript_false() {
        final LessOptionsBuilder builder2 = builder.javaScript(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).javaScript(false);
        verify(builder).getOptions();
        verify(options).setJavaScript(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void javaScriptOn() {
        doReturn(builder).when(builder).javaScript(anyBoolean());

        final LessOptionsBuilder builder2 = builder.javaScriptOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).javaScriptOn();
        verify(builder).javaScript(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void javaScriptOff() {
        doReturn(builder).when(builder).javaScript(anyBoolean());

        final LessOptionsBuilder builder2 = builder.javaScriptOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).javaScriptOff();
        verify(builder).javaScript(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void includePaths_nullCollection() {
        final Collection<CharSequence> includePaths = null;

        final LessOptionsBuilder builder2 = builder.includePaths(includePaths);

        assertThat(builder2).isSameAs(builder);
        verify(builder).includePaths(includePaths);
        verify(builder).getOptions();
        verify(options).setIncludePaths(null);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void includePaths_notNullCollection() {
        final Collection<CharSequence> includePaths = Arrays.<CharSequence>asList("value1", "value2");

        final LessOptionsBuilder builder2 = builder.includePaths(includePaths);

        assertThat(builder2).isSameAs(builder);
        verify(builder).includePaths(includePaths);
        verify(builder).getOptions();
        @SuppressWarnings("unchecked")
        final ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        verify(options).setIncludePaths(captor.capture());
        assertThat(captor.getValue()).containsExactly("value1", "value2");
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void includePaths_nullArray() {
        doReturn(builder).when(builder).includePaths((Collection<CharSequence>) null);
        final CharSequence[] includePaths = null;

        final LessOptionsBuilder builder2 = builder.includePaths(includePaths);

        assertThat(builder2).isSameAs(builder);
        verify(builder).includePaths(includePaths);
        verify(builder).includePaths((Collection<CharSequence>) null);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void includePaths_notNullArray() {
        @SuppressWarnings("unchecked")
        final Class<Collection<CharSequence>> collectionOfCharSequenceClass = (Class<Collection<CharSequence>>) (Class<?>) Collection.class;
        doReturn(builder).when(builder).includePaths(any(collectionOfCharSequenceClass));
        final CharSequence[] includePaths = new String[] { "val1", "val2" };

        final LessOptionsBuilder builder2 = builder.includePaths(includePaths);

        assertThat(builder2).isSameAs(builder);
        verify(builder).includePaths(includePaths);
        @SuppressWarnings("unchecked")
        final ArgumentCaptor<Collection<CharSequence>> captor = ArgumentCaptor.forClass(Collection.class);
        verify(builder).includePaths(captor.capture());
        assertThat(captor.getValue()).containsExactly("val1", "val2");
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void includePathsOff() {
        doReturn(builder).when(builder).includePaths((Collection<CharSequence>) null);

        final LessOptionsBuilder builder2 = builder.includePathsOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).includePathsOff();
        verify(builder).includePaths((Collection<CharSequence>) null);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void lineNumbers() {
        final LineNumbersValue value = LineNumbersValue.COMMENTS;

        final LessOptionsBuilder builder2 = builder.lineNumbers(value);

        assertThat(builder2).isSameAs(builder);
        verify(builder).lineNumbers(value);
        verify(builder).getOptions();
        verify(options).setLineNumbers(LineNumbersValue.COMMENTS);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void lineNumbersOff() {
        doReturn(builder).when(builder).lineNumbers(any(LineNumbersValue.class));

        final LessOptionsBuilder builder2 = builder.lineNumbersOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).lineNumbersOff();
        verify(builder).lineNumbers(LineNumbersValue.OFF);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void lineNumbersComments() {
        doReturn(builder).when(builder).lineNumbers(any(LineNumbersValue.class));

        final LessOptionsBuilder builder2 = builder.lineNumbersComments();

        assertThat(builder2).isSameAs(builder);
        verify(builder).lineNumbersComments();
        verify(builder).lineNumbers(LineNumbersValue.COMMENTS);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void lineNumbersMediaQuery() {
        doReturn(builder).when(builder).lineNumbers(any(LineNumbersValue.class));

        final LessOptionsBuilder builder2 = builder.lineNumbersMediaQuery();

        assertThat(builder2).isSameAs(builder);
        verify(builder).lineNumbersMediaQuery();
        verify(builder).lineNumbers(LineNumbersValue.MEDIA_QUERY);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void lineNumbersAll() {
        doReturn(builder).when(builder).lineNumbers(any(LineNumbersValue.class));

        final LessOptionsBuilder builder2 = builder.lineNumbersAll();

        assertThat(builder2).isSameAs(builder);
        verify(builder).lineNumbersAll();
        verify(builder).lineNumbers(LineNumbersValue.ALL);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void rootPath() {
        final String rootPath = "rootPath";

        final LessOptionsBuilder builder2 = builder.rootPath(rootPath);

        assertThat(builder2).isSameAs(builder);
        verify(builder).rootPath(rootPath);
        verify(builder).getOptions();
        verify(options).setRootPath(rootPath);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void rootPathOff() {
        doReturn(builder).when(builder).rootPath(null);

        final LessOptionsBuilder builder2 = builder.rootPathOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).rootPathOff();
        verify(builder).rootPath(null);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void relativeUrls_true() {
        final LessOptionsBuilder builder2 = builder.relativeUrls(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).relativeUrls(true);
        verify(builder).getOptions();
        verify(options).setRelativeUrls(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void relativeUrls_false() {
        final LessOptionsBuilder builder2 = builder.relativeUrls(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).relativeUrls(false);
        verify(builder).getOptions();
        verify(options).setRelativeUrls(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void relativeUrlsOn() {
        doReturn(builder).when(builder).relativeUrls(anyBoolean());

        final LessOptionsBuilder builder2 = builder.relativeUrlsOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).relativeUrlsOn();
        verify(builder).relativeUrls(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void relativeUrlsOff() {
        doReturn(builder).when(builder).relativeUrls(anyBoolean());

        final LessOptionsBuilder builder2 = builder.relativeUrlsOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).relativeUrlsOff();
        verify(builder).relativeUrls(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void strictMath_true() {
        final LessOptionsBuilder builder2 = builder.strictMath(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictMath(true);
        verify(builder).getOptions();
        verify(options).setStrictMath(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictMath_false() {
        final LessOptionsBuilder builder2 = builder.strictMath(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictMath(false);
        verify(builder).getOptions();
        verify(options).setStrictMath(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictMathOn() {
        doReturn(builder).when(builder).strictMath(anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictMathOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictMathOn();
        verify(builder).strictMath(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void strictMathOff() {
        doReturn(builder).when(builder).strictMath(anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictMathOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictMathOff();
        verify(builder).strictMath(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void strictUnits_true() {
        final LessOptionsBuilder builder2 = builder.strictUnits(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictUnits(true);
        verify(builder).getOptions();
        verify(options).setStrictUnits(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictUnits_false() {
        final LessOptionsBuilder builder2 = builder.strictUnits(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictUnits(false);
        verify(builder).getOptions();
        verify(options).setStrictUnits(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void strictUnitsOn() {
        doReturn(builder).when(builder).strictUnits(anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictUnitsOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictUnitsOn();
        verify(builder).strictUnits(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void strictUnitsOff() {
        doReturn(builder).when(builder).strictUnits(anyBoolean());

        final LessOptionsBuilder builder2 = builder.strictUnitsOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).strictUnitsOff();
        verify(builder).strictUnits(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapRootPath() {
        final String sourceMapRootPath = "sourceMapRootPath";

        final LessOptionsBuilder builder2 = builder.sourceMapRootPath(sourceMapRootPath);

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapRootPath(sourceMapRootPath);
        verify(builder).getOptions();
        verify(options).setSourceMapRootPath(sourceMapRootPath);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapRootPathOff() {
        doReturn(builder).when(builder).sourceMapRootPath(null);

        final LessOptionsBuilder builder2 = builder.sourceMapRootPathOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapRootPathOff();
        verify(builder).sourceMapRootPath(null);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapBasePath() {
        final String sourceMapBasePath = "sourceMapBasePath";

        final LessOptionsBuilder builder2 = builder.sourceMapBasePath(sourceMapBasePath);

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapBasePath(sourceMapBasePath);
        verify(builder).getOptions();
        verify(options).setSourceMapBasePath(sourceMapBasePath);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapBasePathOff() {
        doReturn(builder).when(builder).sourceMapBasePath(null);

        final LessOptionsBuilder builder2 = builder.sourceMapBasePathOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapBasePathOff();
        verify(builder).sourceMapBasePath(null);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapLessInline_true() {
        final LessOptionsBuilder builder2 = builder.sourceMapLessInline(true);

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapLessInline(true);
        verify(builder).getOptions();
        verify(options).setSourceMapLessInline(true);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapLessInline_false() {
        final LessOptionsBuilder builder2 = builder.sourceMapLessInline(false);

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapLessInline(false);
        verify(builder).getOptions();
        verify(options).setSourceMapLessInline(false);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapLessInlineOn() {
        doReturn(builder).when(builder).sourceMapLessInline(anyBoolean());

        final LessOptionsBuilder builder2 = builder.sourceMapLessInlineOn();

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapLessInlineOn();
        verify(builder).sourceMapLessInline(true);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapLessInlineOff() {
        doReturn(builder).when(builder).sourceMapLessInline(anyBoolean());

        final LessOptionsBuilder builder2 = builder.sourceMapLessInlineOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapLessInlineOff();
        verify(builder).sourceMapLessInline(false);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void sourceMapUrl() {
        final String sourceMapUrl = "sourceMapUrl";

        final LessOptionsBuilder builder2 = builder.sourceMapUrl(sourceMapUrl);

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapUrl(sourceMapUrl);
        verify(builder).getOptions();
        verify(options).setSourceMapUrl(sourceMapUrl);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void sourceMapUrlOff() {
        doReturn(builder).when(builder).sourceMapUrl(null);

        final LessOptionsBuilder builder2 = builder.sourceMapUrlOff();

        assertThat(builder2).isSameAs(builder);
        verify(builder).sourceMapUrlOff();
        verify(builder).sourceMapUrl(null);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void encoding_charsequence() {
        final String encoding = "encoding";

        final LessOptionsBuilder builder2 = builder.encoding(encoding);

        assertThat(builder2).isSameAs(builder);
        verify(builder).encoding(encoding);
        verify(builder).getOptions();
        verify(options).setEncoding(encoding);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void encoding_charset() {
        final Charset encoding = Charset.forName("UTF-8");

        final LessOptionsBuilder builder2 = builder.encoding(encoding);

        assertThat(builder2).isSameAs(builder);
        verify(builder).encoding(encoding);
        verify(builder).getOptions();
        verify(options).setEncoding("UTF-8");
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void encodingPlatformDefault() {
        doReturn(builder).when(builder).encoding((CharSequence) null);

        final LessOptionsBuilder builder2 = builder.encodingPlatformDefault();

        assertThat(builder2).isSameAs(builder);
        verify(builder).encodingPlatformDefault();
        verify(builder).encoding((CharSequence) null);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void fileSystems_nullCollection() {
        final Collection<CharSequence> fileSystems = null;

        final LessOptionsBuilder builder2 = builder.fileSystems(fileSystems);

        assertThat(builder2).isSameAs(builder);
        verify(builder).fileSystems(fileSystems);
        verify(builder).getOptions();
        verify(options).setFileSystems(null);
        verifyNoMoreInteractions(builder, options);
    }

    @Test
    public void fileSystems_notNullCollection() {
        final Collection<CharSequence> fileSystems = Arrays.<CharSequence>asList("fileSystem1", "fileSystem2");

        final LessOptionsBuilder builder2 = builder.fileSystems(fileSystems);

        assertThat(builder2).isSameAs(builder);
        verify(builder).fileSystems(fileSystems);
        verify(builder).getOptions();
        @SuppressWarnings("unchecked")
        final ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        verify(options).setFileSystems(captor.capture());
        assertThat(captor.getValue()).containsExactly("fileSystem1", "fileSystem2");
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void fileSystems_nullArray() {
        doReturn(builder).when(builder).fileSystems((Collection<CharSequence>) null);
        final CharSequence[] fileSystems = null;

        final LessOptionsBuilder builder2 = builder.fileSystems(fileSystems);

        assertThat(builder2).isSameAs(builder);
        verify(builder).fileSystems(fileSystems);
        verify(builder).fileSystems((Collection<CharSequence>) null);
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }

    @Test
    public void fileSystems_notNullArray() {
        @SuppressWarnings("unchecked")
        final Class<Collection<CharSequence>> collectionOfCharSequenceClass = (Class<Collection<CharSequence>>) (Class<?>) Collection.class;
        doReturn(builder).when(builder).fileSystems(any(collectionOfCharSequenceClass));
        final CharSequence[] fileSystems = new String[] { "fl1", "fl2" };

        final LessOptionsBuilder builder2 = builder.fileSystems(fileSystems);

        assertThat(builder2).isSameAs(builder);
        verify(builder).fileSystems(fileSystems);
        @SuppressWarnings("unchecked")
        final ArgumentCaptor<Collection<CharSequence>> captor = ArgumentCaptor.forClass(Collection.class);
        verify(builder).fileSystems(captor.capture());
        assertThat(captor.getValue()).containsExactly("fl1", "fl2");
        verifyNoMoreInteractions(builder);
        verifyZeroInteractions(options);
    }
}
