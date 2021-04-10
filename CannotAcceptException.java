/**
 * Dumby exception that will be thrown by Cell objects when trying to assign
 * a value to a cell that cannot accept it
 */
public class CannotAcceptException extends Exception {

    private static final long serialVersionUID = 123L;
    
    public CannotAcceptException() {
        super();
    }

    public CannotAcceptException(String s) {
        super(s);
    }
}
 