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

package javafxlibrary.utils.finder;

import javafxlibrary.exceptions.JavaFXLibraryFatalException;

public class Query {

    private FindPrefix prefix;
    private String query;
    private int index = -1;

    public Query(String query) {
        this.prefix = QueryParser.getPrefix(query);

        if (this.prefix != FindPrefix.XPATH && QueryParser.containsIndex(query)) {
            this.index = QueryParser.getQueryIndex(query);
            if (this.index < 0) {
                throw new JavaFXLibraryFatalException("Invalid query \"" + query + "\": Minimum index value is 1!");
            }
            query = QueryParser.removeQueryIndex(query);
        }

        this.query = QueryParser.removePrefix(query, this.prefix);
    }

    public FindPrefix getPrefix() {
        return this.prefix;
    }

    public String getQuery() {
        return this.query;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean containsIndex() {
        return this.index != -1;
    }
}
