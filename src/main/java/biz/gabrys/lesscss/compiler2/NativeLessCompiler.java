/*
 * LessCSS Compiler
 * http://lesscss-compiler.projects.gabrys.biz/
 *
 * Copyright (c) 2015 Adam Gabrys
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 *  - a copy of the License at project page
 *  - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.lesscss.compiler2;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;

import biz.gabrys.lesscss.compiler2.io.IOUtils;

/**
 * <p>
 * Native <a href="https://github.com/less/less.js/releases/tag/v1.7.5">Less 1.7.5</a> compiler written in JavaScript by
 * Less team and modified by Adam Gabryś.
 * </p>
 * <p>
 * The library is based on the official <a href="http://lesscss.org/">Less</a> JavaScript compiler adapted to the
 * <a href="https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino">Rhino</a> engine.
 * </p>
 * <p>
 * The idea for this class implementation was based on the
 * <a href="https://github.com/marceloverdijk/lesscss-java">lesscss-java</a> library by
 * <a href="https://github.com/marceloverdijk">Marcel Overdijk</a>.
 * </p>
 * <p>
 * Example code:
 * </p>
 * 
 * <pre>
 * // create compiler and source file
 * NativeLessCompiler compiler = new {@link #NativeLessCompiler() NativeLessCompiler}();
 * String input = "/less/file.less";
 * 
 * // compile source file
 * String cssCode = compiler.{@link #execute(Collection) execute}(new {@link NativeLessOptionsBuilder}().inputFile(input).build());
 * 
 * // compile source file with enabled CSS code compression
 * Collection&lt;Object&gt; options = new {@link NativeLessOptionsBuilder}().inputFile(input).compress(true).build();
 * String cssCompressedCode = compiler.{@link #execute(Collection) execute}(options);
 * </pre>
 * 
 * @since 2.0.0
 * @see NativeLessOptionsBuilder
 */
public class NativeLessCompiler {

    private static final String CHARSET = "UTF-8";

    private final Object mutex = new Object();

    private Scriptable scope;
    private ByteArrayOutputStream console;
    private Function compiler;

    /**
     * Constructs a new instance.
     * @since 2.0.0
     */
    public NativeLessCompiler() {
        // do nothing
    }

    /**
     * Executes the compiler. You can use standard <a href="http://lesscss.org/usage/index.html#less-options">Less
     * command line options</a> with some exceptions:
     * <ul>
     * <li>use multiple {@code --include-path} options to declare multiple paths instead of the using separator ';'
     * character on Windows and ':' character on Unix/Linux machines</li>
     * <li>does not support options:
     * <ul>
     * <li>allow imports from insecure HTTPS hosts ({@code --insecure})</li>
     * <li>lint ({@code -l} or {@code --lint})</li>
     * <li>makefile ({@code -M} or {@code --depends})</li>
     * <li>no color ({@code --no-color})</li>
     * <li>plugins ({@code --plugin})</li>
     * <li>version ({@code -v} or {@code --version})</li>
     * </ul>
     * </li>
     * </ul>
     * <p>
     * It also supports few non-standard options:
     * </p>
     * <ul>
     * <li>{@code --encoding} - an encoding used to read source files and save generated code (default: platform default
     * encoding)</li>
     * <li>{@code --file-system} - a file system implementation used to fetch files (default:
     * <code>biz.gabrys.lesscss.compiler2.filesystem.{@link biz.gabrys.lesscss.compiler2.filesystem.LocalFileSystem LocalFileSystem}</code>).
     * This option could be specified multiple times (the order matters)</li>
     * </ul>
     * @param options the compiler options (cannot be {@code null}).
     * @return the compiler output (depends on options it can be e.g. CSS code, logs).
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ConfigurationException if the compiler is configured incorrectly.
     * @throws ReadFileException if the compiler cannot read source file or any &#64;import operation point to
     *             non-existent file.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @throws CompilerException if an other error occurred during execution.
     * @since 2.0.0
     * @see NativeLessOptionsBuilder
     */
    public String execute(final Collection<?> options) {
        synchronized (mutex) {
            if (compiler == null) {
                initialize();
            }
            try {
                final Context context = Context.enter();

                final ScriptableObject compileScope = (ScriptableObject) context.newObject(scope);
                compileScope.setParentScope(null);
                compileScope.setPrototype(scope);

                final Scriptable arguments = context.newArray(compileScope, options.toArray(new Object[0]));
                compileScope.defineProperty("arguments", arguments, ScriptableObject.DONTENUM);

                compiler.call(context, compileScope, null, new Object[0]);
                return console.toString(CHARSET);

            } catch (final JavaScriptException e) {
                throw parseException(e);
            } catch (final Exception e) {
                throw new CompilerException(e);
            } finally {
                console.reset();
                Context.exit();
            }
        }
    }

    private void initialize() {
        InputStream inputStream = null;
        InputStreamReader streamReader = null;
        try {
            final Context context = Context.enter();
            context.setLanguageVersion(Context.VERSION_1_8);

            final Global global = new Global();
            global.init(context);
            scope = context.initStandardObjects(global);

            console = new ByteArrayOutputStream();
            global.setOut(new PrintStream(console, false, CHARSET));

            final URL lessFile = NativeLessCompiler.class.getResource("/biz/gabrys/lesscss/compiler2/less-rhino-1.7.5.js");
            final URL lesscFile = NativeLessCompiler.class.getResource("/biz/gabrys/lesscss/compiler2/lessc-rhino-1.7.5.js");

            final Collection<InputStream> streams = new ArrayList<InputStream>();
            streams.add(lessFile.openConnection().getInputStream());
            streams.add(lesscFile.openConnection().getInputStream());

            inputStream = new SequenceInputStream(Collections.enumeration(streams));
            streamReader = new InputStreamReader(inputStream, CHARSET);
            compiler = (Function) context.compileReader(streamReader, lessFile.toString(), 1, null);

        } catch (final Exception e) {
            throw new InitializationException("Failed to initialize native Less compiler", e);

        } finally {
            IOUtils.closeQuietly(streamReader);
            IOUtils.closeQuietly(inputStream);
            Context.exit();
        }
    }

    private static CompilerException parseException(final JavaScriptException exception) {
        final Scriptable value = (Scriptable) exception.getValue();
        if (value != null && ScriptableObject.hasProperty(value, "message")) {
            final String message = ScriptableObject.getProperty(value, "message").toString();

            final Iterable<ExceptionConverter> converters = Arrays.asList(new ReadFileErrorExceptionConverter(),
                    new NotSupportedProtocolExceptionConverter(), new ConfigurationExceptionConverter(),
                    new SourceFileExceptionConverter());

            for (final ExceptionConverter converter : converters) {
                final Matcher matcher = converter.getPattern().matcher(message);
                if (matcher.find()) {
                    return converter.createException(exception, matcher, message);
                }
            }
            return new SyntaxException(message, exception);
        }
        return new SyntaxException(exception);
    }

    private interface ExceptionConverter {

        Pattern getPattern();

        CompilerException createException(Exception cause, Matcher matcher, String message);
    }

    private static class ReadFileErrorExceptionConverter implements ExceptionConverter {

        private static final String MARKUP = "\\{gabrys-lesscss-compiler-filesystem-exception\\}";

        private static final int ORIGINAL_EXCEPTION_MESSAGE = 1;
        private static final int IMPORTED_FILENAME_GROUP_INDEX = 2;
        private static final int SOURCE_FILEPATH_GROUP_INDEX = 3;

        @Override
        public Pattern getPattern() {
            return Pattern.compile("^FileError:\\sFileSystemError:" + MARKUP + "[^:]+:\\s(.+)" + MARKUP + ":\\s+'(.+)'\\s+in\\s((?s).*)$");
        }

        @Override
        public CompilerException createException(final Exception cause, final Matcher matcher, final String message) {
            final String importedFilename = matcher.group(IMPORTED_FILENAME_GROUP_INDEX);
            return new ReadFileException(String.format("Cannot read file \"%s\": %s.%nError in %s", importedFilename,
                    matcher.group(ORIGINAL_EXCEPTION_MESSAGE), matcher.group(SOURCE_FILEPATH_GROUP_INDEX)), cause, importedFilename);
        }
    }

    private static class NotSupportedProtocolExceptionConverter implements ExceptionConverter {

        private static final int IMPORTED_FILENAME_GROUP_INDEX = 1;
        private static final int SOURCE_FILEPATH_GROUP_INDEX = 2;

        @Override
        public Pattern getPattern() {
            return Pattern.compile("^FileError:\\sNotSupportedProtocol:\\s+'(.+)'\\s+in\\s((?s).*)$");
        }

        @Override
        public CompilerException createException(final Exception cause, final Matcher matcher, final String message) {
            final String importedFilename = matcher.group(IMPORTED_FILENAME_GROUP_INDEX);
            return new ReadFileException(String.format("Cannot read file \"%s\": the protocol is not supported.%nError in %s",
                    importedFilename, matcher.group(SOURCE_FILEPATH_GROUP_INDEX)), cause, importedFilename);
        }
    }

    private static class ConfigurationExceptionConverter implements ExceptionConverter {

        @Override
        public Pattern getPattern() {
            return Pattern.compile("^Configuration problem: (.*)");
        }

        @Override
        public CompilerException createException(final Exception cause, final Matcher matcher, final String message) {
            return new ConfigurationException(matcher.group(1), cause);
        }
    }

    private static class SourceFileExceptionConverter implements ExceptionConverter {

        @Override
        public Pattern getPattern() {
            return Pattern.compile("^Couldn't open file (.*)");
        }

        @Override
        public CompilerException createException(final Exception cause, final Matcher matcher, final String message) {
            return new ReadFileException(message, cause, matcher.group(1));
        }
    }
}
