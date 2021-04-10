/**
 * Simple data holder object to contain a position and a value
 */
public class Move {
    public int i, j, v; 

    public Move() {
        this(0, 0, 0);
    }

    public Move(int i_, int j_, int v_) {
        i = i_;
        j = j_;
        v = v_;
    }
    
    public Move(int i, int j) {
        this(i, j, 0);
    }

    public String toString() {
        return "Move { (" + (i+1) + ", " + (j+1) + ") -> " + v + " }";
    }
}
