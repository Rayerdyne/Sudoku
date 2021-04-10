import java.util.LinkedHashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * An objects that extends JMenuBar for automatic construction of the menu bar 
 * for the sudoku gui.
 */
public class SudokuMenuBar extends JMenuBar implements ActionListener {
    /** Please warnings... */
    private static final long serialVersionUID = 0L;

    SudokuGUI gui;

    JMenu menuFile;
    JMenu menuGame;
    JMenu menuHelp;

    LinkedHashMap<JMenuItem, Procedure> miFile;
    LinkedHashMap<JMenuItem, Procedure> miGame;
    LinkedHashMap<JMenuItem, Procedure> miHelp;
    
    /**
     * Construct a sudoku menu bar
     * @param sgui  The sudoku gui this is related to
     */
    public SudokuMenuBar(SudokuGUI sgui) {
        super();
        gui = sgui;

        menuFile = new JMenu("File");
        menuGame = new JMenu("Game");
        menuHelp = new JMenu("Help");

        miFile = new LinkedHashMap<JMenuItem, Procedure>();
        miGame = new LinkedHashMap<JMenuItem, Procedure>();
        miHelp = new LinkedHashMap<JMenuItem, Procedure>();

        miFile.put(new JMenuItem("Open sudoku"), 
                   () -> { gui.open(); });
        miFile.put(new JMenuItem("Save sudoku"),
                   () -> { gui.save(); });
        miFile.put(new JMenuItem("Info"),
                   () -> { gui.showInfo(); });
        miFile.put(new JMenuItem("Quit"), () -> { 
            if (JOptionPane.showConfirmDialog(null, "Quit program now ?\n", 
                "Quit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                System.exit(0);
            });

        miGame.put(new JMenuItem("Reset (R)"),
                   () -> { gui.reset(); });
        miGame.put(new JMenuItem("Validate (V)"),
                   () -> { gui.validateContent(); });
        miGame.put(new JMenuItem("Play one move (O)"),
                   () -> { gui.playOnce(); });
        miGame.put(new JMenuItem("Solve (S)"),
                   () -> { gui.solveGame(); });
        
        miHelp.put(new JMenuItem("Shortcuts"),
                   () -> { gui.showShortCuts(); });
        miHelp.put(new JMenuItem("About files..."),
                   () -> { gui.showHelpFiles(); });

        for (var x : miFile.keySet()) {
            menuFile.add(x);
            x.addActionListener(this);
        }

        for (var x : miGame.keySet()) {
            menuGame.add(x);
            x.addActionListener(this);
        }

        for (var x : miHelp.keySet()) {
            menuHelp.add(x);
            x.addActionListener(this);
        }

        add(menuFile);
        add(menuGame);
        add(menuHelp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (miFile.containsKey(o)) {
            miFile.get(o).run();
        } else if (miGame.containsKey(o)) {
            miGame.get(o).run();
        } else if (miHelp.containsKey(o)) {
            miHelp.get(o).run();
        }
    }
}
