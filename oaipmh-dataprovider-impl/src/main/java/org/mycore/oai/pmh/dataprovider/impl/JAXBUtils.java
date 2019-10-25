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
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.OAIUtils;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.dataprovider.OAIImplementationException;
import org.mycore.oai.pmh.jaxb.AboutType;
import org.mycore.oai.pmh.jaxb.HeaderType;
import org.mycore.oai.pmh.jaxb.MetadataType;
import org.mycore.oai.pmh.jaxb.RecordType;
import org.mycore.oai.pmh.jaxb.StatusType;

public abstract class JAXBUtils {

    protected final static Logger LOGGER = LogManager.getLogger(JAXBUtils.class);

    public static RecordType toJAXBRecord(Record record, Identify identify) {
        RecordType recordType = new RecordType();
        recordType.setHeader(toJAXBHeader(record.getHeader(), identify));

        if (record.getMetadata() != null) {
            try {
                MetadataType metadataType = new MetadataType();
                metadataType.setAny(OAIUtils.jdomToDOM(record.getMetadata().toXML()));
                recordType.setMetadata(metadataType);
            } catch (JDOMException exc) {
                throw new OAIImplementationException(exc);
            }
        }
        if (record.getAboutList() != null) {
            for (Element about : record.getAboutList()) {
                AboutType aboutType = new AboutType();
                try {
                    aboutType.setAny(OAIUtils.jdomToDOM(about));
                    recordType.getAbout().add(aboutType);
                } catch (JDOMException exc) {
                    throw new OAIImplementationException(exc);
                }
            }
        }
        return recordType;
    }

    public static HeaderType toJAXBHeader(Header header, Identify identify) {
        HeaderType headerType = new HeaderType();
        String id = header.getId();
        // check if id is correct
        if (!OAIUtils.checkIdentifier(id, identify)) {
            LOGGER.warn("invalid OAI-PMH id {}", id);
        }
        headerType.setIdentifier(id);
        headerType.setDatestamp(header.getDatestamp());
        if (header.isDeleted()) {
            headerType.setStatus(StatusType.DELETED);
        }
        for (Set set : header.getSetList()) {
            headerType.getSetSpec().add(set.getSpec());
        }
        return headerType;
    }

}
