## New and noteworthy

Here are the new features introduced in this Gradle release.

### Incremental java compilation

Gradle 2.1 brings incubating support for compiling java code incrementally.
When this feature is enabled, only classes that are considered stale are recompiled.
This way not only the compiler does less work, but also fewer output class files are touched.
The latter feature is extremely important for scenarios involving JRebel - the less output files are touched the quicker the jvm gets refreshed classes.

We will improve the speed and capability of the incremental java compiler. Please give use feedback how does it work for your scenarios.
For more detailss please see the user guide section on “[Incremental compilation](userguide/java_plugin.html#sec:incremental_compile)”.
To enable the feature, configure the [JavaCompile](dsl/org.gradle.api.tasks.compile.JavaCompile.html) task accordingly:

    //configuring a single task:
    compileJava.options.incremental = true

    //configuring all tasks from root project:
    subprojects {
        tasks.withType(JavaCompile) {
            options.incremental = true
        }
    }

We are very excited about the progress on the incremental java compilation.
Class dependency analysis of compiled classes is really useful for many other scenarios that Gradle will handle in future:

* detection of unused jars/classes
* detection of duplicate classes on classpath
* detection of tests to execute
* and more

### Child processes started by Gradle are better described

At the [Gradle Summit 2014 Conference](http://www.gradlesummit.com/conference/santa_clara/2014/06/home)
we ran a [Contributing To Gradle Workshop](http://www.gradlesummit.com/conference/santa_clara/2014/06/session?id=31169).
During the session, [Rob Spieldenner](https://github.com/rspieldenner)
contributed a very nice feature that gives much better insight into the child processes started by Gradle.
The example output of `jps -m` command now also contains the function of the worker process:

    28649 GradleWorkerMain 'Gradle Test Executor 17'
    28630 GradleWorkerMain 'Gradle Compiler Daemon 1'

### Groovy Compiler Configuration Script Support (i)

It is now possible to perform advanced Groovy compilation configuration by way of the new 
[`GroovyCompileOptions.configurationScript`](dsl/org.gradle.api.tasks.compile.GroovyCompileOptions.html#org.gradle.api.tasks.compile.GroovyCompileOptions:configurationScript) 
property 
(the `GroovyCompileOptions` instance is available as the 
[`groovyOptions` property of the `GroovyCompile` task](dsl/org.gradle.api.tasks.compile.GroovyCompile.html#org.gradle.api.tasks.compile.GroovyCompile:groovyOptions)). 
This makes it possible to impose global compiler transformations and other configuration.

For example, to globally enable Groovy's strict type checking, a compiler config script can be created with…

    import groovy.transform.TypeChecked
    
    withConfig(configuration) {
        ast(TypeChecked)
    }
    
And specified in the build script as…

    compileGroovy {
      groovyOptions.configurationScript = file("myConfigScript.groovy")
    }

Where `file("myConfigScript.groovy")` contains the Groovy code from above.
 
This feature was contributed by [Cédric Champeau](https://github.com/melix).
 
### Java Gradle Plugin plugin

This is a plugin to assist in developing gradle plugins.  It validates the plugin structure during the jar task and emits warnings
if the plugin metadata is not valid.

    apply plugin: 'java-gradle-plugin'

### PMD Console Output (i)

It is now possible to have [PMD static analysis](userguide/pmd_plugin.html) output results directly to the console.

    pmd {
      consoleOutput = true
    }

Output will be written to `System.out` in addition to any configured reports.

This feature was contributed by [Vyacheslav Blinov](https://github.com/dant3).

### Dependency exclusions are included in POM file by `maven-publish` plugin (i)

The incubating [maven-publish](userguide/publishing_maven.html) plugin will now handle dependency excludes when generating a POM file for publishing.

So for a dependency declaration like:

    dependencies {
        compile("my.org:my-module:1.2") {
            exclude group: 'commons-logging', module: 'commons-logging'
            exclude group: 'commons-collections'
        }
    }

The generated POM file will contain the following content:

    <dependency>
        <groupId>my.org</groupId>
        <artifactId>my-module</artifactId>
        <version>1.2</version>
        <scope>runtime</scope>
        <exclusions>
            <exclusion>
                <groupId>commons-logging</groupId>
                <artifactId>commons-logging</artifactId>
            </exclusion>
            <exclusion>
                <groupId>commons-collections</groupId>
                <artifactId>*</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

This feature addresses [GRADLE-2945] was contributed by [Biswa Dahal](https://github.com/ffos).

## Promoted features

Promoted features are features that were incubating in previous versions of Gradle but are now supported and subject to backwards compatibility.
See the User guide section on the “[Feature Lifecycle](userguide/feature_lifecycle.html)” for more information.

The following are the features that have been promoted in this Gradle release.

<!--
### Example promoted
-->

## Fixed issues

## Deprecations

Features that have become superseded or irrelevant due to the natural evolution of Gradle become *deprecated*, and scheduled to be removed
in the next major Gradle version (Gradle 3.0). See the User guide section on the “[Feature Lifecycle](userguide/feature_lifecycle.html)” for more information.

The following are the newly deprecated items in this Gradle release. If you have concerns about a deprecation, please raise it via the [Gradle Forums](http://forums.gradle.org).

<!--
### Example deprecation
-->

## Potential breaking changes

### Changed Java compiler integration for joint Java - Scala compilation

The `ScalaCompile` task type now uses the same Java compiler integration as the `JavaCompile` and `GroovyCompile` task types for performing joint Java - Scala
compilation. Previously it would use the old Ant-based Java compiler integration, which is no longer supported in the Gradle 2.x stream.

This change should be backwards compatible for all users, and should improve compilation time when compiling Java and Scala together.

### Incubating native language plugins no longer apply the base plugin

The native language plugins now apply the [`LifecycleBasePlugin`](dsl/org.gradle.language.base.plugins.LifecycleBasePlugin) instead of the `BasePlugin`. This means
that the default values defined by the `BasePlugin` are not available.

TBD - make this more explicit re. what is actually not longer available.

### Changes to incubating Java language plugins

To better support the production of multiple binary outputs for a single set of sources, a new set of Java
language plugins was been introduced in Gradle 1.x. This development continues in this release, with the removal of the
`jvm-lang` plugin, and the replacement of the `java-lang` plugin with a completely new implementation.

The existing `java` plugin is unchanged: only users who explicitly applied the `jvm-lang` or `java-lang` plugins
will be affected by this change.

### Generated maven pom contains dependency exclusions

The `maven-publish` plugin will now correctly add required 'exclusion' elements to the generated POM. If you have a build or plugin that
applies these exclusions itself, the generated POM file may contain duplicate 'exclusion' elements.

### Internal methods removed

- The internal method `Javadoc.setJavadocExecHandleBuilder()` has been removed. You should use `setToolChain()` instead.

### Changes to JUnit class loading

Previously, Gradle initialized test classes before trying to execute any individual test case.
As of Gradle 2.1, classes are not initialized until the execution of the first test case (GRADLE-3114).
This change was made for compatibility with the popular Android unit testing library, [Robolectric](http://robolectric.org).

This change impacts how classes that fail to initialize are reported.
Previously a single failure would be reported with a test case name of `initializerError` with the details of the failure.
After this change, the first test case of the class that cannot be initialized will contain details of the failure, 
while subsequent test cases of the class will fail with a `NoClassDefFoundError`.

This change will not cause tests that previously passed to start failing.
 
## External contributions

We would like to thank the following community members for making contributions to this release of Gradle.

* [Rob Spieldenner](https://github.com/rspieldenner) - Made the worker processes better described in the process list.
* [Vyacheslav Blinov](https://github.com/dant3) - PMD console output.
* [Thibault Kruse](https://github.com/tkruse) - Documentation improvements.
* [Biswa Dahal](https://github.com/ffos) - Dependency exclude support for `maven-publish`.
* [Marcin Zajączkowski](https://github.com/szpak) - Improvements to groovy-library build template.
* [Chris Earle](https://github.com/pickypg) - Improvements to the distribution plugin.
* [Curtis Mahieu](https://github.com/curtpm) - JUnit eager class initialization fix (GRADLE-3114)
* [Cédric Champeau](https://github.com/melix) - Support for Groovy compiler configuration scripts.
* [Martin](https://github.com/effrafax) - improvements to EAR plugin.

We love getting contributions from the Gradle community. For information on contributing, please see [gradle.org/contribute](http://gradle.org/contribute).

## Known issues

Known issues are problems that were discovered post release that are directly related to changes made in this release.
