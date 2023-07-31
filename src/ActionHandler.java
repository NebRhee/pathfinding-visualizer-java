import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionHandler implements ActionListener, ChangeListener {
    VisualPanel grid;
    FormPanel controlPanel;

    public ActionHandler(VisualPanel grid, FormPanel controlPanel) {
        this.grid = grid;
        this.controlPanel = controlPanel;
    }

    /*
        Button Listener
     */
    public void actionPerformed(ActionEvent e) {
        if ((e.getActionCommand()).equals("Visualize!")) {
            System.out.println("Visualize Clicked");
            grid.runAnimation();
            grid.requestFocusInWindow();
        } else if ((e.getActionCommand()).equals("Reset")) {
            System.out.println("Reset Clicked");
            grid.resetGrid();
            grid.requestFocusInWindow();
        }

        // Dropdown
        if (e.getSource() instanceof JComboBox<?>) {
            String sourceName = (String) controlPanel.getComboBox().getSelectedItem();
            System.out.println(sourceName);
            grid.changeAlgorithm(sourceName);
            grid.requestFocusInWindow();
        }

    }

    /*
        Sliders
     */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        String sliderName = source.getName();

        if (sliderName.equals("speedSlider")) {
            int speed = controlPanel.getSpeedValue();
            grid.setGridSpeed(speed);
        }

        if (!source.getValueIsAdjusting() && sliderName.equals("sizeSlider")) {
            int size = controlPanel.getSizeValue();
            grid.setGridSize(size);
        }
        grid.requestFocusInWindow();
    }
}
