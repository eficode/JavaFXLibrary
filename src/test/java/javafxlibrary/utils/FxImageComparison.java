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

package javafxlibrary.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import org.testfx.util.WaitForAsyncUtils;
import static org.junit.Assert.assertEquals;

public interface FxImageComparison {
	/**
	 * Asserts that the node under test produces the same snapshot than the reference one, using a tolerance thresold.
	 * @param referenceSnapshot The path of the reference snapshot (a png picture).
	 * @param nodeUnderTest The node under test.
	 * @param tolerance The tolerance threshold: the percentage (in [0;100]) of the pixels that can differ.
	 * @throws NullPointerException if the reference snapshot is null.
	 * @throws IllegalArgumentException if the reference snapshot is invalid or unsupported.
	 */
	default void assertSnapshotsEqual(final String referenceSnapshot, final Node nodeUnderTest, final double tolerance) throws IOException, URISyntaxException {
		final WritableImage observedImage = new WritableImage((int) nodeUnderTest.getScene().getWidth(), (int) nodeUnderTest.getScene().getHeight());

		Platform.runLater(() -> nodeUnderTest.snapshot(new SnapshotParameters(), observedImage));
		WaitForAsyncUtils.waitForFxEvents();

		final Image oracleImage = new Image(new File(referenceSnapshot).toURI().toURL().toExternalForm());

		assertEquals("The two snapshots differ", 100d, computeSnapshotSimilarity(observedImage, oracleImage), tolerance);
	}

	/**
	 * Compute the similarity of two JavaFX images.
	 * @param image1 The first image to test.
	 * @param image2 The second image to test.
	 * @return A double value in [0;100] corresponding to the similarity between the two images (pixel comparison).
	 * @throws NullPointerException If image1 or image2 is null.
	 */
	default double computeSnapshotSimilarity(final Image image1, final Image image2) {
		final int width = (int) image1.getWidth();
		final int height = (int) image1.getHeight();
		final PixelReader reader1 = image1.getPixelReader();
		final PixelReader reader2 = image2.getPixelReader();

		final double nbNonSimilarPixels = IntStream.range(0, width).parallel().
			mapToLong(i -> IntStream.range(0, height).parallel().filter(j -> reader1.getArgb(i, j) != reader2.getArgb(i, j)).count()).sum();

		return 100d - nbNonSimilarPixels / (width * height) * 100d;
	}
}
