/* Less.js v1.7.5 RHINO | Copyright (c) 2009-2014, Alexis Sellier <self@cloudhead.net> */
/*
 * Modified by Adam Gabryś
 */

less.Parser.fileLoader = function(file, currentFileInfo, callback, env) {
    var filePath = file;
    var absolute = true;

    var includePaths = [''];
    if (currentFileInfo != null && currentFileInfo.currentDirectory != null && !/^(?:[a-z-]+:|\/)/.test(file)) {
        absolute = false;
        includePaths[0] = currentFileInfo.currentDirectory;
    }
    includePaths = gabrysLessCompiler.removeDuplications(includePaths.concat(gabrysLessCompiler.includePaths));

    var fileSystem;
    try {
        for (var i = 0; i < includePaths.length; ++i) {
            if (i > 1) {
                absolute = true;
            }
            filePath = includePaths[i] + file;
            fileSystem = gabrysLessCompiler.getFileSystem(filePath);
            filePath = fileSystem.normalize(filePath);
            var expandedPath = fileSystem.expandRedirection(filePath);
            var tmpPath = filePath;
            while (expandedPath !== tmpPath) {
                tmpPath = expandedPath;
                fileSystem = gabrysLessCompiler.getFileSystem(tmpPath);
                expandedPath = fileSystem.expandRedirection(tmpPath);
            }
            if (fileSystem.exists(expandedPath)) {
                if (filePath !== expandedPath) {
                    filePath = expandedPath;
                    absolute = true;
                }
                break;
            }
        }
    } catch (e) {
        callback(convertException(e, file));
        return;
    }

    var name = Packages.biz.gabrys.lesscss.compiler2.io.FilenameUtils.getName(filePath);
    var directory = filePath.substring(0, filePath.length - name.length());

    var newFileInfo = {
        currentDirectory: directory,
        filename: filePath
    };

    if (currentFileInfo != null) {
        if (absolute) {
            newFileInfo.entryPath = directory;
            newFileInfo.rootpath = directory;
        } else {
            newFileInfo.entryPath = currentFileInfo.entryPath;
            newFileInfo.rootpath = currentFileInfo.rootpath;
        }
        newFileInfo.rootFilename = currentFileInfo.rootFilename;
        newFileInfo.relativeUrls = currentFileInfo.relativeUrls;
    } else {
        newFileInfo.entryPath = directory;
        newFileInfo.rootpath = less.rootpath || directory;
        newFileInfo.rootFilename = filePath;
        newFileInfo.relativeUrls = env.relativeUrls;
    }

    var j = file.lastIndexOf('/');
    if (newFileInfo.relativeUrls && !absolute && j !== -1) {
        var relativeSubDirectory = file.slice(0, j + 1);
        // append (sub|sup) directory path of imported file
        newFileInfo.rootpath = newFileInfo.rootpath + relativeSubDirectory;
    }

    var content;
    try {
        var fileData = fileSystem.fetch(filePath);
        content = fileData.getContentAsString();
    } catch (e) {
        callback(convertException(e, file));
        return;
    }

    try {
        callback(null, content, newFileInfo);
    } catch (e) {
        callback(e);
    }

    function convertException(exception, file) {
        return {
            type: 'File',
            message: exception.message + ": '" + file + "'"
        };
    }
};

gabrysLessCompiler.getFileSystem = function(path) {
    for (var i = 0; i < this.fileSystems.length; ++i) {
        var fileSystem = this.fileSystems[i];
        if (fileSystem.isSupported(path)) {
            return fileSystem;
        }
    }
    throw new Error('NotSupportedProtocol');
};

gabrysLessCompiler.readFile = function(path) {
    var fileSystem = this.getFileSystem(path);
    var filePath = fileSystem.normalize(path);
    var expandedPath = fileSystem.expandRedirection(filePath);
    while (expandedPath !== filePath) {
        filePath = expandedPath;
        fileSystem = this.getFileSystem(filePath);
        expandedPath = fileSystem.expandRedirection(filePath);
    }
    return fileSystem.fetch(expandedPath);
};

gabrysLessCompiler.removeDuplications = function(array) {
    var filtered = [array[0]];
    for (var i = 1; i < array.length; ++i) {
        if (!contains(filtered, array[i])) {
            filtered[filtered.length] = array[i];
        }
    }
    return filtered;

    function contains(array, element) {
        for (var i = 0; i < array.length; ++i) {
            if (array[i] === element) {
                return true;
            }
        }
        return false;
    }
};

(function(args) {

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

    var sourceMapFilePath;
    var sourceMapInline = false;
    var fileSystemsClassNames = [ 'biz.gabrys.lesscss.compiler2.filesystem.LocalFileSystem' ];

    var additionalData = {
        banner: '',
        globalVars: [],
        modifyVars: []
    };

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
                gabrysLessCompiler.includePaths[gabrysLessCompiler.includePaths.length] = match[2];
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
                validateEncoding(match[2]);
                gabrysLessCompiler.encoding = match[2];
                break;
            case 'file-systems':
                validateArgument(arg, match[2]);
                fileSystemsClassNames = match[2].split(',');
                break;
            case 'banner':
                validateArgument(arg, match[2]);
                additionalData.banner = match[2];
                break;
            case 'global-var':
                validateArgument(arg, match[2]);
                additionalData.globalVars[additionalData.globalVars.length] = parseVariable(match[2]);
                break;
            case 'modify-var':
                validateArgument(arg, match[2]);
                additionalData.modifyVars[additionalData.modifyVars.length] = parseVariable(match[2]);
                break;
            default:
                throwConfigurationError('Invalid option "' + arg + '"');
        }
    });

    gabrysLessCompiler.includePaths = gabrysLessCompiler.removeDuplications(gabrysLessCompiler.includePaths);
    if (gabrysLessCompiler.encoding == null) {
        gabrysLessCompiler.encoding = java.nio.charset.Charset.defaultCharset().name();
    }
    gabrysLessCompiler.fileSystems = createFileSystems(fileSystemsClassNames);

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
        var fileData = gabrysLessCompiler.readFile(source);
        input = fileData.getContentAsString();
    } catch (e) {
        throw new Error('Couldn\'t open file ' + source);
    }

    options.filename = source;
    var result;
    try {
        var parser = new less.Parser(options);
        parser.parse(input, function(e, root) {
            if (e) {
                throw new Error(formatError(e));
            }
            result = root.toCSS(options);
            if (output != null) {
                writeFile(output, result);
            } else {
                print(result);
            }
            quit(0);
        }, additionalData);
    } catch (e) {
        throw new Error(formatError(e));
    }

    function throwConfigurationError(message) {
        throw new Error('Configuration problem: ' + message);
    }

    function createFileSystems(classNames) {
        var fileSystems = [];
        for (var i = 0; i < classNames.length; ++i) {
            var className = classNames[i].trim();
            var clazz;
            try {
                clazz = java.lang.Class.forName(className);
            } catch (e) {
                throwConfigurationError('Cannot load a file system class: ' + className);
            }
            try {
                fileSystems[fileSystems.length] = new FileSystem(clazz.newInstance());
            } catch (e) {
                throwConfigurationError('Cannot create a new instance of the file system: ' + className);
            }
        }
        return fileSystems;

        function FileSystem(fileSystem) {
            this.isSupported = function(path) {
                try {
                    return fileSystem.isSupported(path);
                } catch (e) {
                    throw convertFileSystemException(e);
                }
            };

            this.normalize = function(path) {
                try {
                    // convert java.lang.String to JavaScript string
                    return '' + fileSystem.normalize(path);
                } catch (e) {
                    throw convertFileSystemException(e);
                }
            };

            this.exists = function(path) {
                try {
                    return fileSystem.exists(path);
                } catch (e) {
                    throw convertFileSystemException(e);
                }
            };

            this.expandRedirection = function(path) {
                try {
                    // convert java.lang.String to JavaScript string
                    return '' + fileSystem.expandRedirection(path);
                } catch (e) {
                    throw convertFileSystemException(e);
                }
            };

            this.fetch = function(path) {
                var fileData = null;
                try {
                    fileData = fileSystem.fetch(path);
                } catch (e) {
                    throw convertFileSystemException(e);
                }
                if (fileData == null) {
                    return null;
                }
                return {
                    getEncoding: function() {
                        return fileData.getEncoding();
                    },
                    getContent: function() {
                        return fileData.getContent();
                    },
                    getContentAsString: function() {
                        var encoding = this.getEncoding();
                        if (encoding == null) {
                            encoding = gabrysLessCompiler.encoding;
                        }
                        // convert java.lang.String to JavaScript string
                        return '' + new java.lang.String(fileData.getContent(), encoding);
                    }
                };
            };

            function convertFileSystemException(exception) {
                var markup = '{gabrys-lesscss-compiler-filesystem-exception}';
                return new Error('FileSystemError:' + markup + exception.message + markup);
            }
        }
    }

    function validateArgument(arg, option) {
        if (!option) {
            throwConfigurationError('"' + arg + '" option requires a parameter');
        }
    }

    function convertToBoolean(arg, option) {
        var onOff = /^((on|t|true|y|yes)|(off|f|false|n|no))$/i.exec(option);
        if (!onOff) {
            throwConfigurationError('Unable to parse "' + option + '" for ' + arg
                    + ' option as a boolean. Use one of on/t/true/y/yes/off/f/false/n/no');
        }
        return Boolean(onOff[2]);
    }

    function validateEncoding(encoding) {
        var charsetSupported = false;
        try {
            charsetSupported = java.nio.charset.Charset.isSupported(encoding)
        } catch (e) {
            // ignore
        }
        if (!charsetSupported) {
            throwConfigurationError('Encoding "' + encoding + '" is unsupported');
        }
    }

    function parseVariable(variable) {
        var index = variable.indexOf('=');
        return {
          name: variable.substring(0, index),
          value: variable.substr(index + 1)
        };
    }

    function writeFile(path, content) {
        Packages.biz.gabrys.lesscss.compiler2.io.FileUtils.write(new java.io.File(path), content, gabrysLessCompiler.encoding);
    }

    function formatError(ctx) {
        var extract = ctx.extract;
        var error = [];

        // only output a stack if it isn't a less error
        if (ctx.stack && !ctx.type) {
            return ctx.stack;
        }

        if (!ctx.hasOwnProperty('index') || !extract) {
            return ctx.stack || ctx.message;
        }

        if (typeof extract[0] === 'string') {
            error.push((ctx.line - 1) + ' ' + extract[0]);
        }

        if (typeof extract[1] === 'string') {
            var errorTxt = ctx.line + ' ';
            if (extract[1]) {
                errorTxt += extract[1].slice(0, ctx.column) + extract[1][ctx.column] + extract[1].slice(ctx.column + 1);
            }
            error.push(errorTxt);
        }

        if (typeof extract[2] === 'string') {
            error.push((ctx.line + 1) + ' ' + extract[2]);
        }
        error = error.join('\n') + '\n';

        var message = ctx.type + 'Error: ' + ctx.message;
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
}(arguments));
