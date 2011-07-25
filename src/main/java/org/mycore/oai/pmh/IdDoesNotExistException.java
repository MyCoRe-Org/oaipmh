package org.mycore.oai.pmh;

/**
 * Thrown if the value of the identifier argument is unknown or illegal in this
 * repository.
 * 
 * @author Matthias Eichner
 */
public class IdDoesNotExistException extends OAIException {
    private static final long serialVersionUID = 1L;

	private String id;

	public IdDoesNotExistException(String id) {
		this.id = id;
	}

	@Override
	public ErrorCode getCode() {
		return ErrorCode.idDoesNotExist;
	}

	@Override
	public String getMessage() {
	    return "Unknown or illegal id: " + this.id;
	}

}
