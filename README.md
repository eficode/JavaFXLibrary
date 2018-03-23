#JavaFXLibrary

[Robot Framework](http://robotframework.org) library for testing and connecting to a JavaFX java process and using [TestFX](https://github.com/TestFX/TestFX).

This library allows you to use robot/pybot (Python version of Robot Framework) to run test cases over remote library interface although it also works if you are running with jybot (Jython version of Robot Framework). This means that you can use your other pure Python libraries in your test cases that will not work when runnin with Jython.

JavaFXLibrary is tested to work with Robot Framework 3.0.2 or later.

You can connect to applications running on your local machine or even on a different machine.


##Installation

1. Download latest JavaFXLibrary and documentation from https://github.com/robotframework/JavaFXLibrary/releases/
2. Copy(if needed) JAR to desired location and run from command line using
    ```
    java -jar JavaFXLibrary-<version>.jar

    ```
3. JavaFXLibrary in RemoteServer mode should now be running in port [8270](http://localhost:8270)
4. Optionally JAR can be launched with port number as an optional argument:
    ```
    java -jar JavaFXLibrary-<version>.jar 1234
    ```
5. JavaFXLibrary in RemoteServer mode should now be running in port [1234](http://localhost:1234)

## Usage in Robot suite settings

Import the library:
```
***Settings***
Library    Remote    http://localhost:8270/    WITH NAME    JavaFXLibrary
```
Now the keywords can be used as usual:
```
Launch Javafx Application    javafxlibrary.testapps.TestClickRobot
```

In case of duplicate keywords(multiple keywords found with same name) use e.g. `JavaFXLibrary.'Keyword Name'` to get rid of warnings.

## Using multiple remote libraries

If you need to use the Remote library multiple times in a test suite, or just want to give it a more descriptive name, you can import it using the WITH NAME syntax.
```
***Settings***
Library    Remote    http://localhost:8270/    WITH NAME    client1
Library    Remote    http://localhost:8272/    WITH NAME    client2
```

Now the keywords can be used as `client1.List Windows` and `client2.List Windows`


## JavaFXLibrary demo

This can be also used as JavaFXLibrary demo.

Generic way with Maven (in repository root):
```
mvn verify
```

Windows command line:
```
java -cp "target\JavaFXLibrary-<version>.jar"  org.robotframework.RobotFramework --include smoke src\test\robotframework/
```

Linux/OSX command line:
```
java -cp "target/JavaFXLibrary-<version>.jar"  org.robotframework.RobotFramework --include smoke src/test/robotframework/

```

## Known issues

* Tests can be executed with robot/pybot/jybot! Jython can be used locally without remote interface.
* Running tests multiple times requires starting the server again, or applications start launching behind other windows, causing tests to fail when robot is trying to interact with them.
* At the moment there are problems with Java Objects, they might be returned as Java Strings or their methods may be unavailable.
