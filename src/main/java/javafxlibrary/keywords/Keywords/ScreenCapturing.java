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
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.keywords.AdditionalKeywords.ConvenienceKeywords;
import javafxlibrary.utils.RobotLog;
import javafxlibrary.utils.TestFxAdapter;
import org.apache.commons.io.FileUtils;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import static javafxlibrary.utils.HelperFunctions.*;
import static org.testfx.util.WaitForAsyncUtils.asyncFx;

@RobotKeywords
public class ScreenCapturing extends TestFxAdapter {

    @RobotKeyword("Sets whether to embed log images directly into the log.html file or as a link to a file on local disk.\n\n"
            + "Argument ``value`` is a string. Accepted values are ``embedded`` (initial value) and ``diskonly``. They can be given in uppercase as well. \n\n"
            + "\nExample:\n"
            + "| Set Image Logging | DISKONLY |\n")
    @ArgumentNames({"value"})
    public void setImageLogging(String value) {
        if (value.toLowerCase().equals("embedded"))
            TestFxAdapter.logImages = "embedded";
        else if (value.toLowerCase().equals("diskonly"))
            TestFxAdapter.logImages = "diskonly";
        else
            throw new JavaFXLibraryNonFatalException("Value \"" + value + "\" is not supported! Value must be either " +
                    "\"EMBEDDED\" or \"DISKONLY\"");
    }

    @RobotKeyword("Returns a screenshot from whole primary screen. Note that this shows also other applications that are open.\n\n"
            + "``logImage`` is a boolean value that specifies whether a captured image is also printed to test execution log. \n\n "
            + "``mapObject`` is a boolean value that specifies whether a captured image is saved as mapobject and returned from keyword. "
            + "This uses Java heap memory which can result problems if large amount of image capture is done. If set False keyword returns null and image "
            + "is not stored in library bookkeeping. \n\n "
            + "\nExample:\n"
            + "| ${capture}= | Capture Primary Screen | \n"
            + "| ${capture}= | Capture Primary Screen | logImage=False |\n"
            + "| | Capture Primary Screen | logImage=true | mapObject=false |\n")
    @ArgumentNames({"logImage=True", "mapObject=True"})
    public Object capturePrimaryScreen(boolean logImage, boolean mapObject) {
        try {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Rectangle2D target = asyncFx(() -> new Rectangle2D(0, 0, gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight())).get();
            return this.captureImage(target, logImage, mapObject);
        } catch (InterruptedException | ExecutionException iee) {
            throw new JavaFXLibraryNonFatalException("Unable to get Rectangle2D: " + iee.getCause());
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to take capture: ", e.getCause());
        }
    }

    @RobotKeyword("Returns a screenshot of the given locator, or if not given from whole active window.\n\n"
            + "Note that active window might only be part of the visible window, it e.g. dialog is active.\n\n"
            + "``locator`` is either a _query_ or _Object:Bounds, Node, Point2D, Rectangle, PointQuery, Scene, Window_ for identifying the element, see "
            + "`3. Locating JavaFX Nodes`. \n\n"
            + "``logImage`` is a boolean value that specifies whether a captured image is also printed to test execution log. \n\n "
            + "``mapObject`` is a boolean value that specifies whether a captured image is saved as mapobject and returned from keyword. "
            + "This uses Java heap memory which can result problems if large amount of image capture is done. If set False keyword returns null and image "
            + "is not stored in library bookkeeping. \n\n "
            + "\nExample:\n"
            + "| ${region}= | Create Rectangle | 11 | 22 | 33 | 44 | \n"
            + "| ${capture}= | Capture Image | ${region} | \n"
            + "| ${capture}= | Capture Image | ${node} | \n"
            + "| ${capture}= | Capture Image | ${window} | \n"
            + "| ${capture}= | Capture Image | | \n"
            + "| ${capture}= | Capture Image | id=id | logImage=False |\n"
            + "| | Capture Image | id=id | logImage=true | mapObject=false |\n")
    @ArgumentNames({"locator=target window", "logImage=True", "mapObject=True"})
    public Object captureImage(Object locator, boolean logImage, boolean mapObject) {
        checkObjectArgumentNotNull(locator);
        try {
            RobotLog.info("Capturing screenshot from locator: \"" + locator + "\"");
            Image image;
            String logPath;
            Path path = createNewImageFileNameWithPath();

            Bounds targetBounds = asyncFx(() -> objectToBounds(locator)).get();
            image = asyncFx(() -> robot.capture(targetBounds).getImage()).get();
            asyncFx(() -> robotContext().getCaptureSupport().saveImage(image, path)).get();

            if (getCurrentSessionScreenshotDirectoryInLogs() != null) {
                logPath = getCurrentSessionScreenshotDirectoryInLogs() + "/" + path.getFileName();
            } else {
                logPath = path.toString();
            }

            if (logImage) {
                double printSize = targetBounds.getWidth() > 800 ? 800 : targetBounds.getWidth();

                if (TestFxAdapter.logImages.toLowerCase().equals("embedded")) {
                    Image resizedImage = resizeImage(image, path);
                    Path tempPath = Paths.get(getCurrentSessionScreenshotDirectory(), "temp.png");
                    robotContext().getCaptureSupport().saveImage(resizedImage, tempPath);

                    File imageFile = convertToJpeg(tempPath);
                    byte[] imageBytes = FileUtils.readFileToByteArray(imageFile);
                    String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
                    if (imageFile.exists()) {
                        if (!imageFile.delete()) {
                            RobotLog.warn("Capture temporary image \"" + imageFile.getAbsolutePath() + "\" deletion failed.");
                        }
                    }
                    RobotLog.html("<a href=\"" + logPath + "\">"
                            + "<img title=\"Click for full size image\" src=\"data:image/png;base64," + encodedImage + "\" width=\"" + printSize + "px\">"
                            + "</a>");

                } else {
                    // diskonly option
                    RobotLog.html("<a href=\"" + logPath + "\">"
                            + "<img title=\"Click for full size image\" src=\"" + path + "\" width=\"" + printSize + "px\">"
                            + "</a>");
                }
            }
            if (mapObject) {
                return mapObject(image);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException iee) {
            throw new JavaFXLibraryNonFatalException("Unable to take capture (asyncFx thread failed): ", iee.getCause());
        } catch (IOException ioe) {
            throw new JavaFXLibraryNonFatalException("Unable to take capture (IOException): \"" + locator + "\"", ioe.getCause());
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to take capture: \"" + locator + "\"", e.getCause());
        }
    }

    @RobotKeyword("Returns a screenshot of the scene containing given locator.\n\n"
            + "``locator`` is a query locator, see `3.1 Locator syntax`.\n\n "
            + "``logImage`` is a boolean value that specifies whether a captured image is also printed to test execution log. \n\n "
            + "``mapObject`` is a boolean value that specifies whether a captured image is saved as mapobject and returned from keyword. "
            + "This uses Java heap memory which can result problems if large amount of image capture is done. If set False keyword returns null and image "
            + "is not stored in library bookkeeping. \n\n "
            + "\nExample:\n"
            + "| ${capture}= | Capture Scene Containing Node | ${node} | \n"
            + "| ${capture}= | Capture Scene Containing Node | id=id | logImage=False |\n"
            + "| | Capture Scene Containing Node | id=id | logImage=true | mapObject=false |\n")
    @ArgumentNames({"locator", "logImage=True", "mapObject=True"})
    public Object captureSceneContainingNode(Object locator, boolean logImage, boolean mapObject) {
        try {
            Scene scene = asyncFx(() -> (Scene) useMappedObject(new ConvenienceKeywords().getScene(mapObject(locator)))).get();
            return this.captureImage(scene, logImage, mapObject);
        } catch (InterruptedException | ExecutionException iee) {
            throw new JavaFXLibraryNonFatalException("Unable to get scene: " + iee.getCause());
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to take capture: \"" + locator + "\"", e.getCause());
        }
    }

    @RobotKeyword("Loads an image from the given _path_ in hard drive \n\n"
            + "``path`` is the source path for image in local hard drive. \n\n"
            + "\nExample:\n"
            + "| ${image}= | Load Image | ${path to image}node.png |\n")
    @ArgumentNames({"path"})
    public Object loadImage(String path) {
        try {
            RobotLog.info("Loading image from: \"" + path + "\"");
            return mapObject(robot.capture(Paths.get(path)).getImage());
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
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
            RobotLog.info("Loading image from URL: \"" + url + "\"");
            return mapObject(SwingFXUtils.toFXImage(ImageIO.read(new URL(url)), null));
        } catch (Exception e) {
            throw new JavaFXLibraryNonFatalException("Unable to load image from URL: \"" + url + "\"", e);
        }
    }

    @RobotKeyword("Saves given image to given location\n\n"
            + "``image`` is the target _Object:Image_ to be saved\n"
            + "``path`` is the target location where image will be saved")
    @ArgumentNames({"image", "path"})
    public void saveImageAs(Image image, String path) {
        try {
            RobotLog.info("Saving image \"" + image + "\" to path \"" + path + "\"");
            robotContext().getCaptureSupport().saveImage(image, Paths.get(path));
        } catch (Exception e) {
            if (e instanceof JavaFXLibraryNonFatalException)
                throw e;
            throw new JavaFXLibraryNonFatalException("Unable to save image.", e);
        }
    }

    private Path createNewImageFileNameWithPath() {
        ZonedDateTime errorDateTime = ZonedDateTime.now();
        String errorTimestamp = formatErrorTimestamp(errorDateTime);
        String errorImageFilename = "JavaFXLib-" + errorTimestamp + ".png";
        String errorImageFilePath = getCurrentSessionScreenshotDirectory();
        File errDir = new File(errorImageFilePath);
        if (!errDir.exists())
            if (!errDir.mkdirs()) {
                RobotLog.warn("Capture image directory \"" + errorImageFilePath + "\" creation failed.");
            }
        return Paths.get(errorImageFilePath, errorImageFilename);
    }

    private static String formatErrorTimestamp(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
        return dateTime.format(formatter);
    }

    private static Image resizeImage(Image image, Path path) {
        double width = image.getWidth();
        double height = image.getHeight();

        if (width < 800)
            return image;

        double multiplier = width / 800;
        try {
            String url = path.toUri().toURL().toString();
            return new Image(url, width / multiplier, height / multiplier, true, true);
        } catch (MalformedURLException e) {
            throw new JavaFXLibraryNonFatalException("Unable to log the screenshot: image resizing failed!");
        }
    }

    private File convertToJpeg(Path path) throws IOException {
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(path.toFile());
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, java.awt.Color.WHITE, null);
        if (path.toFile().exists()) {
            if (!path.toFile().delete()) {
                RobotLog.warn("Capture temporary image \"" + path + "\" deletion failed.");
            }
        }
        Path tempPathJpeg = Paths.get(getCurrentSessionScreenshotDirectory(), "temp.jpg");
        ImageIO.write(newBufferedImage, "jpg", tempPathJpeg.toFile());
        return tempPathJpeg.toFile();
    }
}