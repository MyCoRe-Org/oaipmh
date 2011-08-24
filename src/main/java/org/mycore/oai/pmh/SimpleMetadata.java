package org.mycore.oai.pmh;

import org.jdom.Element;

/**
 * Simple implementation of the {@link Metadata} interface.
 * 
 * @author Matthias Eichner
 */
public class SimpleMetadata implements Metadata {

    private Element metadataElement;

    public SimpleMetadata(Element metadataElement) {
        this.metadataElement = metadataElement;
    }

    @Override
    public Element toXML() {
        return this.metadataElement;
    }

    public void setMetadataElement(Element metadataElement) {
        this.metadataElement = metadataElement;
    }

}
