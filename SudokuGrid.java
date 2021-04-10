import java.util.HashMap;
import java.util.Vector;

/**
 * An Object that represents a Sudoku grid
 */
public class SudokuGrid implements Cloneable {
    /** The cells of the grid (...) */
    private Cell[][] cells;

    /** A map of cells that have currently no values to their position */
    private HashMap<Cell, Move> notSetCells;

    /** The Grid object for display */
    private Grid g;

    /**
     * Constructs a new empty grid
     */
    public SudokuGrid() {
        cells = new Cell[9][9];
        boolean[][] activated = new boolean[9][9];
        notSetCells = new HashMap<Cell, Move>();

        for (int i = 0; i < 9; i++) 
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new Cell();
                notSetCells.put(cells[i][j], new Move(i, j));
                activated[i][j] = true;
            }
        for (int i = 0; i < 9; i++) 
            for (int j = 0; j < 9; j++) {
                cells[i][j].setRelated(
                    cellsOfLine(i, j),
                    cellsOfColumn(i, j),
                    cellsOfBlock(i, j)
                );
            }

        try {
            // build the Grid object for String representation
            g = new Grid(activated);
            for (int i = 2; i <= 5; i += 3) 
                for (int j = 0; j < 9; j++)
                    g.setWall(i, j, Grid.DOWN, '=');
        
            for (int j = 2; j <= 5; j += 3) 
                for (int i = 0; i < 9; i++)
                    g.setWall(i, j, Grid.RIGHT, '#');
        } catch (GridException e) {
            System.err.println("Should not be printed #1");
        }
    }

    /**
     * Other cells on the same line
     * @param i     The line index of the cell
     * @param j     The column index of the cell
     * @return      An array containing the N-1 other cells in the same line
     */
    private Cell[] cellsOfLine(int i, int j) {
        Cell[] line = new Cell[8];
        int k = 0;
        for (int i2 = 0; i2 < 9; i2++) {
            if (i2 != i) 
                line[k++] = cells[i2][j];
        }
        return line;
    }

    /**
     * Other cells on the same column
     * @param i     The line index of the cell
     * @param j     The column index of the cell
     * @return      An array containing the N-1 other cells in the same column
     */
    private Cell[] cellsOfColumn(int i, int j) {
        Cell[] column = new Cell[8];
        int k = 0;
        for (int j2 = 0; j2 < 9; j2++) {
            if (j2 != j) 
                column[k++] = cells[i][j2];
        }
        return column;
    }

    /**
     * Other cells on the same block
     * @param i     The line index of the cell
     * @param j     The column index of the cell
     * @return      An array containing the N-1 other cells in the same block
     */
    private Cell[] cellsOfBlock(int i, int j) {
        Cell[] block = new Cell[8];
        int a = (i / 3) * 3;
        int b = (j / 3) * 3;
        int k = 0;
        // not cool
        for (int i2 = 0; i2 < 3; i2++) 
            for (int j2 = 0; j2 < 3; j2++) 
                if (a+i2 != i || b+j2 != j) 
                    block[k++] = cells[a+i2][b+j2];
        return block;
    }

    /**
     * Bounds some cell to a new value
     * @param i     The line index of the modified cell
     * @param j     The column index of the modified cell
     * @param v     The new value in the modified cell
     */
    public void bound(int i, int j, int v) {
        try {
            cells[i][j].bound(v);
            if (notSetCells.remove(cells[i][j]) == null)
                System.out.println("Ooops...");
            g.setContent(i, j, Integer.toString(v));
        } catch (GridException e) {
            System.err.println("Should not be printed #2");
        }
    }

    /**
     * Try to bound some cell to a new value
     * @param i     The line index of the modified cell
     * @param j     The column index of the modified cell
     * @param v     The new value in the modified cell
     * @throws CannotAcceptException If that cell cannot accept this value
     */
    public void tryBound(int i, int j, int v) throws CannotAcceptException {
        if (!cells[i][j].accepts(v)) 
            throw new CannotAcceptException();
        else
            bound(i, j, v);
    }

    /**
     * Bound the play described move
     * @param m     The move to be executed on the grid
     */
    public void play(Move m) {
        bound(m.i, m.j, m.v);
    }

    /**
     * Try to play the described move
     * @param m     The move to be executed
     * @throws CannotAcceptException If the cell cannot accept the value
     */
    public void tryPlay(Move m) throws CannotAcceptException {
        tryBound(m.i, m.j, m.v);
    }

    /** @return     Wether or not this grid is complete */
    public boolean complete() { return notSetCells.size() == 0; }

    /**
     * Wether or not some value clashes with lines, columns or block
     * @param i     The line index of the cell
     * @param j     The column index of the cell
     * @param v     The value we test in the cell
     */
    public boolean clashes(int i, int j, int v) {
        return !cells[i][j].accepts(v);
    }

    /** @return  A map of all the cells not currently set to their positions */
    public HashMap<Cell, Move> notSetCells() { return notSetCells; }

    /**
     * Get one single cell
     * @param i     The line index of the cell
     * @param j     The column index of the cell
     * @return      The cell in (i, j)
     */
    public Cell cell(int i, int j) { return cells[i][j]; }

    /**
     * Finds a certain move to be executed on a this grid
     * @return  A move that could be played on this grid
     * @throws NoMoveException  If no move can be found
     * @throws NoSafeMoveException If the grid is full
     */
    public Move findMove() throws NoMoveException, NoSafeMoveException {
        if (notSetCells.isEmpty()) 
            throw new NoMoveException();
        
        for (var c : notSetCells.entrySet()) {
            int cmove = c.getKey().certainMove();
            if (cmove != 0) {
                Move m = c.getValue();
                m.v = cmove;
                return m;       
            }
        }

        throw new NoSafeMoveException();
    }

    /** @return     Wether or not this grid is completed */
    public boolean isComplete() { return notSetCells.size() == 0; }
    
    /**
     * Returns a Sudoku grid represented in a String
     * 
     * Ill-formatted numbers are understood as none.
     * @param s     The String to parse the grid from
     * @return      The parsed Sudoku grid
     * @throws SudokuFormatException  If the String is ill-formatted
     * @throws CannotAcceptException  If the parsed sudoku is inconsistent
     */
    public static SudokuGrid fromString(String s) 
        throws SudokuFormatException, CannotAcceptException {
        SudokuGrid sg = new SudokuGrid();
        String[] lines = s.split("\\r?\\n");
        if (lines.length < 9) 
            throw new SudokuFormatException("Not enough lines");

        for (int i = 0; i < 9; i++) {
            String[] columns = lines[i].split(" ");
            Vector<String> values = new Vector<String>();
            for (int k = 0; k < columns.length; k++) 
                if (!columns[k].isEmpty())
                    values.add(columns[k]);

            if (values.size() < 9) 
                throw new SudokuFormatException("Not enough columns");
            
            for (int j = 0; j < 9; j++) {
                try {
                    int v = Integer.parseInt(values.get(j).trim());
                    if (v <= 0 || v >= 10) 
                        throw new SudokuFormatException("Value " + v +
                                                        " not valid");
                    sg.tryBound(i, j, v);
                } catch (NumberFormatException nfe) {}
            }
        }
        
        return sg;
    }

    /**
     * Performs deep cloning on a sudoku grid
     * @return  The new clone
     */
    public Object clone() {
        SudokuGrid sg = null;

        try {
            sg = (SudokuGrid) super.clone();
            sg.g = (Grid) this.g.clone();
            sg.notSetCells = new HashMap<Cell, Move>();
            sg.cells = new Cell[9][9];

            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    sg.cells[i][j] = (Cell) cells[i][j].clone();

            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++) {
                    sg.cells[i][j].setRelated(
                        sg.cellsOfLine(i, j),
                        sg.cellsOfColumn(i, j),
                        sg.cellsOfBlock(i, j)
                    );
                    if (!cells[i][j].isSet())
                        sg.notSetCells.put(sg.cells[i][j], new Move(i, j));
                }
        } catch (CloneNotSupportedException e) {
            throw new InternalError("unable to clone");
        }

        return sg;
    }

public String toString() { return g.toString(); }

    /**
     * Represent this grid as a string for writing in files
     * @return  The representation of this grid
     */
    public String representationString() {
        String s = "";
        for (int i = 0; i < 9; i++)  {
            for (int j = 0; j < 9; j++) {
                s += cells[i][j].isSet() ? cells[i][j].value() : "-";
                if (j < 8) s += " ";
            }
            s += "\n";
        }
        return s;
    }
}