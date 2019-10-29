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

	private String message;

	public BadResumptionTokenException() {
	    this("");
    }

	public BadResumptionTokenException(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}

	public BadResumptionTokenException setMessage(String message) {
	    this.message = message;
	    return this;
	}

	@Override
	public String getMessage() {
	    if(this.message != null) {
	        return this.message;
	    }
	    return "The value of the resumptionToken argument is invalid or expired " + this.resumptionToken;
	}

	@Override
	public ErrorCode getCode() {
		return ErrorCode.badResumptionToken;
	}

}
