import javax.swing.*;
import java.awt.*;

// Buttons should let me change number of nodes and their size
// Buttons should let me visualize the algorithm
// Buttons should let me step through the algorithm one-by-one
// Buttons should let me reset the graph
// Buttons should let me choose another algorithm
public class FormPanel extends JPanel {
    VisualPanel grid;
    JSlider speedSlider;
    JSlider gridSizeSlider;
    JComboBox dropdown;

    public FormPanel(VisualPanel grid) {
        this.grid = grid;
        initUI();
    }

    public void initUI() {
        ActionHandler actionListen = new ActionHandler(grid, this);

        // Set Layout
        this.setLayout(new GridBagLayout());

        // Layout Constraints
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.NONE;

        // Sliders

        speedSlider = new JSlider(1, 100, 50); // Range 5 to 150, default 50
        speedSlider.setMinorTickSpacing(5);
        speedSlider.setMajorTickSpacing(25);
        speedSlider.setPaintTicks(true);
        speedSlider.setPreferredSize(new Dimension(100, 50));
        JLabel speedLabel = new JLabel("Speed");

        gridSizeSlider = new JSlider(1, 3, 2);
        gridSizeSlider.setMinorTickSpacing(1);
        gridSizeSlider.setMajorTickSpacing(2);
        gridSizeSlider.setPaintTicks(true);
        gridSizeSlider.setPreferredSize(new Dimension(100, 50));
        JLabel sizeLabel = new JLabel("Grid Size");

        speedSlider.setName("speedSlider");
        gridSizeSlider.setName("sizeSlider");

        speedSlider.addChangeListener(actionListen);
        gridSizeSlider.addChangeListener(actionListen);

        // Buttons

        JButton visualizeBtn = new JButton("Visualize!");
        JButton resetBtn = new JButton("Reset");

        visualizeBtn.addActionListener(actionListen);
        resetBtn.addActionListener(actionListen);


        // Dropdown Menu
        String[] names = {"A Star", "BFS"};
        dropdown = new JComboBox(names);
        dropdown.addActionListener(actionListen);

        gc.anchor = GridBagConstraints.LINE_END;
        this.add(resetBtn, gc);

        gc.weightx = 0.1;
        gc.gridx = 2;
        gc.anchor = GridBagConstraints.LINE_END;
        this.add(speedLabel, gc);

        gc.weightx = 0.05;
        gc.gridx = 3;
        gc.anchor = GridBagConstraints.LINE_START;
        this.add(speedSlider, gc);

        gc.weightx = 0.05;
        gc.gridx = 4;
        gc.anchor = GridBagConstraints.LINE_END;
        this.add(sizeLabel, gc);

        gc.weightx = 0.1;
        gc.gridx = 5;
        gc.anchor = GridBagConstraints.LINE_START;
        this.add(gridSizeSlider, gc);

        gc.weightx = 0.1;
        gc.gridx = 6;
        gc.anchor = GridBagConstraints.LINE_START;
        this.add(dropdown);

        gc.weightx = 1;
        gc.gridx = 7;
        this.add(visualizeBtn, gc);
    }

    public int getSpeedValue() {
        return speedSlider.getValue();
    }

    public int getSizeValue() {
        return gridSizeSlider.getValue();
    }

    public JComboBox getComboBox() {
        return dropdown;
    }
}

