package javafxlibrary.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String [] splitQuery = query.split(" ");

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
}
