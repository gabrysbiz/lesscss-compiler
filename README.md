# About
[![License BSD 3-Clause](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg)](http://lesscss-compiler.projects.gabrys.biz/license.txt)
[![Build Status](https://travis-ci.org/gabrysbiz/lesscss-compiler.svg?branch=release%2F1.2)](https://travis-ci.org/gabrysbiz/lesscss-compiler)

The LessCSS Compiler is a Java library which compiles [Less](http://lesscss.org/) source files to the [CSS](http://www.w3.org/Style/CSS/) code.

From [Less](http://lesscss.org/) website:
> Less is a CSS pre-processor, meaning that it extends the CSS language, adding features that allow variables,
> mixins, functions and many other techniques that allow you to make CSS that is more maintainable, themable
> and extendable.

# Compatibility
The compiler is compatible with version	[1.7.5](https://github.com/less/less.js/releases/tag/v1.7.5).
The library is based on the official [Less](http://lesscss.org/) JavaScript compiler adapted to the
[Rhino](https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino) engine.

Restrictions:
* cannot fetch sources from Internet, e.g. `@import (less) "http://example.org/style.less"`

# Requirements
The compiler to run requires:
* Java 5.0 or higher
* Third-Party Dependencies ([see list](http://lesscss-compiler.projects.gabrys.biz/1.2/dependencies.html))

# Download
You can download the library from [this page](http://lesscss-compiler.projects.gabrys.biz/1.2/download.html)
or using various [dependency management tools](http://lesscss-compiler.projects.gabrys.biz/1.2/dependency-info.html).

# Concept
The idea for the `LessCompilerImpl` class was based on the [lesscss-java](https://github.com/marceloverdijk/lesscss-java)
library by [Marcel Overdijk](https://github.com/marceloverdijk).

# Usage
How to compile a source file:
```
// create compiler & source file
LessCompiler compiler = new LessCompilerImpl();
File source = new File("/less/file.less");

// compile file with default options
String cssCode = compiler.compile(source); 

// set custom option: minify CSS code
CompilerOptions options = new CompilerOptionsBuilder().setMinified(true).create();
String cssMinifiedCode = compiler.compile(source, options);
```