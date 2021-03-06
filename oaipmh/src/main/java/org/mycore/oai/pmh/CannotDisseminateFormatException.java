package org.mycore.oai.pmh;

/**
 * Thrown if the {@link MetadataFormat} identified by the value given for the metadataPrefix argument
 * is not supported by the item or by the repository.
 * 
 * @author Matthias Eichner
 */
public class CannotDisseminateFormatException extends OAIException {
    private static final long serialVersionUID = 1L;

    private String metadataPrefix;

    private String id;

    private String message;

    public CannotDisseminateFormatException setId(String id) {
        this.id = id;
        return this;
    }

    public CannotDisseminateFormatException setMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
        return this;
    }

    public CannotDisseminateFormatException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public String getId() {
        return id;
    }

    @Override
    public ErrorCode getCode() {
        return ErrorCode.cannotDisseminateFormat;
    }

    @Override
    public String getMessage() {
        if (this.message != null) {
            return this.message;
        }
        if (id != null && id.length() > 0 && metadataPrefix != null) {
            return "The item " + this.id + " does not support the metadata format " + metadataPrefix;
        } else if (metadataPrefix != null) {
            return "The metadata prefix argument is not supported by the repository: " + metadataPrefix;
        }
        return "";
    }

}
