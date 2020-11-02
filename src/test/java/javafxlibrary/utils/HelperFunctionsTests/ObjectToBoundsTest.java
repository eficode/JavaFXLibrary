package javafxlibrary.utils.HelperFunctionsTests;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Window;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.HelperFunctions;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.testfx.service.query.PointQuery;
import org.testfx.service.query.impl.BoundsPointQuery;

public class ObjectToBoundsTest extends TestFxAdapterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void objectToBounds_Window(@Injectable Window window) {
        new Expectations() {
            {
                window.getX(); result = 50;
                window.getY(); result = 50;
                window.getWidth(); result = 250;
                window.getHeight(); result = 250;
            }
        };
        BoundingBox target = new BoundingBox(50, 50, 250, 250);
        Assert.assertEquals(target, HelperFunctions.objectToBounds(window));
    }

    @Test
    public void objectToBounds_Scene(@Injectable Scene scene) {
        new Expectations() {
            {
                scene.getX(); result = 250;
                scene.getY(); result = 250;
                scene.getWidth(); result = 250;
                scene.getHeight(); result = 250;
            }
        };
        BoundingBox target = new BoundingBox(250, 250, 250, 250);
        Assert.assertEquals(target, HelperFunctions.objectToBounds(scene));
    }

    @Test
    public void objectToBounds_Point2D() {
        new Expectations() {
            {
                getRobot().bounds((Point2D) any).query(); result = new BoundingBox(250, 250, 0, 0);
            }
        };
        BoundingBox target = new BoundingBox(250, 250, 0, 0);
        Assert.assertEquals(target, HelperFunctions.objectToBounds(new Point2D(250, 250)));
    }

    @Test
    public void objectToBounds_Node() {
        new Expectations() {
            {
                getRobot().bounds((Node) any).query(); result = new BoundingBox(720, 720, 0, 0);
            }
        };

        BoundingBox target = new BoundingBox(720, 720, 0, 0);
        Assert.assertEquals(target, HelperFunctions.objectToBounds(new Button()));
    }

    @Test
    public void objectToBounds_String() {
        new MockUp<HelperFunctions>() {
            @Mock
            Node waitUntilExists(String target, int timeout, String timeUnit) {
                return new Button();
            }
        };
        new Expectations() {
            {
                getRobot().bounds((Node) any).query(); result = new BoundingBox(906, 609, 250, 50);
            }
        };

        BoundingBox target = new BoundingBox(906, 609, 250, 50);
        Assert.assertEquals(target, HelperFunctions.objectToBounds("#testNode"));
    }

    @Test
    public void objectToBounds_Bounds() {
        BoundingBox target = new BoundingBox(100, 110, 120, 130);
        Bounds bounds = new BoundingBox(100, 110, 120, 130);
        Assert.assertEquals(target, HelperFunctions.objectToBounds(bounds));
    }

    @Test
    public void objectToBounds_PointQuery() {
        new Expectations() {
            {
                getRobot().bounds((Point2D) any).query(); result = new BoundingBox(10, 10, 200, 100);
            }
        };
        BoundingBox target = new BoundingBox(10, 10, 200, 100);
        PointQuery pq = new BoundsPointQuery(new BoundingBox(10, 10, 200, 100));
        Assert.assertEquals(target, HelperFunctions.objectToBounds(pq));
    }

    @Test
    public void objectToBounds_Rectangle2D() {
        BoundingBox target = new BoundingBox(0, 0, 150, 50);
        Rectangle2D rectangle2D = new Rectangle2D(0, 0, 150, 50);
        Assert.assertEquals(target, HelperFunctions.objectToBounds(rectangle2D));
    }

    @Test
    public void objectToBounds_UnsupportedType() {
        thrown.expect(JavaFXLibraryNonFatalException.class);
        thrown.expectMessage("unsupported parameter type: java.lang.Integer");
        HelperFunctions.objectToBounds(22);
    }
}
