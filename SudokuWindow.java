import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class SudokuWindow extends JFrame {
    /** Please warnings... */
    private static final long serialVersionUID = 1L;
   
    public static void main(String[] args) {
        SudokuWindow sw = new SudokuWindow();
        sw.setVisible(true);
    }

    /**
     * Constructor (... basic)
     */
    public SudokuWindow() {
        super("Sudoku!");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        SudokuGUI gui = new SudokuGUI();
        SudokuMenuBar smb = new SudokuMenuBar(gui);

        Container cp = getContentPane();
        cp.add(gui, BorderLayout.CENTER);

        setJMenuBar(smb);
    }
    
}
