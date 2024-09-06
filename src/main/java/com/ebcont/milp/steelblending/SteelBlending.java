/*
 *  Example taken from https://doi.org/10.1287/inte.7.2.39
 */

package com.ebcont.milp.steelblending;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import static com.google.ortools.linearsolver.MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING;

public class SteelBlending {

    public static void main(String[] args) {
        Loader.loadNativeLibraries();

        // Initialize solver using CBC
        MPSolver solver = new MPSolver("SteelBlendingSolver", CBC_MIXED_INTEGER_PROGRAMMING);

        // Parameters
        double targetCarbon = 25 * 0.05; // tons
        double targetMolybdenum = 25 * 0.05; // tons

        // Ingot data
        int totalIngotCount = 4;
        double[] ingotWeights = {5, 3, 4, 6}; // in tons
        double[] ingotCarbon = {0.05, 0.04, 0.05, 0.03}; // percentages
        double[] ingotMolybdenum = {0.03, 0.03, 0.04, 0.04}; // percentages
        double[] ingotCosts = {350, 330, 310, 280}; // $ per ton

        // Alloy data
        int totalAlloyAndScrapCount = 4;
        double[] alloyAndScrapCarbon = {0.08, 0.07, 0.06, 0.03}; // percentages
        double[] alloyAndScrapMolybdenum = {0.06, 0.07, 0.08, 0.09}; // percentages
        double[] alloyAndScrapCosts = {500, 450, 400, 100}; // $ per ton

        // Decision Variables
        // TODO: initialize decision variables for ingots
        MPVariable[] ingots = solver.makeBoolVarArray(totalIngotCount);

        // Mass in tons variables
        // TODO: initialize number variables for alloys and scrap
        MPVariable[] alloys = solver.makeNumVarArray(totalAlloyAndScrapCount, 0, Double.MAX_VALUE);

        // Objective: Minimize cost
        // TODO: initialize objective
        MPObjective objective = solver.objective();
        for (int i = 0; i < totalIngotCount; i++) {
            // TODO: add ingot cost to objective
            objective.setCoefficient(ingots[i], ingotWeights[i] * ingotCosts[i]);
        }
        for (int i = 0; i < totalAlloyAndScrapCount; i++) {
            // TODO: add alloy/scrap cost to objective
            objective.setCoefficient(alloys[i], alloyAndScrapCosts[i]);
        }
        objective.setMinimization();

        // Constraints
        // 1. Total weight constraint
        // TODO: initialize weight constraint
        MPConstraint weightConstraint = solver.makeConstraint(25, 25);
        for (int i = 0; i < totalIngotCount; i++) {
            // TODO: add ingot coefficient to weight constraint
            weightConstraint.setCoefficient(ingots[i], ingotWeights[i]);
        }
        for (int i = 0; i < totalAlloyAndScrapCount; i++) {
            // TODO: add alloy/scrap coefficient to weight constraint
            weightConstraint.setCoefficient(alloys[i], 1);
        }

        // 2. Carbon composition constraint
        // TODO: initialize carbon constraint
        MPConstraint carbonConstraint = solver.makeConstraint(targetCarbon, targetCarbon);
        for (int i = 0; i < totalIngotCount; i++) {
            // TODO: add ingot coefficient to carbon constraint
            carbonConstraint.setCoefficient(ingots[i], ingotWeights[i] * ingotCarbon[i]);
        }
        for (int i = 0; i < totalAlloyAndScrapCount; i++) {
            // TODO: add alloy/scrap coefficient to carbon constraint
            carbonConstraint.setCoefficient(alloys[i], alloyAndScrapCarbon[i]);
        }

        // 3. Molybdenum composition constraint
        // TODO: initialize molybdenum constraint
        MPConstraint molybdenumConstraint = solver.makeConstraint(targetMolybdenum, targetMolybdenum);
        for (int i = 0; i < totalIngotCount; i++) {
            // TODO: add ingot coefficient to molybdenum constraint
            molybdenumConstraint.setCoefficient(ingots[i], ingotWeights[i] * ingotMolybdenum[i]);
        }
        for (int i = 0; i < totalAlloyAndScrapCount; i++) {
            // TODO: add alloy/scrap coefficient to molybdenum constraint
            molybdenumConstraint.setCoefficient(alloys[i], alloyAndScrapMolybdenum[i]);
        }

        solver.solve();

        System.out.println("Objective value (Total Cost) = " + objective.value() + "$.");
        System.out.println("Steel Blend:");
        for (int i = 0; i < totalIngotCount; i++) {
            if (ingots[i].solutionValue() > 0) {
                System.out.printf("Ingot %d: %.2f tons\n", i + 1, ingotWeights[i]);
            }
        }
        for (int i = 0; i < totalAlloyAndScrapCount; i++) {
            if (alloys[i].solutionValue() > 0) {
                System.out.printf("Alloy %d: %.2f tons\n", i + 1, alloys[i].solutionValue());
            }
        }
    }
}
