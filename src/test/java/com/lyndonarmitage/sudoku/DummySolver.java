package com.lyndonarmitage.sudoku;

/**
 * Created by Lyndon on 08/05/2015.
 */
public class DummySolver implements SudokuSolver{
    public void solve(Sudoku sudoku) throws SudokuException {
        for (int x = 0; x < Sudoku.GRID_SIZE; x ++) {
            for (int y = 0; y < Sudoku.GRID_SIZE; y ++) {
                sudoku.setAbsolute(x, y, 1);
            }
        }
    }
}
