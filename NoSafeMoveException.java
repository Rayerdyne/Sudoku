/**
 * Exception that will be raised when no safe move can be found on the grid
 */
public class NoSafeMoveException extends Exception {

    /** Please javac... */
    private static final long serialVersionUID = 1L;

    public NoSafeMoveException() {
        super();
    }

    public NoSafeMoveException(String info) {
        super(info);
    }
    
}
