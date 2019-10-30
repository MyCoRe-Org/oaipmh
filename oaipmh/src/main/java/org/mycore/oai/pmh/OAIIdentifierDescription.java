package org.mycore.oai.pmh;

import org.jdom2.Element;

/**
 * The OAI identifier format is intended to provide persistent resource identifiers for items in repositories that
 * implement OAI-PMH. This is just one possible format that may be used for identifiers within OAI-PMH.
 * 
 * @author Matthias Eichner
 */
public class OAIIdentifierDescription implements Description {

    private final static String SCHEME = "oai";

    private final static String DELIMITER = ":";

    private String repositoryIdentifier;

    private String sampleId;

    public OAIIdentifierDescription() {
        this("", "");
    }

    public OAIIdentifierDescription(String repositoryIdentifier, String sampleId) {
        this.repositoryIdentifier = repositoryIdentifier;
        this.sampleId = sampleId;
    }

    @Override
    public Element toXML() {
        Element oaiIdentifier = new Element("oai-identifier", OAIConstants.NS_OAI_ID);
        oaiIdentifier.setAttribute("schemaLocation", OAIConstants.SCHEMA_LOC_OAI_ID, OAIConstants.NS_XSI);
        oaiIdentifier.addNamespaceDeclaration(OAIConstants.NS_XSI);
        oaiIdentifier.addContent(new Element("scheme", OAIConstants.NS_OAI_ID).setText(SCHEME));
        oaiIdentifier
            .addContent(new Element("repositoryIdentifier", OAIConstants.NS_OAI_ID).setText(this.repositoryIdentifier));
        oaiIdentifier.addContent(new Element("delimiter", OAIConstants.NS_OAI_ID).setText(DELIMITER));
        oaiIdentifier
            .addContent(new Element("sampleIdentifier", OAIConstants.NS_OAI_ID).setText(getSampleIdentifier()));
        return oaiIdentifier;
    }

    @Override
    public void fromXML(Element oaiIdentifier) {
        this.repositoryIdentifier = oaiIdentifier.getChildText("repositoryIdentifier", OAIConstants.NS_OAI_ID);
        this.sampleId = oaiIdentifier.getChildText("sampleIdentifier", OAIConstants.NS_OAI_ID);
    }

    public String getPrefix() {
        return SCHEME + DELIMITER + this.repositoryIdentifier + DELIMITER;
    }

    public String getSampleIdentifier() {
        return getPrefix() + this.sampleId;
    }

    public String getRepositoryIdentifier() {
        return repositoryIdentifier;
    }

    public static String getDelimiter() {
        return DELIMITER;
    }

    public boolean isValid(String id) {
        // TODO check pattern
        // oai:[a-zA-Z][a-zA-Z0-9\-]*(\.[a-zA-Z][a-zA-Z0-9\-]*)+:[a-zA-Z0-9\-_\.!~\*'\(\);/\?:@&=\+$,%]+
        return id != null && id.startsWith(getPrefix());
    }

    @Override
    public String toString() {
        return getSampleIdentifier();
    }
}
