package com.knighten.ai.genetic.nqueens;

/**
 * Helper methods for the n queens problem.
 */
public class NQueensHelper {

    /**
     * Returns the number of conflicts on the board.
     *
     * @param board the board to be evaluated
     * @return the number on conflicts on the board
     */
    public static int conflictScore(Integer[] board) {
        int score = 0;

        for(int queen = 0; queen < board.length; queen++){

            // Iterate Over Every Queen To The Right Of The Queen Currently Being Evaluated
            for(int remainingQueen=queen+1; remainingQueen<board.length; remainingQueen++){

                // Row Conflict
                if(board[queen] == board[remainingQueen]){
                    score++;
                    continue;
                }

                // Diagonal(Upper or Lower) Conflict
                if(Math.abs(board[queen]-board[remainingQueen]) == Math.abs(queen-remainingQueen)) {
                    score++;
                    continue;
                }

            }

        }

        return  score;
    }
}
