package org.mycore.oai.pmh;


/**
 * Thrown if the combination of the values of the from, until, set and
 * metadataPrefix arguments results in an empty list.
 * 
 * @author Matthias Eichner
 */
public class NoRecordsMatchException extends OAIException {
    private static final long serialVersionUID = 1L;

	@Override
	public ErrorCode getCode() {
		return ErrorCode.noRecordsMatch;
	}

	@Override
	public String getMessage() {
	    return "No records match.";
	}

}
