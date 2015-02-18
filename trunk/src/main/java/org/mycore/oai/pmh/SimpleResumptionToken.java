package org.mycore.oai.pmh;

/**
 * Simple implementation of a resumption token. Contains only the token as string.
 * 
 * @author Matthias Eichner
 */
public class SimpleResumptionToken implements ResumptionToken {

    private String token;

    public SimpleResumptionToken(String token) {
        this.token = token;
    }

    /**
     * Returns the token as string.
     * @return token as string
     */
    @Override
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return getToken();
    }

}
