/*
 * Copyright 2017-2018   Eficode Oy
 * Copyright 2018-       Robot Framework Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import javafxlibrary.exceptions.JavaFXLibraryFatalException;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.exceptions.JavaFXLibraryTimeoutException;
import javafxlibrary.keywords.AdditionalKeywords.RunOnFailure;
import javafxlibrary.utils.HelperFunctions;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import javafxlibrary.utils.TestListener;
import org.apache.commons.io.FileUtils;
import org.python.google.common.base.Throwables;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.remoteserver.RemoteServer;
import org.testfx.util.WaitForAsyncUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import static javafxlibrary.utils.HelperFunctions.*;

public class JavaFXLibrary extends AnnotationLibrary {

    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
    public static final String ROBOT_LIBRARY_VERSION = loadRobotLibraryVersion();
    public static final TestListener ROBOT_LIBRARY_LISTENER = new TestListener();

    static List<String> includePatterns = new ArrayList<String>() {{
        add("javafxlibrary/keywords/AdditionalKeywords/*.class");
        add("javafxlibrary/keywords/Keywords/*.class");
        add("javafxlibrary/keywords/*.class");
        add("javafxlibrary/tests/*.class");
	}};

    public JavaFXLibrary() {
    	this(false);
    }
    
    public JavaFXLibrary(boolean headless) {
        super(includePatterns);
        deleteScreenshotsFrom("report-images/imagecomparison");
        if (headless) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            TestFxAdapter.isHeadless = true;
        } else {
	        //v4.0.15-alpha sets default robot as glass, which breaks rolling
	        //Forcing usage of awt robot as previous versions
	        System.setProperty("testfx.robot", "awt");
        }
    }

    @Autowired
    protected RunOnFailure runOnFailure;

    // overriding the run method to catch the control in case of failure, so that desired runOnFailureKeyword
    // can be executed in controlled manner.
    @Override
    public Object runKeyword(String keywordName, Object[] args) {

        Object[] finalArgs;
        // JavalibCore changes arguments of Call Method keywords to Strings after this check, so they need to handle their own objectMapping
        if (!(keywordName.equals("callObjectMethod") || keywordName.equals("callObjectMethodInFxApplicationThread")))
            finalArgs = HelperFunctions.useMappedObjects(args);
        else
            finalArgs = args;

        AtomicReference<Object> retval = new AtomicReference<>();
        AtomicReference<RuntimeException> retExcep = new AtomicReference<>();

        try {
            RobotLog.ignoreDuplicates();
            // timeout + 500 ms so that underlying timeout has a chance to expire first
            WaitForAsyncUtils.waitFor(getWaitUntilTimeout(TimeUnit.MILLISECONDS) + 500, TimeUnit.MILLISECONDS, () -> {

                try {
                    retval.set(super.runKeyword(keywordName, finalArgs));
                    return true;

                } catch (JavaFXLibraryTimeoutException jfxte){
                    // timeout already expired, catch exception and jump out
                    retExcep.set(jfxte);
                    throw jfxte;

                } catch (RuntimeException e){
                    // catch exception and continue trying
                    retExcep.set(e);
                    return false;
                }
            });
        } catch (TimeoutException te) {
            RobotLog.reset();
            RuntimeException e = retExcep.get();
            runOnFailure.runOnFailure();

            if (e.getCause() instanceof JavaFXLibraryFatalException) {
                RobotLog.trace("JavaFXLibrary: Caught JavaFXLibrary FATAL exception: \n" + Throwables.getStackTraceAsString(e));
                throw e;
            } else if (e.getCause() instanceof JavaFXLibraryNonFatalException) {
                RobotLog.trace("JavaFXLibrary: Caught JavaFXLibrary NON-FATAL exception: \n" + Throwables.getStackTraceAsString(e));
                throw e;
            } else {
                RobotLog.trace("JavaFXLibrary: Caught JavaFXLibrary RUNTIME exception: \n" + Throwables.getStackTraceAsString(e));
                throw e;
            }
        } catch (JavaFXLibraryTimeoutException jfxte) {
            RobotLog.reset();
            RobotLog.trace("JavaFXLibrary: Caught JavaFXLibrary TIMEOUT exception: \n" + Throwables.getStackTraceAsString(jfxte));
            throw jfxte;
        }
        RobotLog.reset();
        return retval.get();
    }

    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__")) {
            try {
                return FileUtils.readFileToString(new File("./src/main/java/libdoc-documentation.txt"), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException occured while reading the documentation file!";
            }
        } else if (keywordName.equals("__init__")) {
        	try {
                return FileUtils.readFileToString(new File("./src/main/java/libdoc-init-documentation.txt"), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException occured while reading the init documentation file!";
            }
        } else {
        	try {
                return super.getKeywordDocumentation(keywordName);
            }
            catch (Exception e) {
                return keywordName;
            }
        }
    }

    /**
     * Returns the currently active JavaFXLibrary instance.
     *
     * @return the library instance
     * @throws ScriptException - if error occurs in script
     */
    public static JavaFXLibrary getLibraryInstance() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");
        engine.put("library", "JavaFXLibrary");
        engine.eval("from robot.libraries.BuiltIn import BuiltIn");
        engine.eval("instance = BuiltIn().get_library_instance(library)");
        return (JavaFXLibrary) engine.get("instance");
    }

    public static void main(String[] args) throws Exception {
        JavaFXLibraryRemoteServer.configureLogging();
        System.out.println("-------------------- JavaFXLibrary --------------------- ");
        RemoteServer server = new JavaFXLibraryRemoteServer();
        server.putLibrary("/RPC2", new JavaFXLibrary());
        int port = 8270;
        InetAddress ipAddr = InetAddress.getLocalHost();

        try {
            if (args.length > 0)
                port = Integer.parseInt(args[0]);
            else
                System.out.println("RemoteServer for JavaFXLibrary will be started at default port of: " + port +
                        ". If you wish to use another port, restart the library and give port number as an argument.");

            server.setPort(port);
            server.start();
            System.out.println("\n        JavaFXLibrary " + ROBOT_LIBRARY_VERSION + " is now available at: " +
                    ipAddr.getHostAddress() + ":" + port + "\n");

        } catch (NumberFormatException nfe) {
            System.out.println("\n        Error! Not a valid port number: " + args[0] + "\n");
            System.exit(1);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(1);
        } catch (BindException be) {
            System.out.println("\n        Error! " + be.getMessage() + ": " + ipAddr.getHostAddress() + ":" + port + "\n");
            System.exit(1);
        }
    }
}
