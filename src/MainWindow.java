import javax.swing.*;
import java.awt.*;

public class MainWindow {

    // Private so JFrame class's internal methods aren't exposed to our launcher
    private JFrame window;
//    public static VisualPanel graph;
//    public static FormPanel form;

    public MainWindow() {
        initialize();
    }

    private void initialize() {
        window = new JFrame();

        window.setTitle("Algorithm Visualizer");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // JPANELS
        VisualPanel grid = new VisualPanel();
        FormPanel form = new FormPanel(grid);

        window.add(grid, BorderLayout.WEST);
        window.add(form, BorderLayout.SOUTH);

        // Sizes frame so all items can fit
        window.pack();

        // Center Frame on screen
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }

    // Make JFrame window visible
    public void show() {
        window.setVisible(true);
    }
}
