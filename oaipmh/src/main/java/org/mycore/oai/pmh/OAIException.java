package org.mycore.oai.pmh;

/**
 * Instances of this class represent a oai specific exception.
 * 
 * @author Matthias Eichner
 */
public abstract class OAIException extends Exception {
    private static final long serialVersionUID = -823449287844627203L;

    /**
     * Enum of all possible oai error codes.
     */
    public enum ErrorCode {
        badArgument,
        badResumptionToken,
        badVerb,
        cannotDisseminateFormat,
        idDoesNotExist,
        noRecordsMatch,
        noMetadataFormats,
        noSetHierarchy
    }

    public OAIException() {
        super();
    }

    public OAIException(String message) {
        super(message);
    }

    public OAIException(Throwable cause) {
        super(cause);
    }

    public OAIException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns the error code of the exception.
     * 
     * @return code of exception
     */
    public abstract ErrorCode getCode();

}
