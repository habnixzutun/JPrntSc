package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

public class Control extends Container {
    Main parent;
    JButton backButton;
    JButton saveButton;
    JButton blockButton;
    JButton openFolderButton;
    JButton openBrowserButton;
    JButton nextButton;
    static int height = 40;
    public Control(Main parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(0, height));
        setLayout(new FlowLayout());

        backButton = new JButton("Back");
        saveButton = new JButton("Save Image");
        blockButton = new JButton("Block Image");
        openFolderButton = new JButton("Open Folder");
        openBrowserButton = new JButton("Open Browser");
        nextButton = new JButton("Next");

        backButton.addActionListener(new ButtonListener(this));
        saveButton.addActionListener(new ButtonListener(this));
        blockButton.addActionListener(new ButtonListener(this));
        openFolderButton.addActionListener(new ButtonListener(this));
        openBrowserButton.addActionListener(new ButtonListener(this));
        nextButton.addActionListener(new ButtonListener(this));

        add(backButton);
        add(saveButton);
        add(blockButton);
        add(openFolderButton);
        add(openBrowserButton);
        add(nextButton);
    }

    static class ButtonListener implements ActionListener {
        Control parent;
        ButtonListener(Control parent) {
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            System.out.println(button.getText());
            switch (button.getText()) {
                case "Back":
                    parent.back();
                    break;
                case "Save Image":
                    parent.save();
                    break;
                case "Block Image":
                    parent.block();
                    break;
                case "Open Folder":
                    parent.openFolder();
                    break;
                case "Open Browser":
                    parent.openBrowser();
                    break;
                case "Next":
                    parent.next();
                    break;
                default:
                    System.out.println("You misspelled or did not implement a button (or missed a break statement)");
                    break;
            }
        }
    }

    void back() {
        try {
            parent.imageContainer.setImage(
                    parent.backgroundLoader.getPrevious()
            );
        }
        catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    void save(){
        try {
            Image image = parent.backgroundLoader.getCurrent();
            ImageIO.write(image.original, "png", new File("src/main/resources/images/" + image.id + ".png"));
            System.out.println("saved image");
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Das Bild konnte nicht gespeichert werden", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    void block () {
        try {
            String hash = Helper.sha1(parent.backgroundLoader.getCurrent().original);

            try (FileWriter writer = new FileWriter(parent.hashFilePath, true)) {
                writer.write(hash + "\n");
            }
            parent.imageContainer.setImage(
                    parent.backgroundLoader.removeCurrentImage()
            );
            System.out.println("blocked image");
            parent.backgroundLoader.removeByHash(hash);
            parent.blockedHashes.add(hash);
        }
        catch (IOException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(parent, "Es ist ein Fehler aufgetreten", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    void openFolder() {
        try {
            File folder = new File("src/main/resources/images/");

            if (!folder.exists()) {
                folder.mkdirs(); // optional
            }

            Desktop.getDesktop().open(folder);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openBrowser() {
        try {
            Desktop.getDesktop().browse(new URI(parent.getTitle()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void next() {
        try {
            parent.imageContainer.setImage(
                    parent.backgroundLoader.getNext()
            );
        }
        catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
