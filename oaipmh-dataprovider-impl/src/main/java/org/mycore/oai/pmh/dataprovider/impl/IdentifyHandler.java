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

import java.util.HashMap;
import java.util.Map;

import org.jdom2.JDOMException;
import org.mycore.oai.pmh.Argument;
import org.mycore.oai.pmh.Description;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.OAIUtils;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.dataprovider.OAIImplementationException;
import org.mycore.oai.pmh.dataprovider.OAIRequest;
import org.mycore.oai.pmh.dataprovider.OAIRequest.ArgumentType;
import org.mycore.oai.pmh.jaxb.DeletedRecordType;
import org.mycore.oai.pmh.jaxb.DescriptionType;
import org.mycore.oai.pmh.jaxb.GranularityType;
import org.mycore.oai.pmh.jaxb.IdentifyType;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;

public class IdentifyHandler extends JAXBVerbHandler {

    public IdentifyHandler(OAIAdapter oaiAdapter) {
        super(oaiAdapter);
    }

    @Override
    public Map<Argument, ArgumentType> getArgumentMap() {
        return new HashMap<>();
    }

    @Override
    public OAIPMHtype handle(OAIRequest request) {
        Identify id = this.oaiAdapter.getIdentify();

        IdentifyType returnId = new IdentifyType();
        returnId.setRepositoryName(id.getRepositoryName());
        returnId.setBaseURL(id.getBaseURL());
        returnId.setProtocolVersion(id.getProtocolVersion());
        returnId.setEarliestDatestamp(id.getEarliestDatestamp());
        returnId.setDeletedRecord(DeletedRecordType.fromValue(id.getDeletedRecordPolicy().value()));
        returnId.setGranularity(GranularityType.valueOf(id.getGranularity().name()));
        for (String adminMail : id.getAdminEmailList()) {
            returnId.getAdminEmail().add(adminMail);
        }
        for (Description description : id.getDescriptionList()) {
            try {
                DescriptionType descriptionType = new DescriptionType();
                descriptionType.setAny(OAIUtils.jdomToDOM(description.toXML()));
                returnId.getDescription().add(descriptionType);
            } catch (JDOMException exc) {
                throw new OAIImplementationException(exc);
            }
        }
        OAIPMHtype oaipmh = new OAIPMHtype();
        oaipmh.setIdentify(returnId);
        return oaipmh;
    }

}
