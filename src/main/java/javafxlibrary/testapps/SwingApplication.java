package javafxlibrary.testapps;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class SwingApplication {

    private static int clicks;
    private static Color[] colors = { Color.AQUA, Color.CRIMSON, Color.MEDIUMSPRINGGREEN, Color.VIOLET, Color.YELLOW };
    private static JFrame frame;

    private static void initAndShowGUI() {
        clicks = 0;
        frame = new JFrame("Swing JFrame");

        // EXIT_ON_CLOSE and DISPOSE_ON_CLOSE affect test execution with Jython, causing mvn verify to fail.
        // Hide the JFrame instead, TestFX should clean EmbeddedWindow and its contents automatically.
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(500, 400);
        frame.setVisible(true);

        Platform.runLater(() -> initFX(fxPanel));
    }

    private static void initFX(JFXPanel fxPanel) {
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private static Scene createScene() {
        VBox root = new VBox();
        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);

        Text text = new Text();
        TextField field = new TextField();
        Button button = new Button("Change color");

        button.setOnMouseClicked((MouseEvent e) -> changeBg(text, root));

        text.setFont(new Font(25));
        text.setText("Swing Embedded JavaFX");
        text.setFill(Color.WHITE);
        text.setId("textValue");

        field.setOnKeyReleased((KeyEvent e) -> text.setText(field.getText()));
        field.setMaxWidth(250);
        field.setId("textField");


        root.getChildren().addAll(text, button, field);
        root.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, null, null)));

        return (scene);
    }

    private static void changeBg(Text text, VBox root) {
        clicks++;

        if (clicks > 4) {
            clicks = 0;
        }

        Color color = colors[clicks];
        text.setText(color.toString());
        text.setFill(color.invert());
        root.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    public static void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingApplication::initAndShowGUI);
    }
}