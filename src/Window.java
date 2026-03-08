import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Window extends JFrame {
    Container frame;
    Control control;
    Image imageContainer;
    BackgroundLoader backgroundLoader;

    public Window() {
        setSize(680, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("prnt.sc");

        frame = getContentPane();
        frame.setLayout(new BorderLayout());

        backgroundLoader = new BackgroundLoader();
        backgroundLoader.start();

        control = new Control(this);
        frame.add(control, BorderLayout.SOUTH);

        imageContainer = new Image(this);
        frame.add(imageContainer, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        Window window = new Window();
        window.setVisible(true);
    }
}
