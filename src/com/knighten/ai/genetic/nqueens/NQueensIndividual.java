package com.knighten.ai.genetic.nqueens;

import com.knighten.ai.genetic.Individual;

public class NQueensIndividual extends Individual<Integer[]> {

    public NQueensIndividual(Integer[] board){
        this.setValue(board);
    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();
        Integer[] board = this.getValue();

        // Iterate Over Every Row And Column (Prints Row By Row)
        for(int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {

                // Print Q For Queen And * For Empty Space
                String spaceValue = board[col] == row ? "Q " : "* ";
                sb.append(spaceValue);
            }

            // Start New Row
            sb.append("\n");
        }

        return sb.toString();
    }
}