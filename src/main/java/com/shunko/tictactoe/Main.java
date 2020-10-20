package com.shunko.tictactoe;

import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        String[][] mainMatrix = matrixInit();
        printMatrix(mainMatrix);
        int[] coordinates;
        boolean isGameFinished = true;

        do {
            coordinates = playerNextMove();
            System.out.println("Adapted Coords: " + coordinates[0] + " " + coordinates[1] + " matrix :" + mainMatrix[coordinates[0]][coordinates[1]]);
            if (checkPlayersCoords(mainMatrix, coordinates)) {
                System.out.println("Correct coordinates!");
                mainMatrix = updateMatrix(mainMatrix, coordinates, whatSymbolToUse(mainMatrix));
                printMatrix(mainMatrix);
                if (isBoardFull(mainMatrix)) {
                    System.out.println("Board is full! Game over!");
                    isGameFinished = false;
                }
            } else {
                System.out.println("This cell is ocupied! Choose another one!");
            }
        } while (isGameFinished);


    }

    // public static int checkGameState(String[][] matrix)

    public static boolean isBoardFull(String[][] matrix) {
        int[] coords = new int[2];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                coords[0] = i;
                coords[1] = j;
                if (checkPlayersCoords(matrix, coords)) return false;
            }
        }
        return true;
    }

    public static String checkIfSomebodyWin(String[][] matrix) {
        for (int i=0; i<2; i++) {
            String symbol = matrix[i][0];
            if (matrix[i][1].equals(symbol) && matrix[i][2].equals(symbol)) return symbol;
        }
        for (int j=0; j<2; j++) {
            String symbol = matrix[0][j];
            if (matrix[1][j].equals(symbol) && matrix[2][j].equals(symbol)) return symbol;
        }
        return "-";
    }

    public static String whatSymbolToUse(String[][] matrix) {
        int xCounter = 0;
        int oCounter = 0;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (matrix[i][j].equals("X")) xCounter++;
                if (matrix[i][j].equals("O")) oCounter++;
            }
        }
        if (xCounter == oCounter) return "X";
        else return "O";
    }

    public static String[][] updateMatrix(String[][] matrix, int[] coords, String symbol) {
        matrix[coords[0]][coords[1]] = symbol;
        return matrix;
    }

    public static boolean checkPlayersCoords(String[][] matrix, int[] coords) {
        return matrix[coords[0]][coords[1]].equals(" ");
    }

    public static String[][] matrixInit() {
        String inputString = scanner.nextLine().toUpperCase();
        char[] processedString = inputString.toCharArray();
        String[][] matrix = new String[3][3];
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (processedString[counter++]) {
                    case 'X':
                        matrix[i][j] = "X";
                        break;
                    case 'O':
                        matrix[i][j] = "O";
                        break;
                    case '_':
                        matrix[i][j] = " ";
                        break;
                }
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
            System.out.println(" |");
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
                    correctInput = false;
                else System.out.println("Coordinates should be from 1 to 3!");
            } else System.out.println("You should enter numbers !");
        } while (correctInput);
        return coords;
    }

    public static boolean checkIsInputDigits(String stringToCheck) {
        for (char check : stringToCheck.toCharArray()) {
            if (!(Character.isDigit(check) || check == ' ')) return false;
        }
        return true;
    }

}
