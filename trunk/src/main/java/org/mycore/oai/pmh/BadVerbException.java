package org.mycore.oai.pmh;

/**
 * Value of the verb argument is not a legal OAI-PMH verb, the verb argument is missing, or the verb argument is repeated.
 * 
 * @author Matthias Eichner
 */
public class BadVerbException extends OAIException {
    private static final long serialVersionUID = 1L;

    private String badVerb;

    @Override
    public ErrorCode getCode() {
        return ErrorCode.badVerb;
    }

    public BadVerbException(String badVerb) {
        this.badVerb = badVerb;
    }

    @Override
    public String getMessage() {
        return "Bad verb: " + this.badVerb;
    }
}
