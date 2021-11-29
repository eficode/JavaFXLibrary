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

package javafxlibrary.exceptions;

@SuppressWarnings("serial")
public class JavaFXLibraryFatalException extends JavaFXLibraryKeywordException {

    /**
     * Avoid adding the exception type as a prefix to failure messages
     */
    public static final boolean ROBOT_SUPPRESS_NAME = true;

    /**
     * This will be a fatal exception that stops the whole test execution gracefully,
     */
    public static final boolean ROBOT_EXIT_ON_FAILURE = true;


    public JavaFXLibraryFatalException() {

        super();
    }

    public JavaFXLibraryFatalException(String string) {

        super(string);
    }

    public JavaFXLibraryFatalException(Throwable t) {

        super(t);
    }

    public JavaFXLibraryFatalException(String string, Throwable t) {

        super(string, t);
    }
}

