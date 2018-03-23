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

package javafxlibrary.keywords.Keywords;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.stage.Screen;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.TestFxAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import static javafxlibrary.utils.HelperFunctions.*;

@RobotKeywords
public class ScreenCapturing extends TestFxAdapter {

    @RobotKeywordOverload
    public Object captureImage(Object locator){
        return captureImage(locator, true);
    }

    @RobotKeyword("Returns a screenshot of the given locator.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, Rectangle, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating or specifying UI elements`. \n\n"
            + "Argument ``logImage`` is a boolean value that specifies whether a captured image is also printed to test execution log. \n\n "
            + "\nExample:\n"
            + "| ${region}= | Create Rectangle | 11 | 22 | 33 | 44 | \n"
            + "| ${capture}= | Capture Image | ${region} | \n"
            + "| ${capture}= | Capture Image | ${node} | \n"
            + "| ${capture}= | Capture Image | ${window} | \n"
            + "| ${capture}= | Capture Image | \\#id | logImage=False |\n" )
    @ArgumentNames({"locator", "logImage=True"})
    public Object captureImage(Object locator, boolean logImage){
        if(locator == null)
            throw new JavaFXLibraryNonFatalException("Unable to capture image, given locator was null!");

        robotLog("INFO", "Capturing screenshot from locator: \"" + locator.toString() +  "\"");
        Image image = null;
        Bounds targetBounds = objectToBounds(locator);

        try {
            image = robot.capture(targetBounds).getImage();
            Path path = createNewImageFileNameWithPath();
            robotContext.getCaptureSupport().saveImage(image, path);

            if(logImage) {
                Double printSize = ( targetBounds.getWidth() > 800 ) ? 800 : targetBounds.getWidth();
                System.out.println("*HTML* <img src=\"" + path + "\" width=\"" + printSize + "px\">");
            }

            return mapObject(image);

        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to take capture : \"" + locator.toString() + "\"", e);
        }
    }

    @RobotKeyword("Loads an image from the given _path_ in hard drive \n\n"
            + "``path`` is the source path for image in local hard drive. \n\n"
            + "\nExample:\n"
            + "| ${image}= | Load Image | ${path to image}node.png |\n")
    @ArgumentNames({"path"})
    public Object loadImage(String path) {
        try {
            robotLog("INFO", "Loading image from: \"" + path + "\"");
            return mapObject(robot.capture(Paths.get(path)).getImage());
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to load image from path: \"" + path + "\"", e);
        }
    }

    @RobotKeyword("Loads an image from the given _url_\n\n"
            + "``url`` is the url for the source image. \n\n"
            + "\nExample:\n"
            + "| ${path}= | Set Variable | http://i.imgur.com | \n"
            + "| ${image}= | Load Image From Url | ${path}/A99VNbK.png |\n")
    @ArgumentNames({"url"})
    public Object loadImageFromUrl(String url) {
        try {
            robotLog("INFO", "Loading image from URL: \"" + url + "\"");
            return mapObject(SwingFXUtils.toFXImage(ImageIO.read(new URL(url)), null));
        } catch(Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to load image from URL: \"" + url + "\"", e);
        }
    }

    @RobotKeyword("Saves given image to given location\n\n"
            + "``image`` is the target _Object:Image_ to be saved\n"
            + "``path`` is the target location where image will be saved")
    @ArgumentNames({ "image", "path" })
    public void saveImageAs(Image image, String path) {
        try {
            robotLog("INFO", "Saving image \"" + image.toString() + "\" to path \"" + path + "\"");
            robotContext.getCaptureSupport().saveImage(image, Paths.get(path));
        } catch (Exception e) {
            if(e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to save image.", e);
        }
    }

    private Path createNewImageFileNameWithPath(){
        ZonedDateTime errorDateTime = ZonedDateTime.now();
        String errorTimestamp = formatErrorTimestamp(errorDateTime, "yyyyMMdd-HHmmss-SSS");
        String errorImageFilename = "JavaFXLib-" + errorTimestamp + ".png";
        String errorImageFilePath = getCurrentSessionScreenshotDirectory();
        File errDir = new File(errorImageFilePath);
        if(!errDir.exists())
            errDir.mkdirs();
        return Paths.get( errorImageFilePath, errorImageFilename);
    }

    private static String formatErrorTimestamp(ZonedDateTime dateTime, String dateTimePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return dateTime.format(formatter);
    }
}