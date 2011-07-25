package org.mycore.oai.pmh;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * The OAI identifier format is intended to provide persistent resource
 * identifiers for items in repositories that implement OAI-PMH. This is just
 * one possible format that may be used for identifiers within OAI-PMH.
 * 
 * @author Matthias Eichner
 */
public class OAIIdentifierDescription implements Description {

    final static Namespace NS_XSI = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	final static Namespace NS_OAI_ID = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/oai-identifier");
    final static String SCHEMA_LOC_OAI_ID = "http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd";

	final static String SCHEME = "oai";
	final static String DELIMITER = ":";

	private String repositoryIdentifier;
	private String sampleId;

	public OAIIdentifierDescription(String repositoryIdentifier, String sampleId) {
		this.repositoryIdentifier = repositoryIdentifier;
		this.sampleId = sampleId;
	}

	@Override
	public Element toXML() {
        Element oaiIdentifier = new Element("oai-identifier", NS_OAI_ID);
        oaiIdentifier.setAttribute("schemaLocation", SCHEMA_LOC_OAI_ID, NS_XSI);
        oaiIdentifier.addNamespaceDeclaration(NS_XSI);
        oaiIdentifier.addContent(new Element("scheme", NS_OAI_ID).setText(SCHEME));
        oaiIdentifier.addContent(new Element("repositoryIdentifier", NS_OAI_ID).setText(this.repositoryIdentifier));
        oaiIdentifier.addContent(new Element("delimiter", NS_OAI_ID).setText(DELIMITER));
        oaiIdentifier.addContent(new Element("sampleIdentifier", NS_OAI_ID).setText(getSampleIdentifier()));
        return oaiIdentifier;
	}

	public String getPrefix() {
        StringBuffer prefix = new StringBuffer(SCHEME).append(DELIMITER);
        prefix.append(this.repositoryIdentifier).append(DELIMITER);
        return prefix.toString();
	}

	public String getSampleIdentifier() {
        StringBuffer sId = new StringBuffer(getPrefix());
        sId.append(this.sampleId);
        return sId.toString();
	}

	public String getRepositoryIdentifier() {
        return repositoryIdentifier;
    }

	public static String getDelimiter() {
        return DELIMITER;
    }

	public boolean isValid(String id) {
	    // TODO check pattern oai:[a-zA-Z][a-zA-Z0-9\-]*(\.[a-zA-Z][a-zA-Z0-9\-]*)+:[a-zA-Z0-9\-_\.!~\*'\(\);/\?:@&=\+$,%]+
	    return id != null && id.startsWith(getPrefix());
	}

	@Override
	public String toString() {
		return getSampleIdentifier();
	}
}
