package org.mycore.oai.pmh.dc;

import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.OAIConstants;

/**
 * For purposes of interoperability, repositories must disseminate Dublin Core, without any qualification. Therefore, the protocol reserves the metadataPrefix
 * 'oai_dc', and the URL of a metadata schema for unqualified Dublin Core, which is http://www.openarchives.org/OAI/2.0/oai_dc.xsd. The corresponding XML
 * namespace URI is http://www.openarchives.org/OAI/2.0/oai_dc/.
 * 
 * @author Matthias Eichner
 */
public class DCMetadataFormat extends MetadataFormat {

    public DCMetadataFormat() {
        super(OAIConstants.NS_OAI_DC, OAIConstants.SCHEMA_DC);
    }

}
