package com.example.demo29;

public class Sudoku {
    public static boolean isSafeCellInGrid(int[][] grid, int row, int col, int num) {
        //this method checks if the value in the cell DOESN'T exist in the same row, column and box

        //here we check rows and columns
        for (int i = 0; i < grid.length; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;//we found the number in row/column, cell is not safe
            }
        }
        //check the 3*3 boxes
        int rowOfBox = row - row % 3;
        int colOfBox = col - col % 3;

        //here we loop through the 3*3 box to see if this number exists in it
        for (int r = rowOfBox; r < rowOfBox + 3; r++) {
            for (int c = colOfBox; c < colOfBox + 3; c++) {
                if (grid[r][c] == num) {
                    return false; //we found the number in this box , cell is not safe
                }
            }
        }
        return true;//the cell is SAFE
    }

    public static void printGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {

                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }


    //BACKTRACKING method
    public static boolean getSudokuSolution(int[][] grid) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid.length; c++) {
                if (grid[r][c] == 0) {//if we find an empty cell

                    System.out.println("found empty cell at " + r + "," + c);
                    for (int x = 1; x <= 9; x++) {
                        if (isSafeCellInGrid(grid, r, c, x)) { //check if this number
                            //we want to assign makes the grid safe or not
                            grid[r][c] = x; //if yes assign it
                            System.out.println("grid after assigning : ");
                            printGrid(grid);

                            if (getSudokuSolution(grid)) {
                                return true; //all cells are filled with safe values

                            } else {
                                grid[r][c] = 0;
                                System.out.println("backtrack");
                                printGrid(grid);
                                //BACKTRACK
                                //*all numbers did not give a solution*
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true; //there is NO UNASSIGNED location
    }
}

