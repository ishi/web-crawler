## Webcrawler
It's a simple implementation of a web crawler that takes single input address and 
analyze all subpages searching for links.
At the end program shows summary on the console in a form of sitemap.

Input
 * starting url

Output (XML similar to sitemap)
 * internal URLs
 * external URLs
 * static content links
 
Restrictions
 * Do not follow external URLs

Assumptions
 * Follow subdomains
 
## Requirements
 * jdk 11 with JAVA_HOME env variable set

## Running
Projects uses gradle build system to manage dependencies and build lifecycle.
Project contains wrapper scripts included in the repository
so no additional software is required on the system.
After downloading source code one has to run following command in order to run application

```sh
./gradlew run --args="http://example.url.com/"
```
In order to get distribution package one can create dist package which will contain all 
the required dependencies and run scripts. Gradle automatically creates packages for two
major systems linux and windows, creating tar and zip packages respectively.
```sh
./gradlew clean assembleDist
```
Created packages are available in build/distributions directory


Tests can be run with command
```sh
./gradlew clean test
```

## Reasoning
I decided to use pure java introducing as less external libraries as possible. The size 
of the application is small so none of advanced frameworks with DI is required.
Structuring the code I followed clean architecture principles that is described on page 
https://github.com/mattia-battiston/clean-architecture-example. The main idea here is to 
separate the core of the application from external world. This way we end up with reversed 
dependency direction.
All the code was created using TDD approach.
 

## Future improvements
It's simple implementation hence there is a lot of room for improvement. The most important
in my opinion are:
* Error handling - although application should be quite resistant to errors related with 
    downloading and parsing data, it would be useful to introduce a better way of passing 
    information about exceptions between component.
* Concurrency - current implementation process pages sequentially which is not
    very efficient. It simplifies things much but is not a good solution when a tree of pages
    is large.
* Streaming content - in order to improve memory usage it would be good to use more streaming approach
    starting from dowloading content, parsing document, formating and ending with saving document
* Async calls - in such application is a lot of I/O call which are blocking by definition. Probably
    it can be beneficial to use non blocking APIs to download data, it can increase throughput.
* Command line arguments parsing - introduce more advanced/clever command line parsers e.g commons-cli
* Additional output destinations - e.g. file