package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.utils.HelperFunctions;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetAllNodesTest extends ApplicationTest {

    private List<Node> children = new ArrayList<>();

    @Test
    public void getAllNodes_LayeredChildren_ReturnsEveryChild() {
        VBox root = setupNodes();
        List<Node> nodes = HelperFunctions.getAllNodes(root);
        Assert.assertEquals(children, nodes);
    }

    @Test
    public void getAllNodes_NoChildren_ReturnsEmptyList() {
        VBox root = new VBox();
        List<Node> nodes = HelperFunctions.getAllNodes(root);
        Assert.assertTrue(nodes.isEmpty());
    }

    private VBox setupNodes() {
        Label label = new Label();
        TextField textField = new TextField();
        Button button = new Button();
        HBox row1 = new HBox(label);
        HBox row2 = new HBox(textField, button);
        HBox row3 = new HBox();
        children.addAll(Arrays.asList(row1, label, row2, textField, button, row3));
        return new VBox(row1, row2, row3);
    }
}
