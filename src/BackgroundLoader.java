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

public class BackgroundLoader {
    private final ArrayList<BufferedImage> previous;
    private final List<BufferedImage> next;
    private BufferedImage current;
    private final Thread thread;
    BackgroundLoader() {
        previous = new ArrayList<>();
        next = new ArrayList<>();
        thread = new Thread(this::searchAndStoreImages);

        try {
            next.add(getImage("https://http.cat/404"));
        }
        catch (IOException | InterruptedException ex) {
            ex.getStackTrace();
        }
        try {
            current = getImage("https://http.cat/403");
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

    void searchAndStoreImages() {
        while (true) {
            System.out.println("Hellooo");
        }
    }

    void start() {
        thread.start();
    }

    BufferedImage getNext() throws ArrayIndexOutOfBoundsException{
        if (next.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Es gibt keine nächsten Bilder");
        }
        BufferedImage temporary;
        temporary = next.getFirst();
        next.removeFirst();
        previous.addFirst(current);
        current = temporary;
        return current;
    }

    BufferedImage getPrevious() throws ArrayIndexOutOfBoundsException {
        if (previous.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Es gibt keine vorherigen Bilder");
        }
        BufferedImage temporary;
        temporary = previous.getFirst();
        previous.removeFirst();
        next.addFirst(current);
        current = temporary;
        return current;
    }
}
