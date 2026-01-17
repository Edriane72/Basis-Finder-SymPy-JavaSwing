import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class VectorBasisGUI extends JFrame {

    JPanel vectorPanel;
    JScrollPane vectorScroll;

    JTextArea outputArea;
    JScrollPane outputScroll;

    JButton addVectorBTN, removeVectorBTN;
    JButton addRowBTN, removeRowBTN;
    JButton resetBTN, runBTN;

    int dimensions = 2; // default starting rows

    ArrayList<ArrayList<JTextField>> vectors = new ArrayList<>();

    public VectorBasisGUI() {
        setTitle("Vector Basis Finder");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // vector panel
        vectorPanel = new JPanel();
        vectorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        vectorPanel.setBorder(BorderFactory.createTitledBorder("Vectors"));

        vectorScroll = new JScrollPane(vectorPanel);
        vectorScroll.setBounds(30, 20, 670, 220);
        add(vectorScroll);

        addVector();
        addVector();

        // button
        addRowBTN = new JButton("+ Add Row");
        removeRowBTN = new JButton("- Remove Row");
        addVectorBTN = new JButton("+ Add Vector");
        removeVectorBTN = new JButton("- Remove Vector");
        resetBTN = new JButton("Reset");
        runBTN = new JButton("Find Basis");

        addRowBTN.setBounds(30, 260, 130, 30);
        removeRowBTN.setBounds(170, 260, 150, 30);
        addVectorBTN.setBounds(330, 260, 150, 30);
        removeVectorBTN.setBounds(490, 260, 170, 30);

        resetBTN.setBounds(200, 310, 120, 35);
        runBTN.setBounds(360, 310, 120, 35);

        add(addRowBTN);
        add(removeRowBTN);
        add(addVectorBTN);
        add(removeVectorBTN);
        add(resetBTN);
        add(runBTN);

        // output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Result"));
        outputScroll.setBounds(30, 360, 670, 170);
        add(outputScroll);

        // actions
        addVectorBTN.addActionListener(e -> addVector());

        removeVectorBTN.addActionListener(e -> {
            if (vectors.size() > 1) {
                vectors.remove(vectors.size() - 1);
                vectorPanel.remove(vectorPanel.getComponentCount() - 1);
                vectorPanel.revalidate();
                vectorPanel.repaint();
            }
        });

        addRowBTN.addActionListener(e -> addRow());

        removeRowBTN.addActionListener(e -> {
            if (dimensions > 1) {
                dimensions--;
                for (int i = 0; i < vectors.size(); i++) {
                    vectors.get(i).remove(vectors.get(i).size() - 1);
                    JPanel column = (JPanel) vectorPanel.getComponent(i);
                    column.remove(column.getComponentCount() - 1);
                    column.remove(column.getComponentCount() - 1);
                }
                vectorPanel.revalidate();
                vectorPanel.repaint();
            }
        });

        resetBTN.addActionListener(e -> resetAll());
        runBTN.addActionListener(e -> runPython());

        setVisible(true);
    }

    // add vector
    void addVector() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel("Vector " + (vectors.size() + 1));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        column.add(lbl);

        ArrayList<JTextField> vector = new ArrayList<>();

        for (int i = 0; i < dimensions; i++) {
            JTextField tf = createCell();
            vector.add(tf);
            column.add(tf);
            column.add(Box.createVerticalStrut(5));
        }

        vectors.add(vector);
        vectorPanel.add(column);
        vectorPanel.revalidate();
        vectorPanel.repaint();
    }

    // add row
    void addRow() {
        dimensions++;

        for (int i = 0; i < vectors.size(); i++) {
            JTextField tf = createCell();
            vectors.get(i).add(tf);

            JPanel column = (JPanel) vectorPanel.getComponent(i);
            column.add(tf);
            column.add(Box.createVerticalStrut(5));
        }

        vectorPanel.revalidate();
        vectorPanel.repaint();
    }

    // reset
    void resetAll() {
        dimensions = 2;
        vectors.clear();
        vectorPanel.removeAll();
        outputArea.setText("");

        addVector();
        addVector();

        vectorPanel.revalidate();
        vectorPanel.repaint();
    }

    // create individual cells
    JTextField createCell() {
        JTextField tf = new JTextField(3);
        tf.setMaximumSize(new Dimension(50, 25));
        tf.setHorizontalAlignment(JTextField.CENTER);
        return tf;
    }

    // run python
    void runPython() {
        try {
            StringBuilder input = new StringBuilder();

            for (ArrayList<JTextField> vector : vectors) {
                for (JTextField tf : vector) {
                    input.append(tf.getText().trim()).append(",");
                }
                input.setLength(input.length() - 1);
                input.append(";");
            }
            input.setLength(input.length() - 1);

            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    "core/basis_cli.py",
                    input.toString()
            );

            pb.directory(new File(System.getProperty("user.dir")));
            pb.redirectErrorStream(true);

            Process p = pb.start();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream())
            );

            outputArea.setText("");
            String line;
            while ((line = br.readLine()) != null) {
                outputArea.append(line + "\n");
            }

        } catch (Exception ex) {
            outputArea.setText("Error:\n" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new VectorBasisGUI();
    }
}
