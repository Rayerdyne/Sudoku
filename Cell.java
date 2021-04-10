/**
 * An objects that represents a cell on the sudoku grid
 */
public class Cell implements Cloneable {
    private static final int NOTSET = 0;

    /** The value in this cell, NOTSET if none */
    private int v;

    /** The 8 other cells on the same line */
    private Cell[] line;
    /** The 8 other cells on the same column */
    private Cell[] column;
    /** The 8 other cells in the same block */
    private Cell[] block;

    /** Wether or not this cell, in its grid, can accept the number [i+1] */
    private boolean[] accepts;
    
    /**
     * Constructor of empty cell
     */
    public Cell() {
        this(NOTSET);
    }

    /**
     * Constructs a cell with a given value written in it
     * @param v_    The value of the cell
     */
    public Cell(int v_) {
        v = v_;
        accepts = new boolean[9];
        for (int i = 0; i < 9; i++) {
            accepts[i] = true;
        }
    }

    /**
     * Sets the related cells
     * @param line_     The 8 other cells on the same line
     * @param column_   The 8 other cells on the same column
     * @param block_    The 8 other cells in the same block
     */
    public void setRelated(Cell[] line_, Cell[] column_, Cell[] block_) {
        line = line_;
        column = column_;
        block = block_;
    }

    /** @return  Wether or not this cells has a value written in it */
    public boolean isSet() { return v != NOTSET; }
    /** @return  The value written in this cell  */
    public int value() { return v; }

    /**
     * Wether or not this cell could accept a given value to be written in
     * @param v The value to test, in [1, 9]
     * @return  Wether or not this cell could accept the value
     */
    public boolean accepts(int v) {
        if (v < 0 || v >= 10)
            return false;
        else 
            return accepts[v-1];
    }

    /**
     * Sets the value of this cell, overwrites it if already present, and
     * advertises the related cells
     * @param v_    The new value of this cell
     */
    public void bound(int v_) { 
        v = v_;
        for (int i = 0; i < 8; i++) {
            line[i].signalRelatedValue(v_);
            column[i].signalRelatedValue(v_);
            block[i].signalRelatedValue(v_);
        }
    }

    /**
     * Try to set the value of this cell, overwrites it if already present, and
     * advertises the related cells if successful
     * @param v    The new value of this cell
     * @throws CannotAcceptException If the cell cannot accept the value v
     */
    public void tryBound(int v) throws CannotAcceptException {
        if (!accepts(v))
            throw new CannotAcceptException();
        else
            bound(v);
    }

    /**
     * Updates the accept array given that some related cell has been assigned
     * a value v
     * @param v     The value assigned to some related cell
     */
    private void signalRelatedValue(int v) {
        accepts[v-1] = false;
    }

    /**
     * The number of possible values for this cell, 0 if already set
     * @return  The number of options this cell has
     */
    public int nOptions() {
        if (isSet()) return 0;
        int n = 0;
        for (int i = 0; i < 9; i++)
            if (accepts[i])
                n++;
        return n;
    }

    /**
     * Returns an eventual certain move to be played on that cell
     * @return  0 if none or already set, or a value between 1 and 9 if this
     *          value can be safely assigned to this cell
     */
    public int certainMove() {
        if (isSet()) return 0;
        int n = 0, last = 0;
        for (int i = 0; i < 9; i++)
            if (accepts[i]) {
                n++;
                last = i + 1;
            }

        if (n == 1)
            return last;
        else 
            return 0;
    }

    public String toString() { 
        return !isSet() ? "Not set cell" : 
                          "Cell with: " + Integer.toString(v);
    }

    /**
     * /!\ dangerous
     * Do not forget that the clone references the same related cells ad the
     * original
     */
    public Object clone() {
        Cell c = null;
        try {
            c = (Cell) super.clone();
            c.accepts = (boolean[]) this.accepts.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError("Could not clone Cell");
        }
        return c;
    }
}
