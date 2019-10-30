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

import java.math.BigInteger;

import org.mycore.oai.pmh.DefaultResumptionToken;
import org.mycore.oai.pmh.ResumptionToken;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.jaxb.ResumptionTokenType;

/**
 * Abstract class for the ListRecords, ListIdentifiers and ListSets handlers implementation.
 * 
 * @author Matthias Eichner
 */
public abstract class ListRequestsHandler extends JAXBVerbHandler {

    public ListRequestsHandler(OAIAdapter oaiAdapter) {
        super(oaiAdapter);
    }

    protected ResumptionTokenType toJAXBResumptionToken(ResumptionToken resumptionToken) {
        ResumptionTokenType rtt = new ResumptionTokenType();
        rtt.setValue(resumptionToken.getToken());
        if (resumptionToken instanceof DefaultResumptionToken) {
            DefaultResumptionToken drt = (DefaultResumptionToken) resumptionToken;
            if (drt.getCompleteListSize() != null) {
                rtt.setCompleteListSize(BigInteger.valueOf(drt.getCompleteListSize()));
            }
            if (drt.getCursor() != null) {
                rtt.setCursor(BigInteger.valueOf(drt.getCursor()));
            }
            if (drt.getExpirationDate() != null) {
                rtt.setExpirationDate(drt.getExpirationDate());
            }
        }
        return rtt;
    }

}
