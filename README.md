# MCRaidz
This plugin allows for all the custom features on our MCRaidz server.

# Download
To setup MCRaidz usage with maven, put the following in your pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>net.minebo</groupId>
        <artifactId>mcraidz</artifactId>
        <version>1.0-DEV</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

```

# Compilation
Compilation requires the following to be fulfilled:
* [Java 21](https://www.oracle.com/java/technologies/downloads/?er=221886#java21)
* [Maven 3](http://maven.apache.org/download.html "Maven 3 Link")

# Updates
This plugin is provided "as is", which means no updates or new features are guaranteed. We will do our best to keep updating and pushing new updates, and you are more than welcome to contribute your time as well and make pull requests for bug fixes.

Once these tasks have been taken care of, compilation via `mvn clean install` will result in `target/mcraidz-1.0-DEV.jar` being created.

# License
This software is available under the following licenses:
* GNU General Public License (GPL) version 3
