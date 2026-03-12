import java.awt.*;
import javax.swing.*;

public class ImageHandler extends Container {
    Window parent;
    JLabel imageLabel;
    private final Thread thread;
    boolean imageSet = false;

    ImageHandler(Window parent) {
        setLayout(new FlowLayout());

        this.parent = parent;

        imageLabel = new JLabel(new ImageIcon());
        add(imageLabel);

        thread = new Thread(this::waitForFirstImage);
        thread.start();
    }

    void waitForFirstImage() {
        Image currentImage = parent.backgroundLoader.getCurrent();
        while (currentImage == null) {
            currentImage = parent.backgroundLoader.getNext();
            try {
                Thread.sleep(50);
                System.out.println("Waiting for image in current");
                System.out.println(currentImage);
            }
            catch (InterruptedException ignored) {}
        }
        setImage(currentImage);
    }

    void setImage(Image image) {

        int width = image.content.getWidth();
        int height = image.content.getHeight();

        System.out.println(width);
        System.out.println(height);

        if (width < 680) {
            width = 680;
        }
        parent.setSize(new Dimension(width, height + parent.control.height * 2));
        parent.setTitle("https://prnt.sc/" + image.id);


        imageLabel.setIcon(new ImageIcon(image.content));
        imageLabel.revalidate();
        imageLabel.repaint();
    }
}
