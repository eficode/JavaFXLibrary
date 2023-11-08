package javafxlibrary.utils.finder;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafxlibrary.TestFxAdapterTest;
import javafxlibrary.utils.TestFxAdapter;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.*;
import org.testfx.api.FxRobot;

import java.util.ArrayList;
import java.util.List;

public class FinderTest extends TestFxAdapterTest {

    private Finder finder;
    @Mocked
    Stage stage;
    @Mocked
    VBox root;

    @Before
    public void setup() {
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

        List<Window> windows = new ArrayList<>();
        windows.add(stage);
        new Expectations() {
            {
                getRobot().listTargetWindows();
                result = windows;
                stage.getScene().getRoot();
                result = root;
                root.lookupAll((String) any);
                result = hBox;
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
        finder = new Finder();

        new Expectations() {
            {
                robot.from(group).query();
                result = group;
            }
        };

        Node result = finder.find(".button", group);
        Assert.assertEquals(button, result);
    }
}
