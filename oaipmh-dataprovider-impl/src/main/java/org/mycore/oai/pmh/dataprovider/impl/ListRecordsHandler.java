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

import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.dataprovider.OAIRequest;
import org.mycore.oai.pmh.jaxb.ListRecordsType;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;

public class ListRecordsHandler extends ListDataHandler {

    public ListRecordsHandler(OAIAdapter oaiAdapter) {
        super(oaiAdapter);
    }

    @Override
    public OAIPMHtype handle(OAIRequest request) throws OAIException {
        OAIDataList<? extends Record> recordList;
        // get header list from oai adapter
        if (request.isResumptionToken()) {
            recordList = this.oaiAdapter.getRecords(request.getResumptionToken());
        } else {
            MetadataFormat format = this.oaiAdapter.getMetadataFormat(request.getMetadataPrefix());
            Set set = null;
            if (request.isSet()) {
                set = this.oaiAdapter.getSet(request.getSet());
            }
            recordList = this.oaiAdapter.getRecords(format, set, request.getFrom(), request.getUntilCalculated());
        }

        // create jaxb element
        ListRecordsType listRecordsType = new ListRecordsType();
        // add records
        for (Record record : recordList) {
            listRecordsType.getRecord().add(JAXBUtils.toJAXBRecord(record, this.oaiAdapter.getIdentify()));
        }
        // set resumption token
        if (recordList.isResumptionTokenSet()) {
            listRecordsType.setResumptionToken(toJAXBResumptionToken(recordList.getResumptionToken()));
        }
        // return oaipmh
        OAIPMHtype oaipmh = new OAIPMHtype();
        oaipmh.setListRecords(listRecordsType);
        return oaipmh;
    }

}
