package com.knighten.ai.genetic.nqueens;

public class NQueenHelper {

    public static  double conflictScore(Integer[] board){
        // Initialize score to 0
        double score = 0.0;

        // We Iterate Over Every Queen Starting With The Far Left Queen
        for(int queen = 0; queen < board.length; queen++){

            // We Iterate Over Every Queen In Front Of The Queen Currently Being Evaluated
            for(int remainingQueen=queen+1; remainingQueen<board.length; remainingQueen++){

                // Check Row Conflict
                if(board[queen] == board[remainingQueen]){
                    score++;
                    continue;
                }

                // Check Diagonals Conflict
                if(Math.abs(board[queen]-board[remainingQueen]) == Math.abs(queen-remainingQueen)) {
                    score++;
                    continue;
                }

            }


        }

        return  score;

    }
}
