import javax.swing.*;
import java.awt.*;

public class Control extends Container {
    JButton back;
    JButton save;
    JButton block;
    JButton openFolder;
    JButton openBrowser;
    JButton next;
    public Control() {
        setPreferredSize(new Dimension(0, 40));
        setLayout(new FlowLayout());

        back = new JButton("Back");
        save = new JButton("Save Image");
        block = new JButton("Block Image");
        openFolder = new JButton("Open Folder");
        openBrowser = new JButton("Open Browser");
        next = new JButton("Next");
        add(back);
        add(save);
        add(block);
        add(openFolder);
        add(openBrowser);
        add(next);
    }
}
