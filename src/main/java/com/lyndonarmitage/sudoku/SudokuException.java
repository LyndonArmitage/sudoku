package com.lyndonarmitage.sudoku;

/**
 * Created by Lyndon on 07/05/2015.
 */
public class SudokuException extends Exception {

    public SudokuException(Sudoku sudoku, String message) {
        super(message + '\n' + sudoku.toString());
    }
}
