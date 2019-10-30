package org.mycore.oai.pmh.dc;

import org.jdom2.Element;
import org.mycore.oai.pmh.Metadata;
import org.mycore.oai.pmh.OAIConstants;

/**
 * OAI-PMH metadata of Dublin Core. Overwrite this class to create your own DC implementation.
 * 
 * @author Matthias Eichner
 */
public abstract class DCMetadata implements Metadata {

    @Override
    public Element toXML() {
        Element oaiDc = new Element("dc", OAIConstants.NS_OAI_DC);
        oaiDc.addNamespaceDeclaration(OAIConstants.NS_DC);
        oaiDc.setAttribute("schemaLocation", OAIConstants.SCHEMA_LOC_OAI_DC, OAIConstants.NS_XSI);
        return oaiDc;
    }

}

