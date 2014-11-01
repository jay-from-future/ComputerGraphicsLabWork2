package main;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private DrawPanel drawPanel;
    private ControlPanel controlPanel;

    public MainWindow(String title, int width, int height, DrawPanel drawPanel, ControlPanel controlPanel)
            throws HeadlessException {
        super(title);
        setSize(new Dimension(width, height));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.drawPanel = drawPanel;
        this.controlPanel = controlPanel;

        setLayout(new BorderLayout());
        add(drawPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
