package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Image {
    BufferedImage original;
    BufferedImage content;
    String id;
    String hash;

    Image(BufferedImage original) {
        this.original = original;
        this.id = "";
        try {
            this.hash = Helper.sha1(original);
        } catch (NoSuchAlgorithmException | IOException ignored) {}
        this.content = generateContent(original);
    }

    Image(BufferedImage original, String id) {
        this.original = original;
        this.id = id;
        try {
            this.hash = Helper.sha1(original);
        } catch (NoSuchAlgorithmException | IOException ignored) {}
        this.content = generateContent(original);
    }

    Image(BufferedImage original, int id) {
        this.original = original;
        this.id = String.valueOf(id);
        try {
            this.hash = Helper.sha1(original);
        } catch (NoSuchAlgorithmException | IOException ignored) {}
        this.content = generateContent(original);
    }

    private BufferedImage generateContent(BufferedImage original) {
        BufferedImage resized = copyImage(original);
        double width = original.getWidth();
        double height = original.getHeight();

        double maxWidth = Main.frameWidth;
        double maxHeight = Main.frameHeight;

        double frameSideRatio = maxWidth / maxHeight;

        if (width / height > frameSideRatio && width > maxWidth) {
            resized = resizeImage(resized, maxWidth, maxWidth / (width / height));
        }
        if (width / height < frameSideRatio && height > maxHeight) {
            resized = resizeImage(resized, maxHeight / (height / width), maxHeight);
        }
        return resized;
    }

    public static BufferedImage copyImage(BufferedImage original) {
        BufferedImage copy = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                original.getType()
        );

        Graphics2D g = copy.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();

        return copy;
    }

    public static BufferedImage resizeImage(BufferedImage original, double dNewWidth, double dNewHeight) {
        int newWidth = (int) dNewWidth;
        int newHeight = (int) dNewHeight;
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());

        Graphics2D g = resized.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resized;
    }
}
