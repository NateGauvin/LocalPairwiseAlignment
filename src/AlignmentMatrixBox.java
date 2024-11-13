public class AlignmentMatrixBox {
    //initializing the default score to the smallest possible value ensure the highest score is picked over say, 0 or -1
    //this had more of a purpose back when the algorithm was global
    private int topScore = Integer.MIN_VALUE, diagonalScore = Integer.MIN_VALUE, sideScore = Integer.MIN_VALUE;
    private int maxScore;
    private char sequence1AminoAcid, sequence2AminoAcid;
    private boolean fromTop = false, fromDiagonal = false, fromSide = false;
    AlignmentMatrixBox(){}
    AlignmentMatrixBox(int baseBoxScore) {
        //This initializes the 0,0 box so that the algorithm doesn't attempt to pull characters from sequences that don't exist
        maxScore = baseBoxScore;
        sequence1AminoAcid = '#';
        sequence2AminoAcid = '#';
    }
    public void calculateScore() {
        if (topScore < 0 && diagonalScore < 0 && sideScore < 0) {
            maxScore = 0;
        }
        else if (topScore >= diagonalScore && topScore >= sideScore) {
            maxScore = topScore;
            fromTop = true;
        }
        else if (diagonalScore >= topScore && diagonalScore >= sideScore) {
            maxScore = diagonalScore;
            fromDiagonal = true;
        }
        else{
            maxScore = sideScore;
            fromSide = true;
        }
    }
    //getters and setters, self-explanatory
    public int getMaxScore() {
        return maxScore;
    }
    public void setTopScore(int score) {
        topScore = score;
    }
    public void setDiagonalScore(int score) {
        diagonalScore = score;
    }
    public void setSideScore(int score) {
        sideScore = score;
    }
    public void setSequence1AminoAcid(char aminoAcid) {
        sequence1AminoAcid = aminoAcid;
    }
    public void setSequence2AminoAcid(char aminoAcid) {
        sequence2AminoAcid = aminoAcid;
    }
    public char getSequence1AminoAcid() {
        return sequence1AminoAcid;
    }
    public char getSequence2AminoAcid() {
        return sequence2AminoAcid;
    }
    //every isFrom method simply points to another box, either up, left, or diagonally
    public boolean isFromDiagonal() {
        return fromDiagonal;
    }
    public boolean isFromSide() {
        return fromSide;
    }
    public boolean isFromTop() {
        return fromTop;
    }
    //notFinalBox will simply return false once a box with no reference is found, ie the box with a 0 score and the start of alignment
    public boolean notFinalBox() {
        return isFromTop() || isFromSide() || isFromDiagonal();
    }
}