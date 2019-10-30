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

import java.time.Instant;

import org.mycore.oai.pmh.BadVerbException;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.OAIException.ErrorCode;
import org.mycore.oai.pmh.Verb;
import org.mycore.oai.pmh.dataprovider.OAIAdapter;
import org.mycore.oai.pmh.dataprovider.OAIBaseProvider;
import org.mycore.oai.pmh.dataprovider.OAIRequest;
import org.mycore.oai.pmh.dataprovider.OAIResponse;
import org.mycore.oai.pmh.jaxb.OAIPMHerrorType;
import org.mycore.oai.pmh.jaxb.OAIPMHerrorcodeType;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;
import org.mycore.oai.pmh.jaxb.RequestType;
import org.mycore.oai.pmh.jaxb.VerbType;

/**
 * Implementation of a {@link OAIBaseProvider} using the JAXB API.
 * 
 * @author Matthias Eichner
 */
public class JAXBOAIProvider extends OAIBaseProvider<JAXBVerbHandler> {

    public JAXBOAIProvider(OAIAdapter oaiAdapter) {
        super(oaiAdapter);
    }

    public JAXBOAIProvider() {
        super();
    }

    @Override
    protected JAXBVerbHandler getVerbHandler(Verb verb) throws BadVerbException {
        if (Verb.GetRecord.equals(verb)) {
            return new GetRecordHandler(this.getAdapter());
        } else if (Verb.Identify.equals(verb)) {
            return new IdentifyHandler(this.getAdapter());
        } else if (Verb.ListIdentifiers.equals(verb)) {
            return new ListIdentifiersHandler(this.getAdapter());
        } else if (Verb.ListMetadataFormats.equals(verb)) {
            return new ListMetadataFormatsHandler(this.getAdapter());
        } else if (Verb.ListRecords.equals(verb)) {
            return new ListRecordsHandler(this.getAdapter());
        } else if (Verb.ListSets.equals(verb)) {
            return new ListSetsHandler(this.getAdapter());
        }
        throw new BadVerbException(verb.name());
    }

    @Override
    protected OAIResponse handle(OAIRequest request, JAXBVerbHandler verbHandler) throws OAIException {
        OAIPMHtype oaipmh = verbHandler.handle(request);
        oaipmh.setRequest(getRequestType(request, false));
        oaipmh.setResponseDate(getResponseDate());
        return new JAXBOAIResponse(oaipmh);
    }

    protected RequestType getRequestType(OAIRequest req, boolean errorOccur) {
        RequestType rt = new RequestType();
        rt.setValue(this.getAdapter().getIdentify().getBaseURL());
        if (!errorOccur) {
            rt.setVerb(VerbType.fromValue(req.getVerb()));
            rt.setIdentifier(req.getIdentifier());
            rt.setMetadataPrefix(req.getMetadataPrefix());
            rt.setFrom(req.getFrom());
            rt.setUntil(req.getUntil());
            rt.setSet(req.getSet());
            if (req.isResumptionToken()) {
                rt.setResumptionToken(req.getResumptionToken());
            }
        }
        return rt;
    }

    @Override
    protected OAIResponse getErrorResponse(OAIException oaiExc, OAIRequest request) {
        OAIPMHtype oaipmh = new OAIPMHtype();
        oaipmh.getError().add(getError(oaiExc.getCode(), oaiExc.getMessage()));
        oaipmh.setRequest(getRequestType(request, true));
        oaipmh.setResponseDate(getResponseDate());
        return new JAXBOAIResponse(oaipmh);
    }

    protected OAIPMHerrorType getError(ErrorCode errorCode, String errorDescription) {
        OAIPMHerrorType error = new OAIPMHerrorType();
        error.setCode(OAIPMHerrorcodeType.fromValue(errorCode.name()));
        error.setValue(errorDescription);
        return error;
    }

    protected Instant getResponseDate() {
        return Instant.now();
    }

}
