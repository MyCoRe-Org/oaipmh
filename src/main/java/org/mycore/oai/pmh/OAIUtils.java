package org.mycore.oai.pmh;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.output.DOMOutputter;

/**
 * Provides some usefull OAI-PMH util methods.
 * 
 * @author Matthias Eichner
 */
public abstract class OAIUtils {

    /**
     * Checks if the identifier is valid. For more information see <a
     * href="http://www.openarchives.org/OAI/openarchivesprotocol.html#UniqueIdentifier">here</a>.
     * 
     * @param identifier id to check
     * @param identify identify object which can contain a {@link OAIIdentifierDescription}
     * @return true if identifier is valid, otherwise false
     */
    public static boolean checkIdentifier(String identifier, Identify identify) {
        OAIIdentifierDescription idDescription = getIdentifierDescription(identify);
        if (idDescription != null) {
            return idDescription.isValid(identifier);
        } else {
            return identifier.startsWith("oai:");
        }
    }

    /**
     * Returns the identifier description of the identify object or null, if no
     * identifier description is set.
     * 
     * @param identify the OAI-PMH identify object
     * @return instance of <code>OAIIdentifierDescription</code> or null
     */
    public static OAIIdentifierDescription getIdentifierDescription(Identify identify) {
        for (Description d : identify.getDescriptionList()) {
            if (d instanceof OAIIdentifierDescription)
                return (OAIIdentifierDescription) d;
        }
        return null;
    }

    /**
     * Converts a jdom element to a org.w3c.dom.Element. Be aware that the element already
     * has a document as parent.
     * 
     * @param jdomElement the jdom element to convert
     * @return new w3c dom element
     * @throws JDOMException if a parsing exception occur
     */
    public static org.w3c.dom.Element jdomToDOM(Element jdomElement) throws JDOMException {
        DOMOutputter outputter = new DOMOutputter();
        org.w3c.dom.Document doc = outputter.output(new Document(jdomElement));
        return doc.getDocumentElement();
    }

    /**
     * Converts a org.w3c.dom.Element element to a org.jdom.Element.
     * 
     * @param domElement org.w3c.dom.Element object
     * @return Element - JDOM Element object
     */
    public static Element domToJDOM(org.w3c.dom.Element domElement) {
        DOMBuilder builder = new DOMBuilder();
        return builder.build(domElement);
    }
}
