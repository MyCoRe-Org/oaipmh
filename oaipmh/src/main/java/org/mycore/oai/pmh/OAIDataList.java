package org.mycore.oai.pmh;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * OAI-PMH Data container, contains a {@link List} with a {@link ResumptionToken}.
 * This is needed for ListSets, ListRecords and ListIdentifiers.
 * 
 * @author Matthias Eichner
 * @param <T> type of Data (e.g. {@link Record})
 */
public class OAIDataList<T> extends ArrayList<T> implements Cloneable {

    private static final long serialVersionUID = 3081555510128912877L;

    private ResumptionToken resumptionToken;

    /**
     * Creates a new <code>OAIDataList</code> without a resumption token.
     */
    public OAIDataList() {
        this(null);
    }

    /**
     * Creates a new <code>OAIDataList</code> with a resumption token.
     * 
     * @param resumptionToken
     *            the resumption token of the data list
     */
    public OAIDataList(ResumptionToken resumptionToken) {
        this.resumptionToken = resumptionToken;
    }

    /**
     * Returns the resumption token of that list.
     * 
     * @return the resumption token
     */
    public ResumptionToken getResumptionToken() {
        return resumptionToken;
    }

    /**
     * Sets the resumption token for this list.
     * 
     * @param resumptionToken the new resumption token
     */
    public void setResumptionToken(ResumptionToken resumptionToken) {
        this.resumptionToken = resumptionToken;
    }

    /**
     * <p>Checks if the resumptionToken element exists AND if the token
     * value is set.</p>
     *  From the protocol:
     * <ul>
     *   <li>
     *   <b>the response containing the incomplete list that completes the list
     *      must include an empty resumptionToken element</b>
     *   </li>
     * </ul>
     * <p>
     * The last page should have an resumptionToken element but NO token value,
     * but the expirationDate, completeListSize and/or the cursor.
     * </p>
     * 
     * @return true if set, otherwise false
     */
    public boolean isResumptionTokenSet() {
        return this.resumptionToken != null && this.resumptionToken.getToken() != null;
    }

    /**
     * Returns the resumption token value if available.
     * 
     * @return optional resumption token
     */
    public Optional<String> getToken() {
        if (isResumptionTokenSet()) {
            return Optional.of(this.resumptionToken.getToken());
        }
        return Optional.empty();
    }

    /**
     * Returns a shallow copy of this <code>OAIDataList</code> instance.
     * The elements themselves are not copied.
     *
     * @return a clone of this <code>OAIDataList</code> instance
     */
    @Override
    public Object clone() {
        OAIDataList<T> copy = new OAIDataList<>();
        copy.addAll(this);
        copy.setResumptionToken(this.resumptionToken);
        return copy;
    }

}

