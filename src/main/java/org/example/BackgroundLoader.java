package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class BackgroundLoader {
    Main parent;
    private final ArrayList<Image> previous;
    private final ArrayList<Image> next;
    private Image current;
    private final Thread thread;
    private final int maxBufferSize = 50;
    private final Random random = new Random();
    BackgroundLoader(Main parent) {
        this.parent = parent;
        previous = new ArrayList<Image>();
        next = new ArrayList<>();
        thread = new Thread(this::searchAndStoreImages);
    }

    private void searchAndStoreImages() {
        int maxRandomVariation = 300;
        int baseDelay = 500;
        int delay;

        while (!Thread.currentThread().isInterrupted()) {
            String currentImgId;
            try {
                if (next.size() > maxBufferSize) {
                    Thread.sleep(200);
                    continue;
                }
                currentImgId = generateImgId();
                Image image = getImage(currentImgId);
                if (image != null) {
                    synchronized (next) {
                        next.add(image);
                    }
                }
                System.out.println("loaded image " + currentImgId);
                delay = baseDelay + (int) (new Random().nextFloat() * maxRandomVariation - (float) maxRandomVariation /2);
                System.out.println("Delay: " + delay);
                Thread.sleep(delay);
            }
            catch (InterruptedException ex) {
                ex.getStackTrace();
            }
        }
    }

    private Image getImage(String id) {
        try {
            Document doc = Jsoup.connect("https://prnt.sc/" + id)
                    .userAgent("Mozilla/5.0")
                    .get();

            Element img = doc.selectFirst("img.screenshot-image");
            if (img == null) {
                return null;
            }

            String src = img.attr("src");

            if (src.isEmpty())
                return null;

            if (!src.startsWith("https://"))
                src = src.startsWith("//") ? "https:" + src : "https://" + src;

            if (!id.equals(img.attr("image-id"))) {
                return null;
            }

            BufferedImage rawImage = ImageIO.read(new URL(src));
            if (rawImage == null) {
                return null;
            }
            String hash = Helper.sha1(rawImage);

            if (parent.blockedHashes.contains(hash)) {
                return null;
            }

            System.out.println(id + " " + hash);

            return new Image(rawImage, id);

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    private String generateImgId() {
        return generateImgId(0);
    }

    private String generateImgId(int recursionLevel) {
        StringBuilder imgId = new StringBuilder();

        // 6 characters: numbers (0-9) and lowercase letters (a-z)
        for (int i = 0; i < 6; i++) {
            int choice = random.nextInt(36); // 10 digits + 26 letters

            if (choice < 10) {
                imgId.append((char) (48 + choice)); // '0'..'9'
            } else {
                imgId.append((char) (97 + choice - 10)); // 'a'..'z'
            }
        }

        // avoid IDs starting with 0
        if (imgId.charAt(0) == '0') {
            if (recursionLevel <= 50) {
                return generateImgId(recursionLevel + 1);
            } else {
                return "abcdef"; // will for sure be skipped
            }
        }

        // 33.3% chance for 7 char id
        if (random.nextInt(3) == 0) {

            // starts with 1 (3:1 chance)
            if (random.nextInt(5) <= 3) {
                imgId.insert(0, '1');
            } else {
                // starts with 2, second char must be digit
                imgId.setCharAt(0, (char) ('0' + random.nextInt(10)));
                imgId.insert(0, '2');
            }
        }

        return imgId.toString();
    }

    void start() {
        thread.start();
    }

    Image getCurrent() {
        if (!next.isEmpty() && current == null) {
            try {
                return next.getFirst();
            }
            catch (NoSuchElementException ignored) {}
        }
        return current;
    }

    Image getNext() throws NoSuchElementException{
        if (next.isEmpty()) {
            throw new NoSuchElementException("Es gibt keine nächsten Bilder");
        }
        Image temporary;
        temporary = next.getFirst();
        next.removeFirst();
        previous.addFirst(current);
        current = temporary;

        if (previous.size() > maxBufferSize) {
            previous.removeLast();
        }

        return current;
    }

    Image getPrevious() throws NoSuchElementException {
        if (previous.isEmpty()) {
            throw new NoSuchElementException("Es gibt keine vorherigen Bilder");
        }
        Image temporary;
        temporary = previous.getFirst();
        previous.removeFirst();
        next.addFirst(current);
        current = temporary;
        return current;
    }

    Image removeCurrentImage() {
        if (!next.isEmpty()) {
            current = next.removeFirst();
        }
        else if (!previous.isEmpty()) {
            current = previous.removeFirst();
        }
        else {
            current = new Image(new BufferedImage(
                    parent.getWidth(),
                    parent.getHeight() - parent.control.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            ));
        }

        return current;
    }

    void removeByHash(String hash) {
        ArrayList<Integer> indexesToRemove = new ArrayList<>();
        Image image;
        synchronized (next) {
            for (int i = next.size() - 1; i >= 0; i--) {
                image = next.get(i);
                if (image.hash.equals(hash) || parent.blockedHashes.contains(image.hash)) {
                    indexesToRemove.add(i);
                }
            }
            for (int i : indexesToRemove) {
                next.remove(i);
            }
        }
    }
}
