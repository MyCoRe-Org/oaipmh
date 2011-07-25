package org.mycore.oai.pmh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contains the unique identifier of the item and properties necessary for selective harvesting.
 * 
 * @author Matthias Eichner
 */
public class Header {

    public enum Status {
        deleted
    }

    private String id;

    private Date datestamp;

    private List<Set> setList;

    private Status status;

    /**
     * Creates a new header.
     * 
     * @param id
     *            the unique identifier of an item in a repository. See <a
     *            href="http://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">here</a> for more information.
     * @param datestamp
     *            the date of creation, modification or deletion
     */
    public Header(String id, Date datestamp) {
        this(id, datestamp, null);
    }

    /**
     * Creates a new header.
     * 
     * @param id
     *            the unique identifier of an item in a repository. See <a
     *            href="http://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">here</a> for more information.
     * @param datestamp
     *            the date of creation, modification or deletion
     * @param status
     *            status attribute with a value of deleted indicates the withdrawal of availability of the specified metadata format for the item
     */
    public Header(String id, Date datestamp, Status status) {
        this.id = id;
        this.datestamp = datestamp;
        this.status = status;
        this.setList = new ArrayList<Set>();
    }

    /**
     * Returns the unique identifier. See <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">here</a> for more information.
     * 
     * @return id as string
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date of creation, modification or deletion of the record for the purpose of selective harvesting.
     * 
     * @return date
     */
    public Date getDatestamp() {
        return datestamp;
    }

    /**
     * Returns the set membership of the item.
     * 
     * @return a list of sets
     */
    public List<Set> getSetList() {
        return setList;
    }

    /**
     * Is the item is deleted.
     * 
     * @return true if deleted, otherwise false
     */
    public boolean isDeleted() {
        return status != null && status.equals(Status.deleted);
    }

    @Override
    public int hashCode() {
        return id.hashCode() + datestamp.hashCode() + status.hashCode();
    }

}
