import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class AlignmentScore {
    private JPanel panel1;
    private JButton determineAlignmentButton;
    private JComboBox matchBox;
    private JComboBox mismatchBox;
    private JComboBox penaltyBox;
    private JLabel matchScoreText;
    private JLabel mismatchPenaltyText;
    private JLabel gapPenaltyText;
    private JTextField sequence1;
    private JTextField sequence2;
    private JLabel sequenceLabel;
    private int matchScore;
    private int mismatchScore;
    private int gapPenalty;
    private boolean validity1;
    private boolean validity2;


    public AlignmentScore() {
        JFrame start = new JFrame("Local Alignment Calculator");
        start.add(panel1);
        start.setSize(400,200);
        start.setVisible(true);
        matchBox.setSelectedIndex(5);
        mismatchBox.setSelectedIndex(1);
        penaltyBox.setSelectedIndex(4);
        start.setLocationRelativeTo(null);
        start.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        determineAlignmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validity1 && validity2) {
                    start.setVisible(false);
                    DynamicMatrixHandler matrix = new DynamicMatrixHandler(sequence1.getText(), sequence2.getText(),
                            Integer.parseInt((String) matchBox.getSelectedItem()),
                            Integer.parseInt((String) mismatchBox.getSelectedItem()),
                            Integer.parseInt((String) penaltyBox.getSelectedItem()));
                    matrix.calculateAllScores();
                    FinalScore finalScore = new FinalScore(matrix.traceback(), matrix.getAlignmentScore());
                }
                else{
                    sequenceLabel.setText("Please only enter valid sequences.");
                }
            }
        });

        sequence1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validity1 = sequence1.getText().matches("[A-Z]+");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validity1 = sequence1.getText().matches("[A-Z]+");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validity1 = sequence1.getText().matches("[A-Z]+");
            }
        });
        sequence2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validity2 = sequence2.getText().matches("[A-Z]+");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validity2 = sequence2.getText().matches("[A-Z]+");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validity2 = sequence2.getText().matches("[A-Z]+");
            }
        });
    }
}
