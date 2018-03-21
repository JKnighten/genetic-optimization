package com.knighten.ai.genetic.nqueens;

import com.knighten.ai.genetic.Individual;

/**
 * Represents an individual used for solving the n queens problem using genetic optimization. Each individual stores
 * an Integer[] array representing a chess board filled with queens. Boards are limited to one queen a column to
 * simplify the problem. Thus each index of the array represents a column, and the value stored there represents what
 * row in that column contains a queen.
 */
public class NQueensIndividual extends Individual<Integer[]> {

    /**
     * Creates an NQueensIndividual instance and assigns its value to the supplied board representation.
     *
     * @param board the chess board representation that is assigned to the individual
     */
    NQueensIndividual(Integer[] board) {
        this.setValue(board);
    }

    /**
     * Converts the NQueensIndividual's board into a string. Q will represent queens and * will represent empty board
     * spaces.
     *
     * @return string representation of the NQueensIndividual's board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Integer[] board = this.getValue();

        for(int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++)
                sb.append((board[column] == row) ? "Q " : "* ");
            // Start New Row
            sb.append("\n");
        }

        return sb.toString();
    }
}