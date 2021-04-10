/**
 * An exception that will be thrown when parsing ill-formated sudoku grids
 */
public class SudokuFormatException extends Exception {
    
    private static final long serialVersionUID = 1456L;

    public SudokuFormatException() {
        super();
    }

    public SudokuFormatException(String s) {
        super(s);
    }
    
}
