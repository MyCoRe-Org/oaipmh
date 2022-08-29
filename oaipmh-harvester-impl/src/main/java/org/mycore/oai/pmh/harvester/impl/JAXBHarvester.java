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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.oai.pmh.BadArgumentException;
import org.mycore.oai.pmh.BadResumptionTokenException;
import org.mycore.oai.pmh.CannotDisseminateFormatException;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.IdDoesNotExistException;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.NoMetadataFormatsException;
import org.mycore.oai.pmh.NoRecordsMatchException;
import org.mycore.oai.pmh.NoSetHierarchyException;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.OAIException;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.harvester.DataProviderConnection;
import org.mycore.oai.pmh.harvester.HarvestException;
import org.mycore.oai.pmh.harvester.Harvester;
import org.mycore.oai.pmh.harvester.HarvesterConfig;
import org.mycore.oai.pmh.jaxb.OAIPMHtype;

/**
 * JAXB implementation of a {@link Harvester}.
 * 
 * @author Matthias Eichner
 */
public class JAXBHarvester implements Harvester {

    private static Logger LOGGER = LogManager.getLogger();

    private String baseURL;

    private HarvesterConfig config;

    private JAXBContext jaxbContext;

    public JAXBHarvester(URL baseURL, HarvesterConfig config) throws JAXBException {
        this.baseURL = baseURL.toString();
        this.config = config;
        this.jaxbContext = JAXBContext.newInstance(OAIPMHtype.class);
    }

    @SuppressWarnings("unchecked")
    protected OAIPMHtype unmarshall(InputStream is) {
        try {
            Unmarshaller um = jaxbContext.createUnmarshaller();
            return ((JAXBElement<OAIPMHtype>) um.unmarshal(is)).getValue();
        } catch (JAXBException jaxbExc) {
            throw new HarvestException("while unmarshalling inputstream", jaxbExc);
        } finally {
            try {
                is.close();
            } catch (IOException ioExc) {
                LOGGER.error("while unmarshalling inputstream", ioExc);
            }
        }
    }

    @Override
    public Identify identify() {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.identify());
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertIdentify(oaipmh);
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Set> listSets() throws NoSetHierarchyException {
        try {
            return this.listSets(null);
        } catch (BadResumptionTokenException exc) {
            throw new HarvestException("Unexpected bad resumption token exception occur", exc);
        }
    }

    @Override
    public OAIDataList<Set> listSets(String resumptionToken) throws NoSetHierarchyException,
        BadResumptionTokenException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listSets(resumptionToken));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListSets(oaipmh);
        } catch (BadResumptionTokenException | NoSetHierarchyException e) { //NOPMD
            throw e;
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

    @Override
    public List<MetadataFormat> listMetadataFormats() {
        try {
            return this.listMetadataFormats(null);
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

    @Override
    public List<MetadataFormat> listMetadataFormats(String identifier) throws IdDoesNotExistException,
        NoMetadataFormatsException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listMetadataFormats(identifier));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListMetadataFormats(oaipmh);
        } catch (IdDoesNotExistException | NoMetadataFormatsException e) { //NOPMD
            throw e;
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Header> listIdentifiers(String metadataPrefix, String from, String until, String setSpec)
        throws BadArgumentException, CannotDisseminateFormatException, NoRecordsMatchException,
        NoSetHierarchyException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listIdentifiers(metadataPrefix, from, until, setSpec));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListIdentifiers(oaipmh);
        } catch (BadArgumentException | CannotDisseminateFormatException | NoRecordsMatchException //NOPMD
            | NoSetHierarchyException e) {
            throw e;
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Header> listIdentifiers(String resumptionToken) throws BadResumptionTokenException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listIdentifiers(resumptionToken));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListIdentifiers(oaipmh);
        } catch (BadResumptionTokenException e) { //NOPMD
            throw e;
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Record> listRecords(String metadataPrefix, String from, String until, String setSpec)
        throws BadArgumentException,
        CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listRecords(metadataPrefix, from, until, setSpec));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListRecords(oaipmh);
        } catch (BadArgumentException | CannotDisseminateFormatException | NoRecordsMatchException //NOPMD
            | NoSetHierarchyException e) {
            throw e;
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

    @Override
    public OAIDataList<Record> listRecords(String resumptionToken) throws BadResumptionTokenException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.listRecords(resumptionToken));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertListRecords(oaipmh);
        } catch (BadResumptionTokenException e) { //NOPMD
            throw e;
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

    @Override
    public Record getRecord(String identifier, String metadataPrefix) throws CannotDisseminateFormatException,
        IdDoesNotExistException {
        DataProviderConnection dp = new DataProviderConnection(this.baseURL);
        OAIPMHtype oaipmh = unmarshall(dp.getRecord(identifier, metadataPrefix));
        JAXBConverter converter = new JAXBConverter(this.config);
        try {
            return converter.convertGetRecord(oaipmh);
        } catch (CannotDisseminateFormatException | IdDoesNotExistException e) { //NOPMD
            throw e;
        } catch (OAIException e) {
            throw new HarvestException("Unexpected exception occur", e);
        }
    }

}
