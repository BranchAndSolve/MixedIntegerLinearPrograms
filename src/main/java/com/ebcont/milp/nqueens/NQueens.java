package com.ebcont.milp.nqueens;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import static com.google.ortools.linearsolver.MPSolver.OptimizationProblemType.BOP_INTEGER_PROGRAMMING;

public class NQueens {

    private final static int N = 8;

    public static void main(String[] args) {
        Loader.loadNativeLibraries();

        // Initialize the solver using Boolean Optimization Problem (BOP) solver
        MPSolver solver = new MPSolver("NQueensSolver", BOP_INTEGER_PROGRAMMING);

        MPVariable[][] board = new MPVariable[N][];

        // Initialize the binary decision variables for each position on the board
        for (int row = 0; row < N; row++) {
            board[row] = solver.makeIntVarArray(N, 0, 1);
        }

        // 1. Constraint: Only one queen per row
        for (int row = 0; row < N; row++) {
            MPConstraint rowConstraint = solver.makeConstraint(1, 1);  // Exactly one queen per row
            for (int column = 0; column < N; column++) {
                rowConstraint.setCoefficient(board[row][column], 1);
            }
        }

        // 2. Constraint: Only one queen per column
        for (int column = 0; column < N; column++) {
            MPConstraint columnConstraint = solver.makeConstraint(1, 1);  // Exactly one queen per column
            for (int row = 0; row < N; row++) {
                columnConstraint.setCoefficient(board[row][column], 1);
            }
        }

        // 3. Constraint: Diagonal constraints (top-left to bottom-right)
        for (int diag = -(N - 1); diag < N; diag++) {
            MPConstraint diagConstraint = solver.makeConstraint(0, 1);  // At most one queen per diagonal
            for (int row = 0; row < N; row++) {
                int column = row - diag;
                if (column >= 0 && column < N) {
                    diagConstraint.setCoefficient(board[row][column], 1);
                }
            }
        }

        // 4. Constraint: Diagonal constraints (top-right to bottom-left)
        for (int diag = 0; diag < 2 * N - 1; diag++) {
            MPConstraint antiDiagConstraint = solver.makeConstraint(0, 1);  // At most one queen per anti-diagonal
            for (int row = 0; row < N; row++) {
                int column = diag - row;
                if (column >= 0 && column < N) {
                    antiDiagConstraint.setCoefficient(board[row][column], 1);
                }
            }
        }

        // Solve the N-Queens problem
        final MPSolver.ResultStatus resultStatus = solver.solve();
        System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");

        // Check if the solution is optimal
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solution found for " + N + "-Queens problem:");
            printSolution(board, N);
        } else {
            System.out.println("No optimal solution found.");
        }
    }

    // Helper method to print the solution in a grid format
    public static void printSolution(MPVariable[][] queens, int N) {
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                if (queens[row][column].solutionValue() == 1.0) {
                    System.out.print("Q ");  // 'Q' for Queen
                } else {
                    System.out.print(". ");  // Empty spot
                }
            }
            System.out.println();  // Newline for the next row
        }
    }
}
