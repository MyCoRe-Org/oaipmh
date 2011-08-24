package org.mycore.oai.pmh;

/**
 * The repository does not support sets.
 * 
 * @author Matthias Eichner
 */
public class NoSetHierarchyException extends OAIException {
    private static final long serialVersionUID = 1L;

    private String message;

    public NoSetHierarchyException setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public ErrorCode getCode() {
        return ErrorCode.noSetHierarchy;
    }

    @Override
    public String getMessage() {
        if (this.message != null) {
            return this.message;
        }
        return "The repository doesn't support sets.";
    }
}
