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

package org.mycore.oai.pmh.harvester.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.oai.pmh.BadArgumentException;
import org.mycore.oai.pmh.BadResumptionTokenException;
import org.mycore.oai.pmh.BadVerbException;
import org.mycore.oai.pmh.CannotDisseminateFormatException;
import org.mycore.oai.pmh.DefaultResumptionToken;
import org.mycore.oai.pmh.Description;
import org.mycore.oai.pmh.Granularity;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.IdDoesNotExistException;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.Identify.DeletedRecordPolicy;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.NoMetadataFormatsException;
import org.mycore.oai.pmh.NoRecordsMatchException;
import org.mycore.oai.pmh.NoSetHierarchyException;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.OAIUtils;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.ResumptionToken;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.SimpleIdentify;
import org.mycore.oai.pmh.SimpleMetadata;
import org.mycore.oai.pmh.harvester.HarvestException;
import org.mycore.oai.pmh.harvester.HarvesterConfig;
import org.mycore.oai.pmh.jaxb.AboutType;
import org.mycore.oai.pmh.jaxb.DescriptionType;
import org.mycore.oai.pmh.jaxb.GetRecordType;
import org.mycore.oai.pmh.jaxb.HeaderType;
import org.mycore.oai.pmh.jaxb.IdentifyType;
import org.mycore.oai.pmh.jaxb.ListIdentifiersType;
import org.mycore.oai.pmh.jaxb.ListMetadataFormatsType;
import org.mycore.oai.pmh.jaxb.ListRecordsType;
import org.mycore.oai.pmh.jaxb.ListSetsType;
import org.mycore.oai.pmh.jaxb.MetadataFormatType;
import org.mycore.oai.pmh.jaxb.OAIPMHerrorType;
import org.mycore.oai.pmh.jaxb.OAIPMHerrorcodeType;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;
import org.mycore.oai.pmh.jaxb.RecordType;
import org.mycore.oai.pmh.jaxb.ResumptionTokenType;
import org.mycore.oai.pmh.jaxb.SetType;
import org.mycore.oai.pmh.jaxb.StatusType;
import org.w3c.dom.Element;

public class JAXBConverter {

    private static Logger LOGGER = LogManager.getLogger();

    private HarvesterConfig config;

    public JAXBConverter(HarvesterConfig config) {
        this.config = config;
    }

    protected void handleOAIException(OAIPMHtype oaipmh) throws OAIException {
        List<OAIPMHerrorType> errorList = oaipmh.getError();
        if (errorList.size() <= 0) {
            return;
        }
        OAIPMHerrorType firstError = errorList.get(0);
        OAIPMHerrorcodeType errorCode = firstError.getCode();
        String msg = firstError.getValue();
        if (OAIPMHerrorcodeType.BAD_ARGUMENT.equals(errorCode)) {
            throw new BadArgumentException(msg);
        } else if (OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN.equals(errorCode)) {
            throw new BadResumptionTokenException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.BAD_VERB.equals(errorCode)) {
            throw new BadVerbException(msg);
        } else if (OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT.equals(errorCode)) {
            throw new CannotDisseminateFormatException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.ID_DOES_NOT_EXIST.equals(errorCode)) {
            throw new IdDoesNotExistException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.NO_METADATA_FORMATS.equals(errorCode)) {
            throw new NoMetadataFormatsException().setMessage(msg);
        } else if (OAIPMHerrorcodeType.NO_RECORDS_MATCH.equals(errorCode)) {
            throw new NoRecordsMatchException(msg);
        } else if (OAIPMHerrorcodeType.NO_SET_HIERARCHY.equals(errorCode)) {
            throw new NoSetHierarchyException().setMessage(msg);
        }
        throw new HarvestException("Unknown oai exception occur: " + msg);
    }

    public Identify convertIdentify(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        IdentifyType idType = oaipmh.getIdentify();
        if (idType == null) {
            throw new HarvestException("Identify is empty");
        }
        // create new identify
        SimpleIdentify id = new SimpleIdentify();
        id.setBaseURL(idType.getBaseURL());
        id.setDeletedRecordPolicy(DeletedRecordPolicy.get(idType.getDeletedRecord().name()));
        id.setEarliestDatestamp(idType.getEarliestDatestamp());
        id.setGranularity(Granularity.valueOf(idType.getGranularity().name()));
        id.setProtocolVersion(idType.getProtocolVersion());
        id.setRepositoryName(idType.getRepositoryName());
        for (String mail : idType.getAdminEmail()) {
            id.getAdminEmailList().add(mail);
        }
        // add descriptions
        id.getDescriptionList().addAll(convertDescriptionList(idType.getDescription()));
        return id;
    }

    public OAIDataList<Set> convertListSets(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        ListSetsType listSetsType = oaipmh.getListSets();
        if (listSetsType == null) {
            throw new HarvestException("ListSets is empty");
        }
        // create new list sets
        OAIDataList<Set> setList = new OAIDataList<>();
        for (SetType setType : listSetsType.getSet()) {
            Set set = new Set(setType.getSetSpec(), setType.getSetName());
            set.getDescription().addAll(convertDescriptionList(setType.getSetDescription()));
            setList.add(set);
        }
        ResumptionToken rsToken = convertResumptionToken(listSetsType.getResumptionToken());
        setList.setResumptionToken(rsToken);
        return setList;
    }

    public List<MetadataFormat> convertListMetadataFormats(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        ListMetadataFormatsType listMetadataFormatsType = oaipmh.getListMetadataFormats();
        if (listMetadataFormatsType == null) {
            throw new HarvestException("ListMetadataFormats is empty");
        }
        // create new list metadata formats
        List<MetadataFormat> metadataFormatList = new ArrayList<>();
        for (MetadataFormatType mft : listMetadataFormatsType.getMetadataFormat()) {
            MetadataFormat mf = new MetadataFormat(mft.getMetadataPrefix(), mft.getMetadataNamespace(),
                mft.getSchema());
            metadataFormatList.add(mf);
        }
        return metadataFormatList;
    }

    public OAIDataList<Header> convertListIdentifiers(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        ListIdentifiersType listIdentifiersType = oaipmh.getListIdentifiers();
        if (listIdentifiersType == null) {
            throw new HarvestException("ListIdentifiers is empty");
        }
        // create new list identifiers
        OAIDataList<Header> headerList = new OAIDataList<>();
        for (HeaderType headerType : listIdentifiersType.getHeader()) {
            headerList.add(convertHeader(headerType));
        }
        ResumptionToken rsToken = convertResumptionToken(listIdentifiersType.getResumptionToken());
        headerList.setResumptionToken(rsToken);
        return headerList;
    }

    public OAIDataList<Record> convertListRecords(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        ListRecordsType listRecordsType = oaipmh.getListRecords();
        if (listRecordsType == null) {
            throw new HarvestException("ListRecords is empty");
        }
        // create new list identifiers
        OAIDataList<Record> recordList = new OAIDataList<>();
        for (RecordType recordType : listRecordsType.getRecord()) {
            recordList.add(convertRecord(recordType));
        }
        ResumptionToken rsToken = convertResumptionToken(listRecordsType.getResumptionToken());
        recordList.setResumptionToken(rsToken);
        return recordList;
    }

    public Record convertGetRecord(OAIPMHtype oaipmh) throws OAIException {
        // first handle possible exception
        handleOAIException(oaipmh);
        GetRecordType recordType = oaipmh.getGetRecord();
        if (recordType == null) {
            throw new HarvestException("GetRecord is empty");
        }
        // create new record
        return convertRecord(recordType.getRecord());
    }

    private Header convertHeader(HeaderType headerType) {
        Header header = new Header(headerType.getIdentifier(), headerType.getDatestamp());
        if (headerType.getStatus() != null && headerType.getStatus().equals(StatusType.DELETED)) {
            header.setDeleted(true);
        }
        for (String spec : headerType.getSetSpec()) {
            header.getSetList().add(new Set(spec));
        }
        return header;
    }

    private Record convertRecord(RecordType recordType) {
        Header header = convertHeader(recordType.getHeader());
        Record record = new Record(header);
        if (recordType.getMetadata() != null) {
            Element domElement = (Element) recordType.getMetadata().getAny();
            if (domElement != null) {
                record.setMetadata(new SimpleMetadata(OAIUtils.domToJDOM(domElement)));
            }
        }
        for (AboutType aboutType : recordType.getAbout()) {
            Element domElement = (Element) aboutType.getAny();
            record.getAboutList().add(OAIUtils.domToJDOM(domElement));
        }
        return record;
    }

    private List<Description> convertDescriptionList(List<DescriptionType> descriptionTypeList) {
        List<Description> descriptionList = new ArrayList<>();
        for (DescriptionType descType : descriptionTypeList) {
            Element domElement = (Element) descType.getAny();
            String name = domElement.getLocalName();
            Description description = this.config.createNewDescriptionInstance(name);
            if (description == null) {
                LOGGER.warn(
                    "Unable to find matching description for '{}'. Use HarvesterConfig#registerDescription() to add one.",
                    name);
                continue;
            }
            description.fromXML(OAIUtils.domToJDOM(domElement));
            descriptionList.add(description);
        }
        return descriptionList;
    }

    private ResumptionToken convertResumptionToken(ResumptionTokenType rsTokenType) {
        if (rsTokenType == null) {
            return null;
        }
        DefaultResumptionToken rsToken = new DefaultResumptionToken();
        rsToken.setToken("".equals(rsTokenType.getValue()) ? null : rsTokenType.getValue());
        if (rsTokenType.getCompleteListSize() != null) {
            rsToken.setCompleteListSize(rsTokenType.getCompleteListSize().intValue());
        }
        if (rsTokenType.getCursor() != null) {
            rsToken.setCursor(rsTokenType.getCursor().intValue());
        }
        if (rsTokenType.getExpirationDate() != null) {
            rsToken.setExpirationDate(rsTokenType.getExpirationDate());
        }
        return rsToken;
    }

}
