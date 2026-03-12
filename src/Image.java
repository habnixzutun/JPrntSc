import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Image {
    BufferedImage content;
    String id;
    String hash;

    Image(BufferedImage content) {
        this.content = content;
        this.id = "";
        try {
            this.hash = Helper.sha1(content);
        } catch (NoSuchAlgorithmException | IOException ignored) {}
    }

    Image(BufferedImage content, String id) {
        this.content = content;
        this.id = id;
        try {
            this.hash = Helper.sha1(content);
        } catch (NoSuchAlgorithmException | IOException ignored) {}
    }

    Image(BufferedImage content, int id) {
        this.content = content;
        this.id = String.valueOf(id);
        try {
            this.hash = Helper.sha1(content);
        } catch (NoSuchAlgorithmException | IOException ignored) {}
    }
}
