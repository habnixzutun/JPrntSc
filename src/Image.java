import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Image extends Container {
    Window parent;
    JLabel imageLabel;

    Image(Window parent) {
        setLayout(new FlowLayout());

        this.parent = parent;

        imageLabel = new JLabel(new ImageIcon());
        add(imageLabel);
    }

    void setImage(BufferedImage image) {
        System.out.println(image.getHeight());
        System.out.println(image.getWidth());

        parent.setSize(new Dimension(image.getWidth(), image.getHeight() + parent.control.height));


        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.revalidate();
        imageLabel.repaint();
    }
}
