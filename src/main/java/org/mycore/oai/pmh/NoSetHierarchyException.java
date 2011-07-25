package org.mycore.oai.pmh;

/**
 * The repository does not support sets.
 * 
 * @author Matthias Eichner
 */
public class NoSetHierarchyException extends OAIException {
    private static final long serialVersionUID = 1L;

	@Override
	public ErrorCode getCode() {
		return ErrorCode.noSetHierarchy;
	}

	@Override
	public String getMessage() {
	    return "The repository doesn't support sets.";
	}
}
