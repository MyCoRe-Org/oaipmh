package org.mycore.oai.pmh;


/**
 * Thrown if the value of the resumptionToken argument is invalid or expired.
 * 
 * @author Matthias Eichner
 *
 */
public class BadResumptionTokenException extends OAIException {
    private static final long serialVersionUID = 1L;

	private String resumptionToken;

	public BadResumptionTokenException() {
	    this("");
    }

	public BadResumptionTokenException(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}

	@Override
	public String getMessage() {
	    return "The value of the resumptionToken argument is invalid or expired " + resumptionToken;
	}

	@Override
	public ErrorCode getCode() {
		return ErrorCode.badResumptionToken;
	}

}
