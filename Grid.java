/**
 * The coordinate are measured as usual:
 * +-------+-------+
 * | (0,1) | (0,1) |
 * |-------+-------+
 * | (1,0) | (1,1) |
 * +-------+-------+
 */

public class Grid implements Cloneable {
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private static final char VERTICAL_WALL = '|';
    private static final char HORIZONTAL_WALL = '-';
    private static final char CORNER = '+';

    /** The Strings printed in the cells */
    private String[][] content;
    /** The array of "activation" of cells */
    private boolean[][] on;

    /** The height and the width of the grid (redundant) */
    private int height, width;

    /** The right wall character of the cells */
    private char[][] rightWalls;
    /** Leftmosts walls characters */
    private char[] leftWalls;
    /** The down wall character of the cells */
    private char[][] downWalls;
    /** Upper walls characters */
    private char[] upWalls;

    public Grid(boolean[][] on) throws GridException {
        this.on = on;
        height = on.length;
        if (height == 0)
            throw new GridException("Height cannot be zero");

        width = on[0].length;
        if (width == 0)
            throw new GridException("Width cannot be zero");
        
        content = new String[height][];
        rightWalls = new char[height][];
        leftWalls = new char[height];
        downWalls = new char[height][];
        upWalls = new char[width];
        for (int i = 0; i < height; i++) {
            content[i] = new String[width];
            rightWalls[i] = new char[width];
            downWalls[i] = new char[width];
            for (int j = 0; j < width; j++) {
                content[i][j] = "";
                rightWalls[i][j] = VERTICAL_WALL;
                downWalls[i][j] = HORIZONTAL_WALL; 
            }
            leftWalls[i] = VERTICAL_WALL;
        }
        for (int j = 0; j < width; j++) 
            upWalls[j] = HORIZONTAL_WALL;
    }

    /**
     * Sets the content of a cell
     * @param i     the row of the cell
     * @param j     the column of the cell
     * @param s     the content of the cell
     * @throws GridException
     */
    public void setContent(int i, int j, String s) throws GridException {
        check(i, j);
        if (s == null) 
            throw new GridException("The content of a cell cannot be null");
        content[i][j] = s;
    }

    /**
     * Sets a given wall to some character
     * @param i             the row of the cell
     * @param j             the column of the cell
     * @param orientation   the direction of the wall we want to set, is one of
     *                      Grid.UP, Grid.DOWN, Grid.LEFT, Grid.RIGHT
     * @param c             the character to set
     * @throws GridException
     */
    public void setWall(int i, int j, int orientation, char c) 
        throws GridException {
        
        check(i, j);

        switch (orientation) {
            case UP:
                if (i == 0) 
                    upWalls[j] = c;
                else 
                    downWalls[i - 1][j] = c;
                break;
            case DOWN:
                downWalls[i][j] = c;
                break;
            case LEFT:
                if (j == 0)
                    leftWalls[i] = c;
                else 
                    rightWalls[i][j - 1] = c;
                break;
            case RIGHT:
                rightWalls[i][j] = c;
                break;
        }
    }

    /**
     * Sets the activation of a cell
     * @param i             the row of the cell
     * @param j             the column of the cell
     * @param activated     wether or not the cell is activated
     * @throws GridException
     */
    public void setActivated(int i, int j, boolean activated) 
        throws GridException {
        
        if (i < 0 || i >= height)
            throw new GridException("Height index out of bounds");

        if (j < 0 || j >= width)
            throw new GridException("Width index out of bounds");
        
        on[i][j] = activated;
    }

    /**
     * Checks if cell positions match the grid
     * @param i     the tow index
     * @param j     the column index
     * @throws GridException
     */
    private void check(int i, int j) throws GridException {
        if (i < 0 || i >= height)
            throw new GridException("Height index out of bounds");

        if (j < 0 || j >= width)
            throw new GridException("Width index out of bounds");

        if (!on[i][j])
            throw new GridException("This cell is not active");
    }

    public String toString() {
        // recompute columns width each time.
        // change ?
        int[] colWidths = new int[width];
        for (int j = 0; j < width; j++) {
            colWidths[j] = 1;
            for (int i = 0; i < height; i++) {
                int l = content[i][j].length();
                if (on[i][j] && l > colWidths[j])
                    colWidths[j] = l;
            }
        }

        String s = buildUpperLine(colWidths) + "\n";
        

        for (int i = 0; i < height - 1; i++) {
            s += buildDataLine(i, colWidths) + "\n";
            s += buildDownLine(i, colWidths) + "\n";
        }
        s += buildDataLine(height-1, colWidths) + "\n";
        s += buildDownLine(height-1, colWidths);

        return s;
    }

    /**
     * Constructs the upper line of the grid
     * @param colWidths     the array containing the widths of all columns
     * @return              the line
     */
    private String buildUpperLine(int[] colWidths) {
        String s = "";

        s += on[0][0] ? CORNER : " ";

        for (int j = 0; j < width; j++) {
            if (on[0][j]) {
                s += "" + HORIZONTAL_WALL + upWalls[j];
                s = dumbReapeat(s, HORIZONTAL_WALL, colWidths[j]);
                s += CORNER;
            } else if (j + 1 < width && on[0][j+1]) {
                s = dumbReapeat(s, ' ', colWidths[j] + 2);
                s += CORNER;
            } else 
                s = dumbReapeat(s, ' ', colWidths[j] + 3);
        }
        return s;
    }

    /**
     * Constructs the i-th "data line", the line that contains the actual info
     * @param i             the index of line to be built
     * @param colWidths     the array containing the widths of all columns
     * @return              the line
     */
    private String buildDataLine(int i, int[] colWidths) {
        String s = "";

        s += on[i][0] ? leftWalls[i] : " ";

        for (int j = 0; j < width; j++) {
            if (on[i][j]) {
                s += " " + content[i][j];
                s = dumbReapeat(s, ' ', colWidths[j] - content[i][j].length() +1);
                s += rightWalls[i][j];
            } else if (j + 1 < width && on[i][j+1]) {
                s = dumbReapeat(s, ' ', colWidths[j] + 2);
                s += rightWalls[i][j];
            } else 
                s = dumbReapeat(s, ' ', colWidths[j] + 3);
        }

        return s;
    }

    /**
     * Constructs the i-th "data line", the line that contains the actual info
     * @param i             the index of line to be built
     * @param colWidths     the array containing the widths of all columns
     * @return              the line
     */
    private String buildDownLine(int i, int[] colWidths) {
        String s = "";

        s += on[i][0] || (i + 1 < height && on[i+1][0]) ? CORNER : " ";
        for (int j = 0; j < width; j++) {
            boolean downIsOn = i + 1 < height && on[i + 1][j];
            boolean rightIsOn = j + 1 < width && on[i][j + 1];
            boolean downRightIsOn = i + 1 < height && j + 1 < width &&
                                    on[i + 1][j + 1];
            
            if (on[i][j] || downIsOn) {
                s += "" + HORIZONTAL_WALL + downWalls[i][j];
                s = dumbReapeat(s, HORIZONTAL_WALL, colWidths[j]);
                s += CORNER;
            } else if (rightIsOn || downRightIsOn) {
                s = dumbReapeat(s, ' ', colWidths[j] + 2);
                s += CORNER;
            } else 
                s = dumbReapeat(s, ' ', colWidths[j] + 3);
        }

        return s;
    }

    private static String dumbReapeat(String s, char c, int n) {
        for (int k = 0; k < n; k++) 
            s += c;
        return s;
    }

    public Grid clone() {
        Grid g = null;

        try {
            g = (Grid) super.clone();
            g.content = new String[9][9];
            for (int i = 0; i < 9; i++) 
                for (int j = 0; j < 9; j++) 
                    g.content[i][j] = this.content[i][j];
            
            // do not need deep copy:
            g.on = (boolean[][]) this.on.clone();
            g.downWalls = (char[][]) this.downWalls.clone();
            g.upWalls = (char[]) this.upWalls.clone();
            g.leftWalls = (char[]) this.leftWalls.clone();
            g.rightWalls = (char[][]) this.rightWalls.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError("Could not clone Grid");
        }

        return g;
    }

}