## Sources

Code is organized according to the Maven convention in `src/main/java` and `src/test/java`.


## Building

JavaFXLibrary uses Apache Maven as a build tool.

* `mvn clean package` packages JavaFXLibrary with dependencies. There is also documentation and xml file created.
* `mvn clean verify` run acceptance tests

## Apache Maven Shade Plugin

* Since JavaFXLibrary runs in the same JVM as the AUT(Application Under Test), it's possible that same dependency libraries
  are being used by both JavaFXLibrary and the AUT. It's not uncommon that a specific version is needed for AUT and another
  version of the same dependency is required for JavaFXLibrary(e.g. TestFX dependency for Google Guava). Not always are these
  dependencies backwards compatible and therefore JavaFXLibrary has adopted Apache Maven Shade Plugin to cope with this issue.
  Currently the package com.google.common  has been renamed in JavaFXLibrary as shaded.com.google.common and org.apache.commons as shaded.org.apache.commons to avoid version
  mismatches with dependencies in AUT and internally.  See https://maven.apache.org/plugins/maven-shade-plugin/ for more info.


## Releasing

* update library version to x.x.x in pom.xml
* run tests ``mvn clean verify``
* copy target/robotframework/libdoc/javafxlibrary.html under docs directory (check that README.md links to correct file)
* ``git commit -m "version to x.x.x" pom.xml docs/javafxlibrary.html``
* create tag ``git tag -a x.x.x``
* push ``git push origin master`` and ``git push origin x.x.x``
* create a new release and upload the jar file, html documentation and xml file to https://github.com/eficode/JavaFXLibrary/releases 
* upload to Maven Repository (uses release profile)
  * Create pgp key if not already: https://central.sonatype.org/pages/working-with-pgp-signatures.html
  * In your .m2/settings.xml
  ````
  <settings>
   <servers>
      <server>
          <id>ossrh</id>
          <username>sonatype username</username>
          <password>sonatype password</password>
      </server>
      <server>
        <id>key id from gpg --list-keys</id>
        <passphrase>passphrase for the key</passphrase>
      </server>
   </servers>
   <profiles>
      <profile>
        <activation>
          <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
          <gpg.keyname>key id from gpg --list-keys</gpg.keyname>
        </properties>
      </profile>
    </profiles>
  </settings>
  ````
  * Release snapshot or actual release (depending what is in version tag in pom.xml)``mvn clean deploy -P release``
  * In case of release log in to https://oss.sonatype.org:
    * from left choose `Staging repositories` and scroll down, choose `robotframework-*` repository and review
    * choose from top toolbar `Release`, add to reason field `x.y.z release` and submit
    * sync takes typically hours before visible in Maven Central
  * snapshots can be found from https://oss.sonatype.org/content/repositories/snapshots/org/robotframework/javafxlibrary/
  * actual releases can be found from https://search.maven.org/ and typing `javafxlibrary` in the search field. 

## Announcements

* Twitter @robotframework
* Robotframework Users and announcements mailing lists
* Slack http://robotframework.slack.com #general and #JavaFXLibrary

