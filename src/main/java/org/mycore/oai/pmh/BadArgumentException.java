package org.mycore.oai.pmh;

/**
 * Thrown if the request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal
 * syntax.
 * 
 * @author Matthias Eichner
 */
public class BadArgumentException extends OAIException {
    private static final long serialVersionUID = 1L;

    public enum Type {
        invalid, missing, repeated, illegalSyntax
    }

    private Type type;

    private String[] arguments;

    private String message;

    @Override
    public ErrorCode getCode() {
        return ErrorCode.badArgument;
    }

    public BadArgumentException(Type type, String... arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public BadArgumentException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        StringBuilder ba = new StringBuilder("Bad argument: ");
        if (this.message != null) {
            return this.message;
        } else {
            if (Type.invalid.equals(type)) {
                return ba.append("following argument(s) are invalid ").append(idsAsString()).toString();
            } else if (Type.missing.equals(type)) {
                return ba.append("following argument(s) are missing ").append(idsAsString()).toString();
            } else if (Type.repeated.equals(type)) {
                return ba.append("following argument(s) are repeated ").append(idsAsString()).toString();
            } else if (Type.illegalSyntax.equals(type)) {
                return ba.append("following argument(s) have an illegal syntax ").append(idsAsString()).toString();
            }
            return ba.append("reason unknown").toString();
        }
    }

    private String idsAsString() {
        StringBuilder idBuf = new StringBuilder();
        for (int i = 0; i < arguments.length; i++) {
            idBuf.append("'").append(arguments[i]).append("'");
            if (i != arguments.length - 1)
                idBuf.append(", ");
        }
        return idBuf.toString();
    }
}
