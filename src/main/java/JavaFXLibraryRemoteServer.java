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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.robotframework.remoteserver.RemoteServer;
import org.robotframework.remoteserver.logging.Jetty2Log4J;

public class JavaFXLibraryRemoteServer extends RemoteServer {

    public JavaFXLibraryRemoteServer(int port) {
        super(port);
    }

    public static void configureLogging() {
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.FATAL);
        org.eclipse.jetty.util.log.Log.setLog(new Jetty2Log4J());
        LogFactory.releaseAll();
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.Log4JLogger");
        Log log = LogFactory.getLog(RemoteServer.class);
    }
}