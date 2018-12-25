/* Less.js v1.7.5 RHINO | Copyright (c) 2009-2014, Alexis Sellier <self@cloudhead.net> */
/*
 * Modified by Adam Gabryś
 */

/*global name:true, less, loadStyleSheet, os */

var lessCompilerGlobals = {
    encoding: java.nio.charset.Charset.defaultCharset().toString()
};

function formatError(ctx) {
    var message = "";
    var extract = ctx.extract;
    var error = [];

    // only output a stack if it isn't a less error
    if (ctx.stack && !ctx.type) {
        return ctx.stack;
    }

    if (!ctx.hasOwnProperty('index') || !extract) {
        return ctx.stack || ctx.message;
    }

    if (typeof (extract[0]) === 'string') {
        error.push((ctx.line - 1) + ' ' + extract[0]);
    }

    if (typeof (extract[1]) === 'string') {
        var errorTxt = ctx.line + ' ';
        if (extract[1]) {
            errorTxt += extract[1].slice(0, ctx.column) + extract[1][ctx.column] + extract[1].slice(ctx.column + 1);
        }
        error.push(errorTxt);
    }

    if (typeof (extract[2]) === 'string') {
        error.push((ctx.line + 1) + ' ' + extract[2]);
    }
    error = error.join('\n') + '\n';

    message += ctx.type + 'Error: ' + ctx.message;
    if (ctx.filename) {
        message += ' in ' + ctx.filename + ' on line ' + ctx.line + ', column ' + (ctx.column + 1) + ':';
    }

    message += '\n' + error;

    if (ctx.callLine) {
        message += 'from ' + (ctx.filename || '') + '/n';
        message += ctx.callLine + ' ' + ctx.callExtract + '/n';
    }

    return message;
}

function writeError(ctx) {
    throw new Error(formatError(ctx));
}

less.Parser.fileLoader = function(file, currentFileInfo, callback, env) {

    var href = file;
    if (currentFileInfo && currentFileInfo.currentDirectory && !/^\//.test(file)) {
        href = less.modules.path.join(currentFileInfo.currentDirectory, file);
    }

    var path = less.modules.path.dirname(href);

    var newFileInfo = {
        currentDirectory: path + '/',
        filename: href
    };

    if (currentFileInfo) {
        newFileInfo.entryPath = currentFileInfo.entryPath;
        newFileInfo.rootpath = currentFileInfo.rootpath;
        newFileInfo.rootFilename = currentFileInfo.rootFilename;
        newFileInfo.relativeUrls = currentFileInfo.relativeUrls;
    } else {
        newFileInfo.entryPath = path;
        newFileInfo.rootpath = less.rootpath || path;
        newFileInfo.rootFilename = href;
        newFileInfo.relativeUrls = env.relativeUrls;
    }

    var j = file.lastIndexOf('/');
    if (newFileInfo.relativeUrls && !/^(?:[a-z-]+:|\/)/.test(file) && j != -1) {
        var relativeSubDirectory = file.slice(0, j + 1);
        // append (sub|sup) directory path of imported file
        newFileInfo.rootpath = newFileInfo.rootpath + relativeSubDirectory;
    }
    newFileInfo.currentDirectory = path;
    newFileInfo.filename = href;

    var data = null;
    try {
        data = readFile(href);
    } catch (e) {
        callback({
            type: 'File',
            message: "'" + less.modules.path.basename(href) + "' does not exist"
        });
        return;
    }

    try {
        callback(null, data, href, newFileInfo, {
            lastModified: 0
        });
    } catch (e) {
        callback(e, null, href);
    }
};

function readFile(filename) {
    var content = Packages.biz.gabrys.lesscss.compiler2.io.FileUtils.read(new java.io.File(filename), lessCompilerGlobals.encoding);
    // convert java.lang.String to JavaScript string
    return '' + content;
}

function writeFile(filename, content) {
    Packages.biz.gabrys.lesscss.compiler2.io.FileUtils.write(new java.io.File(filename), content, lessCompilerGlobals.encoding);
}

// Command line integration via Rhino
(function(args) {

    function throwConfigurationError(message) {
        throw new Error('Configuration problem: ' + message);
    }

    var options = {
        compress: false,
        silent: false,
        paths: [],
        strictImports: false,
        rootpath: '',
        relativeUrls: false,
        ieCompat: true,
        strictMath: false,
        strictUnits: false
    };

    var validateArgument = function(arg, option) {
        if (!option) {
            throwConfigurationError('"' + arg + '" option requires a parameter');
        }
    };

    var convertToBoolean = function(arg, option) {
        var onOff = /^((on|t|true|y|yes)|(off|f|false|n|no))$/i.exec(option);
        if (!onOff) {
            throwConfigurationError('Unable to parse "' + option + '" for ' + arg
                    + ' option as a boolean. Use one of on/t/true/y/yes/off/f/false/n/no');
        }
        return Boolean(onOff[2]);
    };

    var sourceMapFilePath;
    var sourceMapInline = false;

    args = args.filter(function(arg) {
        var match = arg.match(/^-I(.+)$/);

        if (match) {
            options.paths.push(match[1]);
            return false;
        }

        match = arg.match(/^--?([a-z][0-9a-z-]*)(?:=(.*))?$/i);
        if (match) {
            arg = match[1];
        } else {
            // was (?:=([^\s]*)), check!
            return arg;
        }

        switch (arg) {
            case 's':
            case 'silent':
                options.silent = true;
                break;
            case 'strict-imports':
                options.strictImports = true;
                break;
            case 'x':
            case 'compress':
                options.compress = true;
                break;
            case 'no-ie-compat':
                options.ieCompat = false;
                break;
            case 'no-js':
                options.javascriptEnabled = false;
                break;
            case 'include-path':
                validateArgument(arg, match[2]);
                options.paths = match[2].split('gabrys-lesscss-compiler-path-separator').map(function(p) {
                    if (p) {
                        return p;
                    }
                });
                break;
            case 'line-numbers':
                validateArgument(arg, match[2]);
                options.dumpLineNumbers = match[2];
                break;
            case 'source-map':
                options.sourceMap = true;
                if (match[2]) {
                    sourceMapFilePath = match[2];
                }
                break;
            case 'source-map-rootpath':
                validateArgument(arg, match[2]);
                options.sourceMapRootpath = match[2];
                break;
            case 'source-map-basepath':
                validateArgument(arg, match[2]);
                options.sourceMapBasepath = match[2];
                break;
            case 'source-map-map-inline':
                sourceMapInline = true;
                options.sourceMap = true;
                break;
            case 'source-map-less-inline':
                options.outputSourceFiles = true;
                break;
            case 'source-map-url':
                validateArgument(arg, match[2]);
                options.sourceMapURL = match[2];
                break;
            case 'rp':
            case 'rootpath':
                validateArgument(arg, match[2]);
                options.rootpath = match[2].replace(/\\/g, '/');
                break;
            case 'ru':
            case 'relative-urls':
                options.relativeUrls = true;
                break;
            case 'sm':
            case 'strict-math':
                validateArgument(arg, match[2]);
                options.strictMath = convertToBoolean(arg, match[2]);
                break;
            case 'su':
            case 'strict-units':
                validateArgument(arg, match[2]);
                options.strictUnits = convertToBoolean(arg, match[2]);
                break;
            case 'encoding':
                validateArgument(arg, match[2]);
                lessCompilerGlobals.encoding = match[2];
                break;
            default:
                throwConfigurationError('Invalid option "' + arg + '"');
        }
    });

    var source = args[0];
    if (source == null) {
        throwConfigurationError('Source file has not been specified');
    }

    var output = args[1];

    if (options.sourceMap) {
        if (sourceMapFilePath == null && output == null && !sourceMapInline) {
            throwConfigurationError('The sourcemap option has an optional filename only if the output CSS filename is given or you also pass the source-map-map-inline option');
        }
        if (!sourceMapInline) {
            var sourceMapFilename = sourceMapFilePath;
            if (sourceMapFilePath == null) {
                sourceMapFilePath = output + '.map';
                sourceMapFilename = less.modules.path.basename(sourceMapFilePath);
            }
            options.writeSourceMap = function(sourceMapContent) {
                writeFile(sourceMapFilePath, sourceMapContent);
            };
            options.sourceMapFilename = sourceMapFilename;
            options.sourceMapOutputFilename = output;
        }
    }

    var input = null;
    try {
        input = readFile(source);
    } catch (e) {
        throw new Error('Couldn\'t open file ' + source);
    }

    options.filename = source;
    var result;
    try {
        var parser = new less.Parser(options);
        parser.parse(input, function(e, root) {
            if (e) {
                writeError(e);
            }
            result = root.toCSS(options);
            if (output != null) {
                writeFile(output, result);
            } else {
                print(result);
            }
            quit(0);
        });
    } catch (e) {
        writeError(e);
    }
}(arguments));
