import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Window extends JFrame {
    Container frame;
    Control control;
    ImageHandler imageContainer;
    BackgroundLoader backgroundLoader;
    HashSet<String> blockedHashes;
    String hashFilePath = "hashes.txt";

    public Window() {
        setSize(680, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("prnt.sc");
        setAutoRequestFocus(true);
        setResizable(false);

        File imageFolder = new File("images/");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }

        blockedHashes = loadHashes(hashFilePath);

        frame = getContentPane();
        frame.setLayout(new BorderLayout());


        control = new Control(this);
        frame.add(control, BorderLayout.SOUTH);

        imageContainer = new ImageHandler(this);
        frame.add(imageContainer, BorderLayout.CENTER);

        backgroundLoader = new BackgroundLoader(this);
        backgroundLoader.start();
    }

    static HashSet<String> loadHashes(String filePath) {
        try (var lines = Files.lines(Path.of(filePath))) {
            return lines
                    .map(String::trim)              // Entfernt Leerzeichen/Zeilenumbrüche
                    .filter(line -> !line.isEmpty()) // Ignoriert leere Zeilen
                    .collect(Collectors.toCollection(HashSet::new));
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen: " + e.getMessage());
            return new HashSet<>(); // Oder Exception werfen
        }
    }

    public static void main(String[] args) {
        Window window = new Window();
        window.setVisible(true);
    }
}
