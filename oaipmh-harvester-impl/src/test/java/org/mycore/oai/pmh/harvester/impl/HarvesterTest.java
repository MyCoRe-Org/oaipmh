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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mycore.oai.pmh.Header;
import org.mycore.oai.pmh.Identify;
import org.mycore.oai.pmh.MetadataFormat;
import org.mycore.oai.pmh.OAIDataList;
import org.mycore.oai.pmh.Record;
import org.mycore.oai.pmh.ResumptionToken;
import org.mycore.oai.pmh.Set;
import org.mycore.oai.pmh.harvester.Harvester;
import org.mycore.oai.pmh.harvester.HarvesterBuilder;

public class HarvesterTest {

    private String testUrl = "https://pub.uni-bielefeld.de/oai";

    private SimpleOAIProvider oaiProvider;

    private static String TEST_SET = "doc-type:doctoralThesis";

    private Harvester harvester;

    @Before
    public void setUp() throws IOException {
        oaiProvider = new SimpleOAIProvider();
        testUrl = oaiProvider.getBaseURI() + "/oai";
        assertTrue(HarvesterBuilder.getHarvesterFactory().isPresent());
        assertEquals(HarvesterBuilder.getHarvesterFactory().get().getClass(), JAXBHarvesterFactory.class);
        harvester = HarvesterBuilder.createNewInstance(testUrl);
    }

    @After
    public void tearDown() {
        oaiProvider.close();
        oaiProvider = null;
        testUrl = null;
    }

    @Test
    public void identify() {
        Identify identify = harvester.identify();
        assertEquals("repository name differs", "PUB - Publications at Bielefeld University",
            identify.getRepositoryName());
        assertEquals("deletion record policy should be 'No'", "Persistent", identify.getDeletedRecordPolicy().name());
    }

    @Test
    public void listSets() throws Exception {
        OAIDataList<Set> setList = harvester.listSets();
        assertFalse("set list should not be empty", setList.isEmpty());
    }

    @Test
    public void listMetadataFormats() {
        List<MetadataFormat> mfList = harvester.listMetadataFormats();
        assertFalse("metadata format list should not be empty", mfList.isEmpty());
    }

    @Test
    public void listHeaders() throws Exception {
        OAIDataList<Header> headerList = harvester.listIdentifiers("oai_dc", null, null, TEST_SET);
        assertFalse("headers should not be empty", headerList.isEmpty());
        ResumptionToken rsToken = headerList.getResumptionToken();
        assertNotNull("there should be an resumption token", rsToken);
        OAIDataList<Header> headerList2 = harvester.listIdentifiers(rsToken.getToken());
        assertFalse("headers should not be empty", headerList2.isEmpty());
    }

    @Test
    public void listRecords() throws Exception {
        OAIDataList<Record> recordList = harvester.listRecords("oai_dc", null, null, TEST_SET);
        assertFalse("records should not be empty", recordList.isEmpty());
        ResumptionToken rsToken = recordList.getResumptionToken();
        assertNotNull("there should be an resumption token", rsToken);
        OAIDataList<Record> recordList2 = harvester.listRecords(rsToken.getToken());
        assertFalse("records should not be empty", recordList2.isEmpty());
    }

    @Test
    public void getRecord() throws Exception {
        Record record = harvester.getRecord("oai:pub.uni-bielefeld.de:1893305", "oai_dc");
        assertNotNull("the record should exist", record);
    }

}
