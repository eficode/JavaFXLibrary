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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    public static String[] getIndividualQueries(String query) {
        String[] splitQueries = splitOnSpaces(query);
        return combineSplitSelectors(splitQueries);
    }

    public static String[] splitOnSpaces(String query) {
        // Replace spaces of text values with temporary tag to prevent them interfering with parsing of the query
        boolean replaceSpaces = false;

        for (int i = 0; i < query.length(); i++) {
            char current = query.charAt(i);

            if (current == '"')
                replaceSpaces = !replaceSpaces;

            // Query can have escaped quotation marks in it, skip these
            if (current == '\\' && query.charAt(i + 1) == '"')
                query = query.substring(0, i) + "" + query.substring(i + 1);

            if (replaceSpaces && current == ' ')
                query = query.substring(0, i) + ";javafxlibraryfinderspace;" + query.substring(i + 1);
        }
        String[] splitQuery = query.split(" ");

        for (int i = 0; i < splitQuery.length; i++)
            splitQuery[i] = splitQuery[i].replace(";javafxlibraryfinderspace;", " ");

        return splitQuery;
    }

    public static String[] combineSplitSelectors(String[] splitQueries) {
        List<String> confirmedQueries = new ArrayList<>();

        int lastSplit = 0;
        for (int i = 0; i < splitQueries.length; i++) {
            if (startsWithPrefix(splitQueries[i]) && i != 0) {
                String singleQuery = String.join(" ", Arrays.copyOfRange(splitQueries, lastSplit, i));
                confirmedQueries.add(singleQuery);
                lastSplit = i;
            }
        }
        // Add the last part of the query
        confirmedQueries.add(String.join(" ", Arrays.copyOfRange(splitQueries, lastSplit, splitQueries.length)));
        String[] result = new String[confirmedQueries.size()];
        return confirmedQueries.toArray(result);
    }

    protected static boolean startsWithPrefix(String query) {
        String[] prefixes = new String[]{"id=", "css=", "class=", "text=", "xpath=", "pseudo="};

        for (String prefix : prefixes)
            if (query.startsWith(prefix))
                return true;
        return false;
    }

    protected static FindPrefix getPrefix(String query) {

        try {
            String prefix = query.substring(0, query.indexOf('='));

            switch (prefix) {
                case "id":
                    return FindPrefix.ID;
                case "css":
                    return FindPrefix.CSS;
                case "class":
                    return FindPrefix.CLASS;
                case "text":
                    return FindPrefix.TEXT;
                case "xpath":
                    return FindPrefix.XPATH;
                case "pseudo":
                    return FindPrefix.PSEUDO;
                default:
                    throw new IllegalArgumentException("Query \"" + query + "\" does not contain any supported prefix");
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Query \"" + query + "\" does not contain any supported prefix");
        }
    }

    protected static String removePrefix(String query, FindPrefix prefix) {
        switch (prefix) {
            case ID:
                return "#" + query.substring(3);
            case CSS:
                return query.substring(4);
            case CLASS:
            case XPATH:
                return query.substring(6);
            case TEXT:
                if (!query.matches("text=[\"|'].*[\"|']"))
                    throw new IllegalArgumentException("\"text\" query prefix is missing quotation marks.");
                return query.substring(6, query.length() - 1);
            case PSEUDO:
                return query.substring(7);
        }
        throw new IllegalArgumentException("FindPrefix value " + prefix + " of query " + query + " is not supported");
    }

    protected static boolean containsIndex(String query) {
        Pattern pattern = Pattern.compile(".*\\[\\d*]$");
        Matcher matcher = pattern.matcher(query);
        return matcher.matches();
    }

    protected static int getQueryIndex(String query) {
        return Integer.parseInt(query.substring(query.lastIndexOf('[') + 1, query.length() - 1)) - 1;
    }

    protected static String removeQueryIndex(String query) {
        return query.substring(0, query.lastIndexOf('['));
    }
}
