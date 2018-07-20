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
import javafxlibrary.exceptions.JavaFXLibraryFatalException;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.keywords.AdditionalKeywords.RunOnFailure;
import org.apache.commons.io.FileUtils;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.remoteserver.RemoteServer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import static javafxlibrary.utils.HelperFunctions.*;
import static javafxlibrary.utils.TestFxAdapter.objectMap;

public class JavaFXLibrary extends AnnotationLibrary {

    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

    public static final String ROBOT_LIBRARY_VERSION = loadRobotLibraryVersion();

    static List<String> includePatterns = new ArrayList<String>() {{
        add("javafxlibrary/keywords/AdditionalKeywords/*.class");
        add("javafxlibrary/keywords/Keywords/*.class");
        add("javafxlibrary/keywords/*.class");
        add("javafxlibrary/tests/*.class");
	}};

    public JavaFXLibrary() {
        super(includePatterns);
        deleteScreenshotsFrom("report-images/imagecomparison");
   }

    @Autowired
    protected RunOnFailure runOnFailure;

    private void useMappedObjects(Object[] arr) {
        for(Object o : arr) {
            if(o.getClass().isArray()) {
                useMappedObjects((Object[]) o);
            } else {
                if (o instanceof String) {
                    if (objectMap.containsKey(o)) {
                        arr[Arrays.asList(arr).indexOf(o)] = objectMap.get(o);
                    }
                }
            }
        }
    }

    // overriding the run method to catch the control in case of failure, so that desired runOnFailureKeyword
    // can be executed in controlled manner.

    @Override
    public Object runKeyword(String keywordName, Object[] args) {

        useMappedObjects(args);

        try {
            return super.runKeyword(keywordName, args);
        } catch (RuntimeException e) {
            runOnFailure.runOnFailure();
            if ( e.getCause() instanceof JavaFXLibraryFatalException ) {
                robotLog("DEBUG", "JavaFXLibrary: Caught JavaFXLibrary FATAL exception");
                throw e;
            } else if ( e.getCause() instanceof JavaFXLibraryNonFatalException ) {
                robotLog("DEBUG", "JavaFXLibrary: Caught JavaFXLibrary NON-FATAL exception");
                throw e;
            } else {
                robotLog("DEBUG", "JavaFXLibrary: Caught JavaFXLibrary RUNTIME exception");
                throw e;
            }
        }
    }

    /*
    *   Just an empty function to get rid of unnecessary errors, currently get_keyword_tags interface is not implemented.
    */
    public String getKeywordTags(String keywordName) {
        return null;
    }


    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__")) {
            try {
                return FileUtils.readFileToString(new File("libdoc-documentation.txt"), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException occured while reading the documentation file!";
            }
        }
        return super.getKeywordDocumentation(keywordName);
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
        RemoteServer.configureLogging();
        System.out.println("-------------------- JavaFXLibrary --------------------- ");
        RemoteServer server = new RemoteServer();
        server.putLibrary("/", new JavaFXLibrary());
        int port = 8270;
        InetAddress ipAddr = InetAddress.getLocalHost();

        try {
            if(args.length > 0) {
                port = Integer.parseInt(args[0]);
            } else
                System.out.println("RemoteServer for JavaFXLibrary will be started at default port of: " + port + ". If you wish to use another port, restart the library and give port number as an argument. ");

            server.setPort(port);
            server.start();
            System.out.println("\n        JavaFXLibrary " + ROBOT_LIBRARY_VERSION + " is now available at: " + ipAddr.getHostAddress() + ":" + port + "\n ");

        } catch (NumberFormatException nfe) {
            System.out.println("\n        Error! Not a valid port number: " + args[0]);
            System.exit(1);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
            System.exit(1);
        } catch (BindException be) {
            // TODO: Configure RemoteServer logging, stackTrace gets logged here with default config
            System.out.println("\n        Error! " + be.getMessage() + ": " + ipAddr.getHostAddress() +":" + port);
            System.exit(1);
        }
    }
}
