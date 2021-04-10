/**
 * Exception that will be raised when no coherent assumption can be made,
 * thus a previous assumption should be wrong.
 */
public class AssumptionException extends Exception {

    /** Please javac... */
    private static final long serialVersionUID = 1L;

    public AssumptionException() {
        super();
    }

    public AssumptionException(String info) {
        super(info);
    }
    
}
