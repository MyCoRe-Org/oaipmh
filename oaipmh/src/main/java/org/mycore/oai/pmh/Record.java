package org.mycore.oai.pmh;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.mycore.oai.pmh.Header.Status;

/**
 * Implementation of a OAI-PMH record. For more information see <a href="http://www.openarchives.org/OAI/openarchivesprotocol.html#Record">here</a>.
 * 
 * @author Matthias Eichner
 */
public class Record {

    private Header header;

    private Metadata metadata;

    private List<Element> aboutList;

    /**
     * Creates a new record without metadata.
     * 
     * @param id
     *            the unique identifier of an item in a repository
     * @param datestamp
     *            the date of creation, modification or deletion
     */
    public Record(String id, Instant datestamp) {
        this.header = new Header(id, datestamp, null);
        this.aboutList = new ArrayList<>();
    }

    /**
     * Creates a new record without metadata.
     * 
     * @param id
     *            the unique identifier of an item in a repository
     * @param datestamp
     *            the date of creation, modification or deletion
     * @param status
     *            status attribute with a value of deleted indicates the withdrawal of availability
     *            of the specified metadata format for the item
     */
    public Record(String id, Instant datestamp, Status status) {
        this.header = new Header(id, datestamp, status);
        this.aboutList = new ArrayList<>();
    }

    /**
     * Creates a new record.
     * 
     * @param id
     *            the unique identifier of an item in a repository
     * @param datestamp
     *            the date of creation, modification or deletion
     * @param metadata
     *            contains metadata of the record - mods, oai_dc etc.
     */
    public Record(String id, Instant datestamp, Metadata metadata) {
        this.header = new Header(id, datestamp, null);
        this.metadata = metadata;
        this.aboutList = new ArrayList<>();
    }

    /**
     * Creates a new record without metadata.
     * 
     * @param header the header data
     */
    public Record(Header header) {
        this(header, null);
    }

    /**
     * Creates a new record.
     * 
     * @param header the header data
     * @param metadata the metadata
     */
    public Record(Header header, Metadata metadata) {
        this.header = header;
        this.metadata = metadata;
        this.aboutList = new ArrayList<>();
    }

    public Header getHeader() {
        return this.header;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public List<Element> getAboutList() {
        return this.aboutList;
    }

    public int hashCode() {
        return this.header.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Record && ((Record) obj).getHeader().equals(this.getHeader());
    }

    @Override
    public String toString() {
        return this.header.toString();
    }
}

