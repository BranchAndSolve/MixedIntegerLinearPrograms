package com.ebcont.milp.sudoku;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;

import static com.google.ortools.linearsolver.MPSolver.OptimizationProblemType.BOP_INTEGER_PROGRAMMING;

public class Sudoku {

    // Define the size of the Sudoku board
    static final int NUMBERS = 9;
    static final int SIZE = NUMBERS;
    static final int BOX_SIZE = (int) Math.round(Math.sqrt(SIZE));

    // Initial Sudoku grid (0 indicates an empty cell)
    static final int[][] sudoku = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    public static void main(String[] args) {
        Loader.loadNativeLibraries();

        // Initialize the solver using Boolean Optimization Problem (BOP) solver
        MPSolver solver = new MPSolver("SudokuSolver", BOP_INTEGER_PROGRAMMING);

        Cell[][] grid = new Cell[SIZE][SIZE];

        // Initialize the grid with binary variables for each cell
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                // TODO: set the decision variables for the cell
                grid[row][column] = new Cell(solver.makeBoolVarArray(NUMBERS));
            }
        }

        // 1. Constraint: Each cell must contain exactly one number
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                // TODO: initialize the constraint for that cell
                MPConstraint constraint = solver.makeConstraint(1, 1);
                for (int number = 1; number <= NUMBERS; number++) {
                    // TODO: add the number variable for the cell to the constraint
                    constraint.setCoefficient(grid[row][column].getVariable(number), 1);
                }
            }
        }

        // 2. Constraint: Each number must appear exactly once in each row
        for (int number = 1; number <= NUMBERS; number++) {
            for (int row = 0; row < SIZE; row++) {
                // TODO: initialize the constraint for the row
                MPConstraint constraint = solver.makeConstraint(1, 1);
                for (int column = 0; column < SIZE; column++) {
                    // TODO: add the number variable for the cell to the constraint
                    constraint.setCoefficient(grid[row][column].getVariable(number), 1);
                }
            }
        }

        // 3. Constraint: Each number must appear exactly once in each column
        for (int number = 1; number <= NUMBERS; number++) {
            for (int column = 0; column < SIZE; column++) {
                // TODO: initialize the constraint for the column
                MPConstraint constraint = solver.makeConstraint(1, 1);
                for (int row = 0; row < SIZE; row++) {
                    // TODO: add the number variable for the cell to the constraint
                    constraint.setCoefficient(grid[row][column].getVariable(number), 1);
                }
            }
        }

        // 4. Constraint: Each number must appear exactly once in each 3x3 sub-grid
        for (int number = 1; number <= NUMBERS; number++) {
            for (int blockStartRow = 0; blockStartRow < SIZE; blockStartRow += BOX_SIZE) {
                for (int blockStartColumn = 0; blockStartColumn < SIZE; blockStartColumn += BOX_SIZE) {
                    // TODO: initialize the constraint for the column
                    MPConstraint constraint = solver.makeConstraint(1, 1);
                    for (int row = blockStartRow; row < blockStartRow + BOX_SIZE; row++) {
                        for (int column = blockStartColumn; column < blockStartColumn + BOX_SIZE; column++) {
                            // TODO: add the number variable for the cell to the constraint
                            constraint.setCoefficient(grid[row][column].getVariable(number), 1);
                        }
                    }
                }
            }
        }

        // Add constraints for pre-filled cells
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (sudoku[row][column] != 0) {
                    // Set the number in pre-filled cells
                    int number = sudoku[row][column];
                    // TODO: fix the number of that cell
                    grid[row][column].getVariable(number).setLb(1);
                }
            }
        }

        final MPSolver.ResultStatus resultStatus = solver.solve();
        System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Sudoku solved successfully!");
            printSolution(grid);
        } else {
            System.out.println("The solver could not find an optimal solution.");
        }
    }

    public static void printSolution(Cell[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            if (row % BOX_SIZE == 0 && row != 0) {
                System.out.println("------+-------+------");
            }
            for (int column = 0; column < SIZE; column++) {
                if (column % BOX_SIZE == 0 && column != 0) {
                    System.out.print("| ");
                }
                System.out.print(grid[row][column].getSolutionValue() + " ");
            }
            System.out.println();
        }
    }
}
