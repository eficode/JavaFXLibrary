package javafxlibrary.utils.HelperFunctionsTests;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.testfx.framework.junit.ApplicationTest;
import javafxlibrary.utils.HelperFunctions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PrintTreeStructureTest extends ApplicationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private String[] nodes;

    @Before
    public void setUpStreams() {
        // TODO: Tests setting System.out fail on Windows
        org.junit.Assume.assumeTrue(HelperFunctions.isMac());
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void printTreeStructure_LayeredChildren_PrintsList() {
        VBox root = createStructure();
        HelperFunctions.printTreeStructure(root);
        Assert.assertEquals(getTarget(), outContent.toString());
    }

    @Test
    public void printTreeStructure_NoChildren_PrintsEmptyList() {
        VBox root = new VBox();
        HelperFunctions.printTreeStructure(root);
        Assert.assertEquals("*HTML* <ul></ul>\n", outContent.toString());
    }

    private VBox createStructure() {
        Button button = new Button();
        Label label = new Label();
        HBox hBox = new HBox(label);
        nodes = new String[] { button.toString(), hBox.toString(), label.toString() };
        return new VBox(button, hBox);
    }

    private String getTarget() {
        return "*HTML* <ul><details open><summary>Button</summary>" + nodes[0]
                + "</details><details open><summary>HBox</summary>" + nodes[1]
                + "<ul><details open><summary>Label</summary>" + nodes[2]
                + "</details></ul></details></ul>\n";
    }
}
