# About
[![License BSD 3-Clause](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg)](http://lesscss-compiler.projects.gabrys.biz/license.txt)
[![Build Status](https://travis-ci.org/gabrysbiz/lesscss-compiler.svg?branch=feature%2F2.0)](https://travis-ci.org/gabrysbiz/lesscss-compiler)

The LessCSS Compiler is a Java library which compiles [Less](http://lesscss.org/) source files to [CSS](http://www.w3.org/Style/CSS/) code.

From [Less](http://lesscss.org/) website:
> Less is a CSS pre-processor, meaning that it extends the CSS language, adding features that allow variables,
> mixins, functions and many other techniques that allow you to make CSS that is more maintainable, themable
> and extendable.

# Compatibility
The compiler is compatible with version [1.7.5](https://github.com/less/less.js/releases/tag/v1.7.5).
The library is based on the official [Less](http://lesscss.org/) JavaScript compiler adapted to the
[Rhino](https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino) engine.

It supports sources located at:
* local drives
* protocols: 
  * [HTTP](https://www.w3.org/Protocols/) and HTTPS
  * [FTP](https://www.w3.org/Protocols/rfc959/) (requires [Apache Commons Net](https://commons.apache.org/proper/commons-net/") library in the class path)
  * class path (prefix `classpath://`)
* custom - defined by programmers (see [FileSystem](http://lesscss-compiler.projects.gabrys.biz/LATEST/apidocs/index.html?biz/gabrys/lesscss/compiler2/filesystem/FileSystem.html))

# Requirements
The compiler to run requires:
* Java 6.0 or higher
* Third-Party Dependencies ([see list](http://lesscss-compiler.projects.gabrys.biz/LATEST/dependencies.html))

# Download
You can download the library from [this page](http://lesscss-compiler.projects.gabrys.biz/LATEST/download.html)
or using various [dependency management tools](http://lesscss-compiler.projects.gabrys.biz/LATEST/dependency-info.html).

# Concept
The library contains two compilers:
* [NativeLessCompiler](http://lesscss-compiler.projects.gabrys.biz/LATEST/apidocs/index.html?biz/gabrys/lesscss/compiler2/NativeLessCompiler.html) is a compiler with a shell-type API
* [LessCompiler](http://lesscss-compiler.projects.gabrys.biz/LATEST/apidocs/index.html?biz/gabrys/lesscss/compiler2/LessCompiler.html) is a facade for the `NativeLessCompiler` with a developer-friendly API

The idea for the `NativeLessCompiler` class was based on the [lesscss-java](https://github.com/marceloverdijk/lesscss-java)
library by [Marcel Overdijk](https://github.com/marceloverdijk).

# Usage
The `LessCompiler` contains 18 methods. Below is an example of how to use some of them:
```java
String cssCode = null;
LessOptions options = null;

// create compiler
LessCompiler compiler = new LessCompiler();

// compile source code
cssCode = compiler.compile(".basic { display: block; }");

// compile source code with custom options
options = new LessOptionsBuilder().ieCompatibilityOff().build();
cssCode = compiler.compile(".basic { display: block; }", options);

// compile source file
cssCode = compiler.compile(new File("source.less"));

// compile source file and save CSS code in an output file
compiler.compile(new File("source.less"), new File("output.css"));

// compile source file and compress CSS code
cssCode = compiler.compileAndCompress(new File("source.less"));

// compile source file and compress CSS code using custom encoding
cssCode = compiler.compileAndCompress(new File("source.less"), Charset.forName("UTF-8"));

// compile source code and generate inline source map
cssCode = compiler.compileWithInlineSourceMap(".basic { display: block; }", new LessOptions());

// compile source file and generate source map (save it in output.map file)
options = new LessOptionsBuilder().sourceMapBasePath("basePath").build();
compiler.compileWithSourceMap(new File("source.less"), new File("output.css"), new File("output.map"), options);

// compile source file and generate source map (save it in output.css.map file)
compiler.compileWithSourceMap(new File("source.less"), new File("output.css"), options);
```