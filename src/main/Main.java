package main;

import main.MainWindow;

public class Main {
    public static void main(String[] args) {

        DrawPanel drawPanel = new DrawPanel(800, 600);
        ControlPanel controlPanel = new ControlPanel();
        MainWindow mainWindow = new MainWindow("LabWork2", drawPanel, controlPanel);

    }
}
