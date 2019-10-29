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

import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.dataprovider.OAIRequest;
import org.mycore.oai.pmh.jaxb.ListIdentifiersType;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;

public class ListIdentifiersHandler extends ListDataHandler {

    public ListIdentifiersHandler(OAIAdapter oaiAdapter) {
        super(oaiAdapter);
    }

    @Override
    public OAIPMHtype handle(OAIRequest request) throws OAIException {
        OAIDataList<? extends Header> headerList;
        // get header list from oai adapter
        if (request.isResumptionToken()) {
            headerList = this.oaiAdapter.getHeaders(request.getResumptionToken());
        } else {
            MetadataFormat format = this.oaiAdapter.getMetadataFormat(request.getMetadataPrefix());
            Set set = null;
            if (request.isSet()) {
                set = this.oaiAdapter.getSet(request.getSet());
            }
            headerList = this.oaiAdapter.getHeaders(format, set, request.getFrom(), request.getUntilCalculated());
        }

        // create jaxb element
        ListIdentifiersType listIdentifiersType = new ListIdentifiersType();
        // add headers
        for (Header header : headerList) {
            listIdentifiersType.getHeader().add(JAXBUtils.toJAXBHeader(header, this.oaiAdapter.getIdentify()));
        }
        // set resumption token
        if (headerList.isResumptionTokenSet()) {
            listIdentifiersType.setResumptionToken(toJAXBResumptionToken(headerList.getResumptionToken()));
        }
        // return oaipmh
        OAIPMHtype oaipmh = new OAIPMHtype();
        oaipmh.setListIdentifiers(listIdentifiersType);
        return oaipmh;
    }

}
