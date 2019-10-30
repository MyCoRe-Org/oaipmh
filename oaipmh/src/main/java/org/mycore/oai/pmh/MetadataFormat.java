package org.mycore.oai.pmh;

import org.jdom2.Namespace;
import org.mycore.oai.pmh.dc.DCMetadataFormat;

/**
 * MetadataFormat represents a metadata format with its prefix, namespace and schema.
 * OAI-PMH supports the dissemination of records in multiple metadata formats
 * from a repository.
 * 
 * @see DCMetadataFormat
 * @author Matthias Eichner
 */
public class MetadataFormat {

    protected Namespace namespace;

    protected String schema;

    /**
     * Creates a new metadata format.
     * 
     * @param prefix
     *            prefix of the metadata format
     * @param namespaceURI
     *            uri as string
     * @param schema
     *            schema url
     */
    public MetadataFormat(String prefix, String namespaceURI, String schema) {
        this(Namespace.getNamespace(prefix, namespaceURI), schema);
    }

    /**
     * Creates a new metadata format.
     * 
     * @param namespace jdom namespace
     * @param schema the schema
     */
    public MetadataFormat(Namespace namespace, String schema) {
        this.schema = schema;
        this.namespace = namespace;
    }

    /**
     * Returns the unique metadata prefix, a string to specify the metadata format in OAI-PMH requests
     * issued to the repository.
     * The prefix consists of any valid URI unreserved characters.
     * metadataPrefix arguments are used in ListRecords, ListIdentifiers, and GetRecord requests to retrieve records,
     * or the headers of records that include metadata in the format specified by the metadataPrefix
     * 
     * @return prefix of the namespace
     */
    public String getPrefix() {
        return this.namespace.getPrefix();
    }

    /**
     * Returns the schema uri.
     * 
     * @return schema as string
     */
    public String getSchema() {
        return this.schema;
    }

    /**
     * Returns the namespace uri.
     * 
     * @return namespace uri as string
     */
    public String getNamespace() {
        return this.namespace.getURI();
    }

    public int hashCode() {
        return namespace.hashCode();
    }

    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof MetadataFormat)) {
            return false;
        } else {
            return ((MetadataFormat) obj).namespace.equals(this.namespace);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return namespace.getPrefix() + " (" + this.namespace.getURI() + ")";
    }
}

