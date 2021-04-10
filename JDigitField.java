import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Object that extends JTextField to show error if the content is not valid
 * for a sudoku cell
 */
public class JDigitField extends JTextField 
                         implements ActionListener, FocusListener {
    /** Please warnings... */
    private static final long serialVersionUID = 1L;

    /** The color used in correct cells */
    private static final Color normalColor = Color.WHITE;

    /** The color used in clashing cells */
    private static final Color clashingColor = new Color(233, 47, 47);

    /** When focus gained, stores the old text */
    private String oldText;

    /** The sudoku gui this JDigitField is in */
    private SudokuGUI gui;

    /** The coordinate of this digit in the grid */
    private int i, j;
    
    /**
     * Constructor...
     * @param sgui  The sudoku gui this JDigitField is in
     * @param i_    The row index of this digit in the grid
     * @param j_    The column index of this digit in the grid
     */
    public JDigitField(SudokuGUI sgui, int i_, int j_) {
        super();
        addActionListener(this);
        addFocusListener(this);
        addKeyListener(sgui);
        gui = sgui;
        i = i_;
        j = j_;
        setBackground(normalColor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JDigitField source = (JDigitField) e.getSource();

        String t = source.getText();
        if (!t.isEmpty() && !t.matches("^\\d$")) {
            JOptionPane.showMessageDialog(null, "Invalid digit '" + t + "'",
                "Warning", JOptionPane.WARNING_MESSAGE);
            setText(oldText);
            return;
        }

        try {
            gui.signalChange(i, j, Integer.parseInt(t), true);
            setBackground(normalColor);
        } catch (NumberFormatException ex1) {}
        // no NumberFormatException should be caught because of regex
        catch (CannotAcceptException ex2) {
            setBackground(clashingColor);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        oldText = getText();
        gui.focusUpdate(i, j);
    }

    @Override
    public void focusLost(FocusEvent e) {
        String t = getText();
        if (!oldText.equals(t))
            actionPerformed(new ActionEvent(this, 0, ""));
        
    }

    /** @return Wether or not this cell is clashing */
    public boolean isClashing() { 
        return !getBackground().equals(normalColor);
    }
}
