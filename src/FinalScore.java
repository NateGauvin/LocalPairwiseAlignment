import javax.swing.*;

public class FinalScore {
    private JPanel panel1;
    private JLabel sequence1Field;
    private JLabel scoreField;
    private JLabel sequence2Field;

    FinalScore(String[] sequences, int score) {
        JFrame finalScore = new JFrame("Alignment with Score");
        finalScore.add(panel1);
        sequence1Field.setText(sequences[0]);
        sequence2Field.setText(sequences[1]);
        scoreField.setText(String.valueOf(score));
        finalScore.setSize(400,200);
        finalScore.setLocationRelativeTo(null);
        finalScore.setVisible(true);
        finalScore.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
