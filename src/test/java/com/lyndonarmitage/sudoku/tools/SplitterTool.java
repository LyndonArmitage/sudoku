package com.lyndonarmitage.sudoku.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Splitter tool designed to split the Sudokus text file from Project Euler into separate sudoku text files.
 * See <a href='https://projecteuler.net/problem=96'>https://projecteuler.net/problem=96</a>
 */
public class SplitterTool {
    private static final Logger logger = LoggerFactory.getLogger(SplitterTool.class);

    private static final String beginPattern = "Grid \\d+";
    private static final int lineAfter = 9;
    private static final String filePrefix = "";
    private static final String fileExt = ".sudoku.txt";

    public static void main(String[] args) {
        File file = null;
        File outDir = null;
        if (args.length > 0) {
            file = new File(args[0]);
        } else {
            JFileChooser chooser = new JFileChooser(new File("."));
            chooser.setDialogTitle("Choose Sudoku file");
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
                if (file == null || file.isDirectory()) {
                    return;
                }
            }
        }

        if (file != null) {
            if (args.length >= 2) {
                outDir = new File(args[1]);
            } else {
                outDir = file.getParentFile();
            }
            //noinspection ResultOfMethodCallIgnored
            outDir.mkdirs();

            BufferedReader in = null;
            ArrayList<String> sudokus = new ArrayList<>();
            try {
                in = new BufferedReader(new FileReader(file));
                StringBuilder builder = null;
                String line = null;
                boolean begun = false;
                int count = 0;
                while ((line = in.readLine()) != null) {
                    if (!begun) {
                        if (line.matches(beginPattern)) {
                            begun = true;
                            count = 0;
                            builder = new StringBuilder((9 * 9) + 8);
                        }
                    } else {
                        count++;
                        if (count > lineAfter) {
                            begun = false;
                            sudokus.add(builder.toString());
                        } else {
                            if (count > 1) {
                                builder.append('\n');
                            }
                            builder.append(line);
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                logger.error("File not found", e);
            } catch (IOException e) {
                logger.error("Exception when parsing file", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        logger.error("Exception when closing file output", e);
                    }
                }
            }
            for (int i = 0; i < sudokus.size(); i++) {
                String sudoku = sudokus.get(i);
                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new FileWriter(new File(outDir, filePrefix + i + fileExt)));
                    out.write(sudoku);
                } catch (IOException e) {
                    logger.error("Exception when writing file", e);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            logger.error("Exception when closing file output", e);
                        }
                    }
                }
            }
        }
    }
}
