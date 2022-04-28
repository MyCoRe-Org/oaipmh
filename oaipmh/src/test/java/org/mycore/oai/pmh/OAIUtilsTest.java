package org.mycore.oai.pmh;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.junit.Test;

import static org.junit.Assert.*;

public class OAIUtilsTest {

    @Test
    public void jdomToDOM_withDoc() throws JDOMException {
        Document doc=new Document(new Element("junit"));
        final org.w3c.dom.Element domElement = OAIUtils.jdomToDOM(doc.getRootElement());
        assertEquals(doc.getRootElement().getName(), domElement.getNodeName());
        assertTrue(domElement.getOwnerDocument() != null);
    }
    @Test
    public void jdomToDOM_withoutDoc() throws JDOMException {
        final Element jdomElement = new Element("junit");
        final org.w3c.dom.Element domElement = OAIUtils.jdomToDOM(jdomElement);
        assertEquals(jdomElement.getName(), domElement.getNodeName());
        assertTrue(domElement.getOwnerDocument() != null);
    }
}
