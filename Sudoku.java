import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Sudoku {
    public static void main(String[] args) 
        throws FileNotFoundException, IOException {
        // SudokuGrid sg = new SudokuGrid();

        // sg.bound(0, 3, 8);
        // sg.bound(3, 0, 9);
        // System.out.println(sg);
        // System.out.println("0 0 9: " + sg.clashes(0, 0, 9));
        // System.out.println("0 0 8: " + sg.clashes(0, 0, 8));
        // System.out.println("1 4 9: " + sg.clashes(1, 4, 9));
        // System.out.println("4 1 9: " + sg.clashes(4, 1, 9));

        File f = new File("sudoku1.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String data = "";
        for (int i = 0; i < 9; i++) {
            data += br.readLine() + "\n";
        }
        br.close();

        try {
            SudokuGrid sg = SudokuGrid.fromString(data);
            System.out.println(sg);
            System.out.println("1 1 2: " + sg.clashes(1, 1, 2));
            System.out.println("1 1 5: " + sg.clashes(1, 1, 5));
            System.out.println(sg.findMove());

            Brute b = new Brute(sg);
            b.solve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}