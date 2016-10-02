package biz.gabrys.lesscss.compiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public final class CompilerOptionsBuilderTest {

    @Test
    public void getIeCompatabilityCompilerArgument_compatibilityDisabled_returnsArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setIeCompatability(false);
        final String argument = builder.getIeCompatabilityCompilerArgument();
        Assert.assertEquals("Should return argument to disable compatible with IE.", "--no-ie-compat", argument);
    }

    @Test
    public void getIeCompatabilityCompilerArgument_compatibilityEnabled_returnsEmptyText() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setIeCompatability(true);
        final String argument = builder.getIeCompatabilityCompilerArgument();
        Assert.assertEquals("Should return empty text to enable compatible with IE.", "", argument);
    }

    @Test
    public void getIncludePathsCompilerArgument_pathsAreEmpty_returnsEmptyText() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();

        builder.setIncludePaths(null);
        String argument = builder.getIncludePathsCompilerArgument();
        Assert.assertEquals("Should return empty text for null include paths.", "", argument);

        builder.setIncludePaths(Collections.<String>emptyList());
        argument = builder.getIncludePathsCompilerArgument();
        Assert.assertEquals("Should return empty text for empty include paths.", "", argument);
    }

    @Test
    public void getIncludePathsCompilerArgument_pathsContaintOneElement_returnsArgument() {
        final OperatingSystemChecker checker = Mockito.mock(OperatingSystemChecker.class);
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder(checker);
        builder.setIncludePaths(Arrays.asList("url1"));
        final String argument = builder.getIncludePathsCompilerArgument();
        Assert.assertEquals("Should return argument for include paths.", "--include-path=url1", argument);
        Mockito.verifyZeroInteractions(checker);
    }

    @Test
    public void getIncludePathsCompilerArgument_pathsContainTwoElements_returnsArgument() {
        final OperatingSystemChecker checker = Mockito.mock(OperatingSystemChecker.class);
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder(checker);
        builder.setIncludePaths(Arrays.asList("url1", "url2"));

        Mockito.when(checker.isWindows()).thenReturn(true);
        String argument = builder.getIncludePathsCompilerArgument();
        Assert.assertEquals("Should return argument for include paths (Windows).", "--include-path=url1;url2", argument);

        Mockito.when(checker.isWindows()).thenReturn(false);
        argument = builder.getIncludePathsCompilerArgument();
        Assert.assertEquals("Should return argument for include paths (not Windows).", "--include-path=url1:url2", argument);
    }

    @Test
    public void getIncludePathsCompilerArgument_pathsContainThreeElements_returnsArgument() {
        final OperatingSystemChecker checker = Mockito.mock(OperatingSystemChecker.class);
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder(checker);
        builder.setIncludePaths(Arrays.asList("url1", "url2", "url3"));

        Mockito.when(checker.isWindows()).thenReturn(true);
        String argument = builder.getIncludePathsCompilerArgument();
        Assert.assertEquals("Should return argument for include paths (Windows).", "--include-path=url1;url2;url3", argument);

        Mockito.when(checker.isWindows()).thenReturn(false);
        argument = builder.getIncludePathsCompilerArgument();
        Assert.assertEquals("Should return argument for include paths (not Windows).", "--include-path=url1:url2:url3", argument);
    }

    @Test
    public void getMinifiedCompilerArgument_minifiedDisabled_returnsEmptyText() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setMinified(false);
        final String argument = builder.getMinifiedCompilerArgument();
        Assert.assertEquals("Should return empty text to disable code minification.", "", argument);
    }

    @Test
    public void getMinifiedCompilerArgument_minifiedEnabled_returnsArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setMinified(true);
        final String argument = builder.getMinifiedCompilerArgument();
        Assert.assertEquals("Should return argument to enable code minification.", "-x", argument);
    }

    @Test
    public void getRelativeUrlsCompilerArgument_relativePathsDisabled_returnsEmptyText() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setRelativeUrls(false);
        final String argument = builder.getRelativeUrlsCompilerArgument();
        Assert.assertEquals("Should return empty text to disable rewriting relative URLs.", "", argument);
    }

    @Test
    public void getRelativeUrlsCompilerArgument_relativePathsEnabled_returnsArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setRelativeUrls(true);
        final String argument = builder.getRelativeUrlsCompilerArgument();
        Assert.assertEquals("Should return argument to enable rewriting relative URLs.", "--relative-urls", argument);
    }

    @Test
    public void getRootPathCompilerArgument_pathIsEmpty_returnsEmptyText() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();

        builder.setRootPath(null);
        String argument = builder.getRootPathCompilerArgument();
        Assert.assertEquals("Should return empty text for null root path.", "", argument);

        builder.setRootPath("");
        argument = builder.getRootPathCompilerArgument();
        Assert.assertEquals("Should return empty text for empty root path.", "", argument);

        builder.setRootPath(" \t\r\n");
        argument = builder.getRootPathCompilerArgument();
        Assert.assertEquals("Should return empty text for blank root path.", "", argument);
    }

    @Test
    public void getRootPathCompilerArgument_pathIsNotEmpty_returnsArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();

        builder.setRootPath("rooturl");
        String argument = builder.getRootPathCompilerArgument();
        Assert.assertEquals("Should return argument for root path with letters.", "--rootpath=rooturl", argument);

        builder.setRootPath("\t rootWithSpacesUrl \t");
        argument = builder.getRootPathCompilerArgument();
        Assert.assertEquals("Should return argument for root path with letters & whitespaces.", "--rootpath=rootWithSpacesUrl", argument);
    }

    @Test
    public void getStrictImportsCompilerArgument_strictDisabled_returnsEmptyText() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setStrictImports(false);
        final String argument = builder.getStrictImportsCompilerArgument();
        Assert.assertEquals("Should return empty text to disable strict imports.", "", argument);
    }

    @Test
    public void getStrictImportsCompilerArgument_strictEnabled_returnsArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setStrictImports(true);
        final String argument = builder.getStrictImportsCompilerArgument();
        Assert.assertEquals("Should return argument to enable strict imports.", "--strict-imports", argument);
    }

    @Test
    public void getStrictMathCompilerArgument_strictDisabled_returnsOffArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setStrictMath(false);
        final String argument = builder.getStrictMathCompilerArgument();
        Assert.assertEquals("Should return off argument to disable strict math.", "--strict-math=off", argument);
    }

    @Test
    public void getStrictMathCompilerArgument_strictEnabled_returnsOnArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setStrictMath(true);
        final String argument = builder.getStrictMathCompilerArgument();
        Assert.assertEquals("Should return on argument to enable strict math.", "--strict-math=on", argument);
    }

    @Test
    public void getStrictUnitsCompilerArgument_strictDisabled_returnsOffArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setStrictUnits(false);
        final String argument = builder.getStrictUnitsCompilerArgument();
        Assert.assertEquals("Should return off argument to disable strict units.", "--strict-units=off", argument);
    }

    @Test
    public void getStrictUnitsCompilerArgument_strictEnabled_returnsOnArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();
        builder.setStrictUnits(true);
        final String argument = builder.getStrictUnitsCompilerArgument();
        Assert.assertEquals("Should return on argument to enable strict units.", "--strict-units=on", argument);
    }

    @Test
    public void getUrlsArgumentCompilerArgument_argIsEmpty_returnsEmptyText() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();

        builder.setUrlsArgument(null);
        String argument = builder.getUrlsArgumentCompilerArgument();
        Assert.assertEquals("Should return empty text for null URLs argument.", "", argument);

        builder.setUrlsArgument("");
        argument = builder.getUrlsArgumentCompilerArgument();
        Assert.assertEquals("Should return empty text for empty URLs argument.", "", argument);

        builder.setUrlsArgument(" \t\r\n");
        argument = builder.getUrlsArgumentCompilerArgument();
        Assert.assertEquals("Should return empty text for blank URLs argument.", "", argument);
    }

    @Test
    public void getUrlsArgumentCompilerArgument_argIsNotEmpty_returnsArgument() {
        final CompilerOptionsBuilder builder = new CompilerOptionsBuilder();

        builder.setUrlsArgument("args");
        String argument = builder.getUrlsArgumentCompilerArgument();
        Assert.assertEquals("Should return argument for URLs argument with letters", "--url-args=\"args\"", argument);

        builder.setUrlsArgument(" \t argsWithWhitepsaces \t ");
        argument = builder.getUrlsArgumentCompilerArgument();
        Assert.assertEquals("Should return argument for URLs argument with letters & whitespaces", "--url-args=\"argsWithWhitepsaces\"",
                argument);

        builder.setUrlsArgument("argsWith\"Quotes\"");
        argument = builder.getUrlsArgumentCompilerArgument();
        Assert.assertEquals("Should return argument for URLs argument with letters & quotes", "--url-args=\"argsWith\\\"Quotes\\\"\"",
                argument);
    }

    @Test
    public void create_defaultValues_returnsDefaultOptions() {
        final CompilerOptionsBuilder builder = Mockito.spy(new CompilerOptionsBuilder());
        final CompilerOptions options = builder.create();
        final List<Object> arguments = options.getArguments();
        Assert.assertEquals("Arguments should count 2 elements", 2, arguments.size());
        Assert.assertTrue("Strict math should be disabled", arguments.contains("--strict-math=off"));
        Assert.assertTrue("Strict units should be disabled", arguments.contains("--strict-units=off"));

        Mockito.verify(builder).getIeCompatabilityCompilerArgument();
        Mockito.verify(builder).getIncludePathsCompilerArgument();
        Mockito.verify(builder).getMinifiedCompilerArgument();
        Mockito.verify(builder).getRelativeUrlsCompilerArgument();
        Mockito.verify(builder).getRootPathCompilerArgument();
        Mockito.verify(builder).getStrictImportsCompilerArgument();
        Mockito.verify(builder).getStrictMathCompilerArgument();
        Mockito.verify(builder).getStrictUnitsCompilerArgument();
        Mockito.verify(builder).getUrlsArgumentCompilerArgument();
    }
}
