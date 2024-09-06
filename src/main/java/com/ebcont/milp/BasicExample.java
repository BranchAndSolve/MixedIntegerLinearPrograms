package com.ebcont.milp;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import static com.google.ortools.linearsolver.MPSolver.OptimizationProblemType.CLP_LINEAR_PROGRAMMING;

public class BasicExample {

    public static void main(String... args) {

        Loader.loadNativeLibraries();

        // MP = mathematical programming
        // CBC = Coin-or branch and cut
        MPSolver problem = new MPSolver("datingSolver", CLP_LINEAR_PROGRAMMING);

        MPVariable x1 = problem.makeNumVar(0.0, Double.POSITIVE_INFINITY, "x1");
        MPVariable x2 = problem.makeNumVar(0.0, Double.POSITIVE_INFINITY, "x2");

        MPConstraint ct1 = problem.makeConstraint(0, 7.5);
        ct1.setCoefficient(x1, 1.5);
        ct1.setCoefficient(x2, 1);

        MPConstraint ct2 = problem.makeConstraint(0, 3.5);
        ct2.setCoefficient(x1, 0.5);
        ct2.setCoefficient(x2, 1);

        MPObjective objective = problem.objective();
        objective.setCoefficient(x1, 1);
        objective.setCoefficient(x2, 1.1);

        objective.setMaximization();

        problem.solve();

        System.out.println("Objective value = " + objective.value());
        System.out.println("x = " + x1.solutionValue());
        System.out.println("y = " + x2.solutionValue());
    }
}