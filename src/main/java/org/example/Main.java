package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Main extends JFrame {
    Container frame;
    Control control;
    ImageHandler imageContainer;
    BackgroundLoader backgroundLoader;
    HashSet<String> blockedHashes;
    String hashFilePath = "hashes.txt";

    public Main() {
        setSize(680, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("prnt.sc");
        setAutoRequestFocus(true);
        setResizable(false);

        File imageFolder = new File("src/main/resources/images/");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }

        blockedHashes = loadHashes(hashFilePath);

        frame = getContentPane();
        frame.setLayout(new BorderLayout());


        control = new Control(this);
        frame.add(control, BorderLayout.SOUTH);

        backgroundLoader = new BackgroundLoader(this);
        backgroundLoader.start();

        imageContainer = new ImageHandler(this);
        frame.add(imageContainer, BorderLayout.CENTER);

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
        Main window = new Main();
        window.setVisible(true);
    }
}
