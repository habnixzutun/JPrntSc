import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Control extends Container {
    Window parent;
    JButton backButton;
    JButton saveButton;
    JButton blockButton;
    JButton openFolderButton;
    JButton openBrowserButton;
    JButton nextButton;
    int height = 40;
    public Control(Window parent) {
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

    void loadImage() {
        try {
            String imageUrl = "https://http.cat/404";

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(imageUrl))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            BufferedImage image = ImageIO.read(
                    new ByteArrayInputStream(response.body()));
            parent.imageContainer.setImage(image);
        }
        catch (IOException | InterruptedException ex) {
            ex.getStackTrace();
        }
    }

    void back() {
        parent.imageContainer.setImage(
                parent.backgroundLoader.getPrevious()
        );
    }
    void save() {}
    void block () {}
    void openFolder() {}
    void openBrowser() {}
    void next() {
        parent.imageContainer.setImage(
                parent.backgroundLoader.getNext()
        );
    }
}
