import java.awt.image.BufferedImage;

public class Image {
    BufferedImage content;
    String id;

    Image(BufferedImage content) {
        this.content = content;
        this.id = "";
    }

    Image(BufferedImage content, String id) {
        this.content = content;
        this.id = id;
    }

    Image(BufferedImage content, int id) {
        this.content = content;
        this.id = String.valueOf(id);
    }
}
