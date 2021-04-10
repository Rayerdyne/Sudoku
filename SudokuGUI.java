import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;

/**
 * An object that extends JPanel to represent the sudoku grid on the ui.
 * 
 * It will also hold all the interface for interaction with the user.
 */
public class SudokuGUI extends JPanel implements KeyListener {
    /** Please warnings... */
    private static final long serialVersionUID = 1L;

    /** The sudoku grid currently represented */
    private SudokuGrid sg;

    /** A Brute that will be keeped until the user makes changes */
    private Brute brute;

    /** Text fields that actually display the numbers */
    private JDigitField[][] cells;

    /** The coordinates of the cell that currently has the focus */
    private int focusI, focusJ;

    /**
     * Construct a gui on an empty sudoku grid
     */
    public SudokuGUI() {
        super();

        sg = new SudokuGrid();
        cells = new JDigitField[9][9];

        GridLayout gl = new GridLayout(9, 9, 1, 1);
        setLayout(gl);

        Font font = new FontUIResource("Times new Roman", 0, 30);
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JDigitField(this, i, j);
                cells[i][j].setEditable(true);
                cells[i][j].setFont(font);
                add(cells[i][j]);
            }
        
        focusI = 0;
        focusJ = 0;
        addKeyListener(this);
    }

    /**
     * Signal the gui that a change has been made in one cell, and set the
     * brute to null
     * @param i         The row index of the cell
     * @param j         The column index of the cell
     * @param v         The new value of that cell
     * @param fromUser  If the change is from the user
     * @throws CannotAcceptException    If the assignment clashes
     */
    public void signalChange(int i, int j, int v, boolean fromUser) 
        throws CannotAcceptException {

        if (sg.cell(i, j).value() == v)
            return;
        sg.tryBound(i, j, v);
        if (fromUser) 
            brute = null;
        else
            cells[i][j].setText(Integer.toString(v));
    }

    /**
     * Validates the users inputs so far as being given. That is, all set
     * cells will no longer be editable
     */
    public void validateContent() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].isClashing()) {
                    JOptionPane.showMessageDialog(this, 
                        "Could not validate an inconsistent grid",
                        "Warning", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (sg.cell(i, j).isSet()) 
                    cells[i][j].setEditable(false);
        
        brute = null;
    }

    /**
     * Open a file containing the description of a sudoku grid
     */
    public void open() {
        String fileName;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "TXT Files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileName = chooser.getSelectedFile().getPath();
            try {
                openFile(fileName);
            } catch (CannotAcceptException e1) {
                JOptionPane.showMessageDialog(this, 
                    "Sudoku in file contains inconsistency(ies).",
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
            } catch (SudokuFormatException e2) {
                JOptionPane.showMessageDialog(this, 
                    "Sudoku file is ill-formatted.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException e3) {
                JOptionPane.showMessageDialog(this, 
                    "Could not find file \"" + fileName + "\".",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (IOException e4) {
                JOptionPane.showMessageDialog(this, 
                    "Could not read file: " + e4.getMessage() + ".",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Opens file and loads it
     * @param name  The name of the file representing our sudoku
     * @throws CannotAcceptException If the sudoku in the file is inconsistent
     * @throws SudokuFormatException If the file is ill-formatted
     * @throws FileNotFoundException If the file could not be found
     * @throws IOException If the file could not be read
     */
    private void openFile(String name) 
        throws CannotAcceptException, SudokuFormatException, 
        FileNotFoundException, IOException {
        File f = new File("sudoku1.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String data = "";
        for (int i = 0; i < 9; i++) {
            data += br.readLine() + "\n";
        }
        br.close();

        sg = SudokuGrid.fromString(data);
        syncGrids();
    }

    /**
     * After whole reload of the sudoku grid (sg member variable is up to 
     * date), refresh the JTextField cells values and editable property
     */
    private void syncGrids() {
        for (int i = 0; i < 9; i++) 
            for (int j = 0; j < 9; j++) {
                if (sg.cell(i, j).isSet()) {
                    cells[i][j].setText(Integer.toString(
                        sg.cell(i, j).value()));
                } else 
                    cells[i][j].setEditable(true);
            }
    }

    /**
     * Save the file to a choosen file
     */
    public void save() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "TXT Files", "txt");
        chooser.setFileFilter(filter);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = chooser.getSelectedFile().getPath();
            try {
                saveToFile(fileName);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Could not write to file: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Save the current sudoku to a file
     * @param name  The name of the file
     * @throws IOException  If the file could not be written
     */
    private void saveToFile(String name) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(name));
        writer.write(sg.representationString());
        writer.close();
    }

    /**
     * Show a brief info about this thing
     */
    public void showInfo() {
        JOptionPane.showMessageDialog(this, 
            "This sudoku \"breaker\" has been made by myself.\n" +
            "You can have a look at the source code that I will probably " +
            "put on github (see https://github.com/Rayerdyne, look in my " +
            "projects).\nI guess I should put a nice quote about potatoes " +
            "here, but I won't.\n" + 
            "Also, this is delivered to you under the terms of the WTFPL, " +
            "see http://www.wtfpl.net/about/ for more details.", 
            "Info about a wonderful program", 
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Show the shortcuts
     */
    public void showShortCuts() {
        JOptionPane.showMessageDialog(this,
            "Arrows - Move cursor in the grid\n" + 
            "'o'    - Play one computed move\n" +
            "'s'    - Try to solve the current grid\n" +
            "'v'    - Validates the current grid\n" +
            "'r'    - Resets the grid\n" +
            "'s'    - Solves the current grid\n" +
            "\n" + 
            "Fun fact: as you may have noticed, the shortcuts add a " +
            "character to a cell that is removed after the move is played.\n"+
            "This is due to the fact that I have to handle event and I've "+
            "been a bit lazy...\n" +
            "As a result, some character will always trigger the shortcut " +
            "it is not possible to type them.\nBut it's fine as you should" +
            " always enter digits in them C-:", 
            "Shortcuts", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Show a little explaination about the files representing sudokus.
     */
    public void showHelpFiles() {
        JOptionPane.showMessageDialog(this, 
            "As you see, you can load and save sudokus to files.\n" +
            "These are simply encoded as text so that you can write you own" +
            " with a text editor, and it can be read\nas soon as it " +
            "contains 9 lines that each contains 9 elements. Non-numeric " +
            "characters are considered\nas empty cell and numbers describe" +
            " the number in that cell. Notice that writing '88' will raise " +
            "an error,\nas 88 is somewhat greater that 9.\n" +
            "Anyways, if you want a example of valid representation, you " +
            "can simply save one and open it ^^",
            "Files help", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Resets the whole sudoku grid (unsets all cells)
     */
    public void reset() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                cells[i][j].setEditable(true);
                cells[i][j].setText("");
            }
        sg = new SudokuGrid();
        syncGrids();
    }

    /**
     * Make next "brutal" move on the grid
     */
    public void playOnce() {
        try {
            if (sg.isComplete())
                throw new NoMoveException();
            if (brute == null)
                brute = new Brute(sg);

            Move m;
            m = brute.playNextMove();
            // if we had to pop assumptions
            if (sg.cell(m.i, m.j).isSet()) {
                sg = brute.getCurrentGrid();
                syncGrids();
            }
            signalChange(m.i, m.j, m.v, false);
        } catch (NoMoveException e1) {
            JOptionPane.showMessageDialog(this, "The grid is full, dude.",
                "All my trouble seem so far away", 
                JOptionPane.INFORMATION_MESSAGE);
            JDigitField df = cells[focusI][focusJ];
            df.setText(Integer.toString(sg.cell(focusI, focusJ).value()));
        } catch (CannotAcceptException e2) {
            JOptionPane.showMessageDialog(this, 
                "YOU SHOULD NOT GET THIS MESSAGE", "REAL FATAL ERROR MAN",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Make moves until the game is full.
     * I have no idea of what happen if you try this with an empty grid.
     */
    public void solveGame() {
        if (brute == null)
            brute = new Brute(sg);
        
        brute.solve();
        sg = brute.getCurrentGrid();
        syncGrids();
    }

    /**
     * Updates the info about the focused cell: this is the one in (i, j)
     * @param i     The row index of focused cell
     * @param j     The column index of focused cell
     */
    public void focusUpdate(int i, int j) {
        focusI = i;
        focusJ = j;
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        // System.out.println(e.getKeyCode() + " - " + e.getKeyChar());
        // System.out.println(e.getSource().getClass());
        boolean messedUp = false;
        switch (e.getKeyCode()) {
            case 38: // UP
                focusI = focusI == 0 ? 8 : focusI - 1;
                break;
            case 40: // DOWN
                focusI = focusI == 8 ? 0 : focusI + 1;
                break;
            case 39: // RIGHT
                focusJ = focusJ == 8 ? 0 : focusJ + 1;
                break;
            case 37: // LEFT
                focusJ = focusJ == 0 ? 8 : focusJ - 1;
                break;
            case 79: // 'O'
                playOnce();
                messedUp = true;
                break;
            case 82: // 'R'
                reset();
                messedUp = true;
                break;
            case 83: // 'S'
                solveGame();
                messedUp = true;
                break;
            case 86: // 'V'
                validateContent();
                messedUp = true;
                break;
            default:
                break;
        }
        cells[focusI][focusJ].requestFocusInWindow();
        JDigitField df = (JDigitField) e.getSource();
        String s = df.getText();
        if (messedUp && !s.isEmpty() && !s.matches("^\\d$")) 
            df.setText(s.substring(0, s.length() - 1));
    }
}

/**
 * Howdy !
 * Do you like reading such comments ?
 * Do you have fun ?
 * Do you know the skeleton did feel bonely ?
 */
