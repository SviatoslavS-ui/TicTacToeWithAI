package com.shunko.tictactoe;

import java.util.Random;
import static com.shunko.tictactoe.Main.checkPlayersCoords;

public class Player {
    protected boolean ai;
    protected String symbol;
    protected String level;

    public Player(boolean ai, String symbol) {
        this.ai = ai;
        this.symbol = symbol;
        this.level = "user";
    }

    public boolean isAi() {
        return this.ai;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getLevel() {
        return level;
    }

    public int[] makeMove(String[][] mainMatrix) {
        return new int[]{0, 0};
    }
}

class EasyAiPlayer extends Player {

    public EasyAiPlayer(boolean ai, String symbol, String level) {
        super(ai, symbol);
        this.level = level;
    }

    @Override
    public int[] makeMove(String[][] mainMatrix) {
        Random random = new Random();
        int[] coords = new int[2];
        boolean isCorrectCoords = true;
        do {
            coords[0] = random.nextInt(3);
            coords[1] = random.nextInt(3);
            if (checkPlayersCoords(mainMatrix, coords)) isCorrectCoords = false;
        } while (isCorrectCoords);
        return coords;
    }
}

class MediumAiPlayer extends EasyAiPlayer {

    public MediumAiPlayer(boolean ai, String symbol, String level) {
        super(ai, symbol, level);
    }

    @Override
    public int[] makeMove(String[][] mainMatrix) {
        String symbol = this.getSymbol();
        int[] coords = recommendedNextMove(mainMatrix, symbol);
        if (coords == null) {
            coords = recommendedNextMove(mainMatrix, invertSymbol(symbol));
            if (coords == null) return super.makeMove(mainMatrix);
            else return coords;
        } else return coords;
    }

    private int[] recommendedNextMove(String[][] matrix, String symbol) {
        String[] tempLine = new String[3];
        int[] coords = new int[2];
        int tempValue;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tempLine[j] = matrix[j][i];
            }
            if (!((tempValue = isNearWinLine(tempLine, symbol)) == 5)) {
                coords[0] = tempValue;
                coords[1] = i;
                return coords;
            }
        }
        for (int i = 0; i < 3; i++) {
            tempLine = matrix[i];
            if (!((tempValue = isNearWinLine(tempLine, symbol)) == 5)) {
                coords[0] = i;
                coords[1] = tempValue;
                return coords;
            }
        }
        tempLine = new String[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) tempLine[i] = matrix[i][j];
            }
        }
        if (!((tempValue = isNearWinLine(tempLine, symbol)) == 5)) {
            coords[0] = tempValue;
            coords[1] = coords[0];
            return coords;
        }
        tempLine = new String[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == (2 - j)) tempLine[i] = matrix[i][j];
            }
        }
        if (!((tempValue = isNearWinLine(tempLine, symbol)) == 5)) {
            coords[0] = tempValue;
            coords[1] = 2 - tempValue;
            return coords;
        }
        return null;
    }

    private int isNearWinLine(String[] matrixLine, String symbol) {
        if (matrixLine[1].equals(symbol) && matrixLine[2].equals(symbol) && matrixLine[0].equals(" ")) return 0;
        if (matrixLine[0].equals(symbol) && matrixLine[2].equals(symbol) && matrixLine[1].equals(" ")) return 1;
        if (matrixLine[0].equals(symbol) && matrixLine[1].equals(symbol) && matrixLine[2].equals(" ")) return 2;
        else return 5;
    }

    private String invertSymbol(String symbol) {
        if (symbol.equals("X")) return "O";
        else return "X";
    }
}
