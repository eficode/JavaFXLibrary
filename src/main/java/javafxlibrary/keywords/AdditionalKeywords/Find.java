package javafxlibrary.keywords.AdditionalKeywords;

import javafx.scene.Parent;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.Finder;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import static javafxlibrary.utils.HelperFunctions.mapObject;
import static javafxlibrary.utils.HelperFunctions.robotLog;

@RobotKeywords
public class Find {

    // TODO: Fix documentation
    @RobotKeyword("Returns the *first* node matching the query. \n\n"
            + "``query`` is a query locator, see `3.1 Using queries`.\n\n"
            + "``failIfNotFound`` specifies if keyword should fail if nothing is found. By default it's false and "
            + "keyword returns null in case lookup returns nothing.\n\n"
            + "\nExample:\n"
            + "| ${my node}= | Find | some text | | # for finding something based on plain _text_ |\n"
            + "| ${my node}= | Find | .css | | # for finding something based on _css_ class name |\n"
            + "| ${my node}= | Find | \\#id | | # for finding something based on node _id_ |\n"
            + "| ${my node}= | Find | \\#id | failIfNotFound=True | # this search fails if nothing is found |\n\n"
            + "Or, chaining multiple queries together using _id_ and _css_:\n"
            + "| ${my node}= | Find | \\#id .css-first .css-second | # using _id_ and _css_ class name | \n"
            + "Above example would first try to find a node fulfilling a query using _id_, then continue search under previously found node using css class query \n"
            + " _.css-first_, and then continue from there trying to locate css class _css-second_. \n\n")
    @ArgumentNames({ "query", "failIfNotFound=False", "root=" })
    public Object find(String query, boolean failIfNotFound, Parent root) {
        robotLog("INFO", "Trying to find the first node matching the query: \"" + query
                + "\", failIfNotFound= \"" + Boolean.toString(failIfNotFound) + "\", root= \"" + root + "\"");
        try {
            return mapObject(new Finder().find(query, root));

        } catch (JavaFXLibraryNonFatalException e){
            if (failIfNotFound)
                throw new JavaFXLibraryNonFatalException("Unable to find anything with query: \"" + query + "\"");
            return "";

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find operation failed for query: \"" + query + "\"", e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "query", "failIfNotFound=False" })
    public Object find(String query, boolean failIfNotFound) {
        robotLog("INFO", "Trying to find the first node matching the query: \"" + query
                + "\", failIfNotFound= \"" + Boolean.toString(failIfNotFound) + "\"");
        try {
            return mapObject(new Finder().find(query));

        } catch (JavaFXLibraryNonFatalException e){
            if (failIfNotFound)
                throw new JavaFXLibraryNonFatalException("Unable to find anything with query: \"" + query + "\"");
            return "";

        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find operation failed for query: \"" + query + "\"", e);
        }
    }

    @RobotKeywordOverload
    @ArgumentNames({ "query"})
    public Object find(String query) {
        robotLog("INFO", "Trying to find the first node matching the query: \"" + query
                + "\"");
        try {
            return mapObject(new Finder().find(query));
        } catch (JavaFXLibraryNonFatalException e){
            return "";
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Find operation failed for query: \"" + query + "\"", e);
        }
    }
}
