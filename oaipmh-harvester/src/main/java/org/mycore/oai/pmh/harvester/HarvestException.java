package org.mycore.oai.pmh.harvester;

/**
 * General exception for harvesting.
 * 
 * @author Matthias Eichner
 */
public class HarvestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new harvest exception with <code>null</code> as its detail message. The
     * cause is not initialized, and may subsequently be initialized by a call
     * to <code>Throwable.initCause(java.lang.Throwable)</code>.
     */
    public HarvestException() {
        super();
    }

    /**
     * Constructs a new harvest exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to <code>Throwable.initCause(java.lang.Throwable)</code>.
     *
     * @param message the detail message. The detail message is saved for later
     *   retrieval by the Throwable.getMessage() method.
     */
    public HarvestException(String message) {
        super(message);
    }

    /**
     * Constructs a new harvest exception with the specified detail message and
     * cause.
     *
     * Note that the detail message associated with  cause is not automatically
     * incorporated in  this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval  by
     *   the Throwable.getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the
     * <code>Throwable.getCause()</code> method). (A <code>null</code> value is  permitted, and indicates
     * that the cause is nonexistent or  unknown.)
     */
    public HarvestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new harvest exception with the specified cause and a  detail
     * message of <code>(cause==null ? null : cause.toString())</code>  (which typically
     * contains the class and detail message of  cause). This constructor is
     * useful for runtime exceptions  that are little more than wrappers for
     * other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     * <code>Throwable.getCause()</code> method). (A <code>null</code> value is  permitted, and indicates
     * that the cause is nonexistent or  unknown.)
     */
    public HarvestException(Throwable cause) {
        super(cause);
    }

}
