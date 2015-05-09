package com.lyndonarmitage.sudoku.solvers;

import com.lyndonarmitage.sudoku.Sudoku;
import com.lyndonarmitage.sudoku.SudokuSolver;
import com.lyndonarmitage.sudoku.SudokuTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Lyndon on 09/05/2015.
 */
public class LogicSolverTest {

    @Test
    public void testSolve() throws Exception {
        Sudoku sudoku = new Sudoku(SudokuTest.testArray);
        Sudoku expectedSudoku = new Sudoku(SudokuTest.testArrayCompleted);
        SudokuSolver solver = new LogicSolver();
        sudoku.solve(solver);
        assertTrue("Failed to solve test sudoku", sudoku.isValid());
        assertEquals("Did not match expected output", expectedSudoku.toString(), sudoku.toString());
    }
}