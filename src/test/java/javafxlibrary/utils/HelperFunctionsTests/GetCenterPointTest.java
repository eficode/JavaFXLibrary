package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

public class GetCenterPointTest {

    @Test
    public void getCenterPoint_Area() {
        Bounds bounds = new BoundingBox(0, 0, 200, 200);
        Point2D result = HelperFunctions.getCenterPoint(bounds);
        Assert.assertEquals(new Point2D(100, 100), result);
    }

    @Test
    public void getCenterPoint_Point() {
        Bounds bounds = new BoundingBox(200, 200, 0, 0);
        Point2D result = HelperFunctions.getCenterPoint(bounds);
        Assert.assertEquals(new Point2D(200, 200), result);
    }

    @Test
    public void getCenterPoint_NegativeArea() {
        Bounds bounds = new BoundingBox(200, 200, -50, -50);
        Point2D result = HelperFunctions.getCenterPoint(bounds);
        Assert.assertEquals(new Point2D(175, 175), result);
    }

    @Test
    public void getCenterPoint_NegativeLocation() {
        Bounds bounds = new BoundingBox(-200, -200, -50, -50);
        Point2D result = HelperFunctions.getCenterPoint(bounds);
        Assert.assertEquals(new Point2D(-225, -225), result);
    }

    @Test
    public void getCenterPoint_UnevenArea() {
        Bounds bounds = new BoundingBox(0, 0, 3.33, 6.66);
        Point2D result = HelperFunctions.getCenterPoint(bounds);
        Assert.assertEquals(new Point2D(1.665, 3.33), result);
    }
}
