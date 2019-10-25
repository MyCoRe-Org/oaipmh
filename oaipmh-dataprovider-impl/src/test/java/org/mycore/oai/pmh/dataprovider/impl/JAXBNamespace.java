/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mycore.oai.pmh.dataprovider.impl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class JAXBNamespace {
    
    private static JAXBContext jaxbContext;
 
    private static Marshaller marshaller;
 
    static {
        try {
            jaxbContext = JAXBContext.newInstance(TestJAXB.class);
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    public void nsTest() throws Exception {
        // create dom
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document doc = builder.newDocument();
        org.w3c.dom.Element e = doc.createElement("dom");
        e.setAttribute("xmlns", "www.google.de");
        doc.appendChild(e);
        // print dom
        System.out.println("So möchte ich das haben, ein Element ohne Prefix mit Namespace Attribut");
        xmlOut(doc); // perfect - only one xmlns tag
        System.out.println();
        System.out.println();
 
        // create jaxb stuff
        TestJAXB testJAXB = new TestJAXB();
        testJAXB.desciption.any = e;
        System.out.println("Und hier mit Prefix 'dom' und extra Namespace 'xmlns:dom'");
        marshaller.marshal(testJAXB, System.out); // why dom:dom and xmlns:dom? 
    }
 
    @XmlRootElement(name="test")
    public static class TestJAXB {
        public DescriptionType desciption = new DescriptionType();
    }
 
    @XmlType(name = "descriptionType")
    public static class DescriptionType {
        @XmlAnyElement(lax = false)
        public Object any;
    }
 
    public static void xmlOut(org.w3c.dom.Document doc) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Source source = new DOMSource(doc);
        Result output = new StreamResult(System.out);
        transformer.transform(source, output);
    }
 
    public static void main(String[] args) throws Exception {
        JAXBNamespace nsTest = new JAXBNamespace();
        nsTest.nsTest();
    }
}
