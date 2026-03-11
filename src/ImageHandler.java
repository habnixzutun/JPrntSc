import java.awt.*;
import javax.swing.*;

public class ImageHandler extends Container {
    Window parent;
    JLabel imageLabel;

    ImageHandler(Window parent) {
        setLayout(new FlowLayout());

        this.parent = parent;

        imageLabel = new JLabel(new ImageIcon());
        add(imageLabel);
    }

    void setImage(Image image) {

        int width = image.content.getWidth();
        int height = image.content.getHeight();

        System.out.println(width);
        System.out.println(height);

        if (width < 680) {
            width = 680;
        }
        parent.setSize(new Dimension(width, height + parent.control.height));
        parent.setTitle("https://prnt.sc/" + image.id);


        imageLabel.setIcon(new ImageIcon(image.content));
        imageLabel.revalidate();
        imageLabel.repaint();
    }
}
