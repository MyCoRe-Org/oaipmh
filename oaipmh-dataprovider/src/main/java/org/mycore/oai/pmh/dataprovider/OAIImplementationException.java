package org.mycore.oai.pmh.dataprovider;

/**
 * Is thrown if the implementation of the oai provider/adapter is invalid.
 * 
 * @author Matthias Eichner
 */
public class OAIImplementationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OAIImplementationException(String message) {
        super(message);
    }

    public OAIImplementationException(Throwable cause) {
        super(cause);
    }

    public OAIImplementationException(String message, Throwable cause) {
        super(message, cause);
    }

}
