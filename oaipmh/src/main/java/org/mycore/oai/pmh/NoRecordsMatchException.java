package org.mycore.oai.pmh;

/**
 * Thrown if the combination of the values of the from, until, set and metadataPrefix arguments results in an empty list.
 * 
 * @author Matthias Eichner
 */
public class NoRecordsMatchException extends OAIException {
    private static final long serialVersionUID = 1L;

    private String message;

    public NoRecordsMatchException() {
        this(null);
    }

    public NoRecordsMatchException(String message) {
        this.message = message;
    }

    public NoRecordsMatchException setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public ErrorCode getCode() {
        return ErrorCode.noRecordsMatch;
    }

    @Override
    public String getMessage() {
        if (this.message != null) {
            return this.message;
        }
        return "No records match.";
    }

}
