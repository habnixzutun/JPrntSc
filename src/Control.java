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
    JButton back;
    JButton save;
    JButton block;
    JButton openFolder;
    JButton openBrowser;
    JButton next;
    int height = 40;
    public Control(Window parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(0, height));
        setLayout(new FlowLayout());

        back = new JButton("Back");
        save = new JButton("Save Image");
        block = new JButton("Block Image");
        openFolder = new JButton("Open Folder");
        openBrowser = new JButton("Open Browser");
        next = new JButton("Next");

        back.addActionListener(new ButtonListener(this));
        save.addActionListener(new ButtonListener(this));
        block.addActionListener(new ButtonListener(this));
        openFolder.addActionListener(new ButtonListener(this));
        openBrowser.addActionListener(new ButtonListener(this));
        next.addActionListener(new ButtonListener(this));

        add(back);
        add(save);
        add(block);
        add(openFolder);
        add(openBrowser);
        add(next);
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
                    //goBack();
                    parent.loadImage();
                    break;
                case "Save Image":
                    //goBack();
                    break;
                case "Block Image":
                    //goBack();
                    break;
                case "Open Folder":
                    //goBack();
                    break;
                case "Open Browser":
                    //goBack();
                    break;
                case "Next":
                    //goBack();
                    break;
                default:
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
}
