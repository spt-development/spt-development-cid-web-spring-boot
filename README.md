````
  ____  ____ _____   ____                 _                                  _   
 / ___||  _ \_   _| |  _ \  _____   _____| | ___  _ __  _ __ ___   ___ _ __ | |_ 
 \___ \| |_) || |   | | | |/ _ \ \ / / _ \ |/ _ \| '_ \| '_ ` _ \ / _ \ '_ \| __|
  ___) |  __/ | |   | |_| |  __/\ V /  __/ | (_) | |_) | | | | | |  __/ | | | |_ 
 |____/|_|    |_|   |____/ \___| \_/ \___|_|\___/| .__/|_| |_| |_|\___|_| |_|\__|
                                                 |_|                                           
 cid-web-spring-boot-------------------------------------------------------------
````

[![build_status](https://travis-ci.com/spt-development/spt-development-cid-web-spring-boot.svg?branch=main)](https://travis-ci.com/spt-development/spt-development-cid-web-spring-boot)

Library for integrating 
[spt-development/spt-development-cid-web](https://github.com/spt-development/spt-development-cid-web) 
into a Spring Boot application.

Usage
=====

Simply add the Spring Boot starter to your Spring Boot project pom.

    <dependency>
        <groupId>com.spt-development</groupId>
        <artifactId>spt-development-cid-web-spring-boot-starter</artifactId>
        <version>1.0.0</version>
        <scope>runtime</scope>
    </dependency

Building locally
================

To build the library, run the following maven command:

    $ mvn clean install

Release
=======

To build a release and upload to Maven Central run the following maven command:

    $ export GPG_TTY=$(tty) # Required on Mac OS X
    $ mvn deploy -DskipTests -Prelease

NOTE. This is currently a manual step as not currently integrated into the build.
