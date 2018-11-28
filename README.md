# JavaFXLibrary

[TestFX](https://github.com/TestFX/TestFX) based [Robot Framework](http://robotframework.org) library for testing JavaFX Applications.

JavaFXLibrary works with both Jython (local and remote use) and Python (remote only) versions of Robot Framework. This means JavaFXLibrary can be used with Jython incompatible test libraries too by importing it as a remote library.

JavaFXLibrary is tested to work with Robot Framework 3.0.2 or later.

## Keyword documentation
See keyword [documentation](https://eficode.github.io/JavaFXLibrary/javafxlibrary.html).

## Taking the library into use
### As a local library
1. Download JavaFXLibrary jar file from [releases](https://github.com/eficode/JavaFXLibrary/releases/) or [Maven Central](https://search.maven.org/artifact/org.robotframework/javafxlibrary).
2. Import JavaFXLibrary in test settings:
```
*** Settings ***
Library    JavaFXLibrary
```
3. Add library jar to Jython [module search path](http://robotframework.org/robotframework/3.0b1/RobotFrameworkUserGuide.html#configuring-where-to-search-libraries-and-other-extensions) and run your tests:
```
jython -J-cp javafxlibrary-<version>.jar -m robot.run tests.robot
```

### As a remote library
1. Download JavaFXLibrary jar file from [releases](https://github.com/eficode/JavaFXLibrary/releases/) or [Maven Central](https://search.maven.org/artifact/org.robotframework/javafxlibrary).
2. Start JavaFXLibrary as a remote library: `java -jar javafxlibrary-<version>.jar`
  - Remote library starts in port [8270](http://localhost:8270) by default.
  - Port number can also be defined in the start command: `java -jar javafxlibrary-<version>.jar 1234`
3. Import JavaFXLibrary in test settings:
```
*** Settings ***
Library    Remote    http://127.0.0.1:8270    WITH NAME    JavaFXLibrary
```
4. Run your tests: `robot tests.robot`

## Using JavaFXLibrary on macOS Mojave
MacOS Mojave introduced changes to security settings and disabled some of the features JavaFXLibrary uses by default.
To use JavaFXLibrary on macOS Mojave you must enable the accessibility features for terminal in system preferences:
- Navigate to `Apple menu > System Preferences > Security & Privacy > Privacy > Accessibility`
- Click the lock and enter your password to change these settings
- If terminal has requested accessibility features before it should show in the list
- If not, add it by clicking :heavy_plus_sign: and selecting `Applications > Utilities > Terminal`
- Enable accessibility features by checking the box: :white_check_mark: Terminal

## Identifying JavaFX UI objects
[Scenic View](http://fxexperience.com/scenic-view/) is a tool that allows you to inspect the JavaFX application scenegraph. This can be useful especially when you do not have access to the source code.

See [keyword documentation](https://eficode.github.io/JavaFXLibrary/javafxlibrary.html#3.%20Locating%20JavaFX%20Nodes) for detailed information about handling UI elements with the library.

## JavaFXLibrary demo

Library's acceptance test suite can be used as a JavaFXLibrary demo. Running the test suite requires [Maven](https://maven.apache.org) installation.

### Running the demo locally
- Clone the repository: `git clone https://github.com/eficode/JavaFXLibrary.git`
- Run acceptance tests (in repository root): `mvn verify`

### Running the demo using Docker
#### Requirements:
* Docker CE: https://docs.docker.com/install/
* Docker-compose: https://docs.docker.com/compose/install/
* Port 80 is free in your machine

#### Running the tests
1. Build & start the Dockerized environment: `docker-compose up -d robot-framework javafxcompile`
2. Open browser to <docker_daemon_ip>
3. Open xterm from Start menu > System tools > xterm
4. Execute tests: `test.sh`

Executing _test.sh_ runs the acceptance suite twice: first using JavaFXLibrary as a local Robot Framework library on Jython, and after that using the library in remote mode executing the same tests on python version of Robot Framework.

If you want the suite to run only once, you can define which type of library to use by including **local** or **remote** as an argument. For example command `test.sh remote` will execute the suite only in remote mode.