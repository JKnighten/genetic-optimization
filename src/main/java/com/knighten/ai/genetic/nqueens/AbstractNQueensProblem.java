package com.knighten.ai.genetic.nqueens;

import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;

public abstract class AbstractNQueensProblem implements IGenOptimizeProblem<NQueensIndividual> {

    /**
     * Returns the number of conflicts on the board.
     *
     * @param individual the individual whose board is to be evaluated
     * @return the number on conflicts on the board
     */
    public int conflictScore(NQueensIndividual individual) {
        Integer[] board = individual.getGenes();
        int score = 0;

        for (int queen = 0; queen < board.length; queen++) {
            // Iterate Over Every Queen To The Right Of The Queen Currently Being Evaluated
            for (int remainingQueen = queen + 1; remainingQueen < board.length; remainingQueen++) {

                // Row Conflict
                if (board[queen].compareTo(board[remainingQueen]) == 0) {
                    score++;
                    continue;
                }

                // Diagonal(Upper or Lower) Conflict
                if (Math.abs(board[queen] - board[remainingQueen]) == Math.abs(queen - remainingQueen)) {
                    score++;
                    continue;
                }
            }
        }

        return score;
    }

}
