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
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mycore.oai.pmh.harvester.Harvester;
import org.mycore.oai.pmh.harvester.HarvesterBuilder;
import org.mycore.oai.pmh.harvester.HarvesterUtil;

public class HarvesterUtilTest {

    private String testUrl = "https://pub.uni-bielefeld.de/oai";

    private SimpleOAIProvider oaiProvider;

    @Before
    public void setUp() throws IOException {
        oaiProvider = new SimpleOAIProvider();
        testUrl = oaiProvider.getBaseURI() + "/oai";
    }

    @After
    public void tearDown() {
        oaiProvider.close();
        oaiProvider = null;
        testUrl = null;
    }

    @Test
    public void streamRecords() {
        Harvester harvester = HarvesterBuilder.createNewInstance(testUrl);
        assertTrue("there should be six or more records",
            HarvesterUtil.streamRecords(harvester, "oai_dc", null, null, "patent").count() >= 6);
        assertEquals("there should be 3 records", 3,
            HarvesterUtil.streamRecords(harvester, "oai_dc", null, null, "patent")
                .limit(3)
                .count());
    }

    @Test
    public void streamSets() {
        Harvester harvester = HarvesterBuilder.createNewInstance(testUrl);
        assertTrue("there should be more than 100 sets", HarvesterUtil.streamSets(harvester).count() > 100);
    }

}
