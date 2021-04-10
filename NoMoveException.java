/**
 * Exception that will be raised when trying to find a move on a full grid
 */
public class NoMoveException extends Exception {

    /** Please javac... */
    private static final long serialVersionUID = 1L;

    public NoMoveException() {
        super();
    }

    public NoMoveException(String info) {
        super(info);
    }
    
}
