/*
 * LessCSS Compiler
 * http://lesscss-compiler.projects.gabrys.biz/
 *
 * Copyright (c) 2015 Adam Gabryś
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 *  - a copy of the License at project page
 *  - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.lesscss.compiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.ArrayList;
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

/**
 * <p>
 * Default implementation of the {@link LessCompiler}. Compiler is compatible with version
 * <a href="https://github.com/less/less.js/releases/tag/v1.7.5">1.7.5</a> with certain restrictions:
 * </p>
 * <ul>
 * <li>cannot fetch sources from Internet, e.g. {@code @import (less) "http://example.org/style.less"}</li>
 * </ul>
 * <p>
 * The library is based on the official <a href="http://lesscss.org/">Less</a> JavaScript compiler adapted to the
 * <a href="https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino">Rhino</a> engine.
 * </p>
 * <p>
 * The idea for this class implementation was based on the
 * <a href="https://github.com/marceloverdijk/lesscss-java">lesscss-java</a> library by
 * <a href="https://github.com/marceloverdijk">Marcel Overdijk</a>.
 * </p>
 * @since 1.0
 */
public class LessCompilerImpl implements LessCompiler {

    private static final Pattern IMPORT_ERROR_PATTERN = Pattern.compile("^FileError:\\s+'(.+)'\\s+wasn't\\s+found\\s+(?s).*");
    private static final int IMPORT_ERROR_FILE_NAME_GROUP_INDEX = 1;

    private static final String CHARSET = "UTF-8";

    private final Object mutex = new Object();

    private Scriptable scope;
    private ByteArrayOutputStream console;
    private Function compiler;

    /**
     * Constructs a new instance.
     * @since 1.0
     */
    public LessCompilerImpl() {
        // do nothing
    }

    /**
     * {@inheritDoc}
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ResolveImportException if an error occurred during resolve imports.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @since 1.0
     */
    public String compile(final File source) throws CompilerException {
        return compile(source, new CompilerOptions(Collections.emptyList()));
    }

    /**
     * {@inheritDoc}
     * @throws InitializationException if an error occurred during compiler initialization.
     * @throws ResolveImportException if an error occurred during resolve imports.
     * @throws SyntaxException if a syntax error occurred during source file compilation.
     * @since 1.0
     */
    public String compile(final File input, final CompilerOptions options) throws CompilerException {
        synchronized (mutex) {
            if (compiler == null) {
                initialize();
            }
            try {
                final Context context = Context.enter();

                final ScriptableObject compileScope = (ScriptableObject) context.newObject(scope);
                compileScope.setParentScope(null);
                compileScope.setPrototype(scope);

                final Scriptable arguments = context.newArray(compileScope, prepareCompilerArguments(input, options));
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

    private void initialize() throws InitializationException {
        try {
            final Context context = Context.enter();
            context.setLanguageVersion(Context.VERSION_1_8);

            final Global global = new Global();
            global.init(context);
            scope = context.initStandardObjects(global);

            console = new ByteArrayOutputStream();
            global.setOut(new PrintStream(console, false, CHARSET));

            final URL lessFile = LessCompilerImpl.class.getResource("/less/less-rhino-1.7.5.js");
            final URL lesscFile = LessCompilerImpl.class.getResource("/less/lessc-rhino-1.7.5.js");

            final Collection<InputStream> streams = new ArrayList<InputStream>();
            streams.add(lessFile.openConnection().getInputStream());
            streams.add(lesscFile.openConnection().getInputStream());

            final InputStreamReader reader = new InputStreamReader(new SequenceInputStream(Collections.enumeration(streams)), CHARSET);
            compiler = (Function) context.compileReader(reader, lessFile.toString(), 1, null);

        } catch (final Exception e) {
            throw new InitializationException("Failed to initialize Less compiler", e);

        } finally {
            Context.exit();
        }
    }

    private static Object[] prepareCompilerArguments(final File sourceFile, final CompilerOptions options) {
        final Collection<Object> arguments = new ArrayList<Object>();
        arguments.addAll(options.getArguments());
        arguments.add(sourceFile.getAbsolutePath());
        return arguments.toArray();
    }

    private CompilerException parseException(final JavaScriptException exception) {
        final Scriptable value = (Scriptable) exception.getValue();
        if (value != null && ScriptableObject.hasProperty(value, "message")) {
            final String message = ScriptableObject.getProperty(value, "message").toString();
            final Matcher matcher = IMPORT_ERROR_PATTERN.matcher(message);
            if (matcher.find()) {
                return new ResolveImportException(message, matcher.group(IMPORT_ERROR_FILE_NAME_GROUP_INDEX), exception);
            }
            return new SyntaxException(message, exception);
        }
        return new SyntaxException(exception);
    }
}
