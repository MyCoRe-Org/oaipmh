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

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.logging.log4j.LogManager;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.output.XMLOutputter;
import org.mycore.oai.pmh.OAIConstants;
import org.mycore.oai.pmh.dataprovider.OAIImplementationException;
import org.mycore.oai.pmh.dataprovider.OAIResponse;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;

/**
 * JAXB implementation of a OAI response.
 *
 * @author Matthias Eichner
 */
public class JAXBOAIResponse implements OAIResponse {

    private static JAXBContext jaxbContext;

    static {
        try {
            jaxbContext = JAXBContext.newInstance(OAIPMHtype.class);
        } catch (Exception exc) {
            LogManager.getLogger().error("Unable to create jaxb context for OAIPMHType", exc);
        }
    }

    private OAIPMHtype oaipmh;

    public JAXBOAIResponse(OAIPMHtype oaipmh) {
        this.oaipmh = oaipmh;
    }

    private Marshaller createMarshaller() throws JAXBException {
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, OAIConstants.SCHEMA_LOC_OAI);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        return marshaller;
    }

    /**
     * Converts the oaipmh pojo to a jdom representation.
     *
     * @return the oaipmh as jdom
     * @throws JDOMException the conversion went wrong
     */
    private Element marshal() throws JDOMException {
        try {
            NamespaceFilter outFilter = new NamespaceFilter();
            SAXHandler saxHandler = new SAXHandler();
            outFilter.setContentHandler(saxHandler);
            Marshaller marshaller = createMarshaller();
            marshaller.marshal(
                new JAXBElement<>(new QName(OAIConstants.NS_OAI.getURI(), OAIConstants.XML_OAI_ROOT), OAIPMHtype.class,
                    this.oaipmh),
                outFilter);
            Element rootElement = saxHandler.getDocument().detachRootElement();
            return rootElement;
        } catch (Exception exc) {
            throw new JDOMException("while marshalling", exc);
        }
    }

    @Override
    public Element toXML() throws JDOMException {
        return marshal();
    }

    @Override
    public String toString() {
        return toString(Format.unformatted);
    }

    @Override
    public String toString(Format format) {
        try {
            Element e = marshal();
            XMLOutputter out = new XMLOutputter(
                Format.formatted.equals(format) ? org.jdom2.output.Format.getPrettyFormat()
                    : org.jdom2.output.Format.getRawFormat());
            StringWriter writer = new StringWriter();
            out.output(e, writer);
            return writer.toString();
        } catch (Exception exc) {
            throw new OAIImplementationException(exc);
        }
    }

}
