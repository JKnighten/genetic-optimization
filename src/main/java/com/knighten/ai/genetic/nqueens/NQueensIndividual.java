package com.knighten.ai.genetic.nqueens;

import com.knighten.ai.genetic.Individual;

import java.util.stream.IntStream;

/**
 * Represents an individual used for solving the n queens problem using genetic optimization. Each individual genes are
 * an Integer[] array representing a chess board filled with queens. Boards are limited to one queen per column to
 * simplify the problem. Thus each index of the array represents a column containing a queen, and the value stored there
 * represents what row with the queen.
 */
public class NQueensIndividual extends Individual<Integer[]> {

    /**
     * Creates an NQueensIndividual instance and assigns its genes to the supplied board representation.
     *
     * @param board the chess board representation that is assigned to the individual
     */
    public NQueensIndividual(Integer[] board) {

        if(board == null)
            throw new IllegalArgumentException("NQueens Boards Cannot Be Null");

        if(board.length <= 3)
            throw new IllegalArgumentException("NQueens Boards Are Only Solvable When N Is Greater Than 3");

        if(IntStream.range(0, board.length).anyMatch((i) -> board[i] < 0 || board[i] >= board.length))
            throw new IllegalArgumentException("Values On The Board Must Be Between 0 And N-1");

        this.setGenes(board);
    }

    /**
     * Converts the NQueensIndividual's board/genes into a string. Q will represent queens and * will represent empty
     * board spaces.
     *
     * @return string representation of the NQueensIndividual's board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Integer[] board = this.getGenes();

        for(int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++)
                sb.append((board[column] == row) ? "Q " : "* ");

            // Start New Row
            if(row != (board.length-1))
                sb.append("\n");
        }

        return sb.toString();
    }

}