import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class BackgroundLoader {
    Window parent;
    private final ArrayList<BufferedImage> previous;
    private final List<BufferedImage> next;
    private BufferedImage current;
    private final Thread thread;
    private final int maxBufferSize = 50;
    BackgroundLoader(Window parent) {
        this.parent = parent;
        previous = new ArrayList<>();
        next = new ArrayList<>();
        thread = new Thread(this::searchAndStoreImages);

        try {
            current = getImage("https://http.cat/100");
        }
        catch (IOException | InterruptedException ex) {
            ex.getStackTrace();
        }


    }

    private BufferedImage getImage(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );

        BufferedImage image = ImageIO.read(
                new ByteArrayInputStream(response.body()));
        return image;
    }

    String generateId() {
        return "";
    }

    void searchAndStoreImages() {
        int[] codes = new int[] {200, 400, 401, 403, 404, 500, 501};
        int maxRandomVariation = 300;
        int baseDelay = 500;
        int delay;

        for (int code : codes) {
            try {
                next.add(getImage("https://http.cat/" + code));
                System.out.println("loaded image " + code);
                delay = baseDelay + (int) (new Random().nextFloat() * maxRandomVariation - (float) maxRandomVariation /2);
                System.out.println("Delay: " + delay);
                Thread.sleep(delay);
            }
            catch (IOException | InterruptedException ex) {
                ex.getStackTrace();
            }
        }
    }

    void start() {
        thread.start();
    }

    BufferedImage getCurrent() {
        return current;
    }

    BufferedImage getNext() throws NoSuchElementException{
        if (next.isEmpty()) {
            throw new NoSuchElementException("Es gibt keine nächsten Bilder");
        }
        BufferedImage temporary;
        temporary = next.getFirst();
        next.removeFirst();
        previous.addFirst(current);
        current = temporary;

        if (previous.size() > maxBufferSize) {
            previous.removeLast();
        }

        return current;
    }

    BufferedImage getPrevious() throws NoSuchElementException {
        if (previous.isEmpty()) {
            throw new NoSuchElementException("Es gibt keine vorherigen Bilder");
        }
        BufferedImage temporary;
        temporary = previous.getFirst();
        previous.removeFirst();
        next.addFirst(current);
        current = temporary;
        return current;
    }

    BufferedImage removeCurrentImage() {
        if (!next.isEmpty()) {
            current = next.removeFirst();
        }
        else if (!previous.isEmpty()) {
            current = previous.removeFirst();
        }
        else {
            current = new BufferedImage(
                    parent.getWidth(),
                    parent.getHeight() - parent.control.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
        }

        return current;
    }
}
