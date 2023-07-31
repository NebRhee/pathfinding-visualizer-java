import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        // Ensures that all swing code is run on the Event Dispatch Thread (Swing is not thread safe)
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainWindow main = new MainWindow();
                main.show();
            }
        });
    }
}
