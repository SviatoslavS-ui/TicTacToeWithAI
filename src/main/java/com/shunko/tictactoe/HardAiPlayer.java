package com.shunko.tictactoe;

import static com.shunko.tictactoe.Main.checkIfAnybodyWin;
import static com.shunko.tictactoe.Main.isBoardFull;

public class HardAiPlayer extends Player {

    static class Move {
        int col;
        int row;
    }

    public HardAiPlayer(boolean ai, String symbol, String level) {
        super(ai, symbol);
        this.level = level;
    }

    private String player = this.getSymbol();
    private String opponent = invertSymbol(player);

    @Override
    public int[] makeMove(String[][] mainMatrix) {
        Move bestMove = findBestMove(mainMatrix);
        if ((bestMove.col != -1) && (bestMove.row != -1)) return convertMoveToCoords(bestMove);
        else return super.makeMove(mainMatrix);
    }

    private int[] convertMoveToCoords(Move bestMove) {
        int[] bestCoords = new int[2];
        bestCoords[0] = bestMove.row;
        bestCoords[1] = bestMove.col;
        return bestCoords;
    }

    private int evaluate(String[][] matrix) {
        if (checkIfAnybodyWin(matrix).equals(player)) return +10;
        else if (checkIfAnybodyWin(matrix).equals(opponent)) return -10;
        return 0;
    }

    private int minimax(String[][] board, int depth, Boolean isMax) {
        int score = evaluate(board);
        if (score == 10 || score == -10) return score;
        if (isBoardFull(board) && score == 0) return 0;
        if (isMax) {
            int best = -1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].equals(" ")) {
                        board[i][j] = player;
                        // Call minimax recursively and choose the maximum value
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = " ";
                    }
                }
            }
            return best;
        } else {
            int best = 1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].equals(" ")) {
                        board[i][j] = opponent;
                        // Call minimax recursively and choose the minimum value
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[i][j] = " ";
                    }
                }
            }
            return best;
        }
    }

    private Move findBestMove(String[][] board) {
        int bestVal = -1000;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;
        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals(" ")) {
                    board[i][j] = player;
                    // compute evaluation function for this move.
                    int moveVal = minimax(board, 0, false);
                    board[i][j] = " ";
                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal) {
                        bestMove.row = i;
                        bestMove.col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }

    private String invertSymbol(String symbol) {
        if (symbol.equals("X")) return "O";
        else return "X";
    }
}
