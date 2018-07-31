package javafxlibrary.utils;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafxlibrary.TestFxAdapterTest;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testfx.api.FxRobot;

import java.util.List;

public class FinderTest extends TestFxAdapterTest {

    private Finder finder;
    @Mocked Stage stage;
    @Mocked List<Window> windows;
    @Mocked VBox root;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        new Expectations() {
            {
                getRobot().listTargetWindows(); result = windows;
                windows.get(0); result = stage;
                stage.getScene().getRoot(); result = root;
            }
        };

        finder = new Finder();
    }

    @Test
    public void find_DefaultRoot_NewParameterTypesChained() {
        Button button = new Button();
        button.getStyleClass().add("anotherClass");
        Group subGroup = new Group();
        subGroup.getStyleClass().add("sub=group");
        subGroup.getChildren().add(button);
        Group group = new Group();
        group.getStyleClass().addAll("test", "orangeBG");
        group.getChildren().add(subGroup);
        HBox hBox = new HBox();
        hBox.setId("testNode");
        hBox.getChildren().add(group);

        new Expectations() {
            {
                root.lookup((String) any); result = hBox;
            }
        };
        Node result = finder.find("id=testNode css=.test.orangeBG .sub=group css=.anotherClass");
        Assert.assertEquals(button, result);
    }

    @Test
    public void find_CustomRoot_TestFXSelector() {
        Group group = new Group();
        HBox hBox = new HBox();
        Button button = new Button("Target");
        hBox.getChildren().add(button);
        group.getChildren().add(hBox);

        // Use a real FxRobot instance for this test
        FxRobot robot = new FxRobot();
        TestFxAdapter.setRobot(robot);

        new Expectations() {
            {
                robot.from(group).query(); result = group;
            }
        };

        Node result = finder.find(".button", group);
        Assert.assertEquals(button, result);
    }

    @Test
    public void containsPrefixes_AcceptedValues() {
        Assert.assertTrue(finder.containsPrefixes("css=.Vbox .button"));
        Assert.assertTrue(finder.containsPrefixes("id=testNode"));
        Assert.assertTrue(finder.containsPrefixes("class=java.lang.String"));
        Assert.assertTrue(finder.containsPrefixes("xpath=//Rectangle"));
        Assert.assertTrue(finder.containsPrefixes("pseudo=hover"));
        Assert.assertTrue(finder.containsPrefixes("text=\"Text\""));
    }

    @Test
    public void containsPrefixes_InvalidValue() {
        Assert.assertFalse(finder.containsPrefixes("invalid=should be false"));
    }

    @Test
    public void containsMultiplePrefixes_Contains() {
        Assert.assertTrue(finder.containsMultiplePrefixes("css=VBox xpath=[@id=\"special\"]"));
    }

    @Test
    public void containsMultiplePrefixes_DoesNotContain() {
        Assert.assertFalse(finder.containsMultiplePrefixes("id=wrapper"));
    }

    @Test
    public void getPrefix_AcceptedValues() {
        Assert.assertEquals(Finder.FindPrefix.ID, finder.getPrefix("id=nodeId"));
        Assert.assertEquals(Finder.FindPrefix.CLASS, finder.getPrefix("class=java.lang.String"));
        Assert.assertEquals(Finder.FindPrefix.CSS, finder.getPrefix("css=.vBox .hBox"));
        Assert.assertEquals(Finder.FindPrefix.XPATH, finder.getPrefix("xpath=//Rectangle[@id=\"lime\"]"));
        Assert.assertEquals(Finder.FindPrefix.PSEUDO, finder.getPrefix("pseudo=hover"));
        Assert.assertEquals(Finder.FindPrefix.TEXT, finder.getPrefix("text=\"Text\""));
    }

    @Test
    public void getPrefix_NoEquals() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query \"noEquals\" does not contain any supported prefix");
        finder.getPrefix("noEquals");
    }

    @Test
    public void getPrefix_InvalidValue() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query \"notaprefix=someValue\" does not contain any supported prefix");
        finder.getPrefix("notaprefix=someValue");
    }

    @Test
    public void splitQuery_WithSpaces() {
        String[] result = finder.splitQuery("xpath=SomeNode[@text=\"test text\"] text=\"text with spaces\" id=sub");
        String[] target = { "xpath=SomeNode[@text=\"test text\"]", "text=\"text with spaces\"", "id=sub" };
        Assert.assertArrayEquals(target, result);
    }

    @Test
    public void splitQuery_WithQuotes() {
        String[] result = finder.splitQuery("text=\"Teemu \\\"The Finnish Flash\\\" Selanne\"");
        String[] target = { "text=\"Teemu \"The Finnish Flash\" Selanne\"" };
        Assert.assertArrayEquals(target, result);
    }



}
