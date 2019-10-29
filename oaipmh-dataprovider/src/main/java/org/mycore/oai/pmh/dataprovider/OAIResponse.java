package org.mycore.oai.pmh.dataprovider;

import org.jdom2.Element;
import org.jdom2.JDOMException;

/**
 * OAI-PMH response abstraction. A <code>OAIResponse</code> provides methods to get the generated xml of an
 * {@link OAIProvider}.
 *
 * @author Matthias Eichner
 */
public interface OAIResponse {

    enum Format {
        unformatted, formatted
    }

    /**
     * Returns the oai pmh xml as jdom.
     * 
     * @return oai pmh as jdom
     * @throws JDOMException if a parsing exception occur
     */
    Element toXML() throws JDOMException;

    /**
     * Returns the oai pmh xml as unformatted text.
     * 
     * @return oai pmh response as string
     */
    String toString();

    /**
     * Returns the oai pmh xml as formatted text.
     * 
     * @param format format of the text
     * @return oai pmh response as string
     */
    String toString(Format format);

}
