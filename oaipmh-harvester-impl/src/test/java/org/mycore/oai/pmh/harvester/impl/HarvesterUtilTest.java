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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mycore.oai.pmh.harvester.Harvester;
import org.mycore.oai.pmh.harvester.HarvesterBuilder;
import org.mycore.oai.pmh.harvester.HarvesterUtil;

public class HarvesterUtilTest {

    private static String TEST_URL = "https://zs.thulb.uni-jena.de/oai2";

    @Test
    public void streamRecords() {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        assertTrue("there should be six or more records",
            HarvesterUtil.streamRecords(harvester, "oai_dc", null, null, "jportal_jpjournal_00001217").count() >= 6);
    }

    @Test
    public void streamSets() {
        Harvester harvester = HarvesterBuilder.createNewInstance(TEST_URL);
        assertTrue("there should be more than 500 sets", HarvesterUtil.streamSets(harvester).count() > 500);
    }

}
