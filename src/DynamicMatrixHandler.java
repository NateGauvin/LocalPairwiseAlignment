public class DynamicMatrixHandler {
    private final AlignmentMatrixBox[][] dynamicMatrix;
    private final String sequence1;
    private final String sequence2;
    private final int matchScore;
    private final int mismatchScore;
    private final int gapPenalty;
    DynamicMatrixHandler(String string1, String string2, int match, int mismatch, int gap) {
        sequence1 = string1;
        sequence2 = string2;
        matchScore = match;
        mismatchScore = mismatch;
        gapPenalty = gap;
        //creates a 2d array of AlignmentMatrixBox objects with the length of sequence 1's String + 1
        //and a height of sequence 2's String + 1 to account for the 0,0 square having no amino acids
        dynamicMatrix = new AlignmentMatrixBox[sequence2.length() + 1][sequence1.length() + 1];
        for (int i = 0; i <= sequence2.length(); i++) {
            for (int j = 0; j <= sequence1.length(); j++) {
                dynamicMatrix[i][j] = new AlignmentMatrixBox();
            }
        }
        dynamicMatrix[0][0] = new AlignmentMatrixBox(0);
    }
    public void calculateAllScores() {
        //runs calculateThisBox for every box in the 2d array
        for (int i = 0; i <= sequence2.length(); i++) {
            for (int j = 0; j <= sequence1.length(); j++) {
                calculateThisBox(i,j);
            }
        }
    }
    public String[] traceback() {
        int currMax = 0;
        int maxRow = 0, maxCol = 0;
        String alignment1 = "", alignment2 = "";
        //finds the highest score in the 2d array and collects its row and column
        for (int i = 0; i <= sequence2.length(); i++) {
            for (int j = 0; j <= sequence1.length(); j++) {
                if (dynamicMatrix[i][j].getMaxScore() > currMax) {
                    currMax = dynamicMatrix[i][j].getMaxScore();
                    maxRow = i;
                    maxCol = j;
                }
            }
        }
        //will run until the traceback hits a box that contains no reference to another box
        //this box will also be the 0 box, ending the alignment
        //if fromDirection, go in that direction
        while (dynamicMatrix[maxRow][maxCol].notFinalBox()) {
            alignment1 = dynamicMatrix[maxRow][maxCol].getSequence1AminoAcid() + alignment1;
            alignment2 = dynamicMatrix[maxRow][maxCol].getSequence2AminoAcid() + alignment2;
            if (dynamicMatrix[maxRow][maxCol].isFromTop()) {maxRow -= 1;}
            else if (dynamicMatrix[maxRow][maxCol].isFromSide()) {maxCol -= 1;}
            else {
                maxRow -=1;
                maxCol -=1;
            }
        }
        //returns the two alignments together in a String array
        String[] finalAlignment = new String[2];
        finalAlignment[0] = alignment1;
        finalAlignment[1] = alignment2;
        return finalAlignment;
    }
    public int getAlignmentScore() {
        //this method finds the highest score in the 2d array
        int currMax = 0;
        for (int i = 0; i <= sequence2.length(); i++) {
            for (int j = 0; j <= sequence1.length(); j++) {
                if (dynamicMatrix[i][j].getMaxScore() > currMax) {
                    currMax = dynamicMatrix[i][j].getMaxScore();
                }
            }
        }
        return currMax;
    }
    private void calculateThisBox(int rowIndex, int colIndex) {
        //while this section does assign gap penalties to boxes, these penalties are not used in calculation
        //the penalties and any method that leads to a score less than 0 comes from global alignment (no longer in use)
        //however it does still work for local alignments and I do not want to break it, so here it is.
        //initializes 0,0 box
        if (rowIndex == 0 && colIndex == 0) {return;}
        if (rowIndex == 0) {
            dynamicMatrix[rowIndex][colIndex].setSideScore(dynamicMatrix[rowIndex][colIndex-1].getMaxScore() - gapPenalty);
            dynamicMatrix[rowIndex][colIndex].calculateScore();
            //administers the gap penalty for top row
            dynamicMatrix[rowIndex][colIndex].setSequence1AminoAcid(sequence1.charAt(colIndex-1));
            dynamicMatrix[rowIndex][colIndex].setSequence2AminoAcid('-');
            //spreads each character of sequence 1 across the top row
            //the top row only needs the sideScore value
        }
        else if (colIndex == 0) {
            dynamicMatrix[rowIndex][colIndex].setTopScore(dynamicMatrix[rowIndex-1][colIndex].getMaxScore() - gapPenalty);
            dynamicMatrix[rowIndex][colIndex].calculateScore();
            //administers the gap penalty for the leftmost column
            dynamicMatrix[rowIndex][colIndex].setSequence1AminoAcid('-');
            dynamicMatrix[rowIndex][colIndex].setSequence2AminoAcid(sequence2.charAt(rowIndex-1));
            //spreads each character of sequence 2 down the leftmost column
            //the leftmost column only needs the topScore value
        }
        else {
            dynamicMatrix[rowIndex][colIndex].setSideScore(dynamicMatrix[rowIndex][colIndex-1].getMaxScore() - gapPenalty);
            dynamicMatrix[rowIndex][colIndex].setTopScore(dynamicMatrix[rowIndex-1][colIndex].getMaxScore() - gapPenalty);
            //adding gap score to topScore and sideScore
            if (sequence2.charAt(rowIndex-1) == sequence1.charAt(colIndex-1)) {
                dynamicMatrix[rowIndex][colIndex].setDiagonalScore(dynamicMatrix[rowIndex-1][colIndex-1].getMaxScore() + matchScore);
                //adding match score to diagonalScore, if applicable
            }
            else {
                dynamicMatrix[rowIndex][colIndex].setDiagonalScore(dynamicMatrix[rowIndex-1][colIndex-1].getMaxScore() - mismatchScore);
                //adding mismatch score to diagonalScore, if applicable
            }
            dynamicMatrix[rowIndex][colIndex].calculateScore();
            if (dynamicMatrix[rowIndex][colIndex].isFromSide()) {
                dynamicMatrix[rowIndex][colIndex].setSequence1AminoAcid(sequence1.charAt(colIndex-1));
                dynamicMatrix[rowIndex][colIndex].setSequence2AminoAcid('-');
                //assigns the amino acid characters based on horizontal gap
            }
            else if (dynamicMatrix[rowIndex][colIndex].isFromTop()) {
                dynamicMatrix[rowIndex][colIndex].setSequence1AminoAcid('-');
                dynamicMatrix[rowIndex][colIndex].setSequence2AminoAcid(sequence2.charAt(rowIndex-1));
                //assigns the amino acid characters based on vertical gap
            }
            else {
                dynamicMatrix[rowIndex][colIndex].setSequence1AminoAcid(sequence1.charAt(colIndex-1));
                dynamicMatrix[rowIndex][colIndex].setSequence2AminoAcid(sequence2.charAt(rowIndex-1));
                //assigns the amino acid characters based on diagonal match or mismatch
            }
        }
    }
}