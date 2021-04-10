import java.util.Stack;

/**
 * Holds the methods to bruteforce some sudoku grid.
 */
public class Brute {
    /** The sudoku grid to be solved */
    // private SudokuGrid sg;

    /** A stack of sudoku grids to keep a trace of assuptions made */
    private Stack<SudokuGrid> stack;

    /** The stack of move that were assumptions related to each transition
     * from one to another element of stack. */
    private Stack<Move> assumptionMoves;

    /** A boolean to raise when we just made an assumption */
    private boolean assumptionMadeFlag;
    
    public Brute(SudokuGrid sg_) {
        // sg = sg_;
        stack = new Stack<SudokuGrid>();
        assumptionMoves = new Stack<Move>();
        stack.add(sg_);
        assumptionMadeFlag = false;
    }

    /**
     * Finds a certain move to be executed on a the grid
     * @return  A move that could be played on this grid
     * @throws NoMoveException  If the grid is full
     * @throws NoSafeMoveException If no safe move can be found
     */
    public Move tryCertainMove() throws NoMoveException, NoSafeMoveException {
        return stack.peek().findMove();
    }

    /**
     * A next move to be played. If there is a certain move that can be made,
     * it will be before making an assumption.
     * @return  The next move to be played
     * @throws NoMoveException If the grid is full
     * @throws AssumptionException If some previous assumption lead to 
     * inconsistency
     */
    private Move nextMoveOnTop() throws NoMoveException, AssumptionException {
        Move m = new Move();
        try {
            m = tryCertainMove();
        } catch (NoSafeMoveException e) {
            assumptionMadeFlag = true;
            int best = 10;
            for (var x : stack.peek().notSetCells().entrySet()) {
                Cell c = x.getKey();
                int n = c.nOptions();
                if (n < best) {
                    best = n;
                    m = x.getValue();
                    for (int t = 1; t <= 9; t++)
                        if (c.accepts(t)) {
                            m.v = t;
                            break;
                        }
                }
            }
            if (m.v == 0) 
                throw new AssumptionException();
        }
        return m;
    }

    /**
     * Finds the next move to be played, handling assumption errors. Thus, it
     * may pop from the stack.
     * @throws NoMoveException  If the grid is full
     */
    public Move nextMove() throws NoMoveException {
        Move m;
        try {
            m = nextMoveOnTop();
        } catch (AssumptionException e) {
            int old;
            do {
                m = assumptionMoves.pop();
                stack.pop();
                Cell c = stack.peek().cell(m.i, m.j);
                old = m.v;
                for (int i = m.v + 1; i < 10; i++)
                    if (c.accepts(i)) {
                        m.v = i;
                        break;
                    }
            } while (m.v == old);
        }

        if (assumptionMadeFlag) {
            assumptionMadeFlag = false;
            SudokuGrid cur = stack.peek();
            stack.add((SudokuGrid) cur.clone());
            assumptionMoves.add(m);
        }
        return m;
    }

    /**
     * Finds the next move to be played and apply it, handling assumption 
     * errors. Thus, it may pop from the stack.
     * @return  The move that has been played
     * @throws NoMoveException  If the grid is full
     */
    public Move playNextMove() throws NoMoveException {
        Move m = nextMove();
        stack.peek().play(m);
        return m;
    }

    /**
     * Finds a solution to the initial grid.
     */
    public void solve() {
        while (!stack.peek().complete()) {
            try {
                playNextMove();
            } catch (NoMoveException e) {
                System.err.println("Should not be printed #4");
            }
        }
    }

    /**
     * Wether or not our 'current work' relies on an assumption
     * @return  Wether or not an assumption has been made
     */
    public boolean isAssumptionMade() { return stack.size() > 1; }

    /**
     * The current grid the brute is hurting
     * @return  The current sudoku grid
     */
    public SudokuGrid getCurrentGrid() {
        return stack.peek();
    }
}
