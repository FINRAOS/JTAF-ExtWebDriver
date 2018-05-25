JTAF-ExtWebDriver [![Build Status](https://secure.travis-ci.org/FINRAOS/JTAF-ExtWebDriver.png?branch=master)](http://travis-ci.org/FINRAOS/JTAF-ExtWebDriver)
==================
[Extensions for WebDriver](http://finraos.github.io/JTAF-ExtWebDriver/) is an enhancement to the powerful WebDriver API, with robust features that keep your browser automation running smoothly. It includes a widget library, improved session management and extended functions over the existing WebDriver API.

Here is the link to [getting started](http://finraos.github.io/JTAF-ExtWebDriver/howitworks.html)

Latest Version
========
The latest version is version 1.5.5 (July 2017) [[maven central]](https://mvnrepository.com/artifact/org.finra.jtaf/jtaf-extwebdriver/1.5.5)

Maven Dependency:

```xml
<dependency>
    <groupId>org.finra.jtaf</groupId>
    <artifactId>jtaf-extwebdriver</artifactId>
    <version>1.5.5</version>
</dependency>
```

Contributing
=============
We encourage contribution from the open source community to help make ExtWebDriver better. Please refer to the [development](http://finraos.github.io/JTAF-ExtWebDriver/contribute.html) page for more information on how to contribute to this project including sign off and the [DCO](https://github.com/FINRAOS/JTAF-ExtWebDriver/blob/master/DCO) agreement.

If you have any questions or discussion topics, please post them on [Google Groups](https://groups.google.com/forum/#!forum/jtaf-extwebdriver).

Building
=========
ExtWebDriver uses Maven for build. Please install Maven by downloading it [here](http://maven.apache.org/download.cgi).
```sh
# Clone ExtWebDriver git repo
git clone git://github.com/FINRAOS/JTAF-ExtWebDriver.git
cd JTAF-ExtWebDriver

# Run package to compile and create jar
mvn package
```

Running Tests
==============
ExtWebDriver uses Maven plugins to execute both the unit and integration tests. The integration tests run against a locally deployed app (using Jetty server) using HtmlUnitDriver. You can run all of the tests by executing:
```sh
mvn verify
```
You can run individual tests by executing it from your IDE or through command line. If you want to run an integration test, you need to deploy the app locally first before execution. You can start the application server by executing:
```sh
mvn jetty:start
```

Requirements
==============
ExtWebDriver requires Java SE version 8 or above available [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

License Type
=============
JTAF projects including ExtWebDriver is licensed under [Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
