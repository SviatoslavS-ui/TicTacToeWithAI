package com.shunko.tictactoe;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

class Player {
    private boolean ai;
    private String symbol;
    private String level;

    public Player(boolean ai, String symbol, String level) {
        this.ai = ai;
        this.symbol = symbol;
        this.level = level;
    }

    public boolean isAi() {
        return this.ai;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getLevel() {
        return this.level;
    }

}

public class Main {

    private static final Set<String> STRINGS = Set.of("user", "easy");
    private static final Set<String> COMMANDS = Set.of("start", "exit");
    private static Scanner scanner = new Scanner(System.in);
    private static String[][] mainMatrix;

    public static void main(String[] args) {
        String inputString;
        boolean correctInput = true;

        do {
            System.out.print("Input command: ");
            inputString = scanner.nextLine();
            String[] procString = inputString.split(" ");
            if (!procString[0].equals("exit")) {
                if (isCommandCorrect(procString)) mainGameCycle(procString);
                else System.out.println("Bad parameters!");
            } else correctInput = false;
        } while (correctInput);

    }

    public static boolean isCommandCorrect(String[] inputLine) {
        if ((inputLine.length < 3) || !COMMANDS.contains(inputLine[0])) return false;
        return STRINGS.contains(inputLine[1]) && STRINGS.contains(inputLine[2]);
    }

    public static Player playerInit(String player, String symbol) {
        if (player.equals("user")) {
            return new Player(false, symbol, player);
        } else {
            return new Player(true, symbol, player);
        }
    }

    public static void makePlayerMove(Player player) {
        int[] coordinates;
        if (player.isAi()) {
            coordinates = computerNextMove(mainMatrix);
            System.out.println("Making move level " + player.getLevel());
        } else coordinates = playerNextMove();
        updateMatrix(mainMatrix, coordinates, player.getSymbol());
        printMatrix(mainMatrix);
    }

    public static boolean checkIsGameOver(String[][] matrix) {
        try {
            int result = checkGameState(matrix);
            switch (result) {
                case 1:
                    return false;
                case 2:
                    System.out.println("Draw");
                    return true;
                case 3:
                    System.out.println("X wins");
                    return true;
                case 4:
                    System.out.println("O wins");
                    return true;
            }
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static void mainGameCycle(String[] commandLine) {
        Player player1 = playerInit(commandLine[1], "X");
        Player player2 = playerInit(commandLine[2], "O");
        boolean isGameFinished = false;

        mainMatrix = matrixInit();
        printMatrix(mainMatrix);

        while (!isGameFinished) {
            makePlayerMove(player1);
            if (!(isGameFinished = checkIsGameOver(mainMatrix))) {
                makePlayerMove(player2);
                isGameFinished = checkIsGameOver(mainMatrix);
            }
        }
    }

    public static int[] computerNextMove(String[][] mainMatrix) {
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

    public static int checkGameState(String[][] matrix) throws IllegalStateException {
        String result = checkIfAnybodyWin(matrix);
        switch (result) {
            case "-":
                if (isBoardFull(matrix)) return 2;
                else return 1;
            case "X":
                return 3;
            case "O":
                return 4;
            case " ":
                return 1;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }
    }

    public static boolean isBoardFull(String[][] matrix) {
        int[] coords = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                coords[0] = i;
                coords[1] = j;
                if (checkPlayersCoords(matrix, coords)) return false;
            }
        }
        return true;
    }

    public static String checkIfAnybodyWin(String[][] matrix) {
        String symbol;
        int i;
        for (i = 0; i < 3; i++) {
            symbol = matrix[i][0];
            if (checkSymbolStatus(matrix[i][1], matrix[i][2], symbol))
                return symbol;
        }
        for (int j = 0; j < 3; j++) {
            symbol = matrix[0][j];
            if (checkSymbolStatus(matrix[1][j], matrix[2][j], symbol))
                return symbol;
        }
        symbol = matrix[0][0];
        if (checkSymbolStatus(matrix[1][1], matrix[2][2], symbol))
            return symbol;
        symbol = matrix[0][2];
        if (checkSymbolStatus(matrix[1][1], matrix[2][0], symbol))
            return symbol;
        return "-";
    }

    public static boolean checkSymbolStatus(String position1, String position2, String symbol) {
        return position1.equals(symbol) && position2.equals(symbol) && !Objects.equals(symbol, " ");
    }

    public static void updateMatrix(String[][] matrix, int[] coords, String symbol) {
        matrix[coords[0]][coords[1]] = symbol;
    }

    public static boolean checkPlayersCoords(String[][] matrix, int[] coords) {
        return matrix[coords[0]][coords[1]].equals(" ");
    }

    public static String[][] matrixInit() {
        String[][] matrix = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = " ";
            }
        }
        return matrix;
    }

    public static void printMatrix(String[][] matrix) {
        System.out.println("---------");
        for (String[] strings : matrix) {
            System.out.print("| ");
            for (String element : strings) {
                System.out.print(element + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }


    public static int[] playerNextMove() {
        int[] coords = new int[2];
        String inputString;
        boolean correctInput = true;
        do {
            System.out.print("Enter the coordinates: ");
            inputString = scanner.nextLine();
            if (checkIsInputDigits(inputString)) {
                String[] processedString = inputString.split(" ");
                coords[0] = 3 - Integer.parseInt(processedString[1]);
                coords[1] = Integer.parseInt(processedString[0]) - 1;
                if (coords[0] <= 2 && coords[0] >= 0 && coords[1] <= 2 && coords[1] >= 0)
                    if (checkPlayersCoords(mainMatrix, coords)) correctInput = false;
                    else System.out.println("This cell is occupied! Choose another one!");
                else System.out.println("Coordinates should be from 1 to 3!");
            } else System.out.println("You should enter numbers!");
        } while (correctInput);
        return coords;
    }

    public static boolean checkIsInputDigits(String stringToCheck) {
        if (stringToCheck != null && !stringToCheck.isEmpty()) {
            for (char check : stringToCheck.toCharArray()) {
                if (!(Character.isDigit(check) || check == ' ')) return false;
            }
        } else return false;
        return true;
    }

}

