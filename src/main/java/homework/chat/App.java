package ru.geekbrains.classes.lesson8;



import ru.geekbrains.classes.lesson8.swing.MainWindow;

import javax.swing.*;

public class App {

    private static MainWindow mainWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainWindow = new MainWindow();
            }
        });

    }
}
