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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * This filter removes the annoying empty xmlns tags and auto generated namespaces by jaxb.
 * 
 * with filter:
 * &lt;mets:dmdSec ID="MY_DMD_SEC_ID"&gt;
 * 
 * without filter:
 * &lt;mets:dmdSec ID="MY_DMD_SEC_ID" xmlns="" xmlns:ns4="http://www.openarchives.org/OAI/2.0/"&gt;
 *
 * @author Matthias Eichner
 */
public class NamespaceFilter extends XMLFilterImpl {

    private static Logger LOGGER = LogManager.getLogger(NamespaceFilter.class);

    private String xmlns = "";

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        LOGGER.debug("start element ({}): {} {} {}", xmlns, uri, localName, qName);
        if (uri.equals(xmlns)) {
            super.startElement(uri, localName, localName, atts);
        } else {
            super.startElement(uri, localName, qName, atts);
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        LOGGER.debug("start prefix ({}): {} {}", xmlns, prefix, uri);
        if (uri == null || uri.equals("")) {
            return;
        }
        if (prefix == null || prefix.length() == 0) {
            xmlns = uri;
        } else if (!uri.equals(xmlns)) {
            super.startPrefixMapping(prefix, uri);
        }
    }

    @Override
    public void endPrefixMapping(String prefix) {
        LOGGER.debug("end prefix ({}): {}", xmlns, prefix);
    }

}
