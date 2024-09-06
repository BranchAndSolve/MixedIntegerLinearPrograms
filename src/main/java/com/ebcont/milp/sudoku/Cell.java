package com.ebcont.milp.sudoku;

import com.google.ortools.linearsolver.MPVariable;

import java.util.stream.IntStream;

import static com.ebcont.milp.sudoku.Sudoku.NUMBERS;

public class Cell {
    private final MPVariable[] variables; // Array of integer variables representing 1-9

    public Cell(MPVariable[] variables) {
        this.variables = variables;
    }

    public MPVariable[] getVariables() {
        return variables;
    }

    public MPVariable getVariable(int number) {
        return variables[number - 1];
    }

    public int getSolutionValue() {
        return 1 + IntStream.range(0, NUMBERS)
                .filter(i -> variables[i].solutionValue() == 1.0)
                .findFirst()
                .orElse(-1);
    }
}
