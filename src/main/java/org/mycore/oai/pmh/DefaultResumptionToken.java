package org.mycore.oai.pmh;

import java.util.Date;

/**
 * The default resumption token implemenation described <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#FlowControl">here</a>.
 * 
 * @author Matthias Eichner
 */
public class DefaultResumptionToken extends SimpleResumptionToken {

    private Date expirationDate;

    private int completeListSize;

    private int cursor;

    /**
     * Creates an empty resumption token.
     */
    public DefaultResumptionToken() {
        this(null, null, -1, -1);
    }

    /**
     * Creates a new resumption token.
     * 
     * @param token
     *            the token as string
     * @param expirationDate
     *            when the resumptionToken ceases to be valid
     * @param completeListSize
     *            indicating the cardinality of the complete list (i.e., the sum of the cardinalities of the incomplete lists). Because there may be changes in
     *            a repository during a list request sequence, the value of completeListSize may be only an estimate of the actual cardinality of the complete
     *            list and may be revised during the list request sequence.
     * @param cursor
     *            a count of the number of elements of the complete list thus far returned (i.e. cursor starts at 0).
     */
    public DefaultResumptionToken(String token, Date expirationDate, int completeListSize, int cursor) {
        super(token);
        this.setExpirationDate(expirationDate);
        this.setCompleteListSize(completeListSize);
        this.setCursor(cursor);
    }

    /**
     * Returns the size of the whole list.
     * 
     * @return size of the list
     */
    public int getCompleteListSize() {
        return completeListSize;
    }

    public void setCompleteListSize(int completeListSize) {
        this.completeListSize = completeListSize;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

}
