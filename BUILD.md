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
  Currently the package com.google.common  has been renamed in JavaFXLibrary as shaded.com.google.common to avoid version
  mismatches with Google Guava dependencies.  See https://maven.apache.org/plugins/maven-shade-plugin/ for more info.



## Releasing

* update library version to x.x.x in pom.xml
* run tests ``mvn clean verify``
* copy target/robotframework/libdoc/JavaFXLibrary.html under docs directory
* ``git commit -m "version to x.x.x" pom.xml docs/JavaFXLibrary.html``
* create tag ``git tag -a x.x.x``
* push ``git push origin master`` and ``git push origin x.x.x``
* create a new release and upload the jar file, html documentation and xml file to https://github.com/robotframework/JavaFXLibrary/releases
* upload to Maven Repository (TBD)

## Announcements

* Twitter @robotframework
* Robotframework Users and announcements mailing lists
* Slack http://robotframework.slack.com #general and #JavaFXLibrary

