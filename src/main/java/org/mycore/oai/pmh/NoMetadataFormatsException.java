package org.mycore.oai.pmh;

/**
 * Thrown if there are no metadata formats available for the specified item.
 * 
 * @author Matthias Eichner
 */
public class NoMetadataFormatsException extends OAIException {
    private static final long serialVersionUID = 1L;

	private String id;

	public NoMetadataFormatsException(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public ErrorCode getCode() {
		return ErrorCode.noMetadataFormats;
	}

	 @Override
	public String getMessage() {
	     return "No metadata format available for id: " + this.id;
	}

}
