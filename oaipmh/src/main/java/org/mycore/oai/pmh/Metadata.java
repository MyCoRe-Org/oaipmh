package org.mycore.oai.pmh;

import org.jdom2.Element;

/**
 * The internal metadata representation of a OAI-PMH xml response. At a minimum, repositories must be able to return records with metadata expressed in the
 * Dublin Core format, without any qualification. Optionally, a repository may also disseminate other formats of metadata. The specific metadata format of the
 * record to be disseminated is specified by means of an argument -- the metadataPrefix -- in the GetRecord or ListRecords request that produces the record. The
 * ListMetadataFormats request returns the list of all metadata formats available from a repository, or for a specific item (which can be specified as an
 * argument to the ListMetadataFormats request).
 * 
 * @author Matthias Eichner
 */
public interface Metadata {

    /**
     * Returns the metadata as {@link Element}.
     * 
     * @return metadata as jdom
     */
    Element toXML();

}
