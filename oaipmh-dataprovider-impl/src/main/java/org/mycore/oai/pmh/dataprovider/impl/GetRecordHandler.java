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

import org.mycore.oai.pmh.Argument;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.dataprovider.OAIRequest;
import org.mycore.oai.pmh.dataprovider.OAIRequest.ArgumentType;
import org.mycore.oai.pmh.jaxb.GetRecordType;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;
import org.mycore.oai.pmh.jaxb.RecordType;

public class GetRecordHandler extends JAXBVerbHandler {

    private static Map<Argument, ArgumentType> ARGUMENT_MAP;

    static {
        ARGUMENT_MAP = new HashMap<>();
        ARGUMENT_MAP.put(Argument.identifier, ArgumentType.required);
        ARGUMENT_MAP.put(Argument.metadataPrefix, ArgumentType.required);
    }

    public GetRecordHandler(OAIAdapter oaiAdapter) {
        super(oaiAdapter);
    }

    @Override
    public Map<Argument, ArgumentType> getArgumentMap() {
        return ARGUMENT_MAP;
    }

    @Override
    public OAIPMHtype handle(OAIRequest request) throws OAIException {
        String id = request.getIdentifier();
        MetadataFormat mf = this.oaiAdapter.getMetadataFormat(request.getMetadataPrefix());
        Record record = this.oaiAdapter.getRecord(id, mf);

        // convert record to recordtype
        RecordType recordType = JAXBUtils.toJAXBRecord(record, this.oaiAdapter.getIdentify());
        GetRecordType getRecordType = new GetRecordType();
        getRecordType.setRecord(recordType);

        // return oaipmh
        OAIPMHtype oaipmh = new OAIPMHtype();
        oaipmh.setGetRecord(getRecordType);
        return oaipmh;
    }

}
